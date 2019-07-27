package com.tools.modle;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class GameInfo {
    private String detailLink;

    private String name;
    private String dateStr;
    private String player1;
    private String player2;

    private String player1En;
    private String player2En;
    private String result;
    private String bet365First;
    private List<String> list;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
