package com.test.util;

import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.test.util.DriverUtils.invokeCmdCommand;

public class SourceDriver{
    public static AndroidDriver androidDriver;
    public static AndroidDriver getDriver() {
        String udid = (System.getProperty("udid") != null) ? System.getProperty("udid") : "emulator-5554";
        invokeCmdCommand("adb -s " + udid + " shell pm clear co.fun.testgiphy");

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Android");
        cap.setCapability("udid", udid);
        cap.setCapability("deviceName", "device");
        cap.setCapability("platformVersion", "8.1");
        cap.setCapability("app", System.getProperty("user.dir").replaceAll("\\\\", "/") + "/src/test/resources/apk/app-debug.apk");
        cap.setCapability("appPackage", "co.fun.testgiphy");
        cap.setCapability("appWaitActivity", "co.fun.testgiphy.MainActivity");
        cap.setCapability("noReset", "true");
        cap.setCapability("fullReset", "false");
        cap.setCapability("newCommandTimeout", "30000");
        cap.setCapability("automationName", "UiAutomator2");
        AndroidDriver driver = null;
        try {
            System.out.println("starting new driver..." + LocalDateTime.now().toString());
            driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), cap);
            System.out.println("new driver is started!" + LocalDateTime.now().toString());
            DriverUtils.currentWait = 20;
            DriverUtils.deviceWidth = driver.manage().window().getSize().getWidth();
            DriverUtils.deviceHeight = driver.manage().window().getSize().getHeight();
            driver.manage().timeouts().implicitlyWait(DriverUtils.currentWait, TimeUnit.SECONDS);
            androidDriver = driver;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return driver;
    }
}
