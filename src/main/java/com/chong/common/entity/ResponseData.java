package com.chong.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

@ApiModel(value = "ResponseData",description = "Rest请求Json返回值")
public class ResponseData<T> {

    @ApiModelProperty(value = "消息code")
    private int code;
    @ApiModelProperty(value = "消息内容")
    private String message;
    @ApiModelProperty(value = "response值")
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }
}
