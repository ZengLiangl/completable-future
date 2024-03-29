package org.aliang.demo;

import org.aliang.util.CommonUtils;

import java.util.concurrent.CompletableFuture;

/**
 * 如果我们只是想从CompletableFuture的链式操作得到一个完成的通知，甚至都不使用上一个链式操作的结果，
 * 那么CompletableFuture.thenRun()会是你最佳的选择，它需要一个Runnable并返回CompletableFuture<Void>
 *
 * @author ll
 * @date 2024/3/28 15:56
 * @see ThenRunDemo
 */
public class ThenRunDemo {
    public static void main(String[] args) {
        // 需求：仅仅想知道 filter_words.txt 的文件是否读取完成
        CommonUtils.printTheadLog("main start");

        CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("开始读取filter_words.txt");
            return CommonUtils.readFile("filter_words.txt");
        }).thenRun(() -> CommonUtils.printTheadLog("读取filter_words.txt文件完成"));
        // 这些带了Async的异步回调通过在单独的线程中执行回调任务来帮我们去进一步促进并行计算
        //         .thenApplyAsync(content -> {
        //     CommonUtils.printTheadLog("读取filter_words.txt文件完成");
        //     return content.split(",");
        // });

        CommonUtils.printTheadLog("main continue");
        CommonUtils.sleepSecond(4);
        CommonUtils.printTheadLog("main end");

    }
}
