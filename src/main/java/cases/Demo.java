package cases;

import client.AndroidApp;
import client.Device;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import page.forest.ForestPage;
import page.home.HomePage;

import java.net.URL;

/**
 * by qiwei.lu 2023/6/26
 */
public class Demo {
    private AppiumDriver driver;
    private static final String LOCAL_APPIUM_SERVICE = "http://127.0.0.1:4723";
    private static final String LINUX_APPIUM_SERVICE = "http://192.168.253.37:4723";

    @BeforeTest
    public void setup() throws Exception {
        Device device = Device.nova6();
        AndroidApp alipay = AndroidApp.alipay();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(AndroidMobileCapabilityType.PLATFORM_NAME, device.getPlatform());
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,"12.0");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device.getDeviceName());
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, alipay.getAppPackage());
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, alipay.getAppActivity());
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        capabilities.setCapability(AndroidMobileCapabilityType.UNICODE_KEYBOARD, true);
        driver = new AndroidDriver(new URL(LOCAL_APPIUM_SERVICE), capabilities);
//        driver = new AndroidDriver(new URL(LINUX_APPIUM_SERVICE), capabilities);

    }

    @AfterTest
    public void teardown() {

        if (null != driver) {
            String windowHandle = driver.getWindowHandle();
            driver.close();
            driver.quit();
        }
    }

    @Test
    public void test() throws Exception {
//        HomePage homePage = new HomePage(driver);
//        ForestPage forestPage = homePage.toForestPage();
//        while (forestPage.hasNext()) {
//            forestPage.getEnergy();
//            forestPage = forestPage.findFriendEnergy();
//        }
    }
}
