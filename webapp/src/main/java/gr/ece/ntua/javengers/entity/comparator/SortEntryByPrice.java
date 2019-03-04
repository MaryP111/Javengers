package gr.ece.ntua.javengers.entity.comparator;

import gr.ece.ntua.javengers.entity.HasProduct;

import java.util.Comparator;

public class SortEntryByPrice implements Comparator<HasProduct> {

    public int compare(HasProduct entry1, HasProduct entry2) {
        if (entry1.getPrice() > entry2.getPrice()) return 1;
        else return -1;
    }
}
