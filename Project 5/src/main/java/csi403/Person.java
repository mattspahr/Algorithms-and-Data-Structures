package csi403;

import java.util.ArrayList;

public class Person {
    String name;
    ArrayList<Person> friends = new ArrayList<>();

    public Person() {

    }

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, ArrayList<Person> friends) {
        this.name = name;
        this.friends = friends;
    }

    public void setFriends(ArrayList<Person> friends) {
        this.friends = friends;
    }

    public ArrayList<Person> getFriends() {
        return this.friends;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Person other = (Person) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
