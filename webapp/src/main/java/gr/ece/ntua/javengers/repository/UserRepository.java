package gr.ece.ntua.javengers.repository;

import gr.ece.ntua.javengers.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User,Long> {

    @Query("select user from User user where userName = ?1")
    Optional<User> getUserByUserName(String userName);

    @Query("select user from User user where email = ?1")
    Optional<User> getUserByEmail(String email);

    @Query("select user from User user where phoneNumber = ?1")
    Optional<User> getUserByPhoneNumber(Long phoneNumber);


    /*
    @Modifying
    @Query("update User set firstName = ?1, lastName = ?2, age = ?3, university = ?4, profession = ?5, city = ?6, website = ?7 where userName = ?8")
    void updateUser(String firstName, String lastName, Integer age, String university, String profession, String city, String website, String userName);

    */

}
