package gr.ece.ntua.javengers.entity.comparator;

import gr.ece.ntua.javengers.entity.Product;

import java.util.Comparator;

public class SortProductById implements Comparator<Product> {

    public int compare(Product a, Product b) {
        if (a.getId() > b.getId()) return 1;
        else return -1;
    }
}
