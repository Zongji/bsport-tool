package com.tools.modle;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class GameInfo {
    private String detailLink;

    private String name;
    private String dateStr;
    private String player1;
    private String player2;
    private String result;

    private String bet365First;
    private List<String> list;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBet365First() {
        return bet365First;
    }

    public void setBet365First(String bet365First) {
        this.bet365First = bet365First;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
