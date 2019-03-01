package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.HasProduct;
import gr.ece.ntua.javengers.entity.Product;
import gr.ece.ntua.javengers.repository.HasProductRepository;
import gr.ntua.ece.javengers.client.model.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class HasProductServiceImpl implements HasProductService {

    private HasProductRepository hasProductRepository;

    @Autowired
    public HasProductServiceImpl(HasProductRepository hasProductRepository) {

        this.hasProductRepository = hasProductRepository;

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
    public Entry saveEntry(Entry entry) {

        HasProduct hasProduct = new HasProduct();


        // hasProduct.setUserId(1L);
        hasProduct.setProductId(entry.getProductId());
        hasProduct.setStoreId(entry.getShopId());
        hasProduct.setPrice(entry.getPrice());
        hasProduct.setDateFrom(entry.getDateFrom());
        hasProduct.setDateTo(entry.getDateTo());

        Long entryId = hasProductRepository.save(hasProduct).getId();

        entry.setId(entryId.toString());

        return entry;

    }

    @Override
    public List<HasProduct> getEntriesById(Long productId) {

        return hasProductRepository.getEntriesById(productId);
    }

    @Override
    public List<HasProduct> getAllEntries() {

        List<HasProduct> entries = new ArrayList<>();
        hasProductRepository.findAll().forEach(entries::add);
        return entries;
    }


}
