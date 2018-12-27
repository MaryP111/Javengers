package gr.ece.ntua.javengers.entity.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role{
    @Id
    private int id;

    @Column(length = 40, nullable = false)
    private String name;

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(){}

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

