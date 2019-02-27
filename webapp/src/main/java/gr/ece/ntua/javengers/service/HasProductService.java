package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.HasProduct;
import gr.ntua.ece.javengers.client.model.Entry;

import java.util.List;

public interface HasProductService {

    void saveEntry(HasProduct entry);

    List<HasProduct> getEntriesById(Long productId);

    Entry saveEntry(Entry entry);

    List<HasProduct> getAllEntries();

}
