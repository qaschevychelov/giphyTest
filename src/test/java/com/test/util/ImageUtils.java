package com.test.util;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static com.test.util.SourceDriver.androidDriver;

public class ImageUtils {
    /**
     * Вызывать этот метод, когда искомый элемент полностью попадает в экран
     */
    public static void checkIfGifIsRunning(String xpath) {
        try {
            WebElement ele = androidDriver.findElement(By.xpath(xpath));

            // Делаем скрин всего экрана
            File screenshot = ((TakesScreenshot) androidDriver).getScreenshotAs(OutputType.FILE);
            BufferedImage fullImg = ImageIO.read(screenshot);

            // Получаем позицию элемента на экране
            Point point = ele.getLocation();

            // Ширина и высота элемента
            int eleWidth = ele.getSize().getWidth();
            int eleHeight = ele.getSize().getHeight();

            // Обрезаем полный скрин на скрин элемента, ждем и делаем второй скрин
            BufferedImage firstImg = fullImg.getSubimage(point.getX(), point.getY(),
                    eleWidth, eleHeight);
            Thread.sleep(2000);
            BufferedImage secondImg = fullImg.getSubimage(point.getX(), point.getY(),
                    eleWidth, eleHeight);

            // В цикле сравниваем попиксельно 2 скриншота
            // Из-за разных устройств рендеринг элементов происходит немного по-разному. Поэтому в сравнении цвета введем цветовую погрешность
            for (int i = 0; i < secondImg.getWidth(); i++) {
                for (int j = 0; j < secondImg.getHeight(); j++) {
                    Color expColor = new Color(secondImg.getRGB(i, j));
                    Color actColor = new Color(firstImg.getRGB(i, j));
                    if (!((Math.abs((expColor.getRed() - actColor.getRed())) < 10) &&
                            (Math.abs((expColor.getGreen() - actColor.getGreen())) < 10) &&
                            (Math.abs((expColor.getBlue() - actColor.getBlue())) < 10))) {

                        Assert.fail("координаты x:" + i + " y:" + j + "цвет ожидаемого файла: " + expColor + "цвет фактического файла: " + actColor);
                    }
                }
            }

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
