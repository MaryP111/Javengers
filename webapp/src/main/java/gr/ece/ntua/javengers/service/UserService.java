package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.User;

import java.util.List;

public interface UserService {

    List<User> listAll();

    void addUser(User user);

}
