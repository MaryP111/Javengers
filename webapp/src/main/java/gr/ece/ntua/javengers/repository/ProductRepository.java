package gr.ece.ntua.javengers.repository;

import gr.ece.ntua.javengers.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("select product from Product product where barcode = ?1")
    Optional<Product> getProductByBarcode(String barcode);

}