package gr.ece.ntua.javengers.controller;

import gr.ece.ntua.javengers.entity.HasProduct;
import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.Store;
import gr.ece.ntua.javengers.entity.comparator.*;
import gr.ece.ntua.javengers.exception.*;
import gr.ece.ntua.javengers.service.*;
import gr.ntua.ece.javengers.client.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.sql.Date;
import java.text.SimpleDateFormat;
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

    @Autowired
    private ProductTagService productTagService;

    @Autowired
    private StoreTagService storeTagService;

    @Resource(name = "authenticationManager")
    private AuthenticationManager authManager;

    private static List<Long> tokenList;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public HashMap<String, String> loginUser(@RequestParam("format") Optional<String> optionalFormat, LoginUser loginUser) {

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());

        try {

            Authentication auth = authManager.authenticate(authReq);
        }
        catch (RuntimeException exc) {
            throw new ForbiddenException();
        }

        String token = tokenGenerator();
        addToken(token);

        HashMap<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("token", token);

        return jsonResponse;

    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public HashMap<String, String> logoutUser(@RequestParam("format") Optional<String> optionalFormat, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {

        if (!verifyToken(token)) throw new BadRequestException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        deleteToken(token);

        HashMap<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "OK");

        return jsonResponse;


    }


    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ProductList getProducts (@RequestParam("format") Optional<String> optionalFormat, @RequestParam("start") Optional<Integer> optionalStart, @RequestParam("count") Optional<Integer> optionalCount,
                                       @RequestParam("status") Optional<String> optionalStatus, @RequestParam("sort") Optional<String> optionalSort, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {

        if (!verifyToken(token)) throw new ForbiddenException();


        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        Integer start;
        if (optionalStart.isPresent()) start = optionalStart.get();
        else start = 0;

        Integer count;
        if (optionalCount.isPresent()) count = optionalCount.get();
        else count = 20;

        if (start < 0 || count < 0) throw new BadRequestException();

        String status;
        if (optionalStatus.isPresent()) status = optionalStatus.get();
        else status = "ACTIVE";

        if (!status.equalsIgnoreCase("all") && !status.equalsIgnoreCase("active") && !status.equalsIgnoreCase("withdrawn")) throw new BadRequestException();

        String sort;
        if (optionalSort.isPresent()) sort = optionalSort.get();
        else sort = "id|DESC";

        ProductList productList = new ProductList();

        productList.setStart(start);
        productList.setCount(count);

        String parts[] = sort.split(Pattern.quote("|"));

        if (!parts[0].equalsIgnoreCase("id") && !parts[0].equalsIgnoreCase("name")) throw new BadRequestException();

        if (!parts[1].equalsIgnoreCase("asc") && !parts[1].equalsIgnoreCase("desc")) throw new BadRequestException();

        List<Product> products = productService.listAll();

        Boolean flag = true;

        if (status.equalsIgnoreCase("active")) {
            flag = false;
        }

        List<Product> statusProducts = new ArrayList<>();

        if (!status.equalsIgnoreCase("all")) {

            Iterator<Product> productIterator = products.iterator();

            while (productIterator.hasNext()) {

                Product temProduct = productIterator.next();
                if (temProduct.getWithdrawn() == flag) statusProducts.add(temProduct);
            }

        }

        else {

            statusProducts = products;
        }

        productList.setTotal(statusProducts.size());

        if (parts[0].equalsIgnoreCase("id")) {
            Collections.sort(statusProducts, new SortProductById());
        }
        else if (parts[0].equalsIgnoreCase("name")){
            Collections.sort(statusProducts, new SortProductByName());
        }

        if (parts[1].equalsIgnoreCase("desc")) {
            Collections.reverse(statusProducts);
        }

        List<gr.ntua.ece.javengers.client.model.Product> productArrayList = new ArrayList<>();

        Iterator<Product> productIterator = statusProducts.iterator();

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
    public gr.ntua.ece.javengers.client.model.Product getProductById(@RequestParam("format") Optional<String> optionalFormat, @PathVariable("id") Long id, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {


        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (productService.getProductById(id).isPresent()) {

            return productService.getProductAndTagsById(id);
        }

        throw new ProductNotFoundException();

    }

    @RequestMapping(value ="/products", method = RequestMethod.POST)
    public gr.ntua.ece.javengers.client.model.Product postProduct(@RequestParam("format") Optional<String> optionalFormat, gr.ntua.ece.javengers.client.model.Product product, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {


        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (product.getName().equals("") || product.getName() == null) throw new BadRequestException();
        if (product.getDescription().equals("") || product.getDescription() == null) throw new BadRequestException();
        if (product.getCategory().equals("") || product.getCategory() == null) throw new BadRequestException();
        if (product.getTags() == null) throw new BadRequestException();

        return productService.saveProduct(product);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    public gr.ntua.ece.javengers.client.model.Product putProduct(@RequestParam("format") Optional<String> optionalFormat, gr.ntua.ece.javengers.client.model.Product newProduct, @PathVariable("id") Long id, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {

        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (newProduct.getName().equals("") || newProduct.getName() == null) throw new BadRequestException();
        if (newProduct.getDescription().equals("") || newProduct.getDescription() == null) throw new BadRequestException();
        if (newProduct.getCategory().equals("") || newProduct.getCategory() == null) throw new BadRequestException();
        if (newProduct.getTags() == null) throw new BadRequestException();

        if (!productService.getProductById(id).isPresent()) throw new ProductNotFoundException();

        newProduct.setId(id.toString());

        productService.updateProduct(newProduct);

        return newProduct;

    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PATCH)
    public gr.ntua.ece.javengers.client.model.Product patchProduct(@RequestParam("format") Optional<String> optionalFormat, gr.ntua.ece.javengers.client.model.Product updateProduct, @PathVariable("id") Long id, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {


        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (!productService.getProductById(id).isPresent()) throw new ProductNotFoundException();

        gr.ntua.ece.javengers.client.model.Product newProduct = productService.getProductAndTagsById(id);

        if (updateProduct.getName() != null) newProduct.setName(updateProduct.getName());
        else if  (updateProduct.getDescription() != null) newProduct.setDescription(updateProduct.getDescription());
        else if (updateProduct.getCategory() != null) newProduct.setCategory(updateProduct.getCategory());
        else if (updateProduct.getTags() != null) newProduct.setTags(updateProduct.getTags());
        else newProduct.setWithdrawn(updateProduct.getWithdrawn());

        productService.updateProduct(newProduct);

        return newProduct;


    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    public HashMap<String, String> deleteProduct(@RequestParam("format") Optional<String> optionalFormat, @PathVariable("id") Long id, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {

        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (!productService.getProductById(id).isPresent()) throw new ProductNotFoundException();

        productService.deleteProductById(id);   // if else with admin

        HashMap<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "OK");

        return jsonResponse;

    }

    @RequestMapping(value = "/shops", method = RequestMethod.GET)
    public ShopList getShops (@RequestParam("format") Optional<String> optionalFormat, @RequestParam("start") Optional<Integer> optionalStart, @RequestParam("count") Optional<Integer> optionalCount,
                              @RequestParam("status") Optional<String> optionalStatus, @RequestParam("sort") Optional<String> optionalSort, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {



        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        Integer start;
        if (optionalStart.isPresent()) start = optionalStart.get();
        else start = 0;

        Integer count;
        if (optionalCount.isPresent()) count = optionalCount.get();
        else count = 20;

        String status;
        if (optionalStatus.isPresent()) status = optionalStatus.get();
        else status = "ACTIVE";

        if (!status.equalsIgnoreCase("all") && !status.equalsIgnoreCase("active") && !status.equalsIgnoreCase("withdrawn")) throw new BadRequestException();

        String sort;
        if (optionalSort.isPresent()) sort = optionalSort.get();
        else sort = "id|DESC";

        ShopList shopList = new ShopList();

        shopList.setStart(start);
        shopList.setCount(count);

        String parts[] = sort.split(Pattern.quote("|"));

        List<Store> stores = storeService.listAll();

        Boolean flag = true;

        if (status.equalsIgnoreCase("active")) {
            flag = false;
        }

        List<Store> statusStores = new ArrayList<>();

        if (!status.equalsIgnoreCase("all")) {

            Iterator<Store> storeIterator = stores.iterator();

            while (storeIterator.hasNext()) {

                Store tempStore = storeIterator.next();
                if (tempStore.getWithdrawn() == flag) statusStores.add(tempStore);
            }

        }
        else {

            statusStores= stores;
        }

        shopList.setTotal(statusStores.size());


        shopList.setTotal(statusStores.size());

        if (parts[0].equalsIgnoreCase("id")) {
            Collections.sort(statusStores, new SortStoreById());
        }
        else if (parts[0].equalsIgnoreCase("name")){
            Collections.sort(statusStores, new SortStoreByName());
        }

        if (parts[1].equalsIgnoreCase("desc")) {
            Collections.reverse(statusStores);
        }

        if (!parts[0].equalsIgnoreCase("id") && !parts[0].equalsIgnoreCase("name")) throw new BadRequestException();

        if (!parts[1].equalsIgnoreCase("asc") && !parts[1].equalsIgnoreCase("desc")) throw new BadRequestException();

        List<gr.ntua.ece.javengers.client.model.Shop> shopArrayList = new ArrayList<>();

        Iterator<Store> storeIterator = statusStores.iterator();

        for (int i = 0; i < statusStores.size(); i++) {

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
    public gr.ntua.ece.javengers.client.model.Shop getShopById(@RequestParam("format") Optional<String> optionalFormat, @PathVariable("id") Long id, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {

        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (storeService.getStoreById(id).isPresent()) {

            return storeService.getStoreAndTagsById(id);
        }

        throw new ShopNotFoundException();

    }

    @RequestMapping(value ="/shops", method = RequestMethod.POST)
    public gr.ntua.ece.javengers.client.model.Shop postShop(@RequestParam("format") Optional<String> optionalFormat, gr.ntua.ece.javengers.client.model.Shop shop, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {

        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (shop.getName().equals("") || shop.getName() == null) throw new BadRequestException();
        if (shop.getAddress().equals("") || shop.getAddress() == null) throw new BadRequestException();
        if (shop.getLat() <= 0 || shop.getLng() <= 0) throw new BadRequestException();
        if (shop.getTags() == null) throw new BadRequestException();

        return storeService.saveShop(shop);
    }

    @RequestMapping(value = "/shops/{id}", method = RequestMethod.PUT)
    public Shop putShop(@RequestParam("format") Optional<String> optionalFormat, Shop newShop, @PathVariable("id") Long id, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {

        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (newShop.getName().equals("") || newShop.getName() == null) throw new BadRequestException();
        if (newShop.getAddress().equals("") || newShop.getAddress() == null) throw new BadRequestException();
        if (newShop.getLat() <= 0 || newShop.getLng() <= 0) throw new BadRequestException();
        if (newShop.getTags() == null) throw new BadRequestException();

        if (!storeService.getStoreById(id).isPresent()) throw new ShopNotFoundException();

        newShop.setId(id.toString());

        storeService.updateStore(newShop);

        return newShop;

    }

    @RequestMapping(value = "/shops/{id}", method = RequestMethod.PATCH)
    public Shop patchShop (@RequestParam("format") Optional<String> optionalFormat, Shop updateShop, @PathVariable("id") Long id, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {

        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (!storeService.getStoreById(id).isPresent()) throw new ShopNotFoundException();

        Shop newShop = storeService.getStoreAndTagsById(id);

        if (updateShop.getName() != null) newShop.setName(updateShop.getName());
        else if  (updateShop.getAddress() != null) newShop.setAddress(updateShop.getAddress());
        else if (updateShop.getLng() != 0) newShop.setLng(updateShop.getLng());
        else if (updateShop.getLat() != 0) newShop.setLat(updateShop.getLat());
        else if (updateShop.getTags() != null) newShop.setTags(updateShop.getTags());
        else newShop.setWithdrawn(updateShop.getWithdrawn());

        storeService.updateStore(newShop);

        return newShop;


    }


    @RequestMapping(value = "/shops/{id}", method = RequestMethod.DELETE)
    public HashMap<String, String> deleteShop(@RequestParam("format") Optional<String> optionalFormat, @PathVariable("id") Long id, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {


        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (!storeService.getStoreById(id).isPresent()) throw new ShopNotFoundException();

        storeService.deleteStoreById(id);

        HashMap<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "OK");

        return jsonResponse;

    }

    @RequestMapping(value = "prices", method = RequestMethod.GET)
    public PriceInfoList queryEntries (@RequestParam("format") Optional<String> optionalFormat, @RequestParam("start") Optional<Integer> optionalStart, @RequestParam("count") Optional<Integer> optionalCount,
                                       @RequestParam("geoDist") Optional<Integer> optionalGeoDist, @RequestParam("geoLng") Optional<Double> optionalGeoLng, @RequestParam("geoLat") Optional<Double> optionalGeoLat,
                                       @RequestParam("dateFrom") Optional<Date> optionalDateFrom, @RequestParam("dateTo") Optional<Date> optionalDateTo, @RequestParam("shops") Optional<List<String> > optionalShops,
                                       @RequestParam("products") Optional<List<String> > optionalProducts, @RequestParam("tags") Optional<List<String> > optionalTags, @RequestParam("sort") Optional<String> optionalSort,
                                       @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) throws Exception{


        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new BadRequestException();

        Integer start;
        if (optionalStart.isPresent()) start = optionalStart.get();
        else start = 0;

        Integer count;
        if (optionalCount.isPresent()) count = optionalCount.get();
        else count = 20;

        if (start < 0 || count < 0) throw new BadRequestException();

        List<HasProduct> entries = hasProductService.getAllEntries();

        Date dateFrom, dateTo;

        if (optionalDateFrom.isPresent() && optionalDateTo.isPresent()) {

            dateFrom = optionalDateFrom.get();
            dateTo = optionalDateTo.get();

        }
        else if (!optionalDateFrom.isPresent() && !optionalDateTo.isPresent()) {

            dateFrom = getToday();
            dateTo = getToday();

        }

        else {

            throw new BadRequestException();
        }

        Iterator<HasProduct> entryIterator = entries.iterator();
        List<PriceInfo> filteredByDate = new ArrayList<>();


        while (entryIterator.hasNext()) {

            HasProduct entry = entryIterator.next();

            Date date = dateFrom;

            while (date.compareTo(dateTo) <= 0) {

                if (date.compareTo(entry.getDateFrom()) >= 0 && date.compareTo(entry.getDateTo()) <= 0) {
                    PriceInfo priceInfo = new PriceInfo();
                    priceInfo.setPrice(entry.getPrice());
                    priceInfo.setDate(date);
                    priceInfo.setShopId(entry.getStoreId().toString());
                    priceInfo.setProductId(entry.getProductId().toString());
                    filteredByDate.add(priceInfo);
                }
                else if (date.compareTo(entry.getDateTo()) > 0) break;

                date = getNextDate(date);

            }
        }

        Iterator<PriceInfo> priceInfoIterator;


        List<PriceInfo> filteredByDistance = new ArrayList<>();

        if (optionalGeoDist.isPresent() && optionalGeoLat.isPresent() && optionalGeoLng.isPresent()) {

            Integer geoDist = optionalGeoDist.get();
            Double geoLat = optionalGeoLat.get();
            Double geoLng = optionalGeoLng.get();

            priceInfoIterator = filteredByDate.iterator();

            while (priceInfoIterator.hasNext()) {

                PriceInfo priceInfo = priceInfoIterator.next();

                Double distance = distance(geoLat, geoLng, storeService.getStoreById(Long.parseLong(priceInfo.getShopId())).get().getLat(), storeService.getStoreById(Long.parseLong(priceInfo.getShopId())).get().getLng());

                if (distance < geoDist) {
                    priceInfo.setShopDist(distance.intValue());
                    filteredByDistance.add(priceInfo);
                }
            }
        }
        else if (optionalGeoDist.isPresent() || optionalGeoLat.isPresent() || optionalGeoLng.isPresent()) throw new BadRequestException();
        else filteredByDistance = filteredByDate;


        List<PriceInfo> filteredByShops = new ArrayList<>();

        if (optionalShops.isPresent()) {

            priceInfoIterator = filteredByDistance.iterator();

            while (priceInfoIterator.hasNext()) {

                PriceInfo priceInfo = priceInfoIterator.next();

                if (optionalShops.get().contains(priceInfo.getShopId())) filteredByShops.add(priceInfo);
            }
        }
        else filteredByShops = filteredByDate;


        List<PriceInfo> filteredByProducts = new ArrayList<>();

        if (optionalProducts.isPresent()) {

            priceInfoIterator = filteredByShops.iterator();

            while (priceInfoIterator.hasNext()) {
                PriceInfo priceInfo = priceInfoIterator.next();

                if (optionalProducts.get().contains(priceInfo.getProductId())) filteredByProducts.add(priceInfo);
            }

        }
        else filteredByProducts = filteredByShops;

        List<PriceInfo> filteredByTags = new ArrayList<>();

        if (optionalTags.isPresent()) {

            priceInfoIterator = filteredByProducts.iterator();

            while (priceInfoIterator.hasNext()) {
                PriceInfo priceInfo = priceInfoIterator.next();

                Boolean flag = false;

                List<String> productTags = productTagService.getTagsByProductId(Long.parseLong(priceInfo.getProductId()));
                List<String> storeTags = storeTagService.getTagsByStoreId(Long.parseLong(priceInfo.getShopId()));

                Iterator<String> stringIterator = productTags.iterator();

                while (stringIterator.hasNext()) {
                    if (optionalTags.get().contains(stringIterator.next())) {
                        if (!flag) {
                            filteredByTags.add(priceInfo);
                            flag = true;
                        }
                    }
                }

                stringIterator = storeTags.iterator();

                while (stringIterator.hasNext()) {
                    if (optionalTags.get().contains(stringIterator.next())) {
                        if (!flag) {
                            filteredByTags.add(priceInfo);
                            flag = true;
                        }
                    }
                }
            }
        }
        else {
            filteredByTags = filteredByProducts;
        }

        String sort;
        if (optionalSort.isPresent()) sort = optionalSort.get();
        else sort = "price|asc";

        String parts[] = sort.split(Pattern.quote("|"));

        if (parts[0].equalsIgnoreCase("price")) {
            Collections.sort(filteredByTags, new SortPriceInfoByPrice());
        }
        else if (parts[0].equalsIgnoreCase("date")){
            Collections.sort(filteredByTags, new SortPriceInfoByDate());
        }
        else if (parts[0].equalsIgnoreCase("geoDist")) {
            Collections.sort(filteredByTags,new SortPriceInfoByDistance());
        }
        else {
            throw new BadRequestException();
        }

        if (parts[1].equalsIgnoreCase("desc")) {
            Collections.reverse(filteredByTags);
        }

        PriceInfoList queryList = new PriceInfoList();
        queryList.setStart(start);
        queryList.setCount(count);
        queryList.setTotal(filteredByTags.size());


        priceInfoIterator = filteredByTags.iterator();

        while (priceInfoIterator.hasNext()) {

            PriceInfo priceInfo = priceInfoIterator.next();

            Long productId = Long.parseLong(priceInfo.getProductId());

            Product product = productService.getProductById(productId).get();
            priceInfo.setProductName(product.getName());
            priceInfo.setProductTags(productTagService.getTagsByProductId(productId));

            Long shopId = Long.parseLong(priceInfo.getShopId());

            Store store = storeService.getStoreById(shopId).get();
            priceInfo.setShopName(store.getPlace());
            priceInfo.setShopAddress(store.getAddress());
            priceInfo.setShopTags(storeTagService.getTagsByStoreId(shopId));

        }

        queryList.setPrices(filteredByTags);
        return queryList;

    }


    @RequestMapping(value ="/prices", method = RequestMethod.POST)
    public PriceInfoList postEntry(@RequestParam("format") Optional<String> optionalFormat, Entry entry, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {

        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!optionalFormat.isPresent()) format = "json";
        else format = optionalFormat.get();

        if (!format.equalsIgnoreCase("json")) throw new BadRequestException();

        if (entry.getPrice() == null || entry.getPrice() <= 0 || entry.getPrice() >= 1000000) throw new BadRequestException();

        Integer temp =  (int)(entry.getPrice()*100);
        Double price = temp.intValue()/100.0;

        entry.setPrice(price);

        Date dateFrom = entry.getDateFrom();
        Date dateTo = entry.getDateTo();

        if (dateFrom.compareTo(dateTo) > 0) throw new BadRequestException();

        if (!productService.getProductById(entry.getProductId()).isPresent()) throw new ProductNotFoundException();

        if (!storeService.getStoreById(entry.getShopId()).isPresent()) throw new ShopNotFoundException();

        hasProductService.saveEntry(entry);

        List<PriceInfo> prices = new ArrayList<>();

        Date date = dateFrom;

        int cnt = 0;

        while (date.compareTo(dateTo) <= 0) {

            cnt ++;

            PriceInfo priceInfo = new PriceInfo();
            priceInfo.setPrice(entry.getPrice());
            priceInfo.setDate(date);
            priceInfo.setProductId(entry.getProductId().toString());
            priceInfo.setShopId(entry.getShopId().toString());
            prices.add(priceInfo);
            try {
                date = getNextDate(date);
            }
            catch (Exception exc) {
                exc.printStackTrace();
            }

        }

        PriceInfoList priceInfoList = new PriceInfoList();
        priceInfoList.setStart(0);
        priceInfoList.setTotal(cnt);
        priceInfoList.setPrices(prices);


        return priceInfoList;
    }

    public static Double distance(Double lat1, Double lng1, Double lat2, Double lng2) {

        int R = 6378137;   /* Earth's mean radius in meter */
        double dLat = rad(lat1 -lat2);
        double dLong = rad(lng1-lng2);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(rad(lat1)) * Math.cos(rad(lat2)) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        return R*c;
    }


    private static Date getToday() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String dateString = format.format(new java.util.Date());
        Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        java.util.Date dateFrom = format.parse(dateString);
        return new java.sql.Date(dateFrom.getTime());
    }


    private static Date getNextDate(Date curDate) throws Exception {

        String nextDate = getNextDate(curDate.toString());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = sdf1.parse(nextDate);
        return new java.sql.Date(date.getTime());
    }


    private static String getNextDate(String curDate) {


        try {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            final java.util.Date date = format.parse(curDate);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            return format.format(calendar.getTime());
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
        return "";

    }

    private static double rad(double deg) {
        return deg*Math.PI/180;
    }

    private static String tokenGenerator() {

        if (tokenList == null) tokenList = new ArrayList<>();

        SecureRandom random = new SecureRandom();


        long longToken = Math.abs(random.nextLong());
        while (tokenList.contains(longToken)) {
            longToken = Math.abs(random.nextLong());
        }

        return "ABC123";
    }

    private static void addToken(String token) {

        //if (tokenList == null) throw new BadRequestException();
        //tokenList.add(Long.parseLong(token));
    }

    private static Boolean verifyToken(String token) {
        //if (tokenList == null) throw new BadRequestException();
        //return tokenList.contains(token);
        return token.equals("ABC123");
    }
    private static void deleteToken(String token) {

        // if (tokenList == null) throw new BadRequestException();

        // if (!tokenList.contains(token)) throw new RuntimeException();

        // tokenList.remove(token);
    }


}
