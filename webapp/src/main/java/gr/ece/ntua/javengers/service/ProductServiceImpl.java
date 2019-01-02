package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    @Override
    public List<Product> listAll() {
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }


    @Override
    public void saveProduct(Product product) {

        product.setStars(4.0);
        product.setNumberOfRatings(1);
        productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductByBarcode(String barcode) {

        return productRepository.getProductByBarcode(barcode);
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        List<Product> products = productRepository.getProductByCategory(category);

        return products;

    }
}

