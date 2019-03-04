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
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    private final static String authToken = "ABC123";

    @Autowired
    public void setUserService(UserService userService) {

        this.userService = userService;
    }

    @RequestMapping(value = "/admin/login", method = RequestMethod.GET)
    public String adminLogin() {

        return "adminLogin";
    }

    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    public RedirectView adminLogin(String admin, String password) {

        RedirectView redirectView = new RedirectView();

        if (admin.equals("admin") && password.equals("admin")) {
            redirectView.setUrl("/admin/users?token=" + authToken);
        }
        else {
            redirectView.setUrl("/admin/login");
        }

        return redirectView;

    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String adminGetUsers(@RequestParam("token") Optional<String> optionalToken, Model model){

        if (!optionalToken.isPresent() || !optionalToken.get().equals(authToken)) return "adminLogin";

        List<User> users = userService.listAll();

        model.addAttribute("users", users);

        return "adminUsers";
    }

//    @RequestMapping(value= "/admin/users/{id}", method = RequestMethod.GET)
//    public String listUser(@PathVariable("id") Long userId, Model model) {
//         userService.deleteUserById(userId);
//         return adminUsers(model);
//    }


    @RequestMapping(value= "/admin/users/{userId}", method = RequestMethod.GET)
    public RedirectView adminDeletesUser(@PathVariable("userId") Long userId, Model model, @RequestParam("token") Optional<String> optionalToken) {

        RedirectView redirectView = new RedirectView();

        if (!optionalToken.isPresent() || !optionalToken.get().equals(authToken)) {
            redirectView.setUrl("/admin/login");
            return redirectView;
        }

        userService.deleteUserById(userId);
        model.addAttribute("users", userService.listAll());
        redirectView.setUrl("/admin/users?token=" + authToken);
        return redirectView;
    }


    @RequestMapping(value = "/admin/products", method = RequestMethod.GET)
    public String adminGetsProduct(@RequestParam("token") Optional<String> optionalToken, Model model) {

        if (!optionalToken.isPresent() || !optionalToken.get().equals(authToken)) return "adminLogin";

        List<Product> products = productService.listAll();

        model.addAttribute("products", products);

        return "adminProducts";
    }

    @RequestMapping(value= "/admin/products/{productId}", method = RequestMethod.GET)
    public RedirectView adminDeletesProduct(@PathVariable("productId") Long productId, Model model, @RequestParam("token") Optional<String> optionalToken) {

        RedirectView redirectView = new RedirectView();

        if (!optionalToken.isPresent() || !optionalToken.get().equals(authToken)) {
            redirectView.setUrl("admin/login");
            return redirectView;
        }

        productService.deleteProductById(productId);
        model.addAttribute("products", productService.listAll());
        redirectView.setUrl("/admin/products?token=" + authToken);
        return redirectView;

    }


    @RequestMapping(value = "/admin/stores", method = RequestMethod.GET)
    public String adminGetsStores(@RequestParam("token") Optional<String> optionalToken, Model model) {

        if (!optionalToken.isPresent() || !optionalToken.get().equals(authToken)) return "adminLogin";

        List<Store> stores = storeService.listAll();
        model.addAttribute("stores", stores);

        return "adminStores";
    }

    @RequestMapping(value= "/admin/stores/{storeId}", method = RequestMethod.GET)
    public RedirectView adminDeletesStore(@PathVariable("storeId") Long storeId, Model model, @RequestParam("token") Optional<String> optionalToken) {

        RedirectView redirectView = new RedirectView();

        if (!optionalToken.isPresent() || !optionalToken.get().equals(authToken)) {
            redirectView.setUrl("admin/login");
            return redirectView;
        }

        storeService.deleteStoreById(storeId);
        model.addAttribute("stores", storeService.listAll());
        redirectView.setUrl("/admin/stores?token=" + authToken);
        return redirectView;

    }


    @RequestMapping(value = "/admin/entries", method = RequestMethod.GET)
    public String adminGetsEntries(@RequestParam("token") Optional<String> optionalToken, Model model) {

        if (!optionalToken.isPresent() || !optionalToken.get().equals(authToken)) return "adminLogin";

        List<HasProduct> entries = entryService.getAllEntries();
        model.addAttribute("entries", entries);

        return "adminEntries";

    }

    @RequestMapping(value = "/admin/entries/{entryId}", method = RequestMethod.GET)
    public RedirectView adminDeletesEntry(@PathVariable("entryId") Long entryId, Model model, @RequestParam("token") Optional<String> optionalToken) {

        RedirectView redirectView = new RedirectView();

        if (!optionalToken.isPresent() || !optionalToken.get().equals(authToken)) {
            redirectView.setUrl("admin/login");
            return redirectView;
        }

        entryService.deleteEntryById(entryId);
        model.addAttribute("entries", entryService.getAllEntries());
        redirectView.setUrl("/admin/entries?token=" + authToken);
        return redirectView;
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
