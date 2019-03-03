package gr.ece.ntua.javengers.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gr.ece.ntua.javengers.entity.security.Authority;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@JsonIgnoreProperties(value = { "email", "password", "role"})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private Long phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "age", nullable = true)
    private Integer age;

    @Column(name = "university", nullable = true)
    private String university;

    @Column(name = "profession", nullable = true)
    private String profession;

    @Column(name = "city", nullable = true)
    private String city;

    @Column(name = "website", nullable = true)
    private String website;

    public User() {}

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public Integer getAge() {
        return age;
    }

    public String getUniversity() {
        return university;
    }

    public String getProfession() {
        return profession;
    }

    public String getCity() {
        return city;
    }

    public String getWebsite() {
        return website;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new Authority("user"));
        return authorities;
    }

    public String getPassword() {

        return password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
