package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.User;
import gr.ece.ntua.javengers.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> listAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public Optional<User> getUserById(Long id) {

        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUserName(String userName) {

        return userRepository.getUserByUserName(userName);
    }

    @Override
    public Boolean checkUserNameExists(String userName) {

        Optional<User> tempUser = userRepository.getUserByUserName(userName);
        if (tempUser.isPresent()) return true;
        return false;
    }

    @Override
    public Boolean checkEmailExists(String email) {

        Optional<User> tempUser = userRepository.getUserByEmail(email);
        if (tempUser.isPresent()) return true;
        return false;
    }

    @Override
    public Boolean checkPhoneNumberExists(Long phoneNumber) {

        Optional<User> tempUser = userRepository.getUserByPhoneNumber(phoneNumber);
        if (tempUser.isPresent()) return true;
        return false;
    }


    @Override
    public void saveUser(User user) {

        userRepository.save(user);
    }


    @Override
    public void createUser(User user) {

        String password = user.getPassword();

        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        userRepository.save(user);
    }

}
