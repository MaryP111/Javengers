package gr.ece.ntua.javengers.entity.comparator;

import gr.ece.ntua.javengers.entity.Store;

import java.util.Comparator;

public class SortStoreById implements Comparator<Store> {

    public int compare(Store a, Store b) {
        if (a.getId() > b.getId()) return 1;
        else return -1;
    }
}
