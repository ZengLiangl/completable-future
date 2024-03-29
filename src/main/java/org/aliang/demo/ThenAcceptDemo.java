package org.aliang.demo;

import org.aliang.util.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * 如果不想从回调函数返回结果，而只想在Future完成后运行一些代码，则可以使用thenAccpet()，
 * 这些方法是一个Consumer<? super T>，它可以对异步任务的执行结果进行消费使用，方法返回CompletableFuture
 *
 * @author ll
 * @date 2024/3/28 15:21
 * @see ThenAcceptDemo
 */
public class ThenAcceptDemo {

    public static void main(String[] args) throws InterruptedException {
        CommonUtils.printTheadLog("main start");

        // 链式操作
        CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("读取filter_words.txt文件");
            return CommonUtils.readFile("filter_words.txt");
        }).thenApply(content -> {
            CommonUtils.printTheadLog("把文件内容转换成敏感词数组");
            return content.split(",");
        }).thenAccept(fildWord -> CommonUtils.printTheadLog(Arrays.toString(fildWord)));

        // 让异步任务执行完毕
        Thread.sleep(4000);
        CommonUtils.printTheadLog("main end");
    }
}
