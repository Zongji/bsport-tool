package com.tools.controller;

import com.tools.modle.ExcelFile;
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

        ResponseObject<Object> res = new ResponseObject<>();

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sf.parse(date);
            if (d.after(new Date())) {
                res.error("日期不合法,不能指定未来日期");
                return res;
            }
//            Thread.sleep(2000);
        }catch (Exception e) {
            log.error("不合法的日期", e);
            res.error("不合法的日期格式");
            return res;
        }


        try {
            extractTennisDataService.process(date);
            res.success("成功！");
        }catch (Exception e) {
            log.error("获取失败！", e);
            res.error("获取失败！" + e.getMessage());
            return res;
        }

        return res;
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
        List<ExcelFile> list = new ArrayList();
        try {
            list.addAll(dirService.listDir());
        }catch (Exception e) {
            log.error("error!", e);
            return res.error("error!"+ e.getMessage());
        }
        return res.success(list);
    }
}
