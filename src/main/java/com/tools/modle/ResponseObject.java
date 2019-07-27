package com.tools.modle;

import lombok.Data;

@Data
public class ResponseObject<T> {

    private int code;
    private String msg;
    private T data;

    public ResponseObject() {
        this.code = 0;
        this.msg = "success";
    }
    public ResponseObject(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseObject error(String msg) {
        this.code = 1;
        this.msg = msg;
        return this;
    }

    public ResponseObject success(String msg) {
        this.code = 0;
        this.msg = msg;
        return this;
    }

    public ResponseObject success(T data) {
        this.code = 0;
        this.msg = "ok";
        this.data = data;
        return this;
    }

}
