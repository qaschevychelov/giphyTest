package com.test.pages;

import static com.codeborne.selenide.Condition.*;
import com.codeborne.selenide.SelenideElement;
import com.test.util.DriverUtils;
import com.test.util.SourceDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.$;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static com.test.util.SourceDriver.androidDriver;

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
     * локатор любого элемента, содержащего текст
     */
    public final String XPATH_ANY_ELEM_CONT_TEXT = "//*[contains(@text,'%s')]";

    /**
     * локатор любого элемента с альтернативным текстом
     */
    public final String XPATH_ANY_ELEM_WITH_CONTENT_DESC = "//*[@content-desc='%s']";

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
     * Метод запускает закрытое приложение, установленное в капабилити текущего веб драйвера
     */
    public void activateApp() {
        String appPackage = androidDriver.getCapabilities().asMap().get("appPackage").toString();
        androidDriver.activateApp(appPackage);
    }

    /**
     * Метод дожидается видимости элемента
     * @param locator String локатор элемента
     */
    public void waitUntilVisible(String locator) {
        for (int i = 0; i < 60; i++) {
            if (isElementVisible(locator))
                return;
            else waitAbit(300);
        }
        Assert.fail("Элемент '" + locator + "' все еще не виден спустя 20 секунд!");
    }

    /**
     * Метод возвращает видимость элемента
     * @param locator String локатор элемента
     * @return boolean
     */
    public boolean isElementVisible(String locator) {
        androidDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        boolean isVisible;
        try {
            isVisible = androidDriver.findElement(By.xpath(locator)).isDisplayed();
        } catch (Throwable e) {
            isVisible = false;
        }
        androidDriver.manage().timeouts().implicitlyWait(DriverUtils.currentWait, TimeUnit.SECONDS);
        return isVisible;
    }

    /**
     * Метод возвращает состояние элемента
     * @param locator String локатор элемента
     * @return boolean
     */
    public boolean isElementChecked(String locator) {
        androidDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        boolean isChecked;
        try {
            isChecked = Boolean.parseBoolean(androidDriver.findElement(By.xpath(locator)).getAttribute("checked"));
        } catch (Throwable e) {
            isChecked = false;
        }
        androidDriver.manage().timeouts().implicitlyWait(DriverUtils.currentWait, TimeUnit.SECONDS);
        return isChecked;
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
     * Метод возвращает видимость элемента по содержанию составного текста
     * @param text String текст элемента
     * @return boolean
     */
    public boolean isElementVisibleByContainsTexts(String text) {
        String[] msgs;
        // формируем локатор
        StringBuilder buffer = new StringBuilder("//*[");
        // если в тексте есть оператор [И], то формируем локатор через and
        if (text.contains("[И]")) {
            msgs = text.split("\\[И]");

            for (String msg1 : msgs) {
                buffer.append("contains(@text,'").append(msg1).append("') and ");
            }
            text = buffer.toString();
            text = text.substring(0, text.lastIndexOf(" and "));

            // если в тексте есть оператор [ИЛИ], то формируем локатор через or
            // удобно в тех случаях, когда мы точно не знаем, какое сообщение будет отображаться
        } else if (text.contains("[ИЛИ]")) {
            msgs = text.split("\\[ИЛИ]");

            for (String msg1 : msgs) {
                buffer.append("contains(@text,'").append(msg1).append("') or ");
            }
            text = buffer.toString();
            text = text.substring(0, text.lastIndexOf(" or "));
        } else {
            buffer.append("contains(@text,'").append(text).append("')");
            text = buffer.toString();
        }

        //заканчиваем формировать локатор
        text += "]";

        androidDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        boolean isVisible;
        try {
            isVisible = androidDriver.findElement(By.xpath(text)).isDisplayed();
        } catch (Throwable e) {
            isVisible = false;
        }
        androidDriver.manage().timeouts().implicitlyWait(DriverUtils.currentWait, TimeUnit.SECONDS);
        return isVisible;
    }

    /**
     * Метод дожидается исчезновения элемента
     * @param locator String локатор элемента
     */
    public void waitUntilNotVisible(String locator) {
        for (int i = 0; i < 60; i++) {
            if (isElementVisible(locator))
                waitAbit(300);
            else return;
        }
        Assert.fail("Элемент '" + locator + "' все также виден спустя 20 секунд!");
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
     * Метод ожидает отображения любого элемента с текстом
     * @param text String текст
     */
    public void waitUntilAnyElementWithTextIsVisible(String text) {
        for (int i = 0; i < 60; i++) {
            if (isElementVisible(String.format(XPATH_ANY_ELEM_WITH_TEXT, text)))
                return;
            else waitAbit(300);
        }
        Assert.fail("Элемент с текстом '" + text + "' все еще не виден спустя 20 секунд!");
    }

    /**
     * Метод ожидает отображения любого элемента с составным текстом
     * @param text String текст
     */
    public void waitUntilAnyElementWithTextsIsVisible(String text) {
        String[] msgs;
        // формируем локатор
        StringBuilder buffer = new StringBuilder("//*[");
        // если в тексте есть оператор [И], то формируем локатор через and
        if (text.contains("[И]")) {
            msgs = text.split("\\[И]");

            for (String msg1 : msgs) {
                buffer.append("@text='").append(msg1).append("' and ");
            }
            text = buffer.toString();
            text = text.substring(0, text.lastIndexOf(" and "));

            // если в тексте есть оператор [ИЛИ], то формируем локатор через or
            // удобно в тех случаях, когда мы точно не знаем, какое сообщение будет отображаться
        } else if (text.contains("[ИЛИ]")) {
            msgs = text.split("\\[ИЛИ]");

            for (String msg1 : msgs) {
                buffer.append("@text='").append(msg1).append("' or ");
            }
            text = buffer.toString();
            text = text.substring(0, text.lastIndexOf(" or "));
        } else {
            buffer.append("@text='").append(text).append("'");
            text = buffer.toString();
        }

        //заканчиваем формировать локатор
        text += "]";

        for (int i = 0; i < 60; i++) {
            if (isElementVisible(text))
                return;
            else waitAbit(300);
        }
        Assert.fail("Элемент с составным текстом '" + text + "' все еще не виден спустя 20 секунд!");
    }

    /**
     * Метод ожидает исчезновения любого элемента с текстом
     * @param text String текст
     */
    public void waitUntilAnyElementWithTextIsNotVisible(String text) {
        for (int i = 0; i < 60; i++) {
            if (isElementVisible(String.format(XPATH_ANY_ELEM_WITH_TEXT, text)))
                waitAbit(300);
            else return;
        }
        Assert.fail("Элемент с текстом '" + text + "' до сих пор виден спустя 20 секунд!");
    }

    /**
     * Метод кликает на любой элемент с текстом
     * @param text String текст
     */
    public void clickAnyElementWithText(String text) {
        androidDriver.findElement(By.xpath(String.format(XPATH_ANY_ELEM_WITH_TEXT, text))).click();
    }

    /**
     * Метод кликает на любой элемент с составным текстом
     * @param text String текст
     */
    public void clickAnyElementWithTexts(String text) {
        String[] msgs;
        // формируем локатор
        StringBuilder buffer = new StringBuilder("//*[");
        // если в тексте есть оператор [И], то формируем локатор через and
        if (text.contains("[И]")) {
            msgs = text.split("\\[И]");

            for (String msg1 : msgs) {
                buffer.append("@text='").append(msg1).append("' and ");
            }
            text = buffer.toString();
            text = text.substring(0, text.lastIndexOf(" and "));

            // если в тексте есть оператор [ИЛИ], то формируем локатор через or
            // удобно в тех случаях, когда мы точно не знаем, какое сообщение будет отображаться
        } else if (text.contains("[ИЛИ]")) {
            msgs = text.split("\\[ИЛИ]");

            for (String msg1 : msgs) {
                buffer.append("@text='").append(msg1).append("' or ");
            }
            text = buffer.toString();
            text = text.substring(0, text.lastIndexOf(" or "));
        } else {
            buffer.append("@text='").append(text).append("'");
            text = buffer.toString();
        }

        //заканчиваем формировать локатор
        text += "]";
        androidDriver.findElement(By.xpath(text)).click();
    }

    /**
     * Метод ожидает отображения любого элемента с альтернативным текстом
     * @param text String текст
     */
    public void waitUntilAnyElementWithContDescIsVisible(String text) {
        for (int i = 0; i < 60; i++) {
            if (isElementVisible(String.format(XPATH_ANY_ELEM_WITH_CONTENT_DESC, text)))
                return;
            else waitAbit(300);
        }
        Assert.fail("Элемент с альтернативным текстом '" + text + "' все еще не виден спустя 20 секунд!");
    }

    /**
     * Метод ожидает исчезновения любого элемента с альтернативным текстом
     * @param text String текст
     */
    public void waitUntilAnyElementWithContDescIsNotVisible(String text) {
        for (int i = 0; i < 60; i++) {
            if (isElementVisible(String.format(XPATH_ANY_ELEM_WITH_CONTENT_DESC, text)))
                waitAbit(300);
            else return;
        }
        Assert.fail("Элемент с альтернативным текстом '" + text + "' все еще виден спустя 20 секунд!");
    }

    /**
     * Метод кликает на любой элемент с альтернативным текстом
     * @param text String текст
     */
    public void clickAnyElementWithContDesc(String text) {
        androidDriver.findElement(By.xpath(String.format(XPATH_ANY_ELEM_WITH_CONTENT_DESC, text))).click();
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
     * Скрывает клавиатуру
     */
    public void hideAndroidKeyboard() {
        for (int i = 0; i < 50; i++) {
            try {
                if (androidDriver.isKeyboardShown())
                    androidDriver.navigate().back();
                break;
            } catch (Throwable e) {
                assertThat(e.getMessage(), i < 49);
            }
        }
    }

    /**
     * Возвращает видимость клавиатуры
     * @return boolean
     */
    public boolean isKeyBoardVisible() {
        return androidDriver.isKeyboardShown();
    }

    /**
     * Метод-обертка для поиска элемента на странице
     * @param xpath - xpath на элемент
     * @return - найден элемент или нет
     */
    public boolean elementSearch(String xpath) {
        if (findElementWithSwipeDown(xpath, 30)) return true;
        else
            return findElementWithSwipeUp(xpath, 30);
    }

    /**
     * Метод поиска элемента на странице.
     * Далее если элемент не найден, происходит countBy свайпов вниз. На каждой итерации проверяется нахождение элемента
     * @param xpath - если isID true, то передаем в xpath id элемента
     * @param countBy - количество свайпов
     * @return - элемент найден или нет
     */
    public boolean findElementWithSwipeDown(String xpath, int... countBy) {
        hideAndroidKeyboard();
        androidDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        try {
            boolean isFoundElement;
            By myElement;
            myElement = By.xpath(xpath);

            isFoundElement = androidDriver.findElements(myElement).size() > 0;
            int count = 0;
            String pageText = getXMLSource();

            if (countBy.length > 0) {
                while (!isFoundElement && count != countBy[0]) {
                    swipeByCoordinate(
                            PointOption.point(
                                    DriverUtils.deviceWidth / 2,
                                    DriverUtils.deviceHeight / 100 * 50
                            ),
                            PointOption.point(
                                    DriverUtils.deviceWidth / 2,
                                    DriverUtils.deviceHeight / 100 * 20)
                    );
                    isFoundElement = androidDriver.findElements(myElement).size() > 0;
                    count++;

                    // если структура страницы совсем не изменилась, то можно выходить. Значит мы достигли границы
                    if (getXMLSource().equals(pageText)) break;
                    else pageText = getXMLSource();
                }
            } else {
                while (!isFoundElement && count != 10) {
                    swipeByCoordinate(
                            PointOption.point(
                                    DriverUtils.deviceWidth / 2,
                                    DriverUtils.deviceHeight / 100 * 50
                            ),
                            PointOption.point(
                                    DriverUtils.deviceWidth / 2,
                                    DriverUtils.deviceHeight / 100 * 20)
                    );
                    isFoundElement = androidDriver.findElements(myElement).size() > 0;
                    count++;

                    // если структура страницы совсем не изменилась, то можно выходить. Значит мы достигли границы
                    if (getXMLSource().equals(pageText)) break;
                    else pageText = getXMLSource();
                }
            }
            androidDriver.manage().timeouts().implicitlyWait(DriverUtils.currentWait, TimeUnit.SECONDS);
            return isFoundElement;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Метод поиска элемента на странице.
     * Далее если элемент не найден, происходит countBy свайпов вверх. На каждой итерации проверяется нахождение элемента
     * @param xpath - локатор на элемент (id или xpath)
     * @param countBy - количество свайпов
     * @return - элемент найден или нет
     */
    public boolean findElementWithSwipeUp(String xpath, int... countBy) {
        hideAndroidKeyboard();
        androidDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        try {
            boolean isFoundElement;
            By myElement;
            myElement = By.xpath(xpath);

            isFoundElement = androidDriver.findElements(myElement).size() > 0;
            int count = 0;
            String pageText = getXMLSource();

            if (countBy.length > 0) {
                while (!isFoundElement && count != countBy[0]) {
                    swipeByCoordinate(
                            PointOption.point(
                                    DriverUtils.deviceWidth / 2,
                                    DriverUtils.deviceHeight / 100 * 50
                            ),
                            PointOption.point(
                                    DriverUtils.deviceWidth / 2,
                                    DriverUtils.deviceHeight / 100 * 80)
                    );
                    isFoundElement = androidDriver.findElements(myElement).size() > 0;
                    count++;

                    // если структура страницы совсем не изменилась, то можно выходить. Значит мы достигли границы
                    if (getXMLSource().equals(pageText)) break;
                    else pageText = getXMLSource();
                }
            } else {
                while (!isFoundElement && count != 10) {
                    swipeByCoordinate(
                            PointOption.point(
                                    DriverUtils.deviceWidth / 2,
                                    DriverUtils.deviceHeight / 100 * 50
                            ),
                            PointOption.point(
                                    DriverUtils.deviceWidth / 2,
                                    DriverUtils.deviceHeight / 100 * 80)
                    );
                    isFoundElement = androidDriver.findElements(myElement).size() > 0;
                    count++;

                    // если структура страницы совсем не изменилась, то можно выходить. Значит мы достигли границы
                    if (getXMLSource().equals(pageText)) break;
                    else pageText = getXMLSource();
                }
            }
            androidDriver.manage().timeouts().implicitlyWait(DriverUtils.currentWait, TimeUnit.SECONDS);
            return isFoundElement;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получает XML-структуру страницы. Периодически из-за разрыва соединения может падать в ошибку.
     * Поэтому метод выполнен с рекурсией, НО с обязательным счетчиком выхода, чтобы тест не завис
     * */
    public String getXMLSource() {
        String source = "";
        for (int i = 0; i < 50; i++) {
            try {
                source = androidDriver.getPageSource();
                break;
            } catch (Throwable e) {
                assertThat(e.getMessage(), i < 49);
            }
        }
        return source;
    }
}
