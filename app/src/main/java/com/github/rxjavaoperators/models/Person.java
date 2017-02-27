package com.github.rxjavaoperators.models;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 20.02.2017
 * Time: 14:50
 *
 * @author Aleks Sander
 *         Project RxJavaOperators
 */

public class Person implements Serializable {
    private String name;
    private List<Dog> dogs;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs;
    }

    @Override
    public String toString() {
        return "Person{'" + name + "\', dogs=" + dogs + '}';
    }
}
