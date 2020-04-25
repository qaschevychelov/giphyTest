package com.test.pages;

import com.codeborne.selenide.SelenideElement;
import com.test.util.DriverUtils;
import com.test.util.ImageUtils;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage extends BasePage{
    public MainPage() {}

    /**
     * Контейнер гифки
     */
    private final SelenideElement gifCont = $(By.xpath("//*[@resource-id='co.fun.testgiphy:id/llGiphyCardContainer']"));

    /**
     * заголовок экрана
     */
    private final SelenideElement headerTitle = $(By.xpath("//*[@resource-id='co.fun.testgiphy:id/tbFragmentTrending']" +
            "/android.widget.TextView"));

    /**
     * Кнопка поиска в хедере
     */
    private final SelenideElement headerSearchBtn = $(By.xpath("//*[@resource-id='co.fun.testgiphy:id/tbFragmentTrending']" +
            "//*[@resource-id='co.fun.testgiphy:id/menu_trending_search']"));

    /**
     * Сама гифка
     */
    private final String GIF_IMG = ".//*[@resource-id='co.fun.testgiphy:id/ivGiphyCard']";

    /**
     * Заголовок гифки
     */
    private final String GIF_TITLE = ".//*[@resource-id='co.fun.testgiphy:id/tvGiphyCardTitle']";

    /**
     * Автор гифки
     */
    private final String GIF_SOURCE = ".//*[@resource-id='co.fun.testgiphy:id/tvGiphyCardUsername']";

    private final List<SelenideElement> appElems = $$(By.xpath("//*[@package='co.fun.testgiphy']"));

    /**
     * Метод дожидается видимости плитки с гифкой
     */
    public void waitUntilGifIsVisible() {
        gifCont.waitUntil(visible, DriverUtils.currentWait);
    }

    /**
     * Метод дожидается видимости заголовка плитки с гифкой
     */
    public void waitUntilGifTitleIsVisible() {
        gifCont.find(By.xpath(GIF_TITLE)).waitUntil(visible, DriverUtils.currentWait);
    }

    /**
     * Метод дожидается видимости автора плитки с гифкой
     */
    public void waitUntilGifSourceIsVisible() {
        gifCont.find(By.xpath(GIF_SOURCE)).waitUntil(visible, DriverUtils.currentWait);
    }

    /**
     * Метод возвращает видимость заголовка
     * @param text String заголовок
     * @return boolean
     */
    public boolean isTitleVisible(String text) {
        return headerTitle.is(visible) && headerTitle.has(text(text));
    }

    /**
     * Метод возвращает видимость кнопки поиска в шапке
     * @return boolean
     */
    public boolean isSearchBtnVisible() {
        return headerSearchBtn.is(visible);
    }

    /**
     * Метод возвращает видимость 1 гифки
     * @return boolean
     */
    public boolean isGifVisible() {
        return gifCont.find(By.xpath(GIF_IMG)).is(visible);
    }

    /**
     * Метод возвращает видимость заголовка 1 гифки
     * @return boolean
     */
    public boolean isGifTitleVisible() {
        return gifCont.find(By.xpath(GIF_TITLE)).is(visible);
    }

    /**
     * Метод возвращает видимость автора 1 гифки
     * @return boolean
     */
    public boolean isGifSourceVisible() {
        return gifCont.find(By.xpath(GIF_SOURCE)).is(visible);
    }

    /**
     * Метод возвращает текст заголовка в гифке
     * @return String
     */
    public String getGifTitle() {
        return gifCont.find(By.xpath(GIF_TITLE)).getText();
    }

    /**
     * Возвращает заголовки гифок
     * @return List
     */
    public List<String> getGifTitles() {
        return gifCont.findAll(By.xpath(GIF_TITLE)).stream().map(SelenideElement::getText).collect(Collectors.toList());
    }

    /**
     * Возвращает количество заголовков гифок
     * @return int
     */
    public int getGifTitleCount() {
        return $$(By.xpath(GIF_TITLE)).size();
    }

    /**
     * Метод возвращает текст автора в гифке
     * @return String
     */
    public String getGifSource() {
        return gifCont.find(By.xpath(GIF_SOURCE)).getText();
    }

    /**
     * Метод проверяет, что гифка двигается
     */
    public void gifShouldBeRunning() {
        ImageUtils.checkIfGifIsRunning(GIF_IMG);
    }

    /**
     * Кликает по кнопке поиска
     */
    public void clickSearchBtn() {
        headerSearchBtn.click();
    }

    /**
     * Метод нажимает на гифку
     */
    public void clickGif() {
        gifCont.click();
    }

    /**
     * Метод возвращает количество элементов приложения на экране
     */
    public int getAppElemsCount() {
        return appElems.size();
    }
}
