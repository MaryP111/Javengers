package gr.ece.ntua.javengers.repository;

import gr.ece.ntua.javengers.entity.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User,Long> {


}
