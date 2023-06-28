package page;

import com.google.common.collect.ImmutableList;
import enums.MatchType;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumFluentWait;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;


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
        longPress(point, 100L);
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

    /**
     * 获取元素属性值
     * https://w3c.github.io/webdriver/#get-element-attribute
     */
    public String getAttribute(By by, String attribute) {
        System.out.println("getAttributeClass===");
        WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(by));
        return this.getAttribute(element, attribute);
    }

    /**
     * 获取元素属性值
     */
    public String getAttribute(WebElement element, String attribute) {
        logger.info(element.toString());
        return element.getAttribute(attribute);
    }

    /**
     * 直到指定的元素属性值等于value
     */
    public void untilAttributeEquals(By by, String attribute, String value) {
        getWait().until(ExpectedConditions.attributeToBe(by, attribute, value));
    }

    public void untilAttributeEquals(WebElement element, String attribute, String value) {
        getWait().until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    /**
     * 直到指定的元素属性值包含期望的value
     */
    public void untilAttributeContains(By by, String attribute, String value) {
        getWait().until(ExpectedConditions.attributeContains(by, attribute, value));
    }

    public void untilAttributeContains(WebElement element, String attribute, String value) {
        logger.info(element.toString());
        getWait().until(ExpectedConditions.attributeContains(element, attribute, value));
    }

    /**
     * 直到元素属性值不包含某个属性
     */
    public void untilAttributeNotContains(By by, String attribute, String value) {
        getWait().until(d -> {
            String property = this.getAttribute(by, attribute);
            if (property.contains(value)) return false;
            else return true;
        });
    }

    /**
     * 直到元素属性值不包含某个属性
     */
    public void untilAttributeNotContains(WebElement element, String attribute, String value) {
        getWait().until(d -> {
            String property = this.getAttribute(element, attribute);
            if (property.contains(value)) return false;
            else return true;
        });
    }

    /**
     * 直到指定的元素属性值非空
     */
    public void untilAttributeNotEmpty(By by, String attribute) {
        getWait().until(d -> {
            WebElement element = d.findElement(by);
            String value = element.getAttribute(attribute);
            return !"".equals(value);
        });
    }


    /**
     * 选择器中文本值不为空
     *
     * @param by selector
     */
    public void untilTextNotEmpty(By by) {
        getWait().until(d -> StringUtils.isNotBlank(this.getText(by)));
    }


    /**
     * 直到指定的元素属性值变成空
     * 包含两种情况：1.没有此属性；2.有属性，无值
     */
    public void untilAttributeToBeEmpty(By by, String attribute) {
        getWait().until(d -> {
            WebElement e = d.findElement(by);
            String value = e.getAttribute(attribute);
            return "".equals(value);
        });
    }

    /**
     * 直到元素个数大于等于期望的元素个数
     */
    public List<WebElement> untilElementCountGreaterThanOrEqual(By by, Integer expectedCount) {
        if (null == expectedCount) expectedCount = 1;
        Integer finalExpectedCount = expectedCount;
        List<WebElement> ret = getWait().until(d -> {
            List<WebElement> elements = d.findElements(by);
            if (elements.size() >= finalExpectedCount) return elements;
            else return null;
        });
        return ret;
    }

    /**
     * 直到元素文本匹配到期望值,缺省使用正则匹配
     */
    public void untilTextMatch(By by, String expectedText, MatchType matchType) {
        if (null == matchType) matchType = MatchType.match;
        MatchType finalMatchType = matchType;
        getWait().until(d -> {
            String text = this.getText(by);
            return MatchType.match(text, expectedText, finalMatchType);
        });
    }

    /**
     * 直到文本匹配到期望值的取反，缺省使用正则匹配
     * 比如单价默认的值是0.00，当输入商品后，页面会自动更新为非0.00的一个价格
     */
    public void untilTextNotMatch(By by, String unexpectedText, MatchType matchType) {
        if (null == matchType) matchType = MatchType.match;
        MatchType finalMatchType = matchType;
        getWait().until(d -> {
            String text = this.getText(by);
            return !MatchType.match(text, unexpectedText, finalMatchType);
        });
    }


    /**
     * 直到元素文本匹配到期望值，缺省使用正则匹配
     */
    public void untilTextMatch(WebElement element, String expectedText, MatchType matchType) {
        logger.info(element.toString());
        if (null == matchType) matchType = MatchType.match;
        MatchType finalMatchType = matchType;
        getWait().until(d -> {
            String text = element.getText();
            return MatchType.match(text, expectedText, finalMatchType);
        });
    }

    /**
     * 直到文本匹配到期望值的取反，缺省使用正则匹配
     * 比如单价默认的值是0.00，当输入商品后，页面会自动更新为非0.00的一个价格
     */
    public void untilTextNotMatch(WebElement element, String unexpectedText, MatchType matchType) {
        logger.info(element.toString());
        if (null == matchType) matchType = MatchType.match;
        MatchType finalMatchType = matchType;
        getWait().until(d -> {
            String text = element.getText();
            return !MatchType.match(text, unexpectedText, finalMatchType);
        });
    }


    /**
     * 直到元素文本包含期望值
     */
    public void untilTextContains(By by, String expectedText) {
        untilTextMatch(by, expectedText, MatchType.contains);
    }

    public void untilTextContains(WebElement element, String expectedText) {
        untilTextMatch(element, expectedText, MatchType.contains);
    }

    public void untilTextNotContains(By by, String expectedText) {
        untilTextNotMatch(by, expectedText, MatchType.contains);
    }

    public void untilTextNotContains(WebElement element, String expectedText) {
        untilTextNotMatch(element, expectedText, MatchType.contains);
    }

    public void untilTextEqual(By by, String expectedText) {
        untilTextMatch(by, expectedText, MatchType.equal);
    }

    public void untilTextEqual(WebElement element, String expectedText) {
        untilTextMatch(element, expectedText, MatchType.equal);
    }

    public void untilTextNotEqual(By by, String unexpectedText) {
        untilTextNotMatch(by, unexpectedText, MatchType.equal);
    }

    public void untilTextNotEqual(WebElement element, String unexpectedText) {
        untilTextNotMatch(element, unexpectedText, MatchType.equal);
    }

    public WebElement findElement(By by) {
        WebElement element = untilVisible(by);
        return element;
    }

    /**
     * 查找元素，显示等待
     */
    public List<WebElement> findElements(By by) {
        return getWait().until(d -> d.findElements(by));
    }

    /**
     * 查找元素，显示等待
     */
    public WebElement findElement(WebElement element, By by) {
        return getWait().until(d -> element.findElement(by));
    }

    /**
     * 查找元素，显示等待
     */
    public List<WebElement> findElements(WebElement element, By by) {
        return getWait().until(d -> element.findElements(by));
    }

    /**
     * 直到元素消失，包括以下两种情况
     * 1.元素自始至终就没有存在过（主要是第一次尝试查找元素就没有找到）
     * 2.元素找到过，直到某个时间点，元素找不到了
     *
     * @param by
     */
    public void untilElementDestroyed(By by) {
        getWait().until(d -> {
            WebElement e = d.findElement(by);
            return e == null ? true : false;
        });
    }

    public String getText(By by) {
        WebElement element = findElement(by);
        try {
            return getText(element);
        } catch (StaleElementReferenceException e) {
            //如果失败，则重新获取
            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException interruptedException) {
                logger.error("", interruptedException);
            }
            WebElement element1 = findElement(by);
            return element1.getText();
        }
    }

    public String getText(WebElement element) {

        return element.getText();
    }

}
