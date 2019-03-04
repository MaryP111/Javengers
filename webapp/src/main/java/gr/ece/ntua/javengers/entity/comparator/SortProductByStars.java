package gr.ece.ntua.javengers.entity.comparator;

import gr.ece.ntua.javengers.entity.Product;

import java.util.Comparator;

public class SortProductByStars implements Comparator<Product> {

    public int compare(Product a, Product b) {

        if (a.getStars() == null && b.getStars() == null) {
            if (a.getId() > b.getId()) return 1;
            else return -1;
        }

        if (a.getStars() == null) return 1;
        if (b.getStars() == null) return -1;
        if (Double.compare(a.getStars(), b.getStars()) == 0) {
            if (a.getId() > b.getId()) return 1;
            else return -1;
        }
        else if (a.getStars() < b.getStars()) return 1;
        else return -1;
    }
}
