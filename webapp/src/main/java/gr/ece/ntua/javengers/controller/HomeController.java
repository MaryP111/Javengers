package gr.ece.ntua.javengers.controller;

import gr.ece.ntua.javengers.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/home")
    public String home(Model model) {


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

    @RequestMapping("/index")
    public String index() {
        return "index";
    }


    /*
    public String redirectToHome(){
        return "redirect:/home";
    }*/
}
