package org.lucky.demo.modles;

import java.io.Serializable;

public class PersonLocation implements Serializable {

    private Person person;
    private Location location;

    public PersonLocation() {
    }

    public PersonLocation(Person person, Location location) {
        this.person = person;
        this.location = location;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "PersonLocation{" +
                "person=" + person +
                ", location=" + location +
                '}';
    }
}
