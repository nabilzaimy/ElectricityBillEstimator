package com.example.electricitybillestimator; // MAKE SURE THIS PACKAGE MATCHES YOUR MainActivity.java

public class CalculationEntry {
    private int id; // For database primary key
    private String month;
    private double unitsUsed;
    private double totalCharges;
    private double rebatePercentage;
    private double finalCost;
    private long timestamp;

    // Constructor to create a new entry for saving
    public CalculationEntry(String month, double unitsUsed, double totalCharges,
                            double rebatePercentage, double finalCost, long timestamp) {
        this.month = month;
        this.unitsUsed = unitsUsed;
        this.totalCharges = totalCharges;
        this.rebatePercentage = rebatePercentage;
        this.finalCost = finalCost;
        this.timestamp = timestamp;
    }

    // Constructor to retrieve an entry from the database (includes ID)
    public CalculationEntry(int id, String month, double unitsUsed, double totalCharges,
                            double rebatePercentage, double finalCost, long timestamp) {
        this.id = id;
        this.month = month;
        this.unitsUsed = unitsUsed;
        this.totalCharges = totalCharges;
        this.rebatePercentage = rebatePercentage;
        this.finalCost = finalCost;
        this.timestamp = timestamp;
    }

    // --- Getters (required to access data from the object) ---
    public int getId() {
        return id;
    }

    public String getMonth() {
        return month;
    }

    public double getUnitsUsed() {
        return unitsUsed;
    }

    public double getTotalCharges() {
        return totalCharges;
    }

    public double getRebatePercentage() {
        return rebatePercentage;
    }

    public double getFinalCost() {
        return finalCost;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // --- Setters (optional, but good practice for updating in some cases) ---
    public void setId(int id) {
        this.id = id;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setUnitsUsed(double unitsUsed) {
        this.unitsUsed = unitsUsed;
    }

    public void setTotalCharges(double totalCharges) {
        this.totalCharges = totalCharges;
    }

    public void setRebatePercentage(double rebatePercentage) {
        this.rebatePercentage = rebatePercentage;
    }

    public void setFinalCost(double finalCost) {
        this.finalCost = finalCost;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}