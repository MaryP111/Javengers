package gr.ece.ntua.javengers.service;

import gr.ece.ntua.javengers.repository.ProductRepository;
import gr.ece.ntua.javengers.repository.ProductTagRepository;
import gr.ece.ntua.javengers.repository.StoreTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreTagServiceImpl implements StoreTagService {

    private StoreTagRepository storeTagRepository;

    @Autowired
    public StoreTagServiceImpl(StoreTagRepository storeTagRepository) {

        this.storeTagRepository = storeTagRepository;
    }

    @Override
    public List<String> getTagsByStoreId(Long id) {
        return storeTagRepository.getTagsByStoreId(id);
    }
}
