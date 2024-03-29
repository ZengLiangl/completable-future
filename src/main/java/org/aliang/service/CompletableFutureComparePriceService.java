package org.aliang.service;

import org.aliang.HttpRequest;
import org.aliang.pojo.PriceResult;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * 方案三：使用 CompletableFuture 进一步增强并行
 *
 * @author ll
 * @date 2024/3/29 21:43
 * @see CompletableFutureComparePriceService
 */
public class CompletableFutureComparePriceService {
    public PriceResult getCheapestPlatformPrice(String productName) {
        // 获取淘宝平台的价格和优惠
        CompletableFuture<PriceResult> taoBaoCF = CompletableFuture
                .supplyAsync(() -> HttpRequest.getTaoBaoPrice(productName))
                .thenCombine(CompletableFuture.supplyAsync(() -> HttpRequest.getTaoBaoDiscount(productName)), ComparePriceService::computeRealPrice);

        // 获取京东平台的价格和优惠
        CompletableFuture<PriceResult> jDongCF = CompletableFuture
                .supplyAsync(() -> HttpRequest.getJDongPrice(productName))
                .thenCombine(CompletableFuture.supplyAsync(() -> HttpRequest.getJDongDiscount(productName)), ComparePriceService::computeRealPrice);
        // 获取拼多多平台的价格和优惠
        CompletableFuture<PriceResult> pddCF = CompletableFuture
                .supplyAsync(() -> HttpRequest.getPDDPrice(productName))
                .thenCombine(CompletableFuture.supplyAsync(() -> HttpRequest.getPDDDiscount(productName)), ComparePriceService::computeRealPrice);

        return Stream.of(taoBaoCF, jDongCF, pddCF)
                .map(CompletableFuture::join)
                .min(Comparator.comparing(PriceResult::getRealPrice))
                .get();
    }

    public PriceResult batchComparePrice(List<String> products) {
        // 遍历每个商品，根据商品开启异步任务获取最终价，然后归集到List集合
        List<CompletableFuture<PriceResult>> completableFutures = products.stream()
                .map(product -> CompletableFuture
                        .supplyAsync(() -> HttpRequest.getTaoBaoPrice(product))
                        .thenCombine(CompletableFuture.supplyAsync(() -> HttpRequest.getTaoBaoDiscount(product)), ComparePriceService::computeRealPrice)).toList();

        // 把多个商品的最终价进行排序比较获取最小值
        return completableFutures
                .stream()
                .map(CompletableFuture::join)
                .sorted(Comparator.comparing(PriceResult::getRealPrice))
                .findFirst()
                .get();
    }

    public static void main(String[] args) {
        CompletableFutureComparePriceService service = new CompletableFutureComparePriceService();

        // 方案三测试：使用 CompletableFuture 进一步增强并行
        long start = System.currentTimeMillis();
        PriceResult priceResult = service.getCheapestPlatformPrice("iPhone");
        long end = System.currentTimeMillis();

        double costTime = (end - start) / 1000.0;
        System.out.printf("cost %.2f second \n", costTime);

        System.out.println("priceResult = " + priceResult);

        // 测试在一个平台比较同款产品(iPhone14)不同色系的价格
        start = System.currentTimeMillis();
        List<String> products = Arrays.asList("iPhone14黑色", "iPhone14白色", "iPhone14玫瑰红");
        PriceResult res = service.batchComparePrice(products);
        end = System.currentTimeMillis();
        System.out.printf("cost %.2f second \n", costTime);
        System.out.println("priceResult = " + res);
    }
}
