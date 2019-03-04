package gr.ece.ntua.javengers.entity;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "barcode", nullable = true)
    private String barcode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "manufacturer", nullable = true)
    private String manufacturer;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name="stars", nullable = true)
    private Double stars;

    @Column(name="number_of_ratings", nullable = true)
    private Integer numberOfRatings;

    @Column(name="image_url", nullable = true)
    private String imageURL;

    @Column(name="withdrawn", nullable = false)
    private Boolean withdrawn = false;

    public Product() {}

    public Long getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public Double getStars() {
        return stars;
    }

    public Integer getNumberOfRatings() {
        return numberOfRatings;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Boolean getWithdrawn() { return withdrawn; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStars(Double stars) {
        this.stars = stars;
    }

    public void setNumberOfRatings(Integer numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setWithdrawn(Boolean withdrawn) { this.withdrawn = withdrawn; }

    public int compareTo(Product p) {
        return Double.compare(this.stars, p.getStars());
    }
}
