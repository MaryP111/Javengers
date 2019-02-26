package gr.ece.ntua.javengers.entity.comparator;

import gr.ece.ntua.javengers.entity.Store;

import java.util.Comparator;

public class SortStoreByName implements Comparator<Store> {

    public int compare(Store a, Store b) {
        return a.getPlace().compareToIgnoreCase(b.getPlace());
    }
}
