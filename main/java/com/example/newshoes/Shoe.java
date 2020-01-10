package com.example.newshoes;

import java.io.Serializable;
import java.util.ArrayList;

public class Shoe implements Serializable, Comparable<Shoe> {
    private String shoeName;

    private Double desiredDistanceInMiles;

    private Double mileCount;

    private Double meterCount;

    public Shoe(String shoeName, Double distance) {
        this.shoeName = shoeName;
        this.desiredDistanceInMiles = distance;
        mileCount = 0.00;
        meterCount = 0.00;
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

    public void setMeterCount(Double meterCount) {
        this.meterCount += meterCount;
    }

    public Double getMeterCount() {
        return meterCount;
    }

    public void changeShoeName(String shoeName) {
        this.shoeName = shoeName;
    }

    public void changeDesiredDistanceInMiles(Double desiredDistanceInMiles) {
        this.desiredDistanceInMiles = desiredDistanceInMiles;
    }

    public String toString() {
        return String.format("%s - Current Mile Count: %.2f", this.shoeName, this.getMileCount());
    }

    @Override
    public int compareTo(Shoe o) {
        return this.shoeName.compareTo(o.shoeName);
    }
}