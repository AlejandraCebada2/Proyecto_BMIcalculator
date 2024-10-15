package com.examples.ejemplo_navdrawer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeResponse {
    private String datetime; // ISO 8601 format

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Date getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
        try {
            return sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
