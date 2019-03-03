package gr.ntua.ece.javengers.client

import gr.ntua.ece.javengers.client.model.PriceInfo
import gr.ntua.ece.javengers.client.model.PriceInfoList
import gr.ntua.ece.javengers.client.model.Product
import gr.ntua.ece.javengers.client.model.ProductList
import gr.ntua.ece.javengers.client.model.Shop
import gr.ntua.ece.javengers.client.model.ShopList
import groovy.time.TimeCategory

import java.text.SimpleDateFormat

class Helper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd")

    static final String HOST  = "localhost"
    static final int PORT     = 8080
    // static final String TOKEN = "ABC123"
    static final String USER  = "username"
    static final String PASS  = "passwordss"

    static final Product newProduct(String id, Map productData) {
        Product p = new Product(productData)
        p.id = id
        return p
    }

    static final Shop newShop(String id, Map shopData) {
        Shop s = new Shop(shopData)
        s.id = id
        return s
    }

    // Place the fields in the same order as defined in the respective class

    static final def PROD1_DATA = [
            name        : "FirstProduct",
            description : "FirstDescription",
            category    : "FirstCategory",
            tags        : ["Tags", "of", "first", "Product"],
            withdrawn   : false
    ]

    static final def PROD2_DATA = [
            name        : "SecondProduct",
            description : "SecondDescription",
            category    : "SecondCategory",
            tags        : ["Tags", "of", "second", "Product"],
            withdrawn   : false
    ]

    static final def PROD3_DATA = [
            name        : "ThirdName",
            description : "ThirdDescription",
            category    : "ThirdCategory",
            tags        : ["Tags", "of", "third", "Product"],

            withdrawn   : false
    ]

    static final def PROD4_DATA = [
            name        : "FourthName",
            description : "FourthDescription",
            category    : "FourthCategory",
            tags        : ["Tags", "of", "fourth", "Product"],

            withdrawn   : false
    ]

    static final def PROD5_DATA = [
            name        : "FifthName",
            description : "FifthDescription",
            category    : "FifthCategory",
            tags        : ["Tags", "of", "fifth", "Product"],

            withdrawn   : false
    ]

    static final def PROD1UPD_DATA = [
            name        : "FirstNameUpdated",
            description : "FirstDescriptionUpdated",
            category    : "FirstCategoryUpdated",
            tags        : ["One", "Two", "Three"],

            withdrawn   : false
    ]

    static final def PROD4UPD_DATA = [
            name        : "FourthNameUpdated",
            description : "FourthDescriptionUpdated",
            category    : "FourthCategoryUpdated",
            tags        : ["Ένα", "Δύο", "Τρία"],

            withdrawn   : false
    ]

    static final Product PROD1    = newProduct("1", PROD1_DATA)
    static final Product PROD2    = newProduct("2", PROD2_DATA)
    static final Product PROD3    = newProduct("3", PROD3_DATA)
    static final Product PROD4    = newProduct("4", PROD4_DATA)
    static final Product PROD5    = newProduct("5", PROD5_DATA)
    static final Product PROD1UPD = newProduct("1", PROD1UPD_DATA)
    static final Product PROD4UPD = newProduct("4", PROD4UPD_DATA)

    static final ProductList PRODUCTS1 = new ProductList(
            start   : 0,
            count   : 10,
            total   : 4,
            products: [PROD1, PROD2, PROD3, PROD4]
    )

    static final ProductList PRODUCTS2 = new ProductList(
            start   : 0,
            count   : 10,
            total   : 4,
            products: [PROD4, PROD3, PROD2, PROD1]
    )

    static final ProductList PRODUCTS3 = new ProductList(
            start   : 0,
            count   : 10,
            total   : 4,
            products: [PROD1, PROD4, PROD2, PROD3]
    )

    static final ProductList PRODUCTS4 = new ProductList(
            start   : 0,
            count   : 10,
            total   : 0,
            products: []
    )

    static final ProductList PRODUCTS5 = new ProductList(
            start   : 0,
            count   : 10,
            total   : 3,
            products: [PROD2, PROD3, PROD5]
    )

    static final ProductList PRODUCTS6 = new ProductList(
            start   : 0,
            count   : 10,
            total   : 5,
            products: [PROD1UPD, PROD2, PROD3, PROD4UPD, PROD5]
    )

    // Place the fields in the same order as defined in the respective class

    static final def SHOP1_DATA = [
            name     : "Πρώτο Μαγαζί",
            address  : "Ψυχικό",
            lat      : 37.97864720247794,
            lng      : 23.78350140530576,
            tags     : ["Tags", "of", "first", "shop"],
            withdrawn: false
    ]

    static final def SHOP2_DATA = [
            name     : "Δεύτερο Μαγαζί",
            address  : "Φιλοθέη",
            lat      : 37.98136303504576,
            lng      : 23.78413117565094,
            tags     : ["Tags", "of", "second", "shop"],
            withdrawn: false
    ]

    static final def SHOP3_DATA = [
            name     : "Τρίτο Μαγαζί",
            address  : "Κυψέλη",
            lat      : 37.97864720247794,
            lng      : 23.78350140530576,
            tags     : ["Tags", "of", "third", "shop"],
            withdrawn: false
    ]

    static final def SHOP4_DATA = [
            name     : "Τέταρτο Μαγαζί",
            address  : "Ανάβρυτα",
            lat      : 37.97364723247794,
            lng      : 23.78250144550216,
            tags     : ["Tags", "of", "fourth", "shop"],
            withdrawn: false
    ]

    static final def SHOP2UPD_DATA = [
            name     : "Δεύτερο Μαγαζί Ανανεωμένο",
            address  : "Φιλοθέη Ανανεωμένη",
            lat      : 37.97364723247794,
            lng      : 23.78250144550216,
            tags     : ["Ένα", "Δύο", "Τρία", "Τέσσερα"],
            withdrawn: false
    ]

    static final Shop SHOP1    = newShop("1", SHOP1_DATA)
    static final Shop SHOP2    = newShop("2", SHOP2_DATA)
    static final Shop SHOP3    = newShop("3", SHOP3_DATA)
    static final Shop SHOP4    = newShop("4", SHOP4_DATA)
    static final Shop SHOP2UPD = newShop("2", SHOP2UPD_DATA)

    static final ShopList SHOPS1 = new ShopList(
            start: 0,
            count: 10,
            total: 4,
            shops: [SHOP1, SHOP2, SHOP3, SHOP4]
    )

    static final ShopList SHOPS2 = new ShopList(
            start: 0,
            count: 10,
            total: 4,
            shops: [SHOP4, SHOP3, SHOP2, SHOP1]
    )

    static final ShopList SHOPS3 = new ShopList(
            start: 0,
            count: 10,
            total: 3,
            shops: [SHOP4, SHOP3, SHOP1]
    )

    static final ShopList SHOPS4 = new ShopList(
            start: 0,
            count: 10,
            total: 4,
            shops: [SHOP1, SHOP2UPD, SHOP3, SHOP4]
    )

    static final def PINFO_SUBMIT_DATA = [
            price: 13.25,
            dateFrom: "2019-02-21",
            dateTo: "2019-02-22",
            productId: PROD1.id,
            shopId: SHOP1.id
    ]

    static final PriceInfo PINFO1 = new PriceInfo(
            price: 13.25,
            date: "2019-02-21",
            productName: PROD1.name,
            productId: PROD1.id,
            productTags: PROD1.tags,
            shopId: SHOP1.id,
            shopName: SHOP1.name,
            shopTags: SHOP1.tags,
            shopAddress: SHOP1.address
    )

    static final PriceInfo PINFO2 = new PriceInfo(
            price: 13.25,
            date: "2019-02-22",
            productName: PROD1.name,
            productId: PROD1.id,
            productTags: PROD1.tags,
            shopId: SHOP1.id,
            shopName: SHOP1.name,
            shopTags: SHOP1.tags,
            shopAddress: SHOP1.address
    )

    static final PriceInfoList PINFO_LIST = new PriceInfoList(
            start: 0,
            count: 10,
            total: 2,
            prices: [PINFO1, PINFO2]
    )


    static int durationInDays(String dateFrom, String dateTo) {
        Date from = DATE_FORMAT.parse(dateFrom)
        Date to   = DATE_FORMAT.parse(dateTo)
        use(TimeCategory) {
            def duration = to - from
            return duration.days
        }
    }

    static List<String> elementsAt(List<String> source, List<Integer> indexes) {
        List<String> result = []
        int sz = indexes.size()
        for (int i = 0; i < sz; i++) {
            result.push(source[indexes[i]])
        }
        return result
    }

}
