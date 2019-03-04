package gr.ece.ntua.javengers.entity.comparator;

import gr.ece.ntua.javengers.entity.Product;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class SortProductByName implements Comparator<Product> {

    public int compare(Product product1, Product product2) {

        Locale greekLocale = new Locale("el_GR");
        Collator greekCollator = Collator.getInstance(greekLocale);

        return greekCollator.compare(product1.getName(), product2.getName());

    }
}
