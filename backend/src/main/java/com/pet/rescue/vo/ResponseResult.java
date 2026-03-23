package com.pet.rescue.vo;

import java.io.Serializable;

/**
 * 统一响应结果类
 * 用于封装所有API接口的返回结果
 */
public class ResponseResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     * 200: 成功
     * 400: 请求参数错误
     * 401: 未授权
     * 403: 禁止访问
     * 500: 服务器内部错误
     */
    private Integer code;

    /**
     * 提示消息
     */
    private String message;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 成功响应
     */
    public static ResponseResult ok() {
        return new ResponseResult(200, "操作成功");
    }

    /**
     * 成功响应（带自定义消息）
     */
    public static ResponseResult ok(String message) {
        return new ResponseResult(200, message);
    }

    /**
     * 失败响应
     */
    public static ResponseResult error() {
        return new ResponseResult(500, "操作失败");
    }

    /**
     * 失败响应（带自定义消息）
     */
    public static ResponseResult error(String message) {
        return new ResponseResult(500, message);
    }

    /**
     * 失败响应（带状态码和消息）
     */
    public static ResponseResult error(Integer code, String message) {
        return new ResponseResult(code, message);
    }

    /**
     * 构造函数
     */
    public ResponseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 链式调用设置数据
     */
    public ResponseResult data(String key, Object value) {
        if (this.data == null) {
            this.data = new java.util.HashMap<String, Object>();
        }
        if (this.data instanceof java.util.Map) {
            ((java.util.Map<String, Object>) this.data).put(key, value);
        }
        return this;
    }

    /**
     * 链式调用设置数据
     */
    public ResponseResult data(Object data) {
        this.data = data;
        return this;
    }

    // Getter和Setter方法
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}