package com.tools.service;

import com.alibaba.fastjson.JSONObject;
import com.tools.modle.ExcelFile;
import com.tools.modle.GameInfo;
import com.tools.tools.ExcelTool;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Random;

import static com.tools.Constants.*;

@Service
public class DirService {


    public void createFile() throws Exception {
        File dir = new File(EXCEL_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String jsonStr = FileUtils.readFileToString(new File("data.json"), "utf-8");
        List<GameInfo> list = JSONObject.parseArray(jsonStr, GameInfo.class);
        Random random = new Random();
        ExcelTool.create(EXCEL_DIR + random.nextInt(1000) + "", list);
    }

    public List<ExcelFile> listDir() throws IOException {
        List<ExcelFile> list = new ArrayList<>();
        File dir = new File(EXCEL_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            ExcelFile excelFile = new ExcelFile();
            excelFile.setModifyDate(f.lastModified());
            excelFile.setName(f.getName());
            excelFile.setSize(f.length()/1024);
            list.add(excelFile);
        }
        return list;
    }

    public void download(HttpServletResponse response, String fileName) throws Exception {
        String path = EXCEL_DIR + fileName;
        File f = new File(path);
        if (!f.exists()) {
            throw new Exception("文件不存在！");
        }

        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+ new String((fileName).getBytes()));
        OutputStream outputStream = response.getOutputStream();
        InputStream inputStream = FileUtils.openInputStream(f);
        IOUtils.copy(inputStream, outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
