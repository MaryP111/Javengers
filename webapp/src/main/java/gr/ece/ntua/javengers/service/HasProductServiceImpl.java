package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.HasProduct;
import gr.ece.ntua.javengers.repository.HasProductRepository;
//import gr.ntua.ece.javengers.client.model.Entry;
import gr.ece.ntua.javengers.repository.StoreRepository;
import gr.ntua.ece.javengers.client.model.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HasProductServiceImpl implements HasProductService {

    private HasProductRepository hasProductRepository;
    private StoreRepository storeRepository;

    @Autowired
    public HasProductServiceImpl(HasProductRepository hasProductRepository, StoreRepository storeRepository) {

        this.hasProductRepository = hasProductRepository;
        this.storeRepository=storeRepository;

    }

    @Override
    public Optional<HasProduct> getEntryById(Long id) {
        return hasProductRepository.findById(id);
    }

    @Override
    public void updateEntry(HasProduct entry) {

        entry.setWithdrawn(true);
        hasProductRepository.save(entry);
    }

    @Override
    public void saveEntry(HasProduct entry) {


        if (entry.getDateFrom() == null) {

            try {

                entry.setWithdrawn(false);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dateString = format.format(new Date());
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                Date dateFrom = format.parse(dateString);
                java.sql.Date sqlDate = new java.sql.Date(dateFrom.getTime());
                entry.setDateFrom(sqlDate);
            }
            catch (Exception exc) {
                exc.printStackTrace();
            }

        }

        hasProductRepository.save(entry);
    }

    @Override
    public Entry saveEntry(Entry entry) throws Exception{
        HasProduct hasProduct = new HasProduct();
        java.util.Date tempDate;


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tempDate = simpleDateFormat.parse(entry.getDateFrom());
        java.sql.Date dateFrom = new java.sql.Date(tempDate.getTime());

        tempDate = simpleDateFormat.parse(entry.getDateTo());
        java.sql.Date dateTo = new java.sql.Date(tempDate.getTime());


        // hasProduct.setUserId(1L);
        hasProduct.setProductId(entry.getProductId());
        hasProduct.setStoreId(entry.getShopId());
        hasProduct.setPrice(entry.getPrice());
        hasProduct.setDateFrom(dateFrom);
        hasProduct.setDateTo(dateTo);

        Long entryId = hasProductRepository.save(hasProduct).getId();

        entry.setId(entryId.toString());

        return entry;

    }

    @Override
    public List<HasProduct> getActiveEntriesById(Long productId) {

        return hasProductRepository.getActiveEntriesById(productId);
    }


    @Override
    public List<HasProduct> filterEntries(Long productId,double priceFrom,double priceTo,double clientLat,double clientLong,Integer distance) {


        Iterable<HasProduct> entries = hasProductRepository.getActiveEntriesById(productId);

        List<HasProduct> filteredEntries = new ArrayList<>();

        Iterator<HasProduct> entryIterator = entries.iterator();

        while (entryIterator.hasNext()) {
            HasProduct entry = entryIterator.next();
            entry.address = storeRepository.findById(entry.getStoreId()).get().getAddress();
            Double storeLat = storeRepository.findById(entry.getStoreId()).get().getLat();
            entry.lat=storeLat;
            Double storeLong = storeRepository.findById(entry.getStoreId()).get().getLng();
            entry.lng=storeLong;
            entry.storeName=storeRepository.findById(entry.getStoreId()).get().getPlace();
            Double distanceFromEntry = distance(storeLat, storeLong, clientLat, clientLong);

            if (distanceFromEntry <= distance && entry.getPrice() >= priceFrom && entry.getPrice() <= priceTo) filteredEntries.add(entry);

        }
        return filteredEntries;

    }

    public static Double distance(Double lat1, Double lng1, Double lat2, Double lng2) {

        int R = 6378137;   /* Earth's mean radius in meter */
        double dLat = rad(lat1 -lat2);
        double dLong = rad(lng1-lng2);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(rad(lat1)) * Math.cos(rad(lat2)) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        return R*c;
    }

    @Override
    public List<HasProduct> getAllEntries() {

        List<HasProduct> entries = new ArrayList<>();
        hasProductRepository.findAll().forEach(entries::add);
        return entries;
    }

    public static Double rad(Double deg) {
        return deg*Math.PI/180;
    }


    @Override
    public void deleteEntryById(Long id) {

        hasProductRepository.deleteById(id);

    }

}
