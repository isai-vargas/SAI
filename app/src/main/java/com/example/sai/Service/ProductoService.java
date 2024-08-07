package com.example.sai.Service;

import com.example.sai.Model.ProductoModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ProductoService {
    public List<ProductoModel> obtenerProductos(int empresaId, int tipoCliente, int clienteId, int bodegaId, String descripcionGeneral, String producto, String casa, String token) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            // Construir la URL dinámicamente con los parámetros proporcionados
            StringBuilder urlBuilder = new StringBuilder("http://190.143.133.236:8083/api/Producto?");
            urlBuilder.append("empresa=").append(empresaId)
                    .append("&tipociente=").append(tipoCliente)
                    .append("&cliente=").append(clienteId)
                    .append("&bodega=").append(bodegaId);

            if (descripcionGeneral != null && !descripcionGeneral.isEmpty()) {
                urlBuilder.append("&descripciongeneral=").append(descripcionGeneral);
            }
            if (producto != null && !producto.isEmpty()) {
                urlBuilder.append("&producto=").append(producto);
            }
            if (casa != null && !casa.isEmpty()) {
                urlBuilder.append("&casa=").append(casa);
            }

            URL url = new URL(urlBuilder.toString());
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
            Type listType = new TypeToken<List<ProductoModel>>(){}.getType();
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
