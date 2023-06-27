package page.home;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
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

    @FindBy(xpath = "//android.widget.TextView[@text='蚂蚁森林']")
    WebElement antForestIcon;

    public ForestPage toForestPage() {
        click(antForestIcon);
        return new ForestPage(driver);
    }
}
