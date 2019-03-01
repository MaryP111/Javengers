package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.entity.ProductTag;
import gr.ece.ntua.javengers.entity.Store;
import gr.ece.ntua.javengers.entity.StoreTag;
import gr.ece.ntua.javengers.repository.ProductRepository;
import gr.ece.ntua.javengers.repository.StoreRepository;
import gr.ece.ntua.javengers.repository.StoreTagRepository;
import gr.ntua.ece.javengers.client.model.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {

    private StoreRepository storeRepository;

    private StoreTagRepository storeTagRepository;

        @Autowired
        public StoreServiceImpl(StoreRepository storeRepository, StoreTagRepository storeTagRepository) {

            this.storeRepository = storeRepository;
            this.storeTagRepository = storeTagRepository;
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
        public Long saveStore(Store store) {

            Optional<Store> tempStore = getStoreByLocation(store.getLat(), store.getLng());
            if (!tempStore.isPresent()) {
                store.setWithdrawn(false);
                return storeRepository.save(store).getId();
            }
            return 0L;
        }

        @Override
        public Optional<Store> getStoreById(Long id) {
            return storeRepository.findById(id);
        }

        @Override
        public gr.ntua.ece.javengers.client.model.Shop getStoreAndTagsById(Long id) {

            Store tempStore= getStoreById(id).get();

            List<String> storeTags = storeTagRepository.getTagsByStoreId(id);

            gr.ntua.ece.javengers.client.model.Shop shop = new gr.ntua.ece.javengers.client.model.Shop();

            shop.setId(id.toString());
            shop.setName(tempStore.getPlace());
            shop.setAddress(tempStore.getAddress());
            shop.setLat(tempStore.getLat());
            shop.setTags(storeTags);
            shop.setWithdrawn(tempStore.getWithdrawn());

            return shop;
        }

    @Override
    public Shop saveShop(Shop tempShop) {

        Store store= new Store();

        store.setPlace(tempShop.getName());
        store.setAddress(tempShop.getAddress());
        store.setLat(tempShop.getLat());
        store.setLng(tempShop.getLng());
        store.setWithdrawn(tempShop.getWithdrawn());

        Long storeId = storeRepository.save(store).getId();

        tempShop.setId(storeId.toString());

        Iterator<String> stringIterator = tempShop.getTags().listIterator();

        while (stringIterator.hasNext()) {

            StoreTag storeTag = new StoreTag();

            storeTag.setStoreId(storeId);
            storeTag.setTag(stringIterator.next());

            storeTagRepository.save(storeTag);
        }

        return tempShop;
    }

    @Override
    public void updateStore(Shop newShop) {

        Store store = new Store();

        store.setPlace(newShop.getName());
        store.setAddress(newShop.getAddress());
        store.setLat(newShop.getLat());
        store.setLng(newShop.getLng());
        store.setWithdrawn(newShop.getWithdrawn());
        store.setId(Long.parseLong(newShop.getId()));

        Long storeId = storeRepository.save(store).getId();

        List<Long> ids = storeTagRepository.getIdsByStoreId(storeId);

        Iterator<Long> longIterator = ids.iterator();

        while (longIterator.hasNext()) {
            storeTagRepository.deleteById(longIterator.next());
        }

        Iterator<String> stringIterator = newShop.getTags().listIterator();

        while (stringIterator.hasNext()) {

            StoreTag storeTag = new StoreTag();

            storeTag.setStoreId(storeId);
            storeTag.setTag(stringIterator.next());

            storeTagRepository.save(storeTag);
        }


    }


    @Override
    public void deleteStoreById(Long id) {
            storeRepository.deleteById(id);
    }

}
