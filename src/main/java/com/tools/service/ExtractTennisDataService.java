package com.tools.service;

import com.tools.modle.GameInfo;
import com.tools.tools.ExcelTool;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.tools.Constants.*;


@Slf4j
@Service
public class ExtractTennisDataService {


    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     *
     * @param date
     */
    public void process(String date) throws Exception{
        long st = System.currentTimeMillis();
        String excelName = EXCEL_DIR + date + "比赛数据.xls";
        File f = new File(excelName);
        if (f.exists()) {
            log.info(excelName + "文件已经存在！");
            return;
        }

        log.info("开始爬取网页信息");
        List<GameInfo> list = new ArrayList<>();
        int totalPage = 1;

        getGameInfo(list, date, 1, totalPage);
        log.info("比赛信息列表信息抓取完成。");
        log.info("开始抓取每场比赛详细信息，请稍等...");



        List<Future<GameInfo>> futures = new ArrayList<>();
        for(GameInfo info : list) {

//            Future<GameInfo> future = executorService.submit(new GameDetailTask(info));
//            futures.add(future);

            try {
//                getGameDetail(info);
            }catch (Exception e) {
                e.printStackTrace();
                log.info("抓取每场比赛详细信息失败！");
                return;
            }
//            log.info(info);
        }

//        list.clear();
//        for (Future<GameInfo> future: futures) {
//            try {
//                GameInfo info = future.get(30, TimeUnit.SECONDS);
//                list.add(info);
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.err.println("抓取比赛详细信息失败。" + e.getMessage());
//            }
//        }
//        executorService.shutdown();

        log.info("抓取比赛详细信息完成。");
        log.info("开始生成Excel");
        String fileName = EXCEL_DIR + date;
        ExcelTool.create(date,list);
        log.info("开始生成Excel结束，数据抓取结束。");
        log.info("耗时:" + (System.currentTimeMillis() - st)/1000 + "s");
    }


    private Connection getConnection(String link) {
        Connection connection = Jsoup.connect(link).timeout(HTTP_TIMEOUT);
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        header.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        header.put("Accept-Charset", "  GB2312,utf-8;q=0.7,*;q=0.7");
        header.put("cookie", "tz=Asia%2FShanghai; __cfduid=d9869fbe992fb2d2a92aef738594dd2f71563804652; sid=bukm81th09a3iv94ij8p3aipp7; tz=Asia%2FShanghai");
        return connection.headers(header);
    }

    /**
     *
     * @param currentPage 默认1,第1页
     * @return
     * @throws IOException
     */
    private void getGameInfo(List<GameInfo> list, String date, int currentPage, int totalPage) throws Exception {
        String url = BSPORT_HOST + "/cs/tennis/" + date + "/p." + currentPage;
        Connection data = getConnection(url);
        Document doc = data.get();

        if (currentPage == 1) {
//            totalPage = getTotalPage(doc);
        }

        log.info("正在抓取网页信息:" + url);
        log.info("totalPage:" + totalPage + " currentPage:"+ currentPage);

        Elements trs = doc.select(".table tr");
        for (Element tr : trs) {
            //赛事
            Element firstTd = tr.select("td").first();
            String gameName = firstTd.text();
//            log.info(gameName);
            if (gameName.contains("男双") || gameName.contains("女双")) {
                continue;
            }

            //时间
            Element timdTd = firstTd.nextElementSibling();
            String dateStr = timdTd.text();
//            log.info(dateStr);

            //name vs name
            Element nameTd = timdTd.nextElementSibling();
            String playersNames = nameTd.text();
//            log.info(playersNames);

            // link
            Element linkTd = nameTd.nextElementSibling().nextElementSibling();
            String result = linkTd.select("a").text();
//            log.info(result);

            String detailLink = BSPORT_HOST + linkTd.select("a").attr("href");
//            log.info(detailLink);

            GameInfo info = new GameInfo();
            info.setName(gameName);
            info.setDateStr(dateStr);
            info.setPlayer1(playersNames.split("v")[0].trim());
            info.setPlayer2(playersNames.split("v")[1].trim());
            info.setDetailLink(detailLink);
            info.setResult(result);

            getGameDetail(info);
            list.add(info);
        }

        currentPage ++;
        if (totalPage > 1 && currentPage <= totalPage) {
            getGameInfo(list, date, currentPage, totalPage);
        }
    }

    private int getTotalPage(Document doc) {
        Elements items = doc.select(".page-item");
        int pageSize = items.size() - 1;
        return pageSize;
    }

    private GameInfo getGameDetail(GameInfo info) throws Exception {
        log.info("获取详细信息" + info.getDetailLink());
        Connection data = getConnection(info.getDetailLink());
        Document doc = data.get();
        Elements lis = doc.select(".list-group-item");
        List<String> detailList = new ArrayList<>();
        info.setList(detailList);
        for (Element li : lis) {
            String text = li.text();

            String detailStr = "";
            if (text.contains("? ?")|| text.contains("保住")) {
                if (text.contains(info.getPlayer1())) {
                    detailStr = detailStr+"A";
                }else if (text.contains(info.getPlayer2())) {
                    detailStr = detailStr + "B";
                }

                if (text.contains("? ?")) {
                    detailStr = detailStr + "B";
                }else if (text.contains("保住")) {
                    detailStr = detailStr + "H";
                }
                //提取分数
                String score = text.substring(text.indexOf("分")-2, text.indexOf("分"));
                detailStr = detailStr + score;

            }else {
                detailStr = text;
            }
            detailList.add(detailStr);
        }


        Thread.sleep(15);
        return info;
    }


    private class GameDetailTask implements Callable<GameInfo> {
        private GameInfo info;

        public GameDetailTask(GameInfo info) {
            this.info = info;
        }
        @Override
        public GameInfo call() throws Exception {
            return getGameDetail(this.info);
        }
    }

}
