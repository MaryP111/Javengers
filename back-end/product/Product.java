package gr.ntua.ece.webapp.product;

public class Product {

    private int id;
    private String name;
    private String company;
    private String category;
    private float stars;

    public Product(int id, String name, String company, String category, float stars) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.category = category;
        this.stars = stars;
    }

    public Product(String name, String company, String category, float stars) {
        this.name = name;
        this.company = company;
        this.category = category;
        this.stars = stars;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getCategory() {
        return category;
    }

    public float getStars() {
        return stars;
    }
}
