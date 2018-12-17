package com.oauth2.demo.util;


/**
 * 返回结果类。
 */

public class Result<T> {

    // 返回是否成功
    private Boolean success = true;

    // 返回信息
    private String msg = "操作成功";

    /**
     * 返回消息类型,用于复杂返回信息,0代表普通返回信息,1代表特殊返回信息
     */
    private Integer code = 0;

    private T data;


    public Result() {
    }

    private Result(Boolean success, String msg, Integer code, T data) {
        this.success = success;
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 构建返回结果
     */
    public static<T> Result<T> build(Boolean success, String msg, Integer code,T data){
        return new Result<>(success, msg, code, data);
    }

    /**
     * 构建返回结果，code默认值为0
     */
    public static<T> Result<T> build(Boolean success,String msg,T data){
        return build(success,msg,0,data);
    }

    /**
     * 构建返回默认成功结果
     */
    public static Result buildSuccess(){
        return new Result<>();
    }

    /**
     *构建成功结果
     */
    public static<T> Result<T> buildSuccess(String msg,T data) {
        return build(Boolean.TRUE,msg,data );
    }


    /**
     * 构建失败结果
     */
    public static<T> Result<T> buildFailure(Integer code,String msg,T data){
        return build(Boolean.FALSE,msg ,code,data);
    }

    /**
     * 构建失败结果
     */
    public static<T> Result<T> buildFailure(String msg,T data){
        return build(Boolean.FALSE,msg ,data);
    }

    /**
     * 构建成功结果不带信息
     */
    public static Result buildSuccess(String msg){
        return buildSuccess(msg,null);
    }

    /**
     * 构建成功结果带数据
     */
    public static<T> Result<T> buildSuccessData(T data){
        return buildSuccess(null,data);
    }

    /**
     * 构建失败结果不带数据
     */
    public static Result buildFailure(String msg){
        return buildFailure(msg,null);
    }

    /**
     * 构建失败结果不带数据
     */
    public static Result buildFailure(Integer code,String msg){
        return build(Boolean.FALSE,msg,code,null);
    }

    /**
     * 构建失败结果带数据
     */
    public static<T> Result<T> buildFailureData(T data){
        return buildFailure("",data);
    }

    /**
     * 参数错误
     */
    public static<T> Result<T> invalidParam(String msg){
        return build(Boolean.FALSE, msg, 400, null);
    }

    /**
     * 服务器异常
     */
    public static<T> Result<T> serverError(){
        return build(Boolean.FALSE, "服务器异常", 500, null);
    }

    /**
     * 业务不允许
     */
    public static<T> Result<T> serviceRefuse(String msg){
        return build(Boolean.FALSE, msg, 600, null);
    }
}
