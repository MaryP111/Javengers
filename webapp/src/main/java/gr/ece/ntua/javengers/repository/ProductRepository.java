package gr.ece.ntua.javengers.repository;

import gr.ece.ntua.javengers.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("select product from Product product where barcode = ?1")
    Optional<Product> getProductByBarcode(String barcode);

    @Query("select product from Product product where category =?1")
    List<Product> getProductsByCategory(String category);

    @Query("select product.id from Product product where name like ?1")
    List<Long> getProductIdsByName(String name);

    @Query("select product.id from Product product where description like ?1")
    List<Long> getProductIdsByDescription(String description);

    @Query("select product.id from Product product where category like ?1")
    List<Long> getProductIdsByCategory(String category);

    @Query("select product.id from Product product where manufacturer like ?1")
    List<Long> getProductIdsByManufacturer(String manufacturer);

}
