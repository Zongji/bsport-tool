package com.tools.service;

import com.tools.BaseTest;
import com.tools.modle.GameInfo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ExtractTennisDataServiceTest extends BaseTest {

    @Autowired
    private ExtractTennisDataService extractTennisDataService;

    @Test
    public void process() {
        String date = "2019-07-28";

        try {
            extractTennisDataService.process(date);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getGameInfoDetail() {
        GameInfo gameInfo = new GameInfo();
//        gameInfo.setDetailLink("https://bsportsfan.com/r/1745705/Gergely-Madarasz-v-Duje-Kekez");

        gameInfo.setDetailLink("https://betsapi.com/r/1750439/Leylah-Annie-Fernandez-v-Maddison-Inglis");
        try {
            GameInfo info = extractTennisDataService.getGameInfoDetail(gameInfo);
            Assert.assertNotNull(info.getList());
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}