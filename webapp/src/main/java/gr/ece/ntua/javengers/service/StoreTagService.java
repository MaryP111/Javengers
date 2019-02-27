package gr.ece.ntua.javengers.service;

import java.util.List;

public interface StoreTagService {

    List<String> getTagsByStoreId(Long id);
}
