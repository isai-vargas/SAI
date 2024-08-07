package com.example.sai.Service;

import com.example.sai.Model.LoginRequestModel;
import com.example.sai.Model.LoginResponseModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthService {
    private static final String LOGIN_URL = "http://190.143.133.236:8082/api/Auth/login";

    public LoginResponseModel authenticate(String nombre, String clave, int predeterminada) {
        LoginRequestModel loginRequest = new LoginRequestModel(nombre, clave, predeterminada);
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(loginRequest);

        try {
            URL url = new URL(LOGIN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Enviar el cuerpo de la solicitud
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonRequest.getBytes());
                os.flush();
            }

            // Leer la respuesta
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                // Convertir la respuesta JSON en objeto LoginResponseModel
                return gson.fromJson(response.toString(), LoginResponseModel.class);
            } else {
                System.out.println("Error en la conexión: " + connection.getResponseMessage());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
