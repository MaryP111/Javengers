package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.Store;
import gr.ntua.ece.javengers.client.model.Shop;

import java.util.List;
import java.util.Optional;

public interface StoreService {

    List<Store> listAll();

    Optional<Store> getStoreByLocation(Double lat, Double lng);

    Optional<Store> getStoreById(Long id);

    Shop saveShop(Shop shop);

    gr.ntua.ece.javengers.client.model.Shop getStoreAndTagsById(Long id);

    Long saveStore(Store store);

    void updateStore(Shop shop);

    void deleteStoreById(Long id);

}
