package page.home;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import page.AppPage;
import page.forest.ForestPage;

/**
 * 支付宝首页，需要提前将『蚂蚁森林』应用添加到首页
 * by qiwei.lu 2023/6/25
 */
public class HomePage extends AppPage {

    public HomePage(AppiumDriver driver) {
        super(driver);
    }

    By loginBtn = By.xpath("//android.widget.Button[@text='进入支付宝']");

    By antForestIcon = By.xpath("//android.widget.TextView[@text='蚂蚁森林']");

    public ForestPage toForestPage() {
        //先判断是否需要登录
        try {
            WebElement element = findElement(loginBtn);
            click(element);
        } catch (NoSuchElementException | TimeoutException e) {

        }
        //进入蚂蚁森林
        click(antForestIcon);
        return new ForestPage(driver);
    }
}
