package com.tools.other;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class BSportsFanProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);


    //https://cn.bsportsfan.com/r/1737899/Aleshina_Bartone-v-Fichman_Stojanovic
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(https://cn\\.bsportsfan\\.com/r/\\d+/\\w+)").all());
//        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());

//        System.out.println("/html/body/div[1]/div[1]/div[3]/div/table/tbody/tr[2]");

    }

    public Site getSite() {
        return site;
    }


}
