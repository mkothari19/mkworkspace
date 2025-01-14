package org.lucky.demo.modles;

import java.io.Serializable;

public class Location implements Serializable {

    private Integer id;
    private String loc;

    public Location() {
    }

    public Location(Integer id, String loc) {
        this.id = id;
        this.loc = loc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", loc='" + loc + '\'' +
                '}';
    }
}
