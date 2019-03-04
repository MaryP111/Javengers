package gr.ece.ntua.javengers.repository;

import gr.ece.ntua.javengers.entity.StoreTag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StoreTagRepository extends CrudRepository<StoreTag, Long> {

    @Query("select store_tag.tag from StoreTag store_tag where store_id = ?1")
    List<String> getTagsByStoreId(Long storeId);

    @Query("select store_tag.id from StoreTag store_tag where store_id = ?1")
    List<Long> getIdsByStoreId(Long storeId);
}
