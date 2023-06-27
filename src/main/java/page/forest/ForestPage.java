package page.forest;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.*;
import page.AppPage;

/**
 * by qiwei.lu 2023/6/25
 */
public class ForestPage extends AppPage {
    //实例化蚂蚁森林页面的次数，第一次和非第一次，对于校验页面加载完成的方法不同
    private static int instanceTimes;
    //是否还有能量
    private boolean hasNext;

    public ForestPage(AppiumDriver driver) {
        super(driver);
        instanceTimes++;
        if (instanceTimes > 1) {
            try {
                untilVisible(ta);
                this.hasNext = true;
            } catch (RuntimeException e) {
                //非第一次进入蚂蚁页面，并且页面标题不是『xxx的蚂蚁森林』，则认为能量收完了，此时中断循环
                //WebElement backHomeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(backForestHomeBtn));
                this.hasNext = false;
            }
        } else {
            this.hasNext = true;
        }
    }

    public boolean hasNext() {
        return this.hasNext;
    }

    //去种树，当能量满足要求时，支付宝引导用户去种树
    By platTrees = By.xpath("//android.widget.Button[@text='去种树']");

    By ta = By.xpath("//android.view.View[contains(@text,'TA收取你')]");
    //证书解锁，有的话需要关闭掉这个提示
    By unlockTip = By.id("J_pop_treedialog_close");

    //进入朋友的蚂蚁森林页面后，页面加载完成，左上角的标题会变成『xx的蚂蚁森林』
    By title = By.xpath("//*[contains(@text,'的蚂蚁森林')]");

//    By revivalEnergyTip = By.xpath("//android.view.View[@text='帮好友复活能量']");
    By confirmSendBtn = By.xpath("//android.widget.Button[contains(@text,'确认发送')]");
    By tipCloseBtn = By.xpath("//android.widget.Button[@text='关闭']");

    By backForestHomeBtn = By.xpath("//android.widget.Button[@text='返回我的森林']");

    private static final Point p1 = new Point(150, 480);
    private static final Point p2 = new Point(228, 431);
    private static final Point p3 = new Point(324, 406);
    private static final Point p4 = new Point(428, 410);
    private static final Point p5 = new Point(492, 428);
    private static final Point p6 = new Point(591, 493);

    final Point findNext = new Point(652, 1042);

    /**
     * 收能量
     */
    public void getEnergy() {
        //围着树点6下
        getEnergy(p1);
        getEnergy(p2);
        getEnergy(p3);
        getEnergy(p4);
        getEnergy(p5);
        getEnergy(p6);

    }

    public void getEnergy(Point point) {
        tap(point);
        /**
         * 有时候会点到帮好友复活能量，这里需要特殊处理下
         */
        try {
            WebElement element = driver.findElement(confirmSendBtn);
            try {
                click(element);
            } catch (StaleElementReferenceException e) {
                driver.findElement(confirmSendBtn).click();
            }

            Thread.currentThread().sleep(1000);
        } catch (NoSuchElementException e) {
            //这是正常情况，因为大部分点击不会弹出『帮好友收能量』，除非好友能量过期，并且点击到了过期能量球
        } catch (InterruptedException e) {

        }
    }

    public ForestPage findFriendEnergy() throws InterruptedException {
        tap(findNext);//找能量
        Thread.currentThread().sleep(1000);
        return new ForestPage(driver);
    }

}
