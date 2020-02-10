package com.chong.common.util;

import com.chong.common.entity.ResponseData;

public class ResponseUtil {
    public static ResponseData success(){
        return success(null);
    }
    public static ResponseData success(Object data){
        return success("Success",data);
    }
    public static ResponseData success(String message, Object data){
        ResponseData rData = new ResponseData();
        rData.setCode(200);
        rData.setMessage(message);
        rData.setData(data);
        return rData;
    }
    public static ResponseData fail(){
        return fail("Fail");
    }
    public static ResponseData fail(String message){
        return fail(-1,message);
    }
    public static ResponseData fail(int code,String message){
        ResponseData rData = new ResponseData();
        rData.setCode(code);
        rData.setMessage(message);
        return rData;
    }
}
