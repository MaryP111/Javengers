package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.HasProduct;
import gr.ece.ntua.javengers.entity.Store;
import gr.ntua.ece.javengers.client.model.Entry;
//import gr.ece.ntua.javengers.client.model.Entry;

import java.util.List;
import java.util.Optional;

public interface HasProductService {

    void saveEntry(HasProduct entry);

    List<HasProduct> getActiveEntriesById(Long productId);

    Entry saveEntry(Entry entry) throws Exception;

    List<HasProduct> getAllEntries();

    void deleteEntryById(Long id);

    List<HasProduct> filterEntries(Long productId,double priceFrom,double priceTo,double clientLat,double clientLong,Integer distance);

    Optional<HasProduct> getEntryById(Long id);

    void updateEntry(HasProduct entry);
}
