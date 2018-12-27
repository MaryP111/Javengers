package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.Store;

import java.util.List;
import java.util.Optional;

public interface StoreService {

    List<Store> listAll();

    Optional<Store> getStoreByLocation(Double lat, Double lng);

    void saveStore(Store store);

}
