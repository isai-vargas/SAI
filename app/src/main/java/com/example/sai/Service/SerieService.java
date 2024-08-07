package com.example.sai.Service;

import android.util.Log;

import com.example.sai.Model.SerieModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SerieService {

    private static final String TAG = "SerieService";

    public static List<SerieModel> obtenerSeries(int sucursal, int bodega, int empresa, int tipo, String token) {
        String urlString = String.format("http://190.143.133.236:8083/api/Serie?sucursal=%d&bodega=%d&empresa=%d&tipo=%d",
                sucursal, bodega, empresa, tipo);
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parsear la respuesta JSON a una lista de SerieModel
                return new Gson().fromJson(response.toString(), new TypeToken<List<SerieModel>>() {}.getType());
            } else {
                Log.e(TAG, "Error en la respuesta: " + responseCode);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener series", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
