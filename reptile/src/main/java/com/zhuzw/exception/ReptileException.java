package com.zhuzw.exception;

/**
 *
 *
 * @author zhuzhiwei
 * @date 2023/9/21 14:39
 */
public class ReptileException extends RuntimeException {

    private String code;

    private String message;

    public ReptileException() {
        super();
    }

    public ReptileException(String message) {
        super(message);
        this.code = "-1";
        this.message = message;
    }


    public ReptileException(String code, String message) {
        super(message);
        this.code = code;
    }
}
