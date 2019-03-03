package gr.ece.ntua.javengers.repository;

import gr.ece.ntua.javengers.entity.HasProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface HasProductRepository extends CrudRepository<HasProduct, Long> {

    @Query("select hasProduct from HasProduct hasProduct where productId = ?1")
    List<HasProduct> getEntriesById(Long productId);

}
