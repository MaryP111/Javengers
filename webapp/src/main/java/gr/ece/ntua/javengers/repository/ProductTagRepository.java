package gr.ece.ntua.javengers.repository;

import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.ProductTag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductTagRepository extends CrudRepository<ProductTag, Long> {

    @Query("select product_tag.productId from ProductTag product_tag where tag like ?1")
    List<Long> getProductsByTag(String keyword);

    @Query("select product_tag.tag from ProductTag product_tag where product_id = ?1")
    List<String> getTagsByProductId(Long productId);

    @Query("select product_tag.id from ProductTag product_tag where product_id = ?1")
    List<Long> getIdsByProductId(Long productId);

}