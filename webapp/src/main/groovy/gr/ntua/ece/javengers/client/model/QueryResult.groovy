package gr.ntua.ece.javengers.client.model

import groovy.transform.Canonical

import java.sql.Date

@Canonical
public class QueryResult {

    Double price
    Date date
    String productName
    String productId
    List<String> productTags
    String shopId
    String shopName
    List<String> shopTags
    String shopAddress
    Integer shopDist

}

