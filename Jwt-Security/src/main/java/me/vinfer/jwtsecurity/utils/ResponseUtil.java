package me.vinfer.jwtsecurity.utils;

import com.alibaba.fastjson.JSON;
import me.vinfer.jwtsecurity.constants.ErrorCode;
import me.vinfer.jwtsecurity.model.response.ResponseModel;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-19    23:19
 * @description
 **/
public class ResponseUtil {

    public static Object emptySuccess(){
        ResponseModel callback = new ResponseModel(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getDesc(),null );
        return JSON.toJSON(callback);
    }

    public static Object successWithData(Object data){
        ResponseModel callback = new ResponseModel(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getDesc(), data);
        return JSON.toJSON(callback);
    }

    public static Object error(ErrorCode errorCode){
        ResponseModel callback = new ResponseModel(errorCode.getCode(), errorCode.getDesc(), null);
        return JSON.toJSON(callback);
    }


}
