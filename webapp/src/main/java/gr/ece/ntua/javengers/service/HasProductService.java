package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.HasProduct;

import java.util.List;

public interface HasProductService {

    void saveEntry(HasProduct entry);

    List<HasProduct> getEntriesById(Long productId);

}
