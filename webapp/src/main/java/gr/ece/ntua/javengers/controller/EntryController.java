package gr.ece.ntua.javengers.controller;

import gr.ece.ntua.javengers.entity.HasProduct;
import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.Store;
import gr.ece.ntua.javengers.entity.User;
import gr.ece.ntua.javengers.entity.comparator.SortProductByStars;
import gr.ece.ntua.javengers.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @RequestMapping(value = "/entry/list/{id}", method = RequestMethod.GET)
    public String listProducts(@PathVariable("id") Long productId, Model model) {


        List<HasProduct> entries = hasProductService.getEntriesById(productId);

        model.addAttribute("entries", entries);

        return "entries";
    }

    @RequestMapping(value = "/product/list", method = RequestMethod.GET)
    public String searchProducts(@Valid String keyWord, Model model) {


        List<Product> products = productTagService.getProductsByTag(keyWord);

        Collections.sort(products, new SortProductByStars());

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

