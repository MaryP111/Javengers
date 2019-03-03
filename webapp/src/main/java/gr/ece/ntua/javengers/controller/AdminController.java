package gr.ece.ntua.javengers.controller;

import gr.ece.ntua.javengers.entity.HasProduct;
import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.Store;
import gr.ece.ntua.javengers.entity.User;
import gr.ece.ntua.javengers.service.HasProductService;
import gr.ece.ntua.javengers.service.ProductService;
import gr.ece.ntua.javengers.service.StoreService;
import gr.ece.ntua.javengers.service.UserService;
import gr.ntua.ece.javengers.client.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private HasProductService entryService;

    @Autowired
    public void setUserService(UserService userService) {

        this.userService = userService;
    }

    @RequestMapping(value = "/admin/list", method = RequestMethod.GET)
    public Collection<User> listUsers(){

        return userService.listAll();
    }

    @RequestMapping(value= "/admin/users/{id}", method = RequestMethod.GET)
    public String listUser(@PathVariable("id") Long userId, Model model) {
         userService.deleteUserById(userId);
         return adminUsers(model);
    }


    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String adminUsers(Model model) {
        List<User> users = userService.listAll();
        model.addAttribute("users", users);
        return "adminUsers";

    }

    @RequestMapping(value= "/admin/products/{id}", method = RequestMethod.GET)
    public String listProducts(@PathVariable("id") Long productId, Model model) {
        productService.deleteProductById(productId);
        return adminProduct(model);
    }


    @RequestMapping(value = "/admin/products", method = RequestMethod.GET)
    public String adminProduct(Model model) {
        List<Product> products = productService.listAll();
        model.addAttribute("products", products);
        return "adminProducts";
    }

    @RequestMapping(value= "/admin/stores/{id}", method = RequestMethod.GET)
    public String listStores(@PathVariable("id") Long storeId, Model model) {
        storeService.deleteStoreById(storeId);
        return adminStore(model);
    }


    @RequestMapping(value = "/admin/stores", method = RequestMethod.GET)
    public String adminStore(Model model) {
        List<Store> stores = storeService.listAll();
        model.addAttribute("stores", stores);
        return "adminStores";
    }

    @RequestMapping(value= "/admin/entries/{id}", method = RequestMethod.GET)
    public String listEntries(@PathVariable("id") Long entryId, Model model) {
        entryService.deleteEntryById(entryId);
        return adminEntry(model);
    }


    @RequestMapping(value = "/admin/entries", method = RequestMethod.GET)
    public String adminEntry(Model model) {

        List<HasProduct> entries = entryService.getAllEntries();
        model.addAttribute("entries", entries);
        return "adminEntries";

    }

    @RequestMapping(value = "/admin/products/entries/{id}", method = RequestMethod.GET)
    public String adminProductEntries(@PathVariable("id") Long productId, Model model) {
        List<HasProduct> entries = entryService.getEntriesById(productId);
        model.addAttribute("entries", entries);
        return "adminEntries";
    }

    @RequestMapping(value = "/admin/login", method = RequestMethod.GET)
    public String adminLogin() {
        return "adminLogin";
    }

//    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
//    public String adminPass(String adminname, String password, Model model) {
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(adminname , password);
//        securityContext.setAuthentication(authReq);
//        SecurityContextHolder.setContext(securityContext);
//
//        boolean isAuthenticated = SecurityUtils.isAuthenticated();
//        assertThat(isAuthenticated).isTrue();
//        return adminEntry(model);
//    }
}
