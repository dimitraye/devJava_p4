package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * Model of the Parking Spot.
 */
//
public class ParkingSpot {

    /**
     * Number of the parking spot.
     */
    private int number;

    /**
     * Type of the parking spot.
     */
    private ParkingType parkingType;

    /**
     * Availability of a spot.
     */
    private boolean isAvailable;


    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    public ParkingSpot(ParkingSpot parkingSpot) {
        this.number = parkingSpot.getId();
        this.parkingType = parkingSpot.getParkingType();
        this.isAvailable = parkingSpot.isAvailable();
    }

    public int getId() {
        return number;
    }


    public void setId(int number) {
        this.number = number;
    }


    public ParkingType getParkingType() {
        return parkingType;
    }

    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }


    public boolean isAvailable() {
        return isAvailable;
    }


    public void setAvailable(boolean available) {
        isAvailable = available;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }


    @Override
    public int hashCode() {
        return number;
    }
}
