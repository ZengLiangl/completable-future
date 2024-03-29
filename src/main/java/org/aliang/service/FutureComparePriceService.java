package org.aliang.service;

import org.aliang.HttpRequest;
import org.aliang.pojo.PriceResult;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import static org.aliang.service.ComparePriceService.computeRealPrice;

/**
 * 方案二：使用Future+线程池增强并行
 *
 * @author ll
 * @date 2024/3/29 21:40
 * @see FutureComparePriceService
 */
public class FutureComparePriceService {
    public PriceResult getCheapestPlatformPrice(String productName) {
        // 线程池
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 获取淘宝平台的价格和优惠
        Future<PriceResult> taoBaoFuture = executor.submit(() -> {
            PriceResult priceResult = HttpRequest.getTaoBaoPrice(productName);
            int discount = HttpRequest.getTaoBaoDiscount(productName);
            return computeRealPrice(priceResult, discount);
        });

        // 获取京东平台的价格和优惠
        Future<PriceResult> jDongFuture = executor.submit(() -> {
            PriceResult priceResult = HttpRequest.getJDongPrice(productName);
            int discount = HttpRequest.getJDongDiscount(productName);
            return computeRealPrice(priceResult, discount);
        });

        // 获取拼多多平台的价格和优惠
        Future<PriceResult> pddFuture = executor.submit(() -> {
            PriceResult priceResult = HttpRequest.getPDDPrice(productName);
            int discount = HttpRequest.getPDDDiscount(productName);
            return computeRealPrice(priceResult, discount);
        });

        // 比较计算最便宜的平台和价格
        return Stream.of(taoBaoFuture, jDongFuture, pddFuture)
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .min(Comparator.comparing(PriceResult::getRealPrice))
                .get();
    }

    public static void main(String[] args) {
        FutureComparePriceService service = new FutureComparePriceService();
        // 方案二测试：使用Future+线程池增强并行
        long start = System.currentTimeMillis();
        PriceResult priceResult = service.getCheapestPlatformPrice("iPhone");
        long end = System.currentTimeMillis();

        double costTime = (end - start) / 1000.0;
        System.out.printf("cost %.2f second \n", costTime);

        System.out.println("priceResult = " + priceResult);
    }
}
