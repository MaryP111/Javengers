package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.Product;
import org.apache.http.nio.conn.NoopIOSessionStrategy;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> listAll();

    Optional<Product> getProductById(Long id);

    gr.ntua.ece.javengers.client.model.Product getProductAndTagsById(Long id);

    Long saveProduct(Product product);

    void updateProduct(gr.ntua.ece.javengers.client.model.Product newProduct);

    gr.ntua.ece.javengers.client.model.Product saveProduct(gr.ntua.ece.javengers.client.model.Product tempProduct);

    Optional<Product> getProductByBarcode(String barcode);

    void updateStars(Product product, Double stars);

    void deleteProductById(Long id);



}
