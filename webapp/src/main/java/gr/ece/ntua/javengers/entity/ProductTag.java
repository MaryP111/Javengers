package gr.ece.ntua.javengers.entity;


import javax.persistence.*;

@Entity
@Table(name = "product_tags")
public class ProductTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "tag", nullable = false)
    private String tag;

    public ProductTag() {}

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getTag() {
        return tag;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
