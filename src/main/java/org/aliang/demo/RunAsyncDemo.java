package org.aliang.demo;

import org.aliang.util.CommonUtils;

import java.util.concurrent.CompletableFuture;

/**
 * @author ll
 * @date 2024/3/28 14:05
 * @see RunAsyncDemo
 */
public class RunAsyncDemo {

    public static void main(String[] args) {
        CommonUtils.printTheadLog("main start");
        CompletableFuture.runAsync(() -> {
            CommonUtils.printTheadLog("读取文件开始");
            // 使用睡眠来模拟一个长时间的工作任务（例如读取文件，网络请求等）
            CommonUtils.sleepSecond(3);
            String news = CommonUtils.readFile("new.txt");
            System.out.println(news);
        });
        CommonUtils.printTheadLog("here are not blacked main continue");
        // 此处休眠 为的是等待CompletableFuture背后的线程池执行完成。
        CommonUtils.sleepSecond(4);
        CommonUtils.printTheadLog("main end");
    }
}
