package com.wswl.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResp<T> implements Serializable {

    private static final long serialVersionUID = 6583444154449046521L;

    /**
     * 正常响应码
     */
    private static final int SUCCESS_CODE = 200;
    private static final int STATUS_SUCCESS = 1;
    private static final int STATUS_FAIL = 0;

    /**
     * 正常响应消息
     */
    private static final String SUCCESS_MSG = "SUCCESS";

    /**
     * 状态码
     */
    private int status = STATUS_SUCCESS;
    /**
     * 错误码
     */
    private int code = SUCCESS_CODE;

    /**
     * 错误信息
     */
    private String msg = SUCCESS_MSG;

    /**
     * 响应内容，默认为null
     */
    private T data = null;

    /**
     * 是否的正常响应
     *
     * @return true=正常；false=异常
     */
    @JsonIgnore
    public boolean isOK() {
        return code == SUCCESS_CODE;
    }

    /**
     * 无data的正常返回
     */
    public static ApiResp retOK() {
        return new ApiResp();
    }

    /**
     * 有data的正常返回
     *
     * @param data data内容
     * @param <T> data类型
     */
    public static <T> ApiResp<T> retOK(T data) {
        ApiResp<T> response = new ApiResp<>();
        response.setData(data);
        return response;
    }

    /**
     * 无data的失败返回
     *
     * @param error 错误类型
     */
    public static ApiResp retFail(BaseEnumError error) {
        return retFail(error.getCode(), error.getMsg());
    }

    /**
     * 有data的失败返回
     *
     * @param error 错误类型
     * @param data 详细错误信息
     */
    public static <T> ApiResp<T> retFail(BaseEnumError error, T data) {
        return retFail(error.getCode(), error.getMsg(), data);
    }

    /**
     * 无data的失败返回
     *
     * @param code 错误码
     * @param msg 错误信息
     */
    public static <T> ApiResp<T> retFail(int code, String msg) {
        ApiResp<T> response = new ApiResp<>();
        response.setStatus(STATUS_FAIL);
        response.setCode(code);
        response.setMsg("");
        return response;
    }

    /**
     * @param code 错误码
     * @param msg 错误信息
     */
    public static <T> ApiResp<T> retFail(int code, String msg, T data) {
        ApiResp<T> response = new ApiResp<>();
        response.setCode(code);
        response.setStatus(STATUS_FAIL);
        response.setMsg("");
        response.setData(data);
        return response;
    }
}
