package com.examples.ejemplo_navdrawer;

public class BmiItem {
    private double value; // Valor del BMI
    private String category; // Categoría del BMI

    public BmiItem(double value, String category) {
        this.value = value;
        this.category = category;
    }

    public double getValue() {
        return value;
    }

    public String getCategory() {
        return category;
    }
}
