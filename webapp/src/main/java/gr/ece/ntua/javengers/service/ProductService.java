package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.Product;
import org.apache.http.nio.conn.NoopIOSessionStrategy;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> listAll();

    Optional<Product> getProductById(Long id);

    void saveProduct(Product product);

    Optional<Product> getProductByBarcode(String barcode);



}
