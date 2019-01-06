package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.Product;

import java.util.List;

public interface ProductTagService {

    List<Product> getProductsByTag(String keyword);

    void insertTags(String barcode, String productTags);

}
