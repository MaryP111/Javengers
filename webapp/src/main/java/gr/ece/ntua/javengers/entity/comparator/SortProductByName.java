package gr.ece.ntua.javengers.entity.comparator;

import gr.ece.ntua.javengers.entity.Product;

import java.util.Comparator;

public class SortProductByName implements Comparator<Product> {

    public int compare(Product a, Product b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
}
