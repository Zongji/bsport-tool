package com.tools.controller;

import com.tools.service.DirService;
import com.tools.service.ExtractTennisDataService;
import com.tools.modle.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/rest")
public class TennisRestController {

    @Autowired
    private ExtractTennisDataService extractTennisDataService;
    @Autowired
    private DirService dirService;

    @GetMapping("/history/{date}")
    public Object getTennisHistory(@PathVariable("date") String date) {
        log.info("date:{}", date);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sf.parse(date);
            if (d.after(new Date())) {
                return "日期不合法,不能指定未来日期";
            }
        }catch (Exception e) {
            log.error("不合法的日期", e);
            return "error";
        }


        try {
            extractTennisDataService.process(date);
        }catch (Exception e) {
            log.error("获取失败！", e);
            return "error";
        }

        return "ok";
    }


    @GetMapping("/create-file")
    public void createFileTest() {
        try {
            dirService.createFile();
        }catch (Exception e) {
            log.error("error!", e);
        }
    }

    @GetMapping("/list-dir")
    public Object listExcelDir() {
        ResponseObject<Object> res = new ResponseObject<>();
        List<String> list = new ArrayList();
        try {
            list.addAll(dirService.listDir());
        }catch (Exception e) {
            log.error("error!", e);
        }
        res.setData(list);
        return res;
    }
}
