package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

//添加注解，返回的value值为null的key值会消失
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {
    T data;
    String msg;
    int status;

    private ServerResponse(T data, String msg, int status) {
        this.data = data;
        this.msg = msg;
        this.status = status;
    }

    private ServerResponse(T data, int status) {
        this.data = data;
        this.status = status;
    }

    private ServerResponse(String msg, int status) {
        this.msg = msg;
        this.status = status;
    }

    private ServerResponse(int status) {
        this.status = status;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS.getCode();
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public int getStatus() {
        return status;
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getDesc(),ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(msg,ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(data,ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(data,msg,ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createByERROR(){
        return new ServerResponse<T>(ResponseCode.ERROR.getDesc(),ResponseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> createByERROR(String msg){
        return new ServerResponse<T>(msg,ResponseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> createByERROR(int errorCode,String msg){
        return new ServerResponse<T>(msg,errorCode);
    }
}

