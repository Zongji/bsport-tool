package com.tools.controller;

import com.tools.service.DirService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class TennisController {

    @Autowired
    private DirService dirService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/excel/download/{fileName}")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileName) {
        try {
            if (StringUtils.isEmpty(fileName)) {
                response.sendError(501);
            }
            dirService.download(response, fileName);
        }catch (Exception e) {
            log.error("文件下载出错", e);
        }
    }
}
