package page.forest;

import io.appium.java_client.AppiumDriver;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.*;
import page.AppPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * by qiwei.lu 2023/6/25
 */
public class ForestPage extends AppPage {
    //实例化蚂蚁森林页面的次数，第一次和非第一次，对于校验页面加载完成的方法不同
    private static int instanceTimes;
    //是否还有能量
    private boolean hasNext;
    private static String who;
    //去种树，当能量满足要求时，支付宝引导用户去种树
    By platTrees = By.xpath("//android.widget.Button[@text='去种树']");
    //森林新消息，第一次进入时，校验页面是否加载完成时使用
    By newMsg = By.xpath("//android.view.View[@text='森林新消息']");
    By ta = By.xpath("//android.view.View[contains(@text,'TA收取你')]");
    //证书解锁，有的话需要关闭掉这个提示
    By unlockTip = By.id("J_pop_treedialog_close");

    //进入朋友的蚂蚁森林页面后，页面加载完成，左上角的标题会变成『xx的蚂蚁森林』
    By title = By.xpath("//*[contains(@text,'的蚂蚁森林')]");

    //    By revivalEnergyTip = By.xpath("//android.view.View[@text='帮好友复活能量']");
    By confirmSendBtn = By.xpath("//android.widget.Button[contains(@text,'确认发送')]");
    By tipCloseBtn = By.xpath("//android.widget.Button[@text='关闭']");
    By skinTip = By.xpath("//android.view.View[contains(@text,'皮肤')]");
    By backForestHomeBtn = By.xpath("//android.widget.Button[@text='返回我的森林']");

    //挂件相关
    By treeTip = By.xpath("//*[@resource-id='J_treeContainer']//*[@resource-id='J_pop_treedialog_close']");
    /**
     * 注意，不同的手机，屏幕分辨率不同，这里的点需要重新适配
     */
    //能量球
    private static final Point energy1 = new Point(150, 480);
    private static final Point energy2 = new Point(228, 431);
    private static final Point energy3 = new Point(324, 406);
    private static final Point energy4 = new Point(428, 410);
    private static final Point energy5 = new Point(492, 428);
    private static final Point energy6 = new Point(591, 493);

    //找能量
    final Point findNext = new Point(652, 1042);


    public ForestPage(AppiumDriver driver) {
        super(driver);
        instanceTimes++;
        logger.info("-------------------------------------------");
        if (instanceTimes > 1) {
            try {
                untilVisible(ta);
                untilTextNotEqual(title, who + "的蚂蚁森林");
                this.hasNext = true;
                WebElement forestTitle = untilVisible(title);
                String titleText = forestTitle.getText();
                logger.info("进入{}", titleText);
                who = titleText.replaceAll("的蚂蚁森林", "");

            } catch (RuntimeException e) {
                //非第一次进入蚂蚁页面，并且页面标题不是『xxx的蚂蚁森林』，则认为能量收完了，此时中断循环
                //WebElement backHomeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(backForestHomeBtn));
                logger.error("", e);
                this.hasNext = false;
                logger.info("所有好友的能量收取完成");
            }
        } else {
            //判断『森林新消息』，确保页面加载完整
            untilVisible(newMsg);
            this.hasNext = true;
            who = "自己";
            logger.info("进入自己的蚂蚁森林");
        }
    }

    public boolean hasNext() {
        return this.hasNext;
    }


    /**
     * 收能量
     */
    public void getEnergy() throws Exception {
        logger.info("开始收取 {} 的能量", who);
        List<Point> list = new ArrayList<>();
        list.add(energy1);
        list.add(energy2);
        list.add(energy3);
        list.add(energy4);
        list.add(energy5);
        list.add(energy6);
        Collections.shuffle(list);//为了防止被支付宝判定为机器收取，这里做下随机顺序
        list.stream().forEach(this::getEnergy);
        logger.info("结束收取 {} 的能量", who);

    }

    public void getEnergy(Point point) {
        tap(point);
        try {
            if (instanceTimes > 1) {//朋友的蚂蚁森林页面
                WebElement closeBtn = driver.findElement(tipCloseBtn);
                //如果上一步弹出了tip，进一步判断是不是帮好友复活能量
                try {
                    WebElement resurrectionBtn = driver.findElement(confirmSendBtn);
                    click(resurrectionBtn);//确认发送
                    Thread.currentThread().sleep(500);
                } catch (RuntimeException e) {
                    logger.warn("检测到弹出了tip,但不是『帮好友复活能量』", e);
                    try {
                        click(closeBtn);
                        Thread.currentThread().sleep(500);
                    } catch (StaleElementReferenceException e2) {
                        driver.findElement(tipCloseBtn).click();
                        try {
                            Thread.currentThread().sleep(500);
                        } catch (InterruptedException interruptedException) {
                        }
                    } catch (InterruptedException interruptedException) {
                    }
                } catch (InterruptedException e) {
                }
            } else {//自己的蚂蚁森林页面
                WebElement element = driver.findElement(treeTip);
                try {
                    click(element);
                    Thread.currentThread().sleep(500);
                } catch (StaleElementReferenceException e) {
                    driver.findElement(treeTip).click();
                    try {
                        Thread.currentThread().sleep(500);
                    } catch (InterruptedException interruptedException) {
                    }
                } catch (InterruptedException e) {
                }
            }
        } catch (NoSuchElementException e) {
            //没有找到是正常情况，大部分情况并不会点击触发tip
        }

    }

    public ForestPage findFriendEnergy() throws InterruptedException {
        tap(findNext);//找能量
        return new ForestPage(driver);
    }

}
