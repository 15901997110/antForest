package cases;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

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

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        Collections.shuffle(list);
        System.out.println(list.toString());
    }
}
