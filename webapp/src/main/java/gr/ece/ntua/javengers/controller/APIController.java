package gr.ece.ntua.javengers.controller;

import gr.ece.ntua.javengers.entity.HasProduct;
import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.Store;
import gr.ece.ntua.javengers.entity.comparator.SortProductById;
import gr.ece.ntua.javengers.entity.comparator.SortProductByName;
import gr.ece.ntua.javengers.entity.comparator.SortStoreById;
import gr.ece.ntua.javengers.entity.comparator.SortStoreByName;
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
import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
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
    public HashMap<String, String> loginUser(@RequestParam("format") Optional<String> optionalFormat, LoginUser loginUser) { // @RequestHeader Optional<HttpHeaders> headers) {

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
        // headers.get().add("X-OBSERVATORY-AUTH", token);

        //String url = "http://localhost/observatory/api/login";
        //HttpPost httpPost = new HttpPost(url);

        //httpPost.addHeader("X-OBSERVATORY-AUTH", token);

        HashMap<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("token", token);

        return jsonResponse;

    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public HashMap<String, String> logoutUser(@RequestParam("format") Optional<String> formatUR, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {

        if (!verifyToken(token)) throw new BadRequestException();

        deleteToken(token);

        HashMap<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "OK");

        return jsonResponse;


    }


    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ProductList getProducts (@RequestParam("format") Optional<String> formatURL, @RequestParam("start") Optional<Integer> startURL, @RequestParam("count") Optional<Integer> countURL,
                                       @RequestParam("status") Optional<String> statusURL, @RequestParam("sort") Optional<String> sortURL, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {

        if (!verifyToken(token)) throw new ForbiddenException();


        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        Integer start;
        if (startURL.isPresent()) start = startURL.get();
        else start = 0;

        Integer count;
        if (countURL.isPresent()) count = countURL.get();
        else count = 20;

        if (start < 0 || count < 0) throw new BadRequestException();

        String status;
        if (statusURL.isPresent()) status = statusURL.get();
        else status = "ACTIVE";

        if (!status.equalsIgnoreCase("all") && !status.equalsIgnoreCase("active") && !status.equalsIgnoreCase("withdrawn")) throw new BadRequestException();

        String sort;
        if (sortURL.isPresent()) sort = sortURL.get();
        else sort = "id|DESC";

        ProductList productList = new ProductList();

        productList.setStart(start);
        productList.setCount(count);

        String parts[] = sort.split(Pattern.quote("|"));

        if (!parts[0].equalsIgnoreCase("id") && !parts[0].equalsIgnoreCase("name")) throw new BadRequestException();

        if (!parts[1].equalsIgnoreCase("asc") && !parts[1].equalsIgnoreCase("desc")) throw new BadRequestException();

        List<Product> products = productService.listAll();

        Boolean flag = true;

        if (status.equalsIgnoreCase("ACTIVE")) {
            flag = false;
        }

        List<Product> statusProducts = new ArrayList<>();

        if (!status.equalsIgnoreCase("ALL")) {

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

        if (parts[1].equalsIgnoreCase("DESC")) {
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
    public gr.ntua.ece.javengers.client.model.Product getProductById(@RequestParam("format") Optional<String> formatURL, @PathVariable("id") Long id, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {


        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (productService.getProductById(id).isPresent()) {

            return productService.getProductAndTagsById(id);
        }

        throw new ProductNotFoundException();

    }

    @RequestMapping(value ="/products", method = RequestMethod.POST)
    public gr.ntua.ece.javengers.client.model.Product postProduct(@RequestParam("format") Optional<String> formatURL, gr.ntua.ece.javengers.client.model.Product product) {


        String token = "ABC123";

        if (!verifyToken(token)) throw new ForbiddenException();
        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (product.getName().equals("") || product.getName() == null) throw new BadRequestException();
        if (product.getDescription().equals("") || product.getDescription() == null) throw new BadRequestException();
        if (product.getCategory().equals("") || product.getCategory() == null) throw new BadRequestException();
        if (product.getTags() == null) throw new BadRequestException();

        return productService.saveProduct(product);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public gr.ntua.ece.javengers.client.model.Product putProduct(@RequestParam("format") Optional<String> formatURL, @RequestBody gr.ntua.ece.javengers.client.model.Product newProduct, @PathVariable("id") Long id, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {

        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

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

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public gr.ntua.ece.javengers.client.model.Product patchProduct(@RequestParam("format") Optional<String> formatURL, @RequestBody gr.ntua.ece.javengers.client.model.Product updateProduct, @PathVariable("id") Long id, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {


        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

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

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> deleteProduct(@RequestParam("format") Optional<String> formatURL, @PathVariable("id") Long id, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {

        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (!productService.getProductById(id).isPresent()) throw new ProductNotFoundException();

        productService.deleteProductById(id);   // if else with admin

        HashMap<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "OK");

        return jsonResponse;

    }

    @RequestMapping(value = "/shops", method = RequestMethod.GET)
    public ShopList getShops (@RequestParam("format") Optional<String> formatURL, @RequestParam("start") Optional<Integer> startURL, @RequestParam("count") Optional<Integer> countURL,
                                       @RequestParam("status") Optional<String> statusURL, @RequestParam("sort") Optional<String> sortURL, @RequestHeader(value = "X-OBSERVATORY-AUTH") String token) {



        if (!verifyToken(token)) throw new ForbiddenException();

        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        Integer start;
        if (startURL.isPresent()) start = startURL.get();
        else start = 0;

        Integer count;
        if (countURL.isPresent()) count = countURL.get();
        else count = 20;

        String status;
        if (statusURL.isPresent()) status = statusURL.get();
        else status = "ACTIVE";

        if (!status.equalsIgnoreCase("all") && !status.equalsIgnoreCase("active") && !status.equalsIgnoreCase("withdrawn")) throw new BadRequestException();

        String sort;
        if (sortURL.isPresent()) sort = sortURL.get();
        else sort = "id|DESC";

        ShopList shopList = new ShopList();

        shopList.setStart(start);
        shopList.setCount(count);

        String parts[] = sort.split(Pattern.quote("|"));

        List<Store> stores = storeService.listAll();

        Boolean flag = true;

        if (status.equalsIgnoreCase("ACTIVE")) {
            flag = false;
        }

        List<Store> statusStores = new ArrayList<>();

        if (!status.equalsIgnoreCase("ALL")) {

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

        if (parts[1].equalsIgnoreCase("DESC")) {
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
    public gr.ntua.ece.javengers.client.model.Shop getShopById(@RequestParam("format") Optional<String> formatURL, @PathVariable("id") Long id) {

        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (storeService.getStoreById(id).isPresent()) {

            return storeService.getStoreAndTagsById(id);
        }

        throw new ShopNotFoundException();

    }

    @RequestMapping(value ="/shops", method = RequestMethod.POST)
    public gr.ntua.ece.javengers.client.model.Shop postShop(@RequestParam("format") Optional<String> formatURL, gr.ntua.ece.javengers.client.model.Shop shop) {

        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (shop.getName().equals("") || shop.getName() == null) throw new BadRequestException();
        if (shop.getAddress().equals("") || shop.getAddress() == null) throw new BadRequestException();
        if (shop.getLat() <= 0 || shop.getLng() <= 0) throw new BadRequestException();
        if (shop.getTags() == null) throw new BadRequestException();

        return storeService.saveShop(shop);
    }

    @RequestMapping(value = "/shops/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Shop putShop(@RequestParam("format") Optional<String> formatURL, @RequestBody Shop newShop, @PathVariable("id") Long id) {

        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        newShop.setId(id.toString());

        if (newShop.getName().equals("") || newShop.getName() == null) throw new BadRequestException();
        if (newShop.getAddress().equals("") || newShop.getAddress() == null) throw new BadRequestException();
        if (newShop.getLat() <= 0 || newShop.getLng() <= 0) throw new BadRequestException();
        if (newShop.getTags() == null) throw new BadRequestException();

        if (!storeService.getStoreById(id).isPresent()) throw new ShopNotFoundException();

        storeService.updateStore(newShop);

        return newShop;

    }

    @RequestMapping(value = "/shops/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Shop patchShop (@RequestParam("format") Optional<String> formatURL, @RequestBody Shop updateShop, @PathVariable("id") Long id) {

        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

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


    @RequestMapping(value = "/shops/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> deleteShop(@RequestParam("format") Optional<String> formatURL, @PathVariable("id") Long id) {

        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

        if (!format.equalsIgnoreCase("json")) throw new FormatBadRequestException();

        if (!storeService.getStoreById(id).isPresent()) throw new ShopNotFoundException();

        storeService.deleteStoreById(id);

        HashMap<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "OK");

        return jsonResponse;

    }

    @RequestMapping(value = "prices", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public QueryList queryEntries (@RequestParam("format") Optional<String> formatURL, @RequestParam("start") Optional<Integer> startURL, @RequestParam("count") Optional<Integer> countURL,
                                   @RequestParam("geoDist") Optional<Integer> geoDistURL, @RequestParam("geoLng") Optional<Double> geoLngURL, @RequestParam("geoLat") Optional<Double> geoLatURL,
                                   @RequestParam("dateFrom") Optional<Date> dateFromURL, @RequestParam("dateTo") Optional<Date> dateToURL, @RequestParam("shops") Optional<List<String> > shopsURL,
                                   @RequestParam("products") Optional<List<String> > productsURL, @RequestParam("tags") Optional<List<String> > tagsURL, @RequestParam("sort") Optional<String> sortURL) {

        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

        if (!format.equalsIgnoreCase("json")) throw new BadRequestException();

        Integer start;
        if (startURL.isPresent()) start = startURL.get();
        else start = 0;

        Integer count;
        if (countURL.isPresent()) count = countURL.get();
        else count = 20;

        if (start < 0 || count < 0) throw new BadRequestException();

        List<HasProduct> entryList = hasProductService.getAllEntries();

        List<HasProduct> filteredByDistance = new ArrayList<>();

        if (geoDistURL.isPresent() && geoLatURL.isPresent() && geoLngURL.isPresent()) {

            Integer geoDist = geoDistURL.get();
            Double geoLat = geoLatURL.get();
            Double geoLng = geoLngURL.get();

            Iterator<HasProduct> entryIterator = entryList.iterator();

            while (entryIterator.hasNext()) {
                HasProduct entry = entryIterator.next();

                Double distance = distance(geoLat, geoLng, storeService.getStoreById(entry.getStoreId()).get().getLat(), storeService.getStoreById(entry.getStoreId()).get().getLng());

                if (distance < geoDist) filteredByDistance.add(entry);
            }
        }
        else if (geoDistURL.isPresent() || geoLatURL.isPresent() || geoLngURL.isPresent()) throw new BadRequestException();
        else filteredByDistance = entryList;





        Date dateFrom;
        Date dateTo;

        if (dateFromURL.isPresent() && dateToURL.isPresent()) {
            dateFrom = dateFromURL.get();
            dateTo = dateToURL.get();
            if (dateFrom.compareTo(dateTo) > 0) throw new BadRequestException();
        }

        List<HasProduct> filteredByDate = new ArrayList<>();


        if (!dateFromURL.isPresent() && !dateToURL.isPresent()) {


            java.sql.Date sqlDate;

            try {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = dateFormat.format(new java.util.Date());
                Calendar calendar = Calendar.getInstance();
                java.util.Date date = calendar.getTime();
                java.util.Date tempDate = dateFormat.parse(dateString);
                sqlDate = new java.sql.Date(tempDate.getTime());
                dateFrom = sqlDate;
                dateTo = sqlDate;

                Iterator<HasProduct> entryIt = filteredByDistance.iterator();

                while (entryIt.hasNext()) {

                    HasProduct entry = entryIt.next();
                    if (entry.getDateFrom().compareTo(dateTo) < 0  && dateFrom.compareTo(entry.getDateTo()) < 0)
                        filteredByDate.add(entry);

                }
            }
            catch (Exception exc) {
                exc.printStackTrace();
            }

        }
        else if (!dateFromURL.isPresent() || !dateToURL.isPresent()) {
            throw new BadRequestException();
        }


        List<HasProduct> filteredByShops = new ArrayList<>();

        if (shopsURL.isPresent()) {

            Iterator<HasProduct> entryIterator = filteredByDate.iterator();
            while (entryIterator.hasNext()) {
                HasProduct entry = entryIterator.next();

                if (shopsURL.get().contains(entry.getStoreId().toString())) filteredByShops.add(entry);
            }
        }
        else filteredByShops = filteredByDate;

        List<HasProduct> filteredByProducts = new ArrayList<>();

        if (productsURL.isPresent()) {

            Iterator<HasProduct> entryIterator = filteredByShops.iterator();

            while (entryIterator.hasNext()) {
                HasProduct entry = entryIterator.next();

                if (productsURL.get().contains(entry.getProductId().toString())) filteredByProducts.add(entry);
            }

        }
        else filteredByProducts = filteredByShops;

        List<HasProduct> filteredByTags = new ArrayList<>();

        if (tagsURL.isPresent()) {

            Iterator<HasProduct> entryIterator = filteredByProducts.iterator();

            while (entryIterator.hasNext()) {
                HasProduct entry = entryIterator.next();

                Boolean flag = false;

                List<String> productTags = productTagService.getTagsByProductId(entry.getProductId());
                List<String> storeTags = storeTagService.getTagsByStoreId(entry.getStoreId());

                Iterator<String> stringIterator = productTags.iterator();

                while (stringIterator.hasNext()) {
                    if (tagsURL.get().contains(stringIterator.next())) {
                        if (!flag) {
                            filteredByTags.add(entry);
                            flag = true;
                        }
                    }
                }

                stringIterator = storeTags.iterator();

                while (stringIterator.hasNext()) {
                    if (tagsURL.get().contains(stringIterator.next())) {
                        if (!flag) {
                            filteredByTags.add(entry);
                            flag = true;
                        }
                    }
                }
            }
        }
        else {
            filteredByTags = filteredByProducts;
        }


        QueryList queryList = new QueryList();
        queryList.setStart(start);
        queryList.setCount(count);

        queryList.setTotal(filteredByTags.size());

        return queryList;

    }


    @RequestMapping(value ="/prices", method = RequestMethod.POST)
    public Entry postEntry(@RequestParam("format") Optional<String> formatURL, Entry entry) {

        String format;
        if (!formatURL.isPresent()) format = "json";
        else format = formatURL.get();

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

        return hasProductService.saveEntry(entry);
    }

    public static Double distance(Double lat1, Double lng1, Double lat2, Double lng2) {

        int R = 6378137;   /* Earth's mean radius in meter */
        double dLat = rad(lat1 -lat2);
        double dLong = rad(lng1-lng2);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(rad(lat1)) * Math.cos(rad(lat2)) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        return R*c;
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
