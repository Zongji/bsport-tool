package com.tools.modle;

import lombok.Data;

import java.util.Date;

@Data
public class ExcelFile {
    private String name;
    private long modifyDate;
    private long size;  // k单位
}
