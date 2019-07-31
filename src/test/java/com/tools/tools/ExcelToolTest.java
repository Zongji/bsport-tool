package com.tools.tools;

import com.alibaba.fastjson.JSONArray;
import com.tools.modle.GameInfo;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Random;

import static com.tools.Constants.EXCEL_DIR;
import static org.junit.Assert.*;

public class ExcelToolTest {


    @Test
    public void create() {

        try {
            File dir = new File(EXCEL_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            String json = FileUtils.readFileToString(new File("data.json"), "utf-8");
            List<GameInfo> list = JSONArray.parseArray(json, GameInfo.class);
            ExcelTool.create("1111", list);
        }catch (Exception e) {

        }


        Random random = new Random();
        int r = random.nextInt(9);

        System.out.println(r);
    }


}