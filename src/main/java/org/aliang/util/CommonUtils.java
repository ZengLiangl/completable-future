package org.aliang.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * @author ll
 * @date 2024/3/27 21:32
 * @see CommonUtils
 */
public class CommonUtils {
    /**
     * 读取文件路径的文件
     *
     * @param pathToFile
     * @return String
     */
    public static String readFile(String pathToFile) {
        try {
            return Files.readString(Paths.get(pathToFile));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 休眠指定的毫秒数
     *
     * @param millis
     */
    public static void sleepMillis(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 休眠指定的秒数
     *
     * @param second
     */
    public static void sleepSecond(long second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印输出带线程信息的日志
     *
     * @param message
     */
    public static void printTheadLog(String message) {
        // 时间戳 | 线程id | 线程名 | 日志信息
        String result = new StringJoiner(" | ")
                .add(String.valueOf(System.currentTimeMillis()))
                .add(String.format("%2d", Thread.currentThread().getId()))
                .add(Thread.currentThread().getName())
                .add(message)
                .toString();
        System.out.println(result);
    }

    private static String getCurrentTime() {
        LocalTime time = LocalTime.now();
        return time.format(DateTimeFormatter.ofPattern("[HH:mm:ss.SSS]"));
    }

    public static void main(String[] args) {
        String x = readFile("new.txt");
        printTheadLog(x);
    }
}
