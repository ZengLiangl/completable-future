package org.aliang.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ll
 * @date 2024/3/29 21:28
 * @see PriceResult
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceResult {
    /**
     * 平台价格
     */
    private int price;
    /**
     * 折扣
     */
    private int discount;
    /**
     * 最终价
     */
    private int realPrice;
    /**
     * 商品平台
     */
    private String platform;

    public PriceResult(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "PriceResult{" +
                "平台=" + platform +
                ", 价格=" + price +
                ", 折扣=" + discount +
                ", 最终价=" + realPrice +
                '}';
    }
}
