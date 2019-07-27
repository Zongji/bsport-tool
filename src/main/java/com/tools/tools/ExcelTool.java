package com.tools.tools;

import com.alibaba.fastjson.JSONArray;
import com.tools.modle.GameInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static com.tools.Constants.*;

@Slf4j
public class ExcelTool {

    /**
     * @param fileName  excel短名字
     * @param list
     */
    public static void create(String fileName, List<GameInfo> list) {
        Workbook wb = new HSSFWorkbook(); //97-2007
        try  (OutputStream fileOut = new FileOutputStream(EXCEL_DIR + fileName + ".xls")) {
            Sheet sheet = wb.createSheet("网球比赛数据");

            writeHeadRow(sheet);
            writeDataRows(sheet, list);
            wb.write(fileOut);
        } catch (Exception e) {
            log.error("生成Excel失败！", e);
        }

    }
    private static void writeDataRows(Sheet sheet, List<GameInfo> list) {
        int dataLength = list.size();
        for (int i = 0; i < dataLength; i++) {
            Row row = sheet.createRow(i + 1);
            GameInfo info = list.get(i);
            writeDataRow(row, info);
        }
    }


    private static void writeDataRow(Row row, GameInfo info) {
        int c = 0;
        row.createCell(c++).setCellValue(info.getName());
        row.createCell(c++).setCellValue(info.getDateStr());
        row.createCell(c++).setCellValue(info.getPlayer1());
        row.createCell(c++).setCellValue(info.getPlayer2());
        row.createCell(c++).setCellValue(info.getResult());
        row.createCell(c++).setCellValue(info.getBet365First());
        row.createCell(c++).setCellValue(info.getDetailLink());

        List<String> gameList = info.getList();
        if (CollectionUtils.isNotEmpty(gameList)) {
            int size = gameList.size();
            for (int i = 0; i < size; i++) {
                
                row.createCell(c + i).setCellValue(gameList.get(size - 1 - i));
            }
        }

    }

    private static void writeHeadRow(Sheet sheet) {
//        head.setRowStyle();
        Row head = sheet.createRow(0);
        int i= 0;
        head.createCell(i).setCellValue("比赛");
        head.createCell(++i).setCellValue("日期");
        head.createCell(++i).setCellValue("A Player");
        head.createCell(++i).setCellValue("B Player");
        head.createCell(++i).setCellValue("比分结果");
        head.createCell(++i).setCellValue("BET365初赔");
        head.createCell(++i).setCellValue("详细链接");
        head.createCell(++i).setCellValue("倒数第1局");
        head.createCell(++i).setCellValue("倒数第2局");
        head.createCell(++i).setCellValue("倒数第3局");
        head.createCell(++i).setCellValue("倒数第4局");
        head.createCell(++i).setCellValue("倒数第5局");
        head.createCell(++i).setCellValue("倒数第6局");
        head.createCell(++i).setCellValue("倒数第7局");
        head.createCell(++i).setCellValue("倒数第8局");
        head.createCell(++i).setCellValue("倒数第9局");
        head.createCell(++i).setCellValue("倒数第10局");
        head.createCell(++i).setCellValue("倒数第11局");
        head.createCell(++i).setCellValue("倒数第12局");
        head.createCell(++i).setCellValue("倒数第13局");
        head.createCell(++i).setCellValue("倒数第14局");
        head.createCell(++i).setCellValue("倒数第15局");
        head.createCell(++i).setCellValue("倒数第16局");
        head.createCell(++i).setCellValue("倒数第17局");
        head.createCell(++i).setCellValue("倒数第18局");
        head.createCell(++i).setCellValue("倒数第19局");
        head.createCell(++i).setCellValue("倒数第20局");
        head.createCell(++i).setCellValue("倒数第21局");
        head.createCell(++i).setCellValue("倒数第22局");
        head.createCell(++i).setCellValue("倒数第23局");
        head.createCell(++i).setCellValue("倒数第24局");
        head.createCell(++i).setCellValue("倒数第25局");
        head.createCell(++i).setCellValue("倒数第26局");
        head.createCell(++i).setCellValue("倒数第27局");
        head.createCell(++i).setCellValue("倒数第28局");
        head.createCell(++i).setCellValue("倒数第29局");
        head.createCell(++i).setCellValue("倒数第30局");
    }


}
