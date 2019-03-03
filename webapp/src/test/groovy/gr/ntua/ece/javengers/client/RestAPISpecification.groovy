package gr.ntua.ece.javengers.client

import gr.ntua.ece.javengers.client.model.PriceInfo
import gr.ntua.ece.javengers.client.model.PriceInfoList
import gr.ntua.ece.javengers.client.rest.LowLevelAPI
import gr.ntua.ece.javengers.client.rest.RestCallFormat
import gr.ntua.ece.javengers.client.model.Product
import gr.ntua.ece.javengers.client.model.ProductList
import gr.ntua.ece.javengers.client.model.Shop
import gr.ntua.ece.javengers.client.model.ShopList
import org.apache.logging.log4j.core.tools.picocli.CommandLine
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise class RestAPISpecification extends Specification {

    private static final String IGNORED = System.setProperty("IGNORE_SSL_ERRORS", "true")

    private static final String HOST = Helper.HOST //System.getProperty("gretty.host")
    private static final String PORT = Helper.PORT //System.getProperty("gretty.httpsPort")

    @Shared RestAPI api = new RestAPI(HOST, PORT as Integer, false)

    def "User performs login"() {
        when:
        api.login(Helper.USER, Helper.PASS, RestCallFormat.JSON)

        then:
        api.isLoggedIn()
    }

    def "User adds the first product" () {
        when:
        Product sent = new Product(Helper.PROD1_DATA)

        Product returned = api.postProduct(sent, RestCallFormat.JSON)

        then:
                returned.name == sent.name &&
                returned.description == sent.description &&
                returned.category == sent.category &&
                returned.tags == sent.tags &&
                returned.withdrawn == sent.withdrawn
    }



    def "User adds the second product" () {
        when:
        Product sent = new Product(Helper.PROD2_DATA)

        Product returned = api.postProduct(sent, RestCallFormat.JSON)

        then:
                returned.name == sent.name &&
                returned.description == sent.description &&
                returned.category == sent.category &&
                returned.tags == sent.tags &&
                returned.withdrawn == sent.withdrawn
    }


    def "User gets the first product" () {
        when:

        Product returned = api.getProduct("1", RestCallFormat.JSON)

        then:

                returned == Helper.PROD1
    }

    def "User adds the third product" () {
        when:
        Product sent = new Product(Helper.PROD3_DATA)

        Product returned = api.postProduct(sent, RestCallFormat.JSON)

        then:
                returned.name == sent.name &&
                returned.description == sent.description &&
                returned.category == sent.category &&
                returned.tags == sent.tags &&
                returned.withdrawn == sent.withdrawn
    }

    def "User adds the fourth product" () {
        when:
        Product sent = new Product(Helper.PROD4_DATA)

        Product returned = api.postProduct(sent, RestCallFormat.JSON)

        then:
                returned.name == sent.name &&
                returned.description == sent.description &&
                returned.category == sent.category &&
                returned.tags == sent.tags &&
                returned.withdrawn == sent.withdrawn
    }

    def "User gets the fourth product" () {
        when:

        Product returned = api.getProduct("4", RestCallFormat.JSON)

        then:

        returned == Helper.PROD4
    }

    def "User gets all the products with ascending id order" () {
       when:

       ProductList results = api.getProducts(0, 10, "ALL", "id|asc", RestCallFormat.JSON)

        then:

        results == Helper.PRODUCTS1
    }

    def "User gets all the products with descending id order" () {
        when:

        ProductList results = api.getProducts(0, 10, "ALL", "id|desc", RestCallFormat.JSON)

        then:

        results == Helper.PRODUCTS2
    }

    def "User gets all the products with ascending name order" () {
        when:

        ProductList results = api.getProducts(0, 10, "ALL", "name|asc", RestCallFormat.JSON)

        then:

        results == Helper.PRODUCTS3
    }

    def "User gets the withdrawn products with ascending id order" () {
        when:

        ProductList results = api.getProducts(0, 10, "WITHDRAWN", "id|asc", RestCallFormat.JSON)

        then:

        results == Helper.PRODUCTS4
    }

    def "User withdraws the first product" () {

        when:

        api.deleteProduct("1", RestCallFormat.JSON)

        then:

        noExceptionThrown()

    }

    def "User withdraws the fourth product" () {

        when:

        api.deleteProduct("4", RestCallFormat.JSON)

        then:

        noExceptionThrown()

    }

    def "User adds the fifth product" () {
        when:
        Product sent = new Product(Helper.PROD5_DATA)

        Product returned = api.postProduct(sent, RestCallFormat.JSON)

        then:
                returned.name == sent.name &&
                returned.description == sent.description &&
                returned.category == sent.category &&
                returned.tags == sent.tags &&
                returned.withdrawn == sent.withdrawn
    }

    def "User gets the active products with ascending id order" () {
        when:

        ProductList results = api.getProducts(0, 10, "ACTIVE", "id|asc", RestCallFormat.JSON)

        then:

        results == Helper.PRODUCTS5
    }

    def "User gets the withdrawn products with descending id order" () {
        when:

        ProductList results = api.getProducts(0, 10, "WITHDRAWN", "id|desc", RestCallFormat.JSON)

        then:

        results.total == 2
    }

    def "User updates the first product" () {
        when:
        Product sent = new Product(Helper.PROD1UPD_DATA)

        Product returned = api.putProduct("1", sent, RestCallFormat.JSON)

        then:
                returned == Helper.PROD1UPD

    }

    def "User updates the fourth product" () {
        when:
        Product sent = new Product(Helper.PROD4UPD_DATA)

        Product returned = api.putProduct("4", sent, RestCallFormat.JSON)

        then:
        returned == Helper.PROD4UPD

    }

    def "User gets all the products after the updates with ascending id order" () {
        when:

        ProductList results = api.getProducts(0, 10, "ALL", "id|asc", RestCallFormat.JSON)

        then:

        results == Helper.PRODUCTS6
    }

    def "User updates the name of the second product" () {
        when:

        Product result = api.patchProduct("2", "name", "SecondNameUpdated", RestCallFormat.JSON)

        then:

        result.name == "SecondNameUpdated"
    }

    def "User gets the second product" () {
        when:

        Product result = api.getProduct("2", RestCallFormat.JSON)

        then:

        result.name == "SecondNameUpdated"
    }


    def "User adds the first shop"() {
        when:
        Shop sent = new Shop(Helper.SHOP1_DATA)

        Shop returned = api.postShop(sent, RestCallFormat.JSON)

        then:
                returned.name == sent.name &&
                returned.address == sent.address &&
                returned.lat == sent.lat &&
                returned.lng == sent.lng &&
                returned.tags == sent.tags &&
                !returned.withdrawn
    }

    def "User adds the second shop"() {
        when:
        Shop sent = new Shop(Helper.SHOP2_DATA)

        Shop returned = api.postShop(sent, RestCallFormat.JSON)

        then:
                returned.name == sent.name &&
                returned.address == sent.address &&
                returned.lat == sent.lat &&
                returned.lng == sent.lng &&
                returned.tags == sent.tags &&
                !returned.withdrawn
    }

    def "User gets the first shop" () {
        when:

        Shop returned = api.getShop("1", RestCallFormat.JSON)

        then:
                returned == Helper.SHOP1
    }

    def "User adds the third shop"() {
        when:
        Shop sent = new Shop(Helper.SHOP3_DATA)

        Shop returned = api.postShop(sent, RestCallFormat.JSON)

        then:
                returned.name == sent.name &&
                returned.address == sent.address &&
                returned.lat == sent.lat &&
                returned.lng == sent.lng &&
                returned.tags == sent.tags &&
                !returned.withdrawn
    }

    def "User adds the fourth shop"() {
        when:
        Shop sent = new Shop(Helper.SHOP4_DATA)

        Shop returned = api.postShop(sent, RestCallFormat.JSON)

        then:
                returned.name == sent.name &&
                returned.address == sent.address &&
                returned.lat == sent.lat &&
                returned.lng == sent.lng &&
                returned.tags == sent.tags &&
                !returned.withdrawn
    }

    def "User gets all the shops with ascending id order" () {
        when:

        ShopList results = api.getShops(0, 10, "ALL", "id|asc", RestCallFormat.JSON)

        then:

                results == Helper.SHOPS1
    }

    def "User gets all the shops with ascending name order" () {
        when:

        ShopList results = api.getShops(0, 10, "ALL", "name|asc", RestCallFormat.JSON)

        then:

                results == Helper.SHOPS2
    }

    def "User withdraws the second shop" () {

        when:

        api.deleteShop("2", RestCallFormat.JSON)

        then:

                noExceptionThrown()

    }

    def "User gets the active shops with ascending name order" () {
        when:

        ShopList results = api.getShops(0, 10, "ACTIVE", "name|asc", RestCallFormat.JSON)

        then:

                results == Helper.SHOPS3
    }

    def "User gets the withdrawn shops with descending id order" () {
        when:

        ShopList results = api.getShops(0, 10, "WITHDRAWN", "id|desc", RestCallFormat.JSON)

        then:

                results.total == 1
    }

    def "User updates the second shop" () {
        when:
        Shop sent = new Shop(Helper.SHOP2UPD_DATA)

        Shop returned = api.putShop("2", sent, RestCallFormat.JSON)

        then:
                returned == Helper.SHOP2UPD

    }

    def "User gets all the shops after the update with ascending id order" () {
        when:
        ShopList results = api.getShops(0, 10, "ALL", "id|asc", RestCallFormat.JSON)

        then:

                results == Helper.SHOPS4
    }

    def "User updates the Address of the fourth shop" () {

        when:
        Shop returned = api.patchShop("4", "address", "Ανάβυσσος", RestCallFormat.JSON)

        then:

                returned.address == "Ανάβυσσος"

    }

    def "User gets the fourth shop" () {

        when:
        Shop returned = api.getShop("4", RestCallFormat.JSON)

        then:

                returned.address == "Ανάβυσσος"
    }



    def "User adds the first price"() {
        when:
        double price = Helper.PINFO_SUBMIT_DATA.price
        String dateFrom = Helper.PINFO_SUBMIT_DATA.dateFrom
        String dateTo = Helper.PINFO_SUBMIT_DATA.dateTo
        String productId = Helper.PINFO_SUBMIT_DATA.productId
        String shopId = Helper.PINFO_SUBMIT_DATA.shopId
        PriceInfoList list = api.postPrice(price, dateFrom, dateTo, productId, shopId, RestCallFormat.JSON)

        then:
                list.total == Helper.durationInDays(dateFrom, dateTo) + 1 &&
                list.prices.every { PriceInfo p ->
                    p.price == price &&
                    p.productId == productId &&
                    p.shopId == shopId
                } &&
                list.prices[0].date == dateFrom &&
                list.prices[1].date == dateTo
    }

    def "User performs logout" () {
        when:
        api.logout(RestCallFormat.JSON)

        then:

                !api.isLoggedIn()
    }

}
