package gr.ntua.ece.javengers.client.rest

import gr.ntua.ece.javengers.client.model.Product
import gr.ntua.ece.javengers.client.model.ProductList
import gr.ntua.ece.javengers.client.model.Shop
import gr.ntua.ece.javengers.client.model.ShopList

class XmlRestCallResult implements RestCallResult {

    final def xml

    XmlRestCallResult(def xml) {
        this.xml = xml
    }

    void writeTo(Writer w) {

    }

    String getToken() {
        null
    }

    String getMessage() {
        null
    }

    ProductList getProductList() {
        null
    }

    Product getProduct() {
        null
    }

    ShopList getShopList() {
        null
    }

    Shop getShop() {
        null
    }

}


