package gr.ntua.ece.javengers.client.model

import groovy.transform.Canonical

import java.sql.Date

@Canonical
class PriceInfo {

    Double price
    String date
    String productName
    String productId
    List<String> productTags
    String shopId
    String shopName
    List<String> shopTags
    String shopAddress
    Integer shopDist
}

