package com.tools.modle;

import lombok.Data;

import java.util.Date;

@Data
public class ExcelFile implements Comparable<ExcelFile>{
    private String name;
    private long modifyDate;
    private long size;  // k单位


    @Override
    public int compareTo(ExcelFile o) {
        if (this.modifyDate > o.getModifyDate()) {
            return 1;
        } else if (this.modifyDate < o.getModifyDate()) {
            return -1;
        }
        return 0;
    }
}
