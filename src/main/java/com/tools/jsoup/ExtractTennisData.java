package com.tools.jsoup;

import com.tools.modle.GameInfo;
import com.tools.tools.ExcelTool;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class ExtractTennisData {


    public static final String host = "https://cn.bsportsfan.com";
    public static Properties p = new Properties();
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    static {
        try {
            p.load(new FileInputStream(new File("config.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("=======================");
        System.out.println("配置信息:");
        for (Map.Entry entry : p.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
        System.out.println("=======================");

    }

    public static void main(String[] args) {
        long st = System.currentTimeMillis();
        String date = p.getProperty("date").trim();
        String excelName = date+"比赛数据.xls";
        File f = new File(excelName);
        if (f.exists()) {
            System.out.println(excelName + "文件已经存在！");
            return;
        }

        System.out.println("开始爬取网页信息");
        List<GameInfo> list = new ArrayList<>();
        int totalPage = 1;

        try {
            getGameInfo(list, date, 1, totalPage);
        }catch (Exception e) {
            e.printStackTrace();
            System.err.println("开始爬取网页信息失败。" + e.getMessage());
            return;
        }
        System.out.println("比赛信息列表信息抓取完成。");
        System.out.println("开始抓取每场比赛详细信息，请稍等...");



        List<Future<GameInfo>> futures = new ArrayList<>();
        for(GameInfo info : list) {

//            Future<GameInfo> future = executorService.submit(new GameDetailTask(info));
//            futures.add(future);

            try {
                getGameDetail(info);
            }catch (Exception e) {
                System.out.println("抓取每场比赛详细信息失败！");
                return;
            }
//            System.out.println(info);
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

        System.out.println("抓取比赛详细信息完成。");
        System.out.println("开始生成Excel");
        ExcelTool.create(date+"比赛数据",list);
        System.out.println("开始生成Excel结束，数据抓取结束。");
        System.out.println("耗时:" + (System.currentTimeMillis() - st)/1000 + "s");
    }


    private static Connection getConnection(String link) {
        Connection connection = Jsoup.connect(link);
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        header.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        header.put("Accept-Charset", "  GB2312,utf-8;q=0.7,*;q=0.7");
        header.put("cookie", p.getProperty("cookie"));
        return connection.headers(header);
    }

    //https://cn.bsportsfan.com/ce/tennis/  默认当天，按照最近实际按排序

    //https://cn.bsportsfan.com/cs/tennis/2019-07-22/  制定某一天怕，按照实际按先后排序的
    //https://cn.bsportsfan.com/cs/tennis/2019-07-23/p.5
    //https://cn.bsportsfan.com/cs/tennis/p.3

    //odds
    // https://cn.bsportsfan.com/rs/1736746/Amandine-Cazeaux-v-Taylor-Dean


    /**
     *
     * @param currentPage 默认1,第1页
     * @return
     * @throws IOException
     */
    private static void getGameInfo(List<GameInfo> list, String date, int currentPage, int totalPage) throws IOException {
        String url = host + "/cs/tennis/" + date + "/p." + currentPage;
        Connection data = getConnection(url);
        Document doc = data.get();

        if (currentPage == 1) {
            totalPage = getTotalPage(doc);
        }

        System.out.println("正在抓取网页信息:" + url);
        System.out.println("totalPage:" + totalPage + " currentPage:"+ currentPage);

        Elements trs = doc.select(".table tr");
        for (Element tr : trs) {
            //赛事
            Element firstTd = tr.select("td").first();
            String gameName = firstTd.text();
//            System.out.println(gameName);
            if (gameName.contains("男双") || gameName.contains("女双")) {
                continue;
            }

            //时间
            Element timdTd = firstTd.nextElementSibling();
            String dateStr = timdTd.text();
//            System.out.println(dateStr);

            //name vs name
            Element nameTd = timdTd.nextElementSibling();
            String playersNames = nameTd.text();
//            System.out.println(playersNames);

            // link
            Element linkTd = nameTd.nextElementSibling().nextElementSibling();
            String result = linkTd.select("a").text();
//            System.out.println(result);

            String detailLink = host+ linkTd.select("a").attr("href");
//            System.out.println(detailLink);

            GameInfo info = new GameInfo();
            info.setName(gameName);
            info.setDateStr(dateStr);
            info.setPlayer1(playersNames.split("v")[0].trim());
            info.setPlayer2(playersNames.split("v")[1].trim());
            info.setDetailLink(detailLink);
            info.setResult(result);
            list.add(info);
        }

        currentPage ++;
        if (totalPage > 1 && currentPage <= totalPage) {
            getGameInfo(list, date, currentPage, totalPage);
        }
    }

    private static int getTotalPage(Document doc) {
        Elements items = doc.select(".page-item");
        int pageSize = items.size() - 1;
        return pageSize;
    }

    private static GameInfo getGameDetail(GameInfo info) throws IOException {
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

        return info;
    }


    private static class GameDetailTask implements Callable<GameInfo> {
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
