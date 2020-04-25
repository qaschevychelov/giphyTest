package com.test.pages;

import com.codeborne.selenide.SelenideElement;
import com.test.util.DriverUtils;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.test.util.SourceDriver.androidDriver;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static java.time.Duration.ofSeconds;

/**
 * Базовый пейдж
 * Здесь сосредоточены низкоуровневые методы по работе с экраном
 * Методы общие для всех экранов
 */
public class BasePage {
    /**
     * локатор любого элемента с текстом
     */
    public final String XPATH_ANY_ELEM_WITH_TEXT = "//*[@text='%s']";

    /**
     * прогресс-бар
     */
    public final SelenideElement progressBar = $(By.xpath("//android.widget.ProgressBar"));

    /**
     * дожидается исчезновения прогресс-бара
     */
    public void waitUntilProgressBatIsNotVisible() {
        try {
            progressBar.waitUntil(not(visible), DriverUtils.currentWait);
        } catch (Throwable e) {}
    }

    /**
     * Метод возвращает видимость элемента по его тексту
     * @param text String текст элемента
     * @return boolean
     */
    public boolean isElementVisibleByText(String text) {
        androidDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        boolean isVisible;
        try {
            isVisible = androidDriver.findElement(By.xpath(String.format(XPATH_ANY_ELEM_WITH_TEXT, text))).isDisplayed();
        } catch (Throwable e) {
            isVisible = false;
        }
        androidDriver.manage().timeouts().implicitlyWait(DriverUtils.currentWait, TimeUnit.SECONDS);
        return isVisible;
    }

    /**
     * Метод ждет
     * @param mills long количество миллисекунд
     */
    public void waitAbit(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * перегруженный метод без задержки в свайпе
     *
     * @param fromPoint - координаты начала свайпа
     * @param toPoint   - координаты окончания свайпа
     */
    public void swipeByCoordinate(PointOption fromPoint, PointOption toPoint) {
        WebDriver driver = androidDriver;
        TouchAction actions = new TouchAction((MobileDriver) driver);
        actions.press(fromPoint)
                .waitAction(waitOptions(ofSeconds(0)))
                .moveTo(toPoint)
                .release()
                .perform();
    }

    /**
     * Метод нажимает аппаратную кнопку Назад
     */
    public void clickHardwareBackBtn() {
        androidDriver.navigate().back();
    }

    /**
     * Возвращает видимость клавиатуры
     * @return boolean
     */
    public boolean isKeyBoardVisible() {
        return androidDriver.isKeyboardShown();
    }
}
