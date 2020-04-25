package com.test.pages;

import static com.codeborne.selenide.Condition.*;
import com.codeborne.selenide.SelenideElement;
import com.test.util.DriverUtils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class SearchPage extends BasePage {

    /**
     * кнопка Назад
     */
    private final SelenideElement back = $(By.xpath("//*[@resource-id='co.fun.testgiphy:id/tbFragmentSearch']/android.widget.ImageButton"));

    /**
     * поле поиска
     */
    private final SelenideElement searchField = $(By.xpath("//*[@resource-id='co.fun.testgiphy:id/tbFragmentSearch']" +
            "/*[@resource-id='co.fun.testgiphy:id/etSearchToolbar']"));

    /**
     * Дожидается отображения кнопки Назад
     */
    public void waitUntilBackIsVisible() {
        back.waitUntil(visible, DriverUtils.currentWait);
    }

    /**
     * Возвращает видимость кнопки Назад
     * @return boolean
     */
    public boolean isBackBtnVisible() {
        return back.is(visible);
    }

    /**
     * Метод возвращает видимость строки поиска с текстом
     * @param search String текст поиска
     * @return boolean
     */
    public boolean isSearchFieldWithTextVisible(String search) {
        return searchField.is(visible) && searchField.has(text(search));
    }

    /**
     * Метод устанавливает значение поля поиска
     * @param text String значение
     */
    public void typeSearchField(String text) {
        searchField.setValue(text);
    }

    /**
     * Метод кликает по полю поиска
     */
    public void clickSearchField() {
        searchField.click();
    }

    /**
     * Метод нажимает кнопку Назад в шапке
     */
    public void clickHeaderBackBtn() {
        back.click();
    }
}
