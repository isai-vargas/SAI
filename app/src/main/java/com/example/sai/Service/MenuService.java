package com.example.sai.Service;

import com.example.sai.Model.MenuItemModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MenuService {

    private static final String BASE_API_URL = "http://190.143.133.236:8082/api/Menu";

    public List<MenuItemModel> obtenerMenu(int empresaId, String usuario, String token) {
        List<MenuItemModel> menuList = null;
        try {
            URL url = new URL(BASE_API_URL + "?empresa=" + empresaId + "&usuario=" + usuario);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                Gson gson = new Gson();
                Type menuListType = new TypeToken<List<MenuItemModel>>() {}.getType();
                menuList = gson.fromJson(response.toString(), menuListType);
            } else {
                System.out.println("Error en la conexión: " + connection.getResponseMessage());
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuList;
    }

}
