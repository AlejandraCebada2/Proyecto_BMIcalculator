package com.examples.ejemplo_navdrawer;

public class BMIEntry {
    private double bmi;
    private String date;

    public BMIEntry() {
        // Constructor vacío requerido para Firebase
    }

    public BMIEntry(double bmi, String date) {
        this.bmi = bmi;
        this.date = date;
    }

    public double getBmi() {
        return bmi;
    }

    public String getDate() {
        return date;
    }
}
