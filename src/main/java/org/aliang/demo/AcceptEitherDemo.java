package org.aliang.demo;

import org.aliang.util.CommonUtils;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * acceptEither()把两个异步任务做比较，异步任务先到结果的，就对先到的结果进行下一步操作(消费使用)
 *
 * @author ll
 * @date 2024/3/29 09:43
 * @see AcceptEitherDemo
 */
public class AcceptEitherDemo {
    public static void main(String[] args) {

        // 异步任务1
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int x = new Random().nextInt(3);
            CommonUtils.sleepSecond(x);
            CommonUtils.printTheadLog("任务1耗时" + x + "秒");
            return x;
        });

        // 异步任务2
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            int y = new Random().nextInt(3);
            CommonUtils.sleepSecond(y);
            CommonUtils.printTheadLog("任务2耗时" + y + "秒");
            return y;
        });

        // 哪个异步任务结果先到达，使用哪个异步任务的结果
        future1.acceptEither(future2, result -> {
            CommonUtils.printTheadLog("最先到达的是" + result);
        });

        CommonUtils.sleepSecond(4);
    }

}
