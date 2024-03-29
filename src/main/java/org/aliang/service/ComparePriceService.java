package org.aliang.service;

import org.aliang.HttpRequest;
import org.aliang.pojo.PriceResult;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 方案一：串行方式操作商品比价
 *
 * @author ll
 * @date 2024/3/29 21:36
 * @see ComparePriceService
 */
public class ComparePriceService {
    /**
     * 方案一：串行方式操作商品比价
     *
     * @param productName
     * @return PriceResult
     */
    public PriceResult getCheapestPlatformPrice(String productName) {
        PriceResult priceResult;
        int discount;

        // 获取淘宝平台的价格和优惠
        priceResult = HttpRequest.getTaoBaoPrice(productName);
        discount = HttpRequest.getTaoBaoDiscount(productName);
        PriceResult taoBaoPriceResult = this.computeRealPrice(priceResult, discount);

        // 获取京东平台的价格和优惠
        priceResult = HttpRequest.getJDongPrice(productName);
        discount = HttpRequest.getJDongDiscount(productName);
        PriceResult jDongPriceResult = this.computeRealPrice(priceResult, discount);

        // 获取拼多多平台的价格和优惠
        priceResult = HttpRequest.getPDDPrice(productName);
        discount = HttpRequest.getPDDDiscount(productName);
        PriceResult pddPriceResult = this.computeRealPrice(priceResult, discount);

        Stream<PriceResult> stream = Stream.of(taoBaoPriceResult, jDongPriceResult, pddPriceResult);
        Optional<PriceResult> minOpt = stream.min(Comparator.comparing(PriceResult::getRealPrice));
        return minOpt.get();
    }


    /**
     * 计算商品的最终价格 = 平台价格 - 优惠价
     *
     * @param priceResult
     * @param discount
     * @return PriceResult
     */
    public static PriceResult computeRealPrice(PriceResult priceResult, int discount) {
        priceResult.setRealPrice(priceResult.getPrice() - discount);
        priceResult.setDiscount(discount);
        System.out.println(priceResult.getPlatform() + "最终价格计算完成:" + priceResult.getRealPrice());
        return priceResult;
    }

    public static void main(String[] args) {
        // 方案一测试：串行方式操作商品比价
        ComparePriceService service = new ComparePriceService();

        long start = System.currentTimeMillis();
        PriceResult priceResult = service.getCheapestPlatformPrice("iPhone");
        long end = System.currentTimeMillis();

        double costTime = (end - start) / 1000.0;
        System.out.printf("cost %.2f second \n", costTime);

        System.out.println("priceResult = " + priceResult);
    }
}
