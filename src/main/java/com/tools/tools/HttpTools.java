package com.tools.tools;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;

import static com.tools.Constants.HTTP_TIMEOUT;

public class HttpTools {
    public static Connection getConnection(String link) {
        Connection connection = Jsoup.connect(link).timeout(HTTP_TIMEOUT);
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        header.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        header.put("Accept-Charset", "  GB2312,utf-8;q=0.7,*;q=0.7");
        header.put("cookie", "tz=Asia%2FShanghai; __cfduid=d9869fbe992fb2d2a92aef738594dd2f71563804652; sid=bukm81th09a3iv94ij8p3aipp7; tz=Asia%2FShanghai");
        return connection.headers(header);
    }
}
