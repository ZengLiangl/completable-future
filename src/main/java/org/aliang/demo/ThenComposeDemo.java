package org.aliang.demo;

import org.aliang.util.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 编排2个依赖关系的异步任务 thenCompose()
 * 把上一步异步任务的结果，转成一个CompletableFuture对象，这个Completable对象中包含本次异步任务处理后的结果。
 * 也就是说，我们想结合上一步异步任务的结果得到下一个新的异步任务中，结果由这个新的异步任务返回
 * 需要使用 thenCompose() 方法代替， 我们可以把它理解为异步任务的组合
 *
 * @author ll
 * @date 2024/3/28 16:45
 * @see ThenComposeDemo
 */
public class ThenComposeDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 异步读取filter_words.txt文件中的内容，读取完成后，转换成敏感词数组，主线程获取结果打印输出这个数组
        // thnCompose

        CommonUtils.printTheadLog("main start");

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("开始读取filter_words.txt");
            return CommonUtils.readFile("filter_words.txt");
        }).thenCompose(content -> CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("开始转换成敏感词数组");
            return content.split(",");
        })).thenCompose(content -> CompletableFuture.runAsync(() -> {
            CommonUtils.printTheadLog(Arrays.toString(content));
        }));

        CommonUtils.printTheadLog("main continue");
        future.get();

        // CommonUtils.printTheadLog("strings =" + Arrays.toString(strings));
        CommonUtils.printTheadLog("main stop");

    }
}
