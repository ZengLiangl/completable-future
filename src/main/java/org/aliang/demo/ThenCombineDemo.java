package org.aliang.demo;

import org.aliang.util.CommonUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 我们已经知道，当其中一个Future依赖于另一个Future,使用thenCompose()用于组合两个Future。
 * 如果两个Future之间没有依赖关系，你希望两个Future独立运行并在两者都完成之后执行回调操作时，则使用thenCombine()
 * 当两个Future都完成时，才将两个异步任务的结果传递给thenCombine的回调函数进一步处理!!!
 *
 * @author ll
 * @date 2024/3/28 17:02
 * @see ThenCombineDemo
 */
public class ThenCombineDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 需求：替换新闻稿(news.txt)中敏感词汇，把敏感词汇替换成*，敏感词存储在filter_.words.txt中

        CommonUtils.printTheadLog("main start");

        // 读取敏感词并转为敏感词数组
        CompletableFuture<String[]> future1 = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("开始读取filter_words.txt");
            String readFile = CommonUtils.readFile("filter_words.txt");
            CommonUtils.printTheadLog("开始转为敏感词数组");
            return readFile.split(",");
        });

        // 读取新闻稿(news.txt)
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> CommonUtils.readFile("new.txt"));

        // 替换敏感词
        CompletableFuture<String> future3 = future1.thenCombine(future2, (words, newsContent) -> {
            for (String word : words) {
                if (newsContent.indexOf(word) > 0) {
                    newsContent = newsContent.replace(word, "**");
                }
            }
            return newsContent;
        });

        CommonUtils.printTheadLog("main continue");
        String content = future3.get();

        CommonUtils.printTheadLog("content=" + content);
        CommonUtils.printTheadLog("main stop");
    }
}
