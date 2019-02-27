package gr.ece.ntua.javengers.controller;

import gr.ece.ntua.javengers.entity.HasProduct;
import gr.ece.ntua.javengers.entity.User;
import gr.ece.ntua.javengers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    public void setUserService(UserService userService) {

        this.userService = userService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Collection<User> listUsers(){

        return userService.listAll();
    }

    @RequestMapping(value= "/admin/users/{id}", method = RequestMethod.GET)
    public String listUser(@PathVariable("id") Long userId) {
         userService.deleteUserById(userId);
         return "adminUsers";
    }


    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String adminUsers(Model model) {
        List<User> users = userService.listAll();
        model.addAttribute("users", users);
        return "adminUsers";

    }
}
