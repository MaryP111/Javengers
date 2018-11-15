package productsearch;

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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    @Override
    public String toString() {
        return String
                .format("Product [id=%s, name=%s, company=%s, category=%s, stars=%s]",
                        id, name, company, category, stars);
    }
}
