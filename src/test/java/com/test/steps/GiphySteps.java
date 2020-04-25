package com.test.steps;

import com.test.pages.MainPage;
import com.test.pages.SearchPage;
import com.test.util.DriverUtils;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

import static io.appium.java_client.touch.offset.PointOption.point;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GiphySteps {
    private MainPage mainPage;
    private SearchPage searchPage;
    public GiphySteps() {
        mainPage = new MainPage();
        searchPage = new SearchPage();
    }

    @Step("Открываем приложение")
    public void openApp() {
        mainPage.waitUntilGifIsVisible();
        mainPage.waitUntilGifTitleIsVisible();
        mainPage.waitUntilGifSourceIsVisible();
    }

    @Step("Проверка видимости заголовка")
    public void checkIfHeaderTitleIsVisible(String text, boolean isVisible) {
        assertThat(
                "Заголовок '" + text + "' " + (isVisible ? "не " : "") + "отображается!",
                mainPage.isTitleVisible(text),
                is(equalTo(isVisible))
        );
    }

    @Step("Проверка видимости кнопки поиска в шапке")
    public void checkIfSearchBtnIsVisible() {
        assertThat(
                "Кнопка поиска в шапке не отображается",
                mainPage.isSearchBtnVisible()
        );
    }

    @Step("Проверка плитки одной гифки")
    public void checkIfGifIsVisible() {
        assertThat(
                "Гифка не отображается!",
                mainPage.isGifVisible()
        );
        assertThat(
                "Заголовок гифки не отображается или он пустой!",
                mainPage.isGifTitleVisible()
                && !mainPage.getGifTitle().isEmpty()
        );
        assertThat(
                "Автор гифки не отображается или он пустой!",
                mainPage.isGifSourceVisible()
                && !mainPage.getGifSource().isEmpty()
        );
        mainPage.gifShouldBeRunning();
    }

    @Step("запоминаем заголовки гифок")
    public List<String> getVisibleGifTitles() {
        return mainPage.getGifTitles();
    }

    @Step("Крутим экран вниз")
    public void swipeScreenDown(int count) {
        int offsetX = DriverUtils.deviceWidth / 2;
        int startY = DriverUtils.deviceHeight / 100 * 70;
        int endY = DriverUtils.deviceHeight / 100 * 30;
        for (int i = 0; i < count; i++) {
            mainPage.swipeByCoordinate(
                    point(offsetX, startY),
                    point(offsetX, endY)
            );
            mainPage.waitUntilGifIsVisible();
        }
    }

    @Step("Проверим движение анимации")
    public void checkIfGifIsRunning() {
        mainPage.gifShouldBeRunning();
    }

    @Step("Переходим в поиск")
    public void clickSearchBtn() {
        mainPage.clickSearchBtn();
        searchPage.waitUntilBackIsVisible();
    }

    @Step("Проверяем видимость клавиатуры")
    public void checkIfKeyBoardIsVisible(boolean isVisible) {
        assertThat(
                "Клавиатура " + (isVisible ? "не " : "") + "отображается!",
                searchPage.isKeyBoardVisible(),
                is(equalTo(isVisible))
        );
    }

    @Step("Проверяем текст в строке поиска")
    public void checkHeaderSearchText(String search) {
        assertThat(
                "В строке поиска текст '" + search + "' не отображается",
                searchPage.isSearchFieldWithTextVisible(search)
        );
    }

    @Step("Проверяем количество заголовков гифок на экране")
    public int getGifTitleCount() {
        return mainPage.getGifTitleCount();
    }

    @Step("Ищем по значению")
    public void search(String s) {
        searchPage.typeSearchField(s);
        searchPage.waitAbit(2000);
        searchPage.waitUntilProgressBatIsNotVisible();
    }

    @Step("Проверяем отображение текст")
    public void checkIfTextIsVisible(String text, boolean isVisible) {
        assertThat(
                "Текст '" + text + "' " + (isVisible ? "не " : "") + "отображается!",
                searchPage.isElementVisibleByText(text),
                is(equalTo(isVisible))
        );
    }

    @Step("Нажимаем на первую гифку")
    public void clickGif() {
        mainPage.clickGif();
    }

    @Step("ждем")
    public void waitabit(int i) {
        mainPage.waitAbit(i);
    }

    @Step("Проверяем видимость кнопки Назад")
    public void checkIfBackBtnIsVisible(boolean isVisible) {
        assertThat(
                "Кнопка назад " + (isVisible ? "не " : "") + "отображается",
                searchPage.isBackBtnVisible(),
                is(equalTo(isVisible))
        );
    }

    @Step("нажимаем на поле поиска")
    public void clickSearchField() {
        searchPage.clickSearchField();
        searchPage.waitAbit(1000);
    }

    @Step("Нажимаем аппаратную кнопку Назад")
    public void clickHardwareBackBtn() {
        searchPage.clickHardwareBackBtn();
    }

    @Step("Нажимаем кнопку Назад в шапке")
    public void clickHeaderBackBtn() {
        searchPage.clickHeaderBackBtn();
        searchPage.waitUntilProgressBatIsNotVisible();
        mainPage.waitUntilGifIsVisible();
    }

    @Step("Приложение закрыто")
    public void checkIfAppIsClosed() {
        boolean isClosed = false;
        for (int i = 0; i < 5; i++) {
            if (mainPage.getAppElemsCount() == 0) {
                isClosed = true;
                break;
            } else
                mainPage.waitAbit(2000);
        }
        assertThat(
                "Приложение не закрылось!",
                isClosed
        );
    }
}
