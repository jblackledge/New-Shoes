package com.example.newshoes;

import java.io.Serializable;
import java.util.ArrayList;

public class Shoe implements Serializable, Comparable<Shoe> {
    private String shoeName;

    private Double desiredDistanceInMiles;

    private Double mileCount;

    public Shoe(String shoeName, Double distance) {
        this.shoeName = shoeName;
        this.desiredDistanceInMiles = distance;
        mileCount = 0.00;
    }

    public String getName() {
        return shoeName;
    }

    public Double getDesiredDistanceInMiles() {
        return desiredDistanceInMiles;
    }

    public Double getMileCount() {
        return mileCount;
    }

    public void setMileCount(Double mileCount) {
        this.mileCount += mileCount;
    }

    public String toString() {
        return String.format("%s - Current Mile Count: %.2f", this.shoeName, this.getMileCount());
    }

    @Override
    public int compareTo(Shoe o) {
        return this.shoeName.compareTo(o.shoeName);
    }
}
