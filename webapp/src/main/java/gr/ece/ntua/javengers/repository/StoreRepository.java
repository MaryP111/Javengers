package gr.ece.ntua.javengers.repository;

import gr.ece.ntua.javengers.entity.Store;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface StoreRepository extends CrudRepository<Store, Long> {

    @Query("select store from Store store where abs(lat -?1) < 0.000001 and abs(lng-?2) < 0.000001")
    Optional<Store> getStoreByLocation(Double lat, Double lng);

    @Modifying
    @Transactional
    @Query("delete from Store store where id = ?1")
    void deleteById(Long id);
}
