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

    }

    @Test
    public void getGameInfoDetail() {
        GameInfo gameInfo = new GameInfo();
        gameInfo.setDetailLink("https://bsportsfan.com/r/1745705/Gergely-Madarasz-v-Duje-Kekez");
        try {
            GameInfo info = extractTennisDataService.getGameInfoDetail(gameInfo);
            Assert.assertNotNull(info.getList());
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}