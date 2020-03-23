package com.example.newshoes;

import java.io.Serializable;
import java.util.ArrayList;

public class Shoe implements Serializable, Comparable<Shoe> {
    private String shoeName;

    //the distance, set by the user, to travel in the Shoe before they want to get a new pair of shoes
    private Double desiredDistanceInMiles;

    //total miles traveled by this Shoe object
    private Double mileCount;

    //total meters traveled by this Shoe object, used for the progress bar in the StartRun activity
    private Double meterCount;

    //Number of runs a user has made after reaching their set goal
    private Integer runsSinceGoalReached;

    public Shoe(String shoeName, Double distance) {
        this.shoeName = shoeName;
        this.desiredDistanceInMiles = distance;
        mileCount = 0.00;
        meterCount = 0.00;
        runsSinceGoalReached = 0;
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

    /**
     * Method to check if the user has reached their mile goal for the given pair of Shoes
     */
    public boolean hasReachedGoal() {
        if(mileCount >= desiredDistanceInMiles)
        {
            return true;
        }
        else {
            return false;
        }
    }

    public void incrementRunsSinceGoalReached() {
        ++runsSinceGoalReached;
    }

    public int getRunsSinceGoalReached() {
        return runsSinceGoalReached;
    }

    public String toString() {
        return String.format("%s - Current Mile Count: %.2f", this.shoeName, this.getMileCount());
    }

    /**
     * Method used to implement a manner to sort Shoe objects using the Collections class. Here, we
     * are sorting by the ShoeName, in alphabetical order, and for some reason, capital letters
     * first
     */
    @Override
    public int compareTo(Shoe o) {
        return this.shoeName.compareTo(o.shoeName);
    }
}