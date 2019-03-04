package gr.ntua.ece.javengers.client.rest

import gr.ntua.ece.javengers.client.model.PriceInfoList
import gr.ntua.ece.javengers.client.model.Product
import gr.ntua.ece.javengers.client.model.ProductList
import gr.ntua.ece.javengers.client.model.Shop
import gr.ntua.ece.javengers.client.model.ShopList

interface RestCallResult {

    void writeTo(Writer w)

    String getToken()

    String getMessage()

    ProductList getProductList()

    Product getProduct()

    ShopList getShopList()

    Shop getShop()

    PriceInfoList getPriceInfoList()

}
