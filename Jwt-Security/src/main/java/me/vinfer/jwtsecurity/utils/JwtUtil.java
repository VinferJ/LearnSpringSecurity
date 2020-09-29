package me.vinfer.jwtsecurity.utils;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import me.vinfer.jwtsecurity.exception.TokenExpiredException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Donald
 * @description     jwt秘钥生成工具类
 * @date 2020-03-12
 **/
public class JwtUtil {

    /**生成签名是所使用的秘钥*/
    private static final String BASE64_ENCODED_SECRET_KEY = "2!$@f&77XEGWSmC4";

    /**生成签名的时候所使用的加密算法*/
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;


    /**
     * 生成 JWT Token 字符串
     *
     * @param iss            签发人
     * @param ttlMillis      jwt 过期时间  毫秒
     * @param claimsParam    额外添加到payload部分的信息。
     *                       例如可以添加用户名、用户ID、用户（加密前的）密码等信息
     */
    public static String encode(String iss, long ttlMillis, Map<String, Object> claimsParam) {
        Map<String, Object> claims = new HashMap<>(16);

        if (null != claimsParam) {
            claims.putAll(claimsParam);
        }

        // 签发时间（iat）：payload部分的标准字段之一
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder()
                // payload部分的非标准字段/附加字段，一般写在标准的字段之前。
                .setClaims(claims)
                // JWT ID（jti）：payload部分的标准字段之一，JWT 的唯一性标识，虽不强求，但尽量确保其唯一性。
                .setId(UUID.randomUUID().toString())
                // 签发时间（iat）：payload部分的标准字段之一，代表这个 JWT 的生成时间。
                .setIssuedAt(now)
                // 签发人（iss）：payload部分的标准字段之一，代表这个 JWT 的所有者。通常是 username、userid 这样具有用户代表性的内容。
                .setSubject(iss)
                // 设置生成签名的算法和秘钥
                .signWith(SIGNATURE_ALGORITHM, BASE64_ENCODED_SECRET_KEY);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            // 过期时间（exp）：payload部分的标准字段之一，代表这个 JWT 的有效期。
            builder.setExpiration(exp);
        }

        return builder.compact();
    }


    public static String decode(String jwt){
        Jws<Claims> jws = Jwts.parser().setSigningKey(BASE64_ENCODED_SECRET_KEY).parseClaimsJws(jwt);
        Claims body = jws.getBody();
        return JSON.toJSONString(body.get("data"));
    }


    public static void validToken(String jwt) throws TokenExpiredException {
        JwtParser parser = Jwts.parser().setSigningKey(BASE64_ENCODED_SECRET_KEY);
        try {
            parser.parse(jwt);
        }catch (ExpiredJwtException ex){
            throw new TokenExpiredException(ex.getMessage());
        }
    }

}
