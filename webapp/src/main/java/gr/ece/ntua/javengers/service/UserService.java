package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> listAll();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByUserName(String userName);

    Boolean checkUserNameExists(String userName);

    Boolean checkEmailExists(String email);

    Boolean checkPhoneNumberExists(Long phoneNumber);

    void createUser(User user);

    void saveUser(User user);

}
