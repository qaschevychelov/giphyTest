package com.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;


public class DriverUtils {
    /**
     * для хранения текущего implicitly wait
     */
    public static long currentWait;

    /**
     * ширина экрана
     */
    public static int deviceWidth;

    /**
     * высота экрана
     */
    public static int deviceHeight;

    /**
     * Метод выполняет bash команды на телефоне
     * @param command String команда
     */
    public static void invokeCmdCommand(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            assertThat(e.getMessage(), false);
        }
        try (
                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(Objects.requireNonNull(process).getInputStream()));
                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(process.getErrorStream()))
        ) {
            String msg;
            // считываем вывод команды
            while ((msg = stdInput.readLine()) != null) {
                System.out.println("Команда вернула следующее сообщение: " + msg);
            }
            // считываем ошибки, если есть
            while ((msg = stdError.readLine()) != null) {
                System.out.println("Результат команды: " + msg);
            }
        } catch (IOException e) {
            assertThat(e.getMessage(), false);
        }
    }
}
