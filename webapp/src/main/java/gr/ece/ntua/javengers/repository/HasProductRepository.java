package gr.ece.ntua.javengers.repository;

import gr.ece.ntua.javengers.entity.HasProduct;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface HasProductRepository extends CrudRepository<HasProduct, Long> {

    @Query("select hasProduct from HasProduct hasProduct where productId = ?1 and withdrawn=false")
    List<HasProduct> getActiveEntriesById(Long productId);


    @Modifying
    @Transactional
    @Query("delete from HasProduct entry where id = ?1")
    void deleteEntryById(Long id);
}
