package gr.ece.ntua.javengers.controller;

import gr.ece.ntua.javengers.entity.User;
import gr.ece.ntua.javengers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {

        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Collection<User> listUsers(){

        return userService.listAll();
    }

    @RequestMapping(value = "/add", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String addUser (@RequestBody User user) {

        userService.addUser(user);
        return "Added a new user";

    }


}
