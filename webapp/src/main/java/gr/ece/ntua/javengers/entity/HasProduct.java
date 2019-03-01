package gr.ece.ntua.javengers.entity;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "has_product")
public class HasProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(name="product_id", nullable = false)
    private Long productId;

    @Column(name="store_id", nullable = false)
    private Long storeId;

    @Column(name = "price", nullable = false)
    private Double price;

    @Basic
    @Column(name = "date_from", nullable = false)
    private Date dateFrom;

    @Basic
    @Column(name = "date_to", nullable = true)
    private Date dateTo;

    @Column(name = "withdrawn", nullable = false)
    private Boolean withdrawn = false;

    public HasProduct() {}

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public Double getPrice() {
        return price;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public Boolean getWithdrawn() {
        return withdrawn;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;

    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void setWithdrawn(Boolean withdrawn) {
        this.withdrawn = withdrawn;
    }
}
