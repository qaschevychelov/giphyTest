package com.test;

import com.codeborne.selenide.WebDriverRunner;
import com.test.steps.GiphySteps;
import com.test.util.SourceDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class GiphyTest {
    private GiphySteps giphySteps;

    // переменные теста
    private List<String> gifTitles;
    private String longValue = "odfgoi jofigjsd ojosdj l sdfjl jodfgoi jofigjsd ojosdj l sdfjl jodfgoi jofigjsd ojosdj l sdfjl jodfgoi jofigjsd ojosdj l sdfjl jodfgoi jofigjsd ojosdj l sdfjl j ";

    @BeforeClass
    public void before() {
        WebDriverRunner.setWebDriver(SourceDriver.getDriver());
        giphySteps = new GiphySteps();
    }

    @AfterClass
    public void after() {
        SourceDriver.androidDriver.quit();
    }

    @Description("Проверка обязательных элементов экрана")
    @Test(description = "Проверка обязательных элементов экрана")
    public void step_01() {
        giphySteps.openApp();

        // проверим элементы
        giphySteps.checkIfHeaderTitleIsVisible("Giphy", true);
        giphySteps.checkIfBackBtnIsVisible(false);
        giphySteps.checkIfSearchBtnIsVisible();
        giphySteps.checkIfGifIsVisible();

        gifTitles = giphySteps.getVisibleGifTitles();
    }

    @Description("Проверка нажатия на гифку")
    @Test(description = "Проверка нажатия на гифку")
    public void step_02() {
        giphySteps.clickGif();
        // подождем, чтобы убедиться, что ничего не произошло
        giphySteps.waitabit(5000);

        // проверки
        giphySteps.checkIfHeaderTitleIsVisible("Giphy", true);
        giphySteps.checkIfBackBtnIsVisible(false);
        giphySteps.checkIfSearchBtnIsVisible();
        giphySteps.checkIfGifIsVisible();
        assertThat(
                "Гифки изменились!",
                giphySteps.getVisibleGifTitles(),
                is(equalTo(gifTitles))
        );
    }

    @Description("Проверка подгрузки новых гифок")
    @Test(description = "Проверка подгрузки новых гифок")
    public void step_03() {
        giphySteps.swipeScreenDown(5);
        assertThat(
                "Новые гифки не подгружаются!",
                giphySteps.getVisibleGifTitles(),
                is(not(equalTo(gifTitles)))
        );
    }

    @Description("Проверка анимации новых гифок")
    @Test(description = "Проверка анимации новых гифок")
    public void step_04() {
        giphySteps.checkIfGifIsRunning();
    }

    @Description("Проверка элементов экрана поиска по умолчанию")
    @Test(description = "Проверка элементов экрана поиска по умолчанию")
    public void step_05() {
        giphySteps.clickSearchBtn();

        // проверки
        giphySteps.checkIfKeyBoardIsVisible(false);
        giphySteps.checkHeaderSearchText("Search");
        giphySteps.checkIfHeaderTitleIsVisible("Giphy", false);
        giphySteps.checkIfBackBtnIsVisible(true);
        assertThat(
                "Отображаются гифки! По умолчанию без поиска не должны",
                giphySteps.getGifTitleCount() == 0
        );
        giphySteps.checkIfTextIsVisible("Empty list", false);
    }

    @Description("Поиск пробелов")
    @Test(description = "Поиск пробелов")
    public void step_06() {
        giphySteps.search("      ");
        // проверки
        assertThat(
                "Отображаются гифки!",
                giphySteps.getGifTitleCount() == 0
        );
        giphySteps.checkIfTextIsVisible("Empty list", true);
    }

    @Description("Поиск нормального значения")
    @Test(description = "Поиск нормального значения")
    public void step_07() {
        giphySteps.search("test");
        // проверки
        assertThat(
                "Не отображаются гифки!",
                giphySteps.getGifTitleCount() > 0
        );
        giphySteps.checkIfGifIsVisible();
        giphySteps.checkIfGifIsRunning();
        giphySteps.checkIfTextIsVisible("Empty list", false);
        gifTitles = giphySteps.getVisibleGifTitles();
    }

    @Description("Проверка подгрузки новых гифок на экране поиска")
    @Test(description = "Проверка подгрузки новых гифок на экране поиска")
    public void step_08() {
        giphySteps.swipeScreenDown(5);
        assertThat(
                "Новые гифки не подгружаются!",
                giphySteps.getVisibleGifTitles(),
                is(not(equalTo(gifTitles)))
        );
    }

    @Description("Проверка открытия гифки на экране поиска")
    @Test(description = "Проверка открытия гифки на экране поиска")
    public void step_09() {
        giphySteps.clickGif();
        // подождем, чтобы убедиться, что ничего не произошло
        giphySteps.waitabit(5000);

        // проверки
        giphySteps.checkIfHeaderTitleIsVisible("Giphy", false);
        giphySteps.checkIfBackBtnIsVisible(true);
        giphySteps.checkHeaderSearchText("test");
        giphySteps.checkIfGifIsVisible();
        assertThat(
                "Гифки изменились!",
                giphySteps.getVisibleGifTitles(),
                is(equalTo(gifTitles))
        );
    }

    @Description("Поиск очень длинного значения")
    @Test(description = "Поиск очень длинного значения")
    public void step_10() {
        giphySteps.search(longValue);
        // проверки
        assertThat(
                "Отображаются гифки!",
                giphySteps.getGifTitleCount() == 0
        );
        giphySteps.checkIfTextIsVisible("Empty list", true);
    }

    @Description("Поиск очень спецсимволов")
    @Test(description = "Поиск очень спецсимволов")
    public void step_11() {
        // необходимо, чтобы избежать маскирования
        giphySteps.search("test");
        assertThat(
                "Не отображаются гифки!",
                giphySteps.getGifTitleCount() > 0
        );
        giphySteps.checkIfTextIsVisible("Empty list", false);
        giphySteps.search("!\"№;%:?*)(*?:%;");
        // проверки
        assertThat(
                "Отображаются гифки!",
                giphySteps.getGifTitleCount() == 0
        );
        giphySteps.checkIfTextIsVisible("Empty list", true);
    }

    @Description("Проверка клавиатуры")
    @Test(description = "Проверка клавиатуры")
    public void step_12() {
        giphySteps.clickSearchField();
        // проверка
        giphySteps.checkIfKeyBoardIsVisible(true);
        // аппаратная кнопка Назад
        giphySteps.clickHardwareBackBtn();
        // проверка
        giphySteps.checkIfKeyBoardIsVisible(false);
    }

    @Description("Возврат на домашний экран с помощью программной кнопки Назад")
    @Test(description = "Возврат на домашний экран с помощью программной кнопки Назад")
    public void step_13() {
        giphySteps.clickHeaderBackBtn();

        // проверим элементы
        giphySteps.checkIfHeaderTitleIsVisible("Giphy", true);
        giphySteps.checkIfBackBtnIsVisible(false);
        giphySteps.checkIfSearchBtnIsVisible();
        giphySteps.checkIfGifIsVisible();
    }

    @Description("Возврат на домашний экран с помощью аппаратной кнопки Назад")
    @Test(description = "Возврат на домашний экран с помощью аппаратной кнопки Назад")
    public void step_14() {
        giphySteps.clickSearchBtn();
        giphySteps.clickHardwareBackBtn();

        // проверим элементы
        giphySteps.checkIfHeaderTitleIsVisible("Giphy", true);
        giphySteps.checkIfBackBtnIsVisible(false);
        giphySteps.checkIfSearchBtnIsVisible();
        giphySteps.checkIfGifIsVisible();
    }

    @Description("Проверка закрытия приложения")
    @Test(description = "Проверка закрытия приложения")
    public void step_15() {
        giphySteps.clickHardwareBackBtn();
        giphySteps.checkIfAppIsClosed();
    }
}