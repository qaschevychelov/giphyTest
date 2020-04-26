package com.test.util;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ScreenShot implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        makeScreenshotOnFailure();
    }

    @Attachment("Фейл")
    private byte[] makeScreenshotOnFailure() {
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }
}
