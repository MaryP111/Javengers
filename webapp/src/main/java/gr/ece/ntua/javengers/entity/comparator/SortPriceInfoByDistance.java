package gr.ece.ntua.javengers.entity.comparator;

import gr.ntua.ece.javengers.client.model.PriceInfo;

import java.util.Comparator;

public class SortPriceInfoByDistance implements Comparator<PriceInfo> {

    public int compare(PriceInfo priceInfo1, PriceInfo priceInfo2) {
        if (priceInfo1.getShopDist() > priceInfo2.getShopDist()) return 1;
        else return -1;
    }
}
