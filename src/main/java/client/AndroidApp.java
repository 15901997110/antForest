package client;

/**
 * by qiwei.lu 2023/6/27
 */
public class AndroidApp {
    private String appPackage;
    private String appActivity;

    public AndroidApp() {
    }

    public AndroidApp(String appPackage, String appActivity) {
        this.appPackage = appPackage;
        this.appActivity = appActivity;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppActivity() {
        return appActivity;
    }

    public void setAppActivity(String appActivity) {
        this.appActivity = appActivity;
    }


    public static AndroidApp alipay() {
        AndroidApp app = new AndroidApp();
        app.setAppPackage("com.eg.android.AlipayGphone");
        app.setAppActivity("AlipayLogin");
        return app;
    }


}
