package client;

import java.io.Serializable;

/**
 * 设备
 * by qiwei.lu 2023/6/27
 */
public class Device implements Serializable {
    private String platform;
    private String deviceName;

    public Device() {
    }

    public Device(String platform, String deviceName) {
        this.platform = platform;
        this.deviceName = deviceName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public static Device nova6() {
        Device device = new Device();
        device.setPlatform("android");
        device.setDeviceName("FML4C19C09007688");
        return device;
    }
}
