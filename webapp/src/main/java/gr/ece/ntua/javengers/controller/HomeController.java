package gr.ece.ntua.javengers.controller;

import gr.ece.ntua.javengers.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping("/home")
    public String home() {

        return "index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Boolean isRegisteredUser = true;

        try {
            User loggedUser =  (User)authentication.getPrincipal();
        }
        catch (Exception exc) {
            isRegisteredUser = false;
        }

        model.addAttribute("isRegisteredUser", isRegisteredUser);

        return "index";
    }


    /*
    public String redirectToHome(){
        return "redirect:/home";
    }*/
}
