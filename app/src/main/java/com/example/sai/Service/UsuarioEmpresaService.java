package com.example.sai.Service;

import com.example.sai.Model.UsuarioEmpresaModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UsuarioEmpresaService {

    private static final String BASE_API_URL = "http://190.143.133.236:8082/api/UsuarioEmpresa/";

    public UsuarioEmpresaModel obtenerUsuarioEmpresa(String username) {
        UsuarioEmpresaModel usuarioEmpresa = null;

        try {
            URL url = new URL(BASE_API_URL + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                Gson gson = new Gson();
                usuarioEmpresa = gson.fromJson(response.toString(), UsuarioEmpresaModel.class);
            } else {
                System.out.println("Error en la conexión: " + connection.getResponseMessage());
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarioEmpresa;
    }
}
