package gr.ece.ntua.javengers.repository;

import gr.ece.ntua.javengers.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User,Long> {

    @Query("select user from User user where userName = ?1")
    Optional<User> getUserByUserName(String userName);

    @Query("select user from User user where email = ?1")
    Optional<User> getUserByEmail(String email);

    @Query("select user from User user where phoneNumber = ?1")
    Optional<User> getUserByPhoneNumber(Long phoneNumber);

    @Modifying
    @Transactional
    @Query("delete from User user where id = ?1")
    void deleteUserById(Long id);

}
