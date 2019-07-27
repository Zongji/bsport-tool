package com.tools.service;

import com.tools.modle.GameInfo;
import com.tools.tools.ExcelTool;
import com.tools.tools.HttpTools;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.tools.Constants.*;


@Slf4j
@Service
public class ExtractTennisDataService {


//    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     *
     * @param date
     */
    public void process(String date) throws Exception{
        long st = System.currentTimeMillis();
//        String excelName = EXCEL_DIR + date + ".xls";
//        File f = new File(excelName);
//        if (f.exists()) {
//            log.error(excelName + "文件已经存在！");
//            throw new Exception(excelName + "文件已经存在！");
//        }

        log.info("开始抓取网页信息");
        List<GameInfo> list = new ArrayList<>();
        int totalPage = 1;

        getGameInfoList(list, date, 1, totalPage); //已经包含详情
        log.info("比赛信息列表信息抓取完成。");

//        log.info("开始抓取每场比赛详细信息，请稍等...");
//        for(GameInfo info : list) {
//            getGameInfoDetail(info);
////            log.info(info);
//        }

//        List<Future<GameInfo>> futures = new ArrayList<>();
//        for(GameInfo info : list) {
//            Future<GameInfo> future = executorService.submit(new GameDetailTask(info));
//            futures.add(future);
//
//            try {
//                getGameInfoDetail(info);
//            }catch (Exception e) {
//                e.printStackTrace();
//                log.info("抓取每场比赛详细信息失败！");
//                return;
//            }
////            log.info(info);
//        }

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
//        log.info("抓取比赛详细信息完成。");

        log.info("开始生成Excel");
        String fileName = EXCEL_DIR + date;
        ExcelTool.create(date,list);
        log.info("开始生成Excel结束，数据抓取结束。");
        log.info("耗时:" + (System.currentTimeMillis() - st)/1000 + "s");
    }

    /**
     *
     * @param currentPage 默认1,第1页
     * @return
     * @throws IOException
     */
    private void getGameInfoList(List<GameInfo> list, String date, int currentPage, int totalPage) throws Exception {
        String url = BSPORT_HOST_CN + "/cs/tennis/" + date + "/p." + currentPage;
        Connection data = HttpTools.getConnection(url);
        Document doc = data.get();

        if (currentPage == 1) {
            totalPage = getTotalPage(doc);
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
            if (result.contains("away") || result.contains("home")
                    || result.contains("已取消")) {
                continue;
            }

            String detailLink = BSPORT_HOST_EN + linkTd.select("a").attr("href");
//            log.info(detailLink);

            GameInfo info = new GameInfo();
            info.setName(gameName);
            info.setDateStr(dateStr);
            info.setPlayer1(playersNames.split("v")[0].trim());
            info.setPlayer2(playersNames.split("v")[1].trim());
            info.setDetailLink(detailLink);
            info.setResult(result);

            this.getGameInfoDetail(info);
            list.add(info);
        }

        currentPage ++;
        if (totalPage > 1 && currentPage <= totalPage) {
            getGameInfoList(list, date, currentPage, totalPage);
        }
    }

    private int getTotalPage(Document doc) {
        Elements items = doc.select(".page-item");
        int pageSize = items.size() - 1;
        return pageSize;
    }

    public GameInfo getGameInfoDetail(GameInfo info) {
        log.info("获取详细信息：" + info.getDetailLink());
        try {
            Connection data = HttpTools.getConnection(info.getDetailLink());
            Document doc = data.get();

            //获取英文名
            parsePlayerName(doc, info);

            // get event
            parseEventList(doc, info);

            Thread.sleep(15);
        }catch (Exception e) {
            log.error("出错了!", e);
        }
        return info;
    }

    /**
     *
     * @param doc
     * @param info
     */
    private void parseEventList(Document doc, GameInfo info) {
        Elements lis = doc.select(".list-group-item");
        List<String> detailList = new ArrayList<>();
        info.setList(detailList);
        for (Element li : lis) {
            String text = li.text();

            String eventResult = "";

            if (text.contains(HOLDS_TO)) {
                if (text.contains(HOLDS_TO_LOVE)) {
                    String player = getPlayer(info, text);
                    eventResult = player + "H0";
                } else {
                    String player = getPlayer(info, text);

                    //提取分数
                    String score = text.substring(text.indexOf(HOLDS_TO) + HOLDS_TO.length() + 1);
                    eventResult = player + "H" + score;
                }
            }else if (text.contains(BREAKS_TO)) {
                if (text.contains(BREAKS_TO_LOVE)) {
                    String player = getPlayer(info, text);
                    eventResult = player + "B0";
                }else {
                    String player = getPlayer(info, text);
                    String score = text.substring(text.indexOf(BREAKS_TO) + BREAKS_TO.length() + 1);
                    eventResult = player + "B" + score;
                }
            }
            else {
                eventResult = text;
            }
            detailList.add(eventResult);
        }
    }

    private String getPlayer(GameInfo info, String text) {
        String player = "";
        if (text.contains(info.getPlayer1En())) {
            player = "A";
        } else if (text.contains(info.getPlayer2En())) {
            player = "B";
        }
        return player;
    }

    /**
     * 解析英文名
     * @param doc
     * @param info
     */
    private void parsePlayerName(Document doc, GameInfo info) {
        Elements elements = doc.select(".col-md-6 a");
        Element firstA = elements.get(0);
        info.setPlayer1En(firstA.text());

        Element nameB = elements.get(1);
        info.setPlayer2En(nameB.text());
    }


    private class GameDetailTask implements Callable<GameInfo> {
        private GameInfo info;

        public GameDetailTask(GameInfo info) {
            this.info = info;
        }
        @Override
        public GameInfo call() throws Exception {
            return getGameInfoDetail(this.info);
        }
    }

}
