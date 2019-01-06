package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.entity.HasProduct;
import gr.ece.ntua.javengers.repository.HasProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

        hasProductRepository.save(entry);
    }

    @Override
    public List<HasProduct> getEntriesById(Long productId) {

        return hasProductRepository.getEntriesById(productId);
    }


}
