package page;

import com.google.common.collect.ImmutableList;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumFluentWait;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Interaction;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Sleeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.Clock;
import java.time.Duration;


/**
 * by Lu 2022/5/18
 */
public abstract class AppPage implements Serializable {
    protected static final long DEFAULT_TIMEOUT = 5L;
    protected Logger logger;
    protected AppiumDriver driver;
    protected AppiumFluentWait<AppiumDriver> wait;
    protected int width;
    protected int height;

    public AppPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new AppiumFluentWait(driver, Clock.systemDefaultZone(), Sleeper.SYSTEM_SLEEPER);
        wait.withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT));
        Dimension size = driver.manage().window().getSize();
        this.width = size.getWidth();
        this.height = size.getHeight();
        logger = LoggerFactory.getLogger(this.getClass());
        //页面初始化
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public FluentWait<AppiumDriver> getWait() {
        return wait;
    }

    protected void input(By by, String content) {
        WebElement element = wait.until(d -> d.findElement(by));
        element.sendKeys(content);
    }

    protected void input(WebElement element, String content) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(content);
    }

    protected void click(By by) {
        WebElement element = wait.until(d -> d.findElement(by));
        click(element);
    }

    protected void click(WebElement element) {
        //先判断元素可见，WebElement.isDisplayed()==true;
        wait.until(ExpectedConditions.visibilityOf(element));
        element.click();
    }

    /**
     * 直到元素出现
     */
    protected WebElement untilPresence(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    /**
     * 直到元素可见（Element.isEnabled=true）
     */
    protected WebElement untilVisible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected WebElement untilVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * 判断元素可见（Element.isEnabled=true）
     */
    protected Boolean ifVisibilityOfElementLocated(By by) {
        Boolean flag;
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 从开始点，滑动至结束点
     * by qiwei.lu 2023.6.15
     *
     * @param start
     * @param end
     * @param durationMillis
     */
    protected void swipe(Point start, Point end, long durationMillis) {
        swipe(start.x, start.y, end.x, end.y, durationMillis);
    }

    /**
     * 从指定坐标滑动到指定坐标
     * by qiwei.lu 2023.6.1
     */
    protected void swipe(int startX, int startY, int endX, int endY, long durationMillis) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Interaction moveToStart = finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY);
        Interaction pressDown = finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg());
        Interaction moveToEnd = finger.createPointerMove(Duration.ofMillis(durationMillis), PointerInput.Origin.viewport(), endX, endY);
        Interaction pressUp = finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg());

        Sequence swipe = new Sequence(finger, 0);
        swipe.addAction(moveToStart).addAction(pressDown).addAction(moveToEnd).addAction(pressUp);

        driver.perform(ImmutableList.of(swipe));
    }

    /**
     * 滑屏操作，从起始元素的坐标滑动至结束元素的坐标，元素默认坐标点为元素的left_top（左上）位置
     * by qiwei.lu 2023.6.10
     *
     * @param startElement
     * @param endElement
     * @param durationMillis
     * @param centerToCenter true-从元素中心滑动到元素的中心，false-从元素左上角坐标移动到元素左上角坐标，缺省false
     */
    protected void swipe(WebElement startElement, WebElement endElement, long durationMillis, Boolean centerToCenter) {
        if (null == centerToCenter) {
            centerToCenter = false;
        }
        Point start = startElement.getLocation();
        Point end = endElement.getLocation();
        if (centerToCenter) {
            Dimension startElementSize = startElement.getSize();
            Dimension endElementSize = endElement.getSize();
            int startX = start.getX() + startElementSize.width / 2;//元素位置加上元素宽度的一半，定位到元素中心位置
            int startY = start.getY() + startElementSize.height / 2;
            int endX = end.getX() + endElementSize.width / 2;
            int endY = end.getY() + endElementSize.height / 2;
            swipe(startX, startY, endX, endY, durationMillis);
        } else {
            swipe(start.getX(), start.getY(), end.getX(), end.getY(), durationMillis);
        }
    }

    /**
     * 从起始元素位置滑动到结束元素位置
     *
     * @param startElement   起始元素，元素的left-top坐标点
     * @param endElement     结束元素，元素的left-top坐标点
     * @param durationMillis 持续时间，单位毫秒
     */
    protected void swipe(WebElement startElement, WebElement endElement, long durationMillis) {
        swipe(startElement, endElement, durationMillis, false);
    }

    /**
     * 点按操作
     *
     * @param point
     */
    protected void tap(Point point) {
        longPress(point, 200L);
    }

    /**
     * 长按操作，缺省时按1200ms
     *
     * @param point
     */
    protected void longPress(Point point) {
        longPress(point, 1200L);
    }

    /**
     * 长按操作
     *
     * @param point
     * @param durationMillis
     */
    protected void longPress(Point point, Long durationMillis) {
        PointerInput input = new PointerInput(PointerInput.Kind.TOUCH, "finger2");
        Sequence tap = new Sequence(input, 0);
        Interaction moveToStart = input.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), point.x, point.y);
        Interaction pressDown = input.createPointerDown(PointerInput.MouseButton.LEFT.asArg());
        Interaction pause = new Pause(input, Duration.ofMillis(durationMillis));
        Interaction pressUp = input.createPointerUp(PointerInput.MouseButton.LEFT.asArg());
        tap.addAction(moveToStart).addAction(pressDown).addAction(pause).addAction(pressUp);
        driver.perform(ImmutableList.of(tap));
    }
}
