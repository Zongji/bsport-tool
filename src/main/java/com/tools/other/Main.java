package com.tools.other;

import us.codecraft.webmagic.Spider;

public class Main {
    public static void main(String[] args) {
        Spider.create(new BSportsFanProcessor())
                .addUrl("https://cn.bsportsfan.com/ce/tennis/")
                .thread(1)
                .run();
    }
}
