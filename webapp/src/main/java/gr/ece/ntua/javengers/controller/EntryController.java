package gr.ece.ntua.javengers.controller;

import gr.ece.ntua.javengers.entity.HasProduct;
import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.Store;
import gr.ece.ntua.javengers.entity.User;
import gr.ece.ntua.javengers.entity.comparator.SortEntryByPrice;
import gr.ece.ntua.javengers.entity.comparator.SortProductByStars;
import gr.ece.ntua.javengers.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.*;

@Controller
public class EntryController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private HasProductService hasProductService;

    @Autowired
    private ProductTagService productTagService;

    @Autowired
    public void setProductService(UserService userService, ProductService productService, StoreService storeService, HasProductService hasProductService, ProductTagService productTagService) {

        this.userService = userService;
        this.productService = productService;
        this.storeService = storeService;
        this.hasProductService = hasProductService;
        this.productTagService = productTagService;
    }

//    @RequestMapping(value = "/entry/list/{id}", method = RequestMethod.GET)
//    public String listProducts(@PathVariable("id") Long productId, Model model) {
//
//
//        List<HasProduct> entries = hasProductService.getActiveEntriesById(productId);
//
//        model.addAttribute("entries", entries);
//
//        return "entries";
//    }

    @RequestMapping(value = "/entry/list", method = RequestMethod.GET)
    public String listProducts(@RequestParam("id") Optional<Long> optionalProductId, @RequestParam("priceFrom") Optional<Double> optionalPriceFrom, @RequestParam("priceTo") Optional<Double> optionalPriceTo,
                               @RequestParam("clientLat") Optional<Double> optionalClientLat,@RequestParam("clientLong") Optional<Double> optionalClientLong,@RequestParam("distance") Optional<Integer> optionalDistance,@RequestParam("entryId") Optional<Long> optionalEntryId,Model model) {

        Double priceFrom = 0.0;
        if (optionalPriceFrom.isPresent()) priceFrom = optionalPriceFrom.get();

        Double priceTo = 10000000.0;
        if (optionalPriceFrom.isPresent()) priceTo = optionalPriceTo.get();

        Integer distance=1000000;
        if(optionalDistance.isPresent()) distance=optionalDistance.get();

        Double clientLat=37.975504;
        Double clientLong=23.735696;
        if((optionalClientLat.isPresent() && !optionalClientLong.isPresent()) || (optionalClientLong.isPresent() && !optionalClientLat.isPresent())){
            throw new RuntimeException();
        }
        else if(optionalClientLat.isPresent() && optionalClientLong.isPresent()){
            clientLat = optionalClientLat.get();
            clientLong = optionalClientLong.get();
        }
        if(optionalProductId.isPresent()) {
            Long productId=optionalProductId.get();
            List<HasProduct> filteredEntries = hasProductService.filterEntries(productId, priceFrom, priceTo, clientLat, clientLong, distance);

            Collections.sort(filteredEntries, new SortEntryByPrice());
            model.addAttribute("entries", filteredEntries);
            model.addAttribute("productId", productId);
        }


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Boolean isRegisteredUser = true;

        try {
            User loggedUser =  (User)authentication.getPrincipal();
        }
        catch (Exception exc) {
            isRegisteredUser = false;
        }

        model.addAttribute("isRegisteredUser", isRegisteredUser);

        /*Boolean storeSelected = false;
        if(optionalEntryId.isPresent()) {
            storeSelected = true;
            Long entryId=optionalEntryId.get();
            Store store= hasProductService.getStoreByEntryId(entryId);
            model.addAttribute("store",store);
        }
        */
        //model.addAttribute("storeSelected" , storeSelected);
        return "entries";
    }



//    @RequestMapping(value = "/profile/myentries", method = RequestMethod.GET)
//    public String listUserEntries(@RequestParam, Model model) {
//
//
//        List<Product> products = productTagService.getProductsByTag(keyWord);
//
//        model.addAttribute("products", products);
//
//        return "products";
//
//    }

    @RequestMapping(value = "/product/list", method = RequestMethod.GET)
    public String searchProducts(@RequestParam("keyWord") String keyWord, @RequestParam("pageIndex") Optional<Integer> optionalPageIndex, Model model) {


        Integer sizeOfPage = 6;

        Integer pageIndex = 1;
        if (optionalPageIndex.isPresent()) pageIndex = optionalPageIndex.get();

        Boolean leftPage = false;
        if (pageIndex != 1) leftPage = true;

        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("leftPage", leftPage);

        if (optionalPageIndex.isPresent()) pageIndex = optionalPageIndex.get();

        List<Product> products = productTagService.getProductsByTag(keyWord);

        Collections.sort(products, new SortProductByStars());

        if (products == null || products.size() == 0) {

            return "/index";
        }

        Boolean rightPage = true;
        if (products.size() <= sizeOfPage*pageIndex) rightPage = false;
        model.addAttribute("rightPage", rightPage);

        List<Product> productsOfPage = new ArrayList<>();

        Integer previousProducts = sizeOfPage*(pageIndex-1);

        Iterator<Product> productIterator = products.iterator();

        Integer cnt = 0;

        while (productIterator.hasNext()) {

            Product product = productIterator.next();
            if (cnt++ < previousProducts) continue;
            productsOfPage.add(product);
            if (cnt >= sizeOfPage*pageIndex) break;
        }

        model.addAttribute("products", productsOfPage);

        return "products";

    }



    @RequestMapping(value = "/product/{category}", method = RequestMethod.GET)
    public /*@ResponseBody*/ String getProductsByCategory(@PathVariable("category") String category, Model model){
        List<Product> products = productService.getProductsByCategory(category);
        model.addAttribute("products", products);
        return "products";
    }



    /*@RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<Product> getProductById(@PathVariable("id") Long id){

        return productService.getProductById(id);
    }*/

    /*
    @RequestMapping(value = "/product/{name}", method = RequestMethod.GET)
    public @ResponseBody Optional<Product> getProductByName(@PathVariable("name") String name){

        return productService.getProductByName(name);
    }*/

    @RequestMapping(value = "entry/list/update/{id}", method = RequestMethod.GET)
    public RedirectView withdrawEntry(@PathVariable("id") Long id, Model model) {

        Optional<HasProduct> optionalEntry = hasProductService.getEntryById(id);

        if (!optionalEntry.isPresent()) throw new RuntimeException();

        HasProduct entry = optionalEntry.get();

        hasProductService.updateEntry(entry);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/entry/list?id=" + entry.getProductId());

        return redirectView;
    }


    @RequestMapping(value = "profile/add/entry", method = RequestMethod.GET)
    public String addProduct(Model model) {

        Product product = new Product();

        model.addAttribute("product", product);

        Store store = new Store();

        model.addAttribute("store", store);

        HasProduct hasProduct = new HasProduct();

        model.addAttribute("hasProduct", hasProduct);

        model.addAttribute("barcodeSearched", false);

        return "addProduct";

    }

    @RequestMapping(value = "profile/add/entry", method = RequestMethod.POST)
    public String addProductPost(@Valid Product product, @Valid Store store, @Valid HasProduct hasProduct, @Valid Double productStars, @Valid String productTags, Model model, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "error";


        Boolean addEntry = store.getPlace() != null;

        if (addEntry) {


            productService.updateStars(product, productStars);

            productTagService.insertTags(product.getBarcode(), productTags);

            storeService.saveStore(store);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            User loggedUser =  (User)authentication.getPrincipal();

            User user = userService.getUserByUserName(loggedUser.getUserName()).get();

            Long userId = user.getId();

            Long productId = productService.getProductByBarcode(product.getBarcode()).get().getId();

            Long storeId = storeService.getStoreByLocation(store.getLat(), store.getLng()).get().getId();

            hasProduct.setUserId(userId);
            hasProduct.setProductId(productId);
            hasProduct.setStoreId(storeId);

            hasProductService.saveEntry(hasProduct);

            model.addAttribute("user", user);

            return "/profile";

        }

        else {

            Boolean searchBarcode = product.getName() == null;

            if (searchBarcode) {

                Boolean productExists;
                Optional<Product> tempProduct = productService.getProductByBarcode(product.getBarcode());

                productExists = tempProduct.isPresent();

                model.addAttribute("barcodeSearched", true);
                model.addAttribute("productExists", productExists);

                if (productExists)
                    model.addAttribute("product", tempProduct.get());

            }
            else {

                productService.saveProduct(product);
                model.addAttribute("productAdded", true);
                model.addAttribute("barcodeSearched", true);
            }

            model.addAttribute("store", store);
            model.addAttribute("hasProduct", hasProduct);

            return "addProduct";
        }
    }

    /*
    private static Date stringToDate(String strDate) throws Exception {

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = sdf1.parse(strDate);

        return new java.sql.Date(date.getTime());


    }*/

}

