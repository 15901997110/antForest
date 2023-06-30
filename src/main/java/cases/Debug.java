package cases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * by qiwei.lu 2023/6/29
 */
public class Debug {
    private static final Logger logger= LoggerFactory.getLogger(Debug.class);

    @BeforeTest
    public void setup(){
        logger.info("setup");
    }
    @AfterTest
    public void teardown(){
        logger.info("teardown");
    }
    @Test
    public void test(){
        logger.info("test");
    }
}
