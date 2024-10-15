package com.examples.ejemplo_navdrawer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherFragment extends Fragment {
    private Button checkWeatherButton;
    private TextView weatherInfoTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        checkWeatherButton = view.findViewById(R.id.check_weather_button);
        weatherInfoTextView = view.findViewById(R.id.weather_info_text_view);

        checkWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherInfo();
            }
        });

        return view;
    }

    private void getWeatherInfo() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<TimeResponse> call = apiService.getCurrentTime();

        call.enqueue(new Callback<TimeResponse>() {
            @Override
            public void onResponse(Call<TimeResponse> call, Response<TimeResponse> response) {
                Log.d("API Response", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Date date = response.body().getDate();
                    if (date != null) {
                        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                        SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                        String day = dayFormat.format(date);
                        String hour = hourFormat.format(date);

                        String message = "La mejor hora y día para hacer ejercicio es: " + day + " a las " + hour;
                        weatherInfoTextView.setText(message);
                    } else {
                        Toast.makeText(getActivity(), "Error al procesar la fecha", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (response.errorBody() != null) {
                        try {
                            Log.e("API Error", "Error Body: " + response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Toast.makeText(getActivity(), "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TimeResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
