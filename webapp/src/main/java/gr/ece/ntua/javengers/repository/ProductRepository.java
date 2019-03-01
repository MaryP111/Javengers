package gr.ece.ntua.javengers.repository;

import gr.ece.ntua.javengers.entity.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("select product from Product product where barcode = ?1")
    Optional<Product> getProductByBarcode(String barcode);

    @Modifying
    @Transactional
    @Query("delete from Product product where id = ?1")
    void deleteProductById(Long id);
}
