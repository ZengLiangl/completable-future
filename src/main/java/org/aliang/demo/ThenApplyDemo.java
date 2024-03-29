package org.aliang.demo;

import org.aliang.util.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 使用thenApply()方法可以处理和转换CompletableFuture的结果，
 * 它以Function<T, U>作为参数。 Function<T, U>是一个函数式接口，表示一个转换操作，它接受类型T的参数并产生类型R的结果
 *
 * @author ll
 * @date 2024/3/28 15:14
 * @see ThenApplyDemo
 */
public class ThenApplyDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CommonUtils.printTheadLog("main start");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("开始读取filter_wors.txt文件");
            String str = CommonUtils.readFile("filter_words.txt");
            return str;
        });
        CompletableFuture<String[]> apply = future.thenApply(context -> {
            CommonUtils.printTheadLog("开始读取敏感词组");
            String[] split = context.split(",");
            return split;
        });
        CommonUtils.printTheadLog("main lock");
        String[] strings = apply.get();
        String splitWord = Arrays.toString(strings);
        CommonUtils.printTheadLog("splitWord =" + splitWord);
    }
}
