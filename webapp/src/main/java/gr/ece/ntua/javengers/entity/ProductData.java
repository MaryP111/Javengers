package gr.ece.ntua.javengers.entity;


import javax.persistence.*;

@Entity
@Table(name = "product_data")
public class ProductData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "data", nullable = false)
    private String data;

    public ProductData() {}

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getData() {
        return data;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setData(String data) {
        this.data = data;
    }
}
