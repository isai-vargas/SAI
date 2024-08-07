package com.example.sai.Service;

import com.example.sai.Model.MenuOpcionModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MenuOpcionService {

    private static final String BASE_URL = "http://190.143.133.236:8082/api/MenuOpcion";

    public List<MenuOpcionModel> obtenerMenuOpciones(int empresaId, String usuario, String padre, String token) {
        List<MenuOpcionModel> menuOpciones = new ArrayList<>();

        try {
            URL url = new URL(BASE_URL + "?empresa=" + empresaId + "&usuario=" + usuario + "&padre=" + padre);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            menuOpciones = gson.fromJson(response.toString(), new TypeToken<List<MenuOpcionModel>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return menuOpciones;
    }

}
