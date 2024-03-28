package org.aliang.demo;

import org.aliang.util.CommonUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ll
 * @date 2024/3/28 14:18
 * @see SupplyAsyncDemo
 */
public class SupplyAsyncDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        CommonUtils.printTheadLog("main start");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            String s = CommonUtils.readFile("new.txt");
            CommonUtils.printTheadLog(s);
            return s;
        }, pool);
        CommonUtils.printTheadLog("here are not blocked,main continue");
        // 阻塞并等待future完成
        String s = future.get();
        CommonUtils.printTheadLog("news: " + s);
        // 关闭线程池
        pool.shutdown();

        CommonUtils.printTheadLog("main end");
    }
}
