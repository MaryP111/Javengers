package gr.ece.ntua.javengers.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.ProductTag;
import gr.ece.ntua.javengers.repository.ProductRepository;
import gr.ece.ntua.javengers.repository.ProductTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private ProductTagRepository productTagRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductTagRepository productTagRepository) {

        this.productRepository = productRepository;
        this.productTagRepository = productTagRepository;
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
    public gr.ntua.ece.javengers.client.model.Product getProductAndTagsById(Long id) {

        Product tempProduct = getProductById(id).get();

        List<String> productTags = productTagRepository.getTagsByProductId(id);

        gr.ntua.ece.javengers.client.model.Product product = new gr.ntua.ece.javengers.client.model.Product();

        product.setId(id.toString());
        product.setName(tempProduct.getName());
        product.setDescription(tempProduct.getDescription());
        product.setCategory(tempProduct.getCategory());
        product.setTags(productTags);
        product.setWithdrawn(tempProduct.getWithdrawn());

        return product;
    }

    @Override
    public Long saveProduct(Product product) {


        product.setNumberOfRatings(0);

        return productRepository.save(product).getId();

    }

    @Override
    public gr.ntua.ece.javengers.client.model.Product saveProduct(gr.ntua.ece.javengers.client.model.Product tempProduct) {

        Boolean withdrawn = tempProduct.getWithdrawn();

        if (withdrawn == null) withdrawn = false;

        tempProduct.setWithdrawn(withdrawn);

        tempProduct.setWithdrawn(tempProduct.getWithdrawn());


        Product product = new Product();

        product.setName(tempProduct.getName());
        product.setDescription(tempProduct.getDescription());
        product.setCategory(tempProduct.getCategory());
        product.setWithdrawn(tempProduct.getWithdrawn());

        Long productId = saveProduct(product);

        tempProduct.setId(productId.toString());

        Iterator<String> stringIterator = tempProduct.getTags().listIterator();

        while (stringIterator.hasNext()) {

            ProductTag productTag = new ProductTag();

            productTag.setProductId(productId);
            productTag.setTag(stringIterator.next());

            productTagRepository.save(productTag);
        }

        return tempProduct;
    }

    @Override
    public void updateProduct(gr.ntua.ece.javengers.client.model.Product newProduct) {


        Boolean withdrawn = newProduct.getWithdrawn();

        if (withdrawn == null) withdrawn = false;

        newProduct.setWithdrawn(withdrawn);

        Product product = new Product();

        product.setName(newProduct.getName());
        product.setDescription(newProduct.getDescription());
        product.setCategory(newProduct.getCategory());
        product.setWithdrawn(newProduct.getWithdrawn());
        product.setId(Long.parseLong(newProduct.getId()));

        Long productId = saveProduct(product);

        List<Long> ids = productTagRepository.getIdsByProductId(productId);

        Iterator<Long> longIterator = ids.iterator();

        while (longIterator.hasNext()) {
            productTagRepository.deleteById(longIterator.next());
        }

        Iterator<String> stringIterator = newProduct.getTags().listIterator();

        while (stringIterator.hasNext()) {

            ProductTag productTag = new ProductTag();

            productTag.setProductId(productId);
            productTag.setTag(stringIterator.next());

            productTagRepository.save(productTag);
        }


    }

    @Override
    public Optional<Product> getProductByBarcode(String barcode) {

        return productRepository.getProductByBarcode(barcode);
    }

    @Override
    public void updateStars(Product product, Double stars) {

        if (stars == null) return;

        stars = 20*stars;

        product = productRepository.getProductByBarcode(product.getBarcode()).get();

        if (product.getNumberOfRatings() == null || product.getNumberOfRatings() == 0) {
            product.setNumberOfRatings(1);
            product.setStars(stars);
        }
        else {
            Integer numberOfRatings = product.getNumberOfRatings();
            Double newStars = (product.getStars()*numberOfRatings+stars)/(numberOfRatings+1);
            newStars = 10*newStars;
            newStars = newStars.intValue()/10.0;
            product.setStars(newStars);
            product.setNumberOfRatings(numberOfRatings+1);
        }

        productRepository.save(product);

    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);

    }
}

