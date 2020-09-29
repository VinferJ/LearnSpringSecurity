package me.vinfer.jwtsecurity.advice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import me.vinfer.jwtsecurity.config.EncryptBodyConfig;
import me.vinfer.jwtsecurity.exception.DecryptBodyFailException;
import me.vinfer.jwtsecurity.exception.RequestExpiredException;

import me.vinfer.jwtsecurity.utils.JwtUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Vinfer
 * @date 2020-09-20    20:07
 * @description 解密请求正文的advice对象，这里只抛异常，不进行捕获与处理
 *              该advice对象仅对方法中带有@RequestBody注解的参数的方法进行处理
 **/
@RestControllerAdvice(basePackages = "me.vinfer.jwtsecurity.controller")
@Order(1)
public class DecryptRequestBodyAdvice extends RequestBodyAdviceAdapter {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class DecryptInputMessage implements HttpInputMessage{

        private InputStream requestBody;
        private HttpHeaders requestHeader;

        @Override
        public InputStream getBody() throws IOException {
            return requestBody;
        }

        @Override
        public HttpHeaders getHeaders() {
            return requestHeader;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class DecryptBody{
        private Object data;
        /*请求时间戳，用来判断请求是否失效/超时，单位为秒*/
        private Long timestamp;
    }

    private static final Logger LOG = LoggerFactory.getLogger(DecryptRequestBodyAdvice.class);
    /**时区信息*/
    private static final ZoneOffset BEIJING_TIME_ZONE = ZoneOffset.of("+08:00");

    @Autowired
    private EncryptBodyConfig config;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 带@RequestBody参数的http请求读前处理，即在进入controller前
     * 通过截取流的形式对请求体（requestBody）以及入参等进行相关处理
     * 可以做一些统一的前置的字段解密、封装，数据过滤等功能
     * 这里完成的是对请求数据做jwt解密
     * @param inputMessage          http请求消息数据体
     * @param parameter             方法入参
     * @param targetType            对应的传入类型
     * @param converterType         策略接口，指定可以在HTTP请求和响应之间进行转换的转换器。
     * @return                      {@link HttpInputMessage}
     * @throws IOException          {@link IOException} IO异常
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        /*
        *
        *
        * TODO  前端请求的body封装格式：
        *           {'data':jwtString }
        *           jwtString即为通过jwt加密后的内容
        *           在jwtString的playLoad中装载真实的请求数据，封装格式应为：
        *               {'timestamp':xxxx, 'data': jsonObj }
        *               其中jsonObj是请求数据体，即后端@RequestBody接收的部分
        *               timestamp是请求发送的时间，用于后端请求处理的超时判断
        *       后端进行jwt解密，并将数据封装成javabean：
        *           1. 先进行整体body的json字符串转换
        *           2. 从转换后的body中取出data的值
        *           2. 将body通过Gson转换成DecryptBody对象（@link DecryptBody{long timestamp;Object data}）
        *           3. 将DecryptBody的data值取出，并封装成一个解密后的HttpInputMessage并返回
        * */

        //解密开关，返回false时不解密
        if (!config.getEnable()){
            return inputMessage;
        }

        String originBody;

        /*获取请求正文（requestBody），并且封装为String，解密需要使用String类型*/
        try {
            originBody = IOUtils.toString(inputMessage.getBody(), StandardCharsets.UTF_8);
        }catch (Exception e){
            throw new DecryptBodyFailException("获取请求正文失败，请检查请求的数据体是否符合规范");
        }
        if (originBody == null || StringUtils.isEmpty(originBody)){
            throw new DecryptBodyFailException("请求正文为NULL或空串，解密处理失败");
        }

        //转成json对象取出data中封装的jwtString
        JSONObject jsonObject = JSON.parseObject(originBody);
        String jwtString = (String) jsonObject.get("data");

        if (jwtString == null || StringUtils.isEmpty(jwtString)){
            throw new DecryptBodyFailException("请求中封装的jwt数据为空，解密失败");
        }

        String body;
        /*对jwtString进行，解密得到的仍是一个字符串*/
        try {
            body = decrypt(jwtString);
        }catch (Exception e){
            throw new DecryptBodyFailException("请求正文jwt解密失败，请检查解密配置");
        }

        LOG.info("请求参数：{}", body);

        /*
        * 转换成DecryptBody对象
        * 取出请求的时间戳去判断请求是否超时，超时则抛出异常
        * */
        DecryptBody decryptBody;
        try {
            decryptBody = new Gson().fromJson(body, DecryptBody.class);
        }catch (Exception e){
            throw new DecryptBodyFailException("请求正文格式错误");
        }

        /*超时判断，默认超时时间设置为10s*/
        if (isValidRequest(decryptBody)){
            throw new RequestExpiredException(config.getRequestValidTime());
        }

        try {
            /*将DecryptBody的data值取出，并封装成一个解密后的HttpInputMessage并返回*/
            InputStream ins = IOUtils.toInputStream(JSON.toJSONString(decryptBody.getData()), StandardCharsets.UTF_8);
            return new DecryptInputMessage(ins,inputMessage.getHeaders());
        }catch (Exception e){
            throw new DecryptBodyFailException("字符串转换为输入流对象异常，请检查编码等格式是否正确");
        }
    }

    private String decrypt(String jwtString){
        return JwtUtil.decode(jwtString);
    }

    private boolean isValidRequest(DecryptBody decryptBody){
        //当前时间秒数，单位是秒
        long now = LocalDateTime.now().toEpochSecond(BEIJING_TIME_ZONE);
        /*
        * 格式化body的时刻一定在now之前
        * 如果出现now小于body.timestamp那么就等于请求异常，直接返回false
        * */
        if (now < decryptBody.getTimestamp()){
            return false;
        }
        /*
        * 超时，返回false，未超时返回true
        * now - decryptBody.getTimestamp() = 请求已执行的时间
        * config.getRequestValidTime()：请求的超时/有效时间
        * */
        return now - decryptBody.getTimestamp() <= config.getRequestValidTime();
    }

}
