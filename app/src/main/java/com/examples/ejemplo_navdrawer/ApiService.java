package com.examples.ejemplo_navdrawer;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/timezone/America/Mexico_City")
    Call<TimeResponse> getCurrentTime();
}
//# curl "http://worldtimeapi.org/api/timezone/America/Mexico_City"