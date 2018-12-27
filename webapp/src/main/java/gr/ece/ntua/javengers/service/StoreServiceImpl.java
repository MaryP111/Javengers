package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.Store;
import gr.ece.ntua.javengers.repository.ProductRepository;
import gr.ece.ntua.javengers.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {

    private StoreRepository storeRepository;

        @Autowired
        public StoreServiceImpl(StoreRepository storeRepository) {

            this.storeRepository = storeRepository;
        }

        @Override
        public List<Store> listAll() {
            List<Store> stores = new ArrayList<>();
            storeRepository.findAll().forEach(stores::add);
            return stores;
        }

        @Override
        public Optional<Store> getStoreByLocation(Double lat, Double lng) {

            return storeRepository.getStoreByLocation(lat, lng);
        }


        @Override
        public void saveStore(Store store) {

            Optional<Store> tempStore = getStoreByLocation(store.getLat(), store.getLng());
            if (!tempStore.isPresent())
                storeRepository.save(store);
        }

}
