package com.tools;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.tools.Constants.EXCEL_DIR;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes={BSportApplication.class})// 指定启动类
public class BaseTest {

    @Test
    public void test() {
        log.info(EXCEL_DIR);
    }
}
