package gr.ece.ntua.javengers.controller;

import gr.ece.ntua.javengers.entity.HasProduct;
import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.Store;
import gr.ece.ntua.javengers.entity.User;
import gr.ece.ntua.javengers.service.HasProductService;
import gr.ece.ntua.javengers.service.ProductService;
import gr.ece.ntua.javengers.service.StoreService;
import gr.ece.ntua.javengers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public void setProductService(UserService userService, ProductService productService, StoreService storeService, HasProductService hasProductService) {

        this.userService = userService;
        this.productService = productService;
        this.storeService = storeService;
        this.hasProductService = hasProductService;
    }

    @RequestMapping(value = "/product/list", method = RequestMethod.GET)
    public String listProducts(@Valid String keyWord, Model model) {

        List<Product> products = productService.listAll();

        model.addAttribute("products", products);

        return "products";
    }

    //Returns multiple products of a specific category
    @RequestMapping(value = "/product/{category}", method = RequestMethod.GET)
    public /*@ResponseBody*/ String getProductByCategory(@PathVariable("category") String category, Model model){
        List<Product> products = productService.getProductByCategory(category);
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
    public String addProductPost(@Valid Product product, @Valid Store store, @Valid HasProduct hasProduct, Model model, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "error";


        Boolean addEntry = store.getPlace() != null;

        if (addEntry) {

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

}

