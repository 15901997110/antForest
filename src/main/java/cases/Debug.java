package cases;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * by qiwei.lu 2023/6/29
 */
public class Debug {
    private static final Logger logger = LoggerFactory.getLogger(Debug.class);

    @BeforeTest
    public void setup() {
        logger.info("setup");
    }

    @AfterTest
    public void teardown() {
        logger.info("teardown");
    }

    @Test
    public void test() {
        logger.info("test");
    }

    public static void main(String[] args) throws InterruptedException {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(10L);
        Duration between = Duration.between(start, end);
        Duration between2 = Duration.between(end, start).abs();

        System.out.println(between);
        System.out.println(between2);
        System.out.println("between.isNegative():" + between.isNegative());
        System.out.println("between2.isNegative():" + between2.isNegative());
        System.out.println(between.toMillis());
        System.out.println(between2.toMillis());
    }
}
