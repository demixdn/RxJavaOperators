package com.github.rxjavaoperators.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Date: 20.02.2017
 * Time: 14:35
 *
 * @author Aleks Sander
 *         Project RxJavaOperators
 */

public class Category implements MarkedItem, Comparable<Category>, Serializable {

    private final String name;

    private Category(String name) {
        this.name = name;
    }

    public static Category create(String name) {
        return new Category(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return name != null ? name.equals(category.name) : category.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int compareTo(@NonNull Category o) {
        return name.compareToIgnoreCase(o.getName());
    }

}
