package org.aliang.demo;

import org.aliang.util.CommonUtils;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * applyToEither() 把两个异步任务做比较，异步任务先得到结果的，就对其获得的结果进行下一步操作。
 *
 * @author ll
 * @date 2024/3/29 09:37
 * @see ApplyToEitherDemo
 */
public class ApplyToEitherDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

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
        CompletableFuture<Integer> future3 = future1.applyToEither(future2, result -> {
            CommonUtils.printTheadLog("最先到达的是" + result);
            return result;
        });


        CommonUtils.sleepSecond(4);

        Integer ret = future3.get();
        CommonUtils.printTheadLog("ret =" + ret);

        // 异步任务交互指两个异步任务，哪个结果先到，就使用哪个结果（先到先用）
    }

}
