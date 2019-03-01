package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.ProductTag;
import gr.ece.ntua.javengers.repository.ProductRepository;
import gr.ece.ntua.javengers.repository.ProductTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Service
public class ProductTagServiceImpl implements ProductTagService {


    private ProductRepository productRepository;
    private ProductTagRepository productTagRepository;

    @Autowired
    public ProductTagServiceImpl(ProductRepository productRepository, ProductTagRepository productTagRepository) {

        this.productRepository = productRepository;
        this.productTagRepository = productTagRepository;
    }


    public List<Product> getProductsByTag(String keyword) {

        List<Long> productIds = productTagRepository.getProductsByTag("%" + keyword + "%");

        List<Product> products = new ArrayList<>();


        for (ListIterator<Long> iterator = productIds.listIterator(); iterator.hasNext(); ) {

            Long id = iterator.next();

            products.add(productRepository.findById(id).get());

        }

        return products;

    }


    @Override
    public void insertTags(String barcode, String productTags) {

        if (productTags == null) return;

        Long productId = productRepository.getProductByBarcode(barcode).get().getId();

        String[] tags = productTags.split("#");

        for (int i =1; i<tags.length; i++) {

            String tag = tags[i].replaceAll(" ", "").replaceAll(",", "");

            ProductTag productTag = new ProductTag();

            productTag.setProductId(productId);
            productTag.setTag(tag);

            productTagRepository.save(productTag);

        }

    }

    @Override
    public List<String> getTagsByProductId(Long productId) {
        return productTagRepository.getTagsByProductId(productId);
    }
}
