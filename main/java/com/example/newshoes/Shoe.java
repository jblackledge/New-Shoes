package com.example.newshoes;

import java.util.ArrayList;

public class Shoe {
    private String shoeName;

    private Integer desiredDistanceInMiles;

    public Shoe(String shoeName, Integer distance) {
        this.shoeName = shoeName;
        this.desiredDistanceInMiles = distance;
    }

    public String getName() {
        return shoeName;
    }

    public Integer getDesiredDistanceInMiles() {
        return desiredDistanceInMiles;
    }
}
