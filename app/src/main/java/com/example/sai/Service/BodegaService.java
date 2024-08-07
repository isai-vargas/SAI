package com.example.sai.Service;

import com.example.sai.Model.BodegaModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BodegaService {

    public List<BodegaModel> obtenerBodegas(int empresaId, String usuario, String centro, String token) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://190.143.133.236:8082/api/Bodega?empresa=" + empresaId + "&usuario=" + usuario + "&centro=" + centro);
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
            Type listType = new TypeToken<List<BodegaModel>>(){}.getType();
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
