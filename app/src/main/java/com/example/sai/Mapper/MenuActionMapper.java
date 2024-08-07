package com.example.sai.Mapper;

import com.example.sai.FrmFacturaActivity;

import java.util.HashMap;
import java.util.Map;

public class MenuActionMapper {

    //mapa estático que mapea los nombres de las acciones a las clases correspondientes:
    private static final Map<String, Class<?>> actionMap = new HashMap<>();

    static {
        actionMap.put("FRMFACTURA", FrmFacturaActivity.class);


        //actionMap.put("FRMCLIENTE", FrmClienteActivity.class);
        // Agrega aquí todas las demás acciones y sus correspondientes Activities
    }

    public static Class<?> getActivityClass(String action) {
        return actionMap.get(action);
    }
}