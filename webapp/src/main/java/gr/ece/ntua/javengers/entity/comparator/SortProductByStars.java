package gr.ece.ntua.javengers.entity.comparator;

import gr.ece.ntua.javengers.entity.Product;

import java.util.Comparator;

public class SortProductByStars implements Comparator<Product> {

    public int compare(Product a, Product b) {
        if (a.getStars() < b.getStars()) return 1;
        else return -1;
    }
}
