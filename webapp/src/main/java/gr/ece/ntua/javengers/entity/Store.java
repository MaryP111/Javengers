package gr.ece.ntua.javengers.entity;

import javax.persistence.*;

@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String place;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "lat", nullable = false)
    private Double lat;

    @Column(name = "lng", nullable = false)
    private Double lng;

    @Column(name = "withdrawn", nullable = false)
    private Boolean withdrawn = false;

    public Store() {}

    public Long getId() {
        return id;
    }

    public String getPlace() {
        return place;
    }

    public String getAddress() { return address; }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public Boolean getWithdrawn() { return withdrawn; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setAddress(String address) { this.address = address; }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void setWithdrawn(Boolean withdrawn) { this.withdrawn = withdrawn;}
}
