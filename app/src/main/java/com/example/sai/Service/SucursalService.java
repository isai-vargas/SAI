package com.example.sai.Service;

import com.example.sai.Model.SucursalModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SucursalService {

    private static final String API_URL = "http://190.143.133.236:8082/api/Sucursal";

    public List<SucursalModel> obtenerSucursales(int empresaId, String usuario, String token) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            // Construir la URL con parámetros
            URL url = new URL(API_URL + "?empresa=" + empresaId + "&usuario=" + usuario);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            // Leer la respuesta
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Convertir la respuesta JSON a una lista de objetos
            Gson gson = new Gson();
            Type sucursalListType = new TypeToken<List<SucursalModel>>() {}.getType();
            return gson.fromJson(response.toString(), sucursalListType);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
