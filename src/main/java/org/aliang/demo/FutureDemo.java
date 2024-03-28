package org.aliang.demo;

import org.aliang.util.CommonUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author ll
 * @date 2024/3/27 21:51
 * @see FutureDemo
 */
public class FutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        // 1、读取敏感词 thread1
        Future<String[]> futureWords = executor.submit(() -> CommonUtils.readFile("filter_words.txt").split(","));

        // 3、解析文件 thread2
        Future<String> futureNews = executor.submit(() -> CommonUtils.readFile("new.txt"));

        // 2、替换操作 thread3
        Future<String> filter = executor.submit(() -> {
            String[] words = futureWords.get();
            String news = futureNews.get();
            for (String word : words) {
                if (news.contains(word)) {
                    news = news.replace(word, "**");
                }
            }
            return news;
        });
        // 4、打印替换后的新闻稿 main
        System.out.println(filter.get());
    }
}
