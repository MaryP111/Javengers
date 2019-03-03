package gr.ece.ntua.javengers.controller;

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
import java.util.Optional;

@Controller
public class UserController {

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

    /*@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<User> getUserById(@PathVariable("id") Long id){

        return userService.getUserById(id);
    }*/

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam("login") Optional<String> loginFlag, Model model) {

        Boolean loginFailed = loginFlag.isPresent();

        model.addAttribute("loginFailed", loginFailed);

        return "login";
    }

    /*@RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login() {
        return "login";
    }*/

    @RequestMapping(value ="/signup", method = RequestMethod.GET)
    public String signup(Model model) {

        User user = new User();

        model.addAttribute("user", user);

        return "signup";

    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupPost(@Valid User user, Model model, BindingResult bindingResult) {


        if (bindingResult.hasErrors())
            return "error";

        Boolean flag = false;

        if (userService.checkUserNameExists(user.getUserName())) {
            model.addAttribute("userNameExists", true);
            flag = true;
        }

        if (userService.checkEmailExists(user.getEmail())) {
            model.addAttribute("emailExists", true);
            flag = true;
        }

        if (userService.checkPhoneNumberExists(user.getPhoneNumber())) {
            model.addAttribute("phoneNumberExists", true);
            flag = true;
        }

        if (flag) return "signup";

        userService.createUser(user);

        return "index";

    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User loggedUser =  (User)authentication.getPrincipal();

        model.addAttribute("user", loggedUser);

        return "profile";

    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String profilePost(@Valid User user, Model model, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "error";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User updatedUser =  (User)authentication.getPrincipal();

        updatedUser.setAge(user.getAge());
        updatedUser.setUniversity(user.getUniversity());
        updatedUser.setProfession(user.getProfession());
        updatedUser.setCity(user.getCity());
        updatedUser.setWebsite(user.getWebsite());

        userService.saveUser(updatedUser);

        //model.addAttribute("user", user);

        return "/profile";

    }


}
