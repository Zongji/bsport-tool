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


}
