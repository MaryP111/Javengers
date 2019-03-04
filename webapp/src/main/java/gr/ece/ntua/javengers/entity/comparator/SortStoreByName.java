package gr.ece.ntua.javengers.entity.comparator;

import gr.ece.ntua.javengers.entity.Store;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class SortStoreByName implements Comparator<Store> {

    public int compare(Store store1, Store store2) {


        Locale greekLocale = new Locale("el_GR");
        Collator greekCollator = Collator.getInstance(greekLocale);

        return greekCollator.compare(store1.getPlace(), store2.getPlace());

    }
}
