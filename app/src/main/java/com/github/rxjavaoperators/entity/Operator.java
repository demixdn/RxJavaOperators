package com.github.rxjavaoperators.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Date: 20.02.2017
 * Time: 14:31
 *
 * @author Aleks Sander
 *         Project RxJavaOperators
 */
@SuppressWarnings("unused")
public class Operator implements MarkedItem, Comparable<Operator>, Serializable {
    @NonNull
    private Category category;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @SerializedName("official_link")
    @NonNull
    private String officialLink;
    @SerializedName("official_image_link")
    @Nullable
    private String officialImageLink;
    @SerializedName("marbles_link")
    @Nullable
    private String marblesLink;

    public Operator() {
        this.category = Category.create("Combining");
        this.name = "Merge";
        this.description = "Элементы Result Observable включают элементы из исходных Observable в том порядке, в котором они были выпущены в исходных Observable";
        this.officialLink = "http://reactivex.io/documentation/operators/merge.html";
        this.officialImageLink = "http://reactivex.io/documentation/operators/images/merge.C.png";
        this.marblesLink = null;
    }

    @NonNull
    public Category getCategory() {
        return category;
    }

    public void setCategory(@NonNull Category category) {
        this.category = category;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getOfficialLink() {
        return officialLink;
    }

    public void setOfficialLink(@NonNull String officialLink) {
        this.officialLink = officialLink;
    }

    @Nullable
    public String getOfficialImageLink() {
        return officialImageLink;
    }

    public void setOfficialImageLink(@Nullable String officialImageLink) {
        this.officialImageLink = officialImageLink;
    }

    @Nullable
    public String getMarblesLink() {
        return marblesLink;
    }

    public void setMarblesLink(@Nullable String marblesLink) {
        this.marblesLink = marblesLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Operator operator = (Operator) o;

        if (!category.equals(operator.category)) return false;
        return name.equals(operator.name);

    }

    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "category=" + category +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Operator o) {
        return name.compareToIgnoreCase(o.getName());
    }
}
