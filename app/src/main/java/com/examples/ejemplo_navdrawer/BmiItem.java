package com.examples.ejemplo_navdrawer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BmiItem {
    private double value;  // Valor del BMI
    private String category;  // Categoría del BMI (Bajo peso, Normal, etc.)
    private Date date;  // Fecha del cálculo (ahora es de tipo Date)

    // Constructor
    public BmiItem(double value, String category, Date date) {
        this.value = value;
        this.category = category;
        this.date = date;
    }

    // Métodos getter
    public double getValue() {
        return value;
    }

    public String getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }

    // Convertir la fecha a String en formato "dd/MM/yyyy"
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}


