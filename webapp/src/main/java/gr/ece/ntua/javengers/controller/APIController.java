package gr.ece.ntua.javengers.controller;

import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.Store;
import gr.ece.ntua.javengers.entity.comparator.SortProductById;
import gr.ece.ntua.javengers.entity.comparator.SortProductByName;
import gr.ece.ntua.javengers.entity.comparator.SortStoreById;
import gr.ece.ntua.javengers.entity.comparator.SortStoreByName;
import gr.ece.ntua.javengers.exception.ProductNotFoundException;
import gr.ece.ntua.javengers.exception.ShopNotFoundException;
import gr.ece.ntua.javengers.service.HasProductService;
import gr.ece.ntua.javengers.service.ProductService;
import gr.ece.ntua.javengers.service.StoreService;
import gr.ntua.ece.javengers.client.model.Entry;
import gr.ntua.ece.javengers.client.model.ProductList;
import gr.ntua.ece.javengers.client.model.Shop;
import gr.ntua.ece.javengers.client.model.ShopList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/observatory/api")
public class APIController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private HasProductService hasProductService;


    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ProductList getAllProducts (@RequestParam("start") Optional<Integer> startURL, @RequestParam("count") Optional<Integer> countURL,
                                       @RequestParam("status") Optional<String> statusURL, @RequestParam("sort") Optional<String> sortURL) {

        Integer start;
        if (startURL.isPresent()) start = startURL.get();
        else start = 0;

        Integer count;
        if (countURL.isPresent()) count = countURL.get();
        else count = 20;

        String status;
        if (statusURL.isPresent()) status = statusURL.get();
        else status = "ACTIVE";

        String sort;
        if (sortURL.isPresent()) sort = sortURL.get();
        else sort = "id|DESC";

        ProductList productList = new ProductList();

        productList.setStart(start);
        productList.setCount(count);

        String parts[] = sort.split(Pattern.quote("|"));

        List<Product> products = productService.listAll();

        productList.setTotal(products.size());

        if (parts[0].equals("id")) {
            Collections.sort(products, new SortProductById());
        }
        else if (parts[0].equals("name")){
            Collections.sort(products, new SortProductByName());
        }

        if (parts[1].equals("DESC")) {
            Collections.reverse(products);
        }

        List<gr.ntua.ece.javengers.client.model.Product> productArrayList = new ArrayList<>();

        Iterator<Product> productIterator = products.iterator();

        for (int i = 0; i < products.size(); i++) {

            if (!productIterator.hasNext()) break;
            Product tempProduct = productIterator.next();
            if (i < start) continue;
            if (i >= start + count) break;

            productArrayList.add(productService.getProductAndTagsById(tempProduct.getId()));
        }

        productList.setProducts(productArrayList);
        return productList;

    }


    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public gr.ntua.ece.javengers.client.model.Product getProductById(@PathVariable("id") Long id) {

        if (productService.getProductById(id).isPresent()) {

            return productService.getProductAndTagsById(id);
        }

        throw new ProductNotFoundException();

    }

    @RequestMapping(value ="/products", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public gr.ntua.ece.javengers.client.model.Product postProduct(@RequestBody gr.ntua.ece.javengers.client.model.Product product) {

        return productService.saveProduct(product);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public gr.ntua.ece.javengers.client.model.Product putProduct(@RequestBody gr.ntua.ece.javengers.client.model.Product newProduct, @PathVariable("id") Long id) {

        newProduct.setId(id.toString());

        productService.updateProduct(newProduct);

        return newProduct;

    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> deleteProduct(@PathVariable("id") Long id) {

        productService.deleteProductById(id);

        HashMap<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "OK");

        return jsonResponse;

    }

    @RequestMapping(value = "/shops", method = RequestMethod.GET)
    public ShopList getAllShops (@RequestParam("start") Optional<Integer> startURL, @RequestParam("count") Optional<Integer> countURL,
                                       @RequestParam("status") Optional<String> statusURL, @RequestParam("sort") Optional<String> sortURL) {

        Integer start;
        if (startURL.isPresent()) start = startURL.get();
        else start = 0;

        Integer count;
        if (countURL.isPresent()) count = countURL.get();
        else count = 20;

        String status;
        if (statusURL.isPresent()) status = statusURL.get();
        else status = "ACTIVE";

        String sort;
        if (sortURL.isPresent()) sort = sortURL.get();
        else sort = "id|DESC";

        ShopList shopList = new ShopList();

        shopList.setStart(start);
        shopList.setCount(count);

        String parts[] = sort.split(Pattern.quote("|"));

        List<Store> stores = storeService.listAll();

        shopList.setTotal(stores.size());

        if (parts[0].equals("id")) {
            Collections.sort(stores, new SortStoreById());
        }
        else if (parts[0].equals("name")){
            Collections.sort(stores, new SortStoreByName());
        }

        if (parts[1].equals("DESC")) {
            Collections.reverse(stores);
        }

        List<gr.ntua.ece.javengers.client.model.Shop> shopArrayList = new ArrayList<>();

        Iterator<Store> storeIterator = stores.iterator();

        for (int i = 0; i < stores.size(); i++) {

            if (!storeIterator.hasNext()) break;
            Store tempStore = storeIterator.next();
            if (i < start) continue;
            if (i >= start + count) break;

            shopArrayList.add(storeService.getStoreAndTagsById(tempStore.getId()));
        }

        shopList.setShops(shopArrayList);
        return shopList;

    }

    @RequestMapping(value = "/shops/{id}", method = RequestMethod.GET)
    public gr.ntua.ece.javengers.client.model.Shop getShopById(@PathVariable("id") Long id) {

        if (storeService.getStoreById(id).isPresent()) {

            return storeService.getStoreAndTagsById(id);
        }

        throw new ShopNotFoundException();

    }

    @RequestMapping(value ="/shops", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public gr.ntua.ece.javengers.client.model.Shop postShop(@RequestBody gr.ntua.ece.javengers.client.model.Shop shop) {

        return storeService.saveShop(shop);
    }

    @RequestMapping(value = "/shops/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Shop putShop(@RequestBody Shop newShop, @PathVariable("id") Long id) {

        newShop.setId(id.toString());

        storeService.updateStore(newShop);

        return newShop;

    }

    @RequestMapping(value = "/shops/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> deleteShop(@PathVariable("id") Long id) {

        storeService.deleteStoreById(id);

        HashMap<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "OK");

        return jsonResponse;

    }

    @RequestMapping(value ="/prices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Entry postEntry(@RequestBody Entry entry) {

        return hasProductService.saveEntry(entry);
    }


}
