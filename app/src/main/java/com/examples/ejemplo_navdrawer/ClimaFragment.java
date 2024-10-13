package com.examples.ejemplo_navdrawer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClimaFragment extends Fragment {

    private EditText etCiudad;
    private Button btnBuscar;
    private TextView tvClima;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clima, container, false);

        etCiudad = view.findViewById(R.id.et_ciudad);
        btnBuscar = view.findViewById(R.id.btn_buscar);
        tvClima = view.findViewById(R.id.tv_clima);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarClima();
            }
        });

        return view;
    }

    private void buscarClima() {
        String ciudad = etCiudad.getText().toString().trim();  // Eliminar espacios en blanco

        // Verificar si la ciudad no está vacía
        if (ciudad.isEmpty()) {
            tvClima.setText("Error: Debe ingresar una ciudad");
            return;
        }

        // Crear la URL para la API de OpenWeatherMap
        String apiKey = "e07ccd51d6dfa8420755c0c7d16965e3"; // Tu clave API
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + ciudad + "&appid=" + apiKey + "&units=metric&lang=es";

        // Realizar la solicitud HTTP en un hilo separado (importante en Android)
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Leer la respuesta JSON
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // Procesar la respuesta JSON en el hilo principal
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                procesarRespuesta(response.toString(), ciudad);
                            }
                        });

                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvClima.setText("Error: Código de respuesta " + responseCode);
                                Log.e("ClimaFragment", "Error response code: " + responseCode);
                            }
                        });
                    }
                } catch (Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvClima.setText("Error: " + e.getMessage());
                            Log.e("ClimaFragment", "Error: " + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    // Método para procesar la respuesta JSON y mostrar el clima
    private void procesarRespuesta(String respuesta, String ciudad) {
        try {
            JSONObject jsonObject = new JSONObject(respuesta);
            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            if (weatherArray != null && weatherArray.length() > 0) {
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String clima = weatherObject.getString("description");

                // Obtener la temperatura
                JSONObject mainObject = jsonObject.getJSONObject("main");
                double temperatura = mainObject.getDouble("temp");

                // Mostrar el clima y la temperatura
                tvClima.setText("Clima en " + ciudad + ": " + clima + "\nTemperatura: " + temperatura + "°C");
            } else {
                tvClima.setText("Error: No se obtuvieron datos del clima");
            }
        } catch (Exception e) {
            tvClima.setText("Error al procesar los datos");
            Log.e("ClimaFragment", "Error al procesar la respuesta JSON: " + e.getMessage());
        }
    }
}
