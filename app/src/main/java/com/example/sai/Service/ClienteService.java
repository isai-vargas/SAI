package com.example.sai.Service;

import com.example.sai.Model.ClienteModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ClienteService {

    public List<ClienteModel> obtenerClientes(Integer codigo, String nombre, String nit, String token) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            // Construcción de la URL con parámetros opcionales
            StringBuilder urlBuilder = new StringBuilder("http://190.143.133.236:8083/api/Cliente?");

            if (codigo != null) {
                urlBuilder.append("codigo=").append(codigo).append("&");
            }

            if (nombre != null && !nombre.isEmpty()) {
                urlBuilder.append("nombre=").append(nombre).append("&");
            }

            if (nit != null && !nit.isEmpty()) {
                urlBuilder.append("nit=").append(nit).append("&");
            }

            // Eliminar el último "&" si existe
            String urlStr = urlBuilder.toString();
            if (urlStr.endsWith("&")) {
                urlStr = urlStr.substring(0, urlStr.length() - 1);
            }

            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            Gson gson = new Gson();
            Type listType = new TypeToken<List<ClienteModel>>() {}.getType();
            return gson.fromJson(json.toString(), listType);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
