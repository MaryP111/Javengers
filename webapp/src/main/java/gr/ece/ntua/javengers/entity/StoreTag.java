package gr.ece.ntua.javengers.entity;

import javax.persistence.*;

@Entity
@Table(name = "store_tags")
public class StoreTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "tag", nullable = false)
    private String tag;

    public StoreTag() {}

    public Long getId() {
        return id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public String getTag() {
        return tag;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

