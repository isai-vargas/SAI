package com.example.sai;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sai.Data.DatosGlobalesSingleton;
import com.example.sai.Mapper.MenuActionMapper;
import com.example.sai.Model.MenuItemModel;
import com.example.sai.Model.MenuOpcionModel;
import com.example.sai.Service.MenuOpcionService;
import com.example.sai.Service.MenuService;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Map<Integer, String> menuItemIdToClaveMap = new HashMap<>();
    private LinearLayout buttonContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonContainer = findViewById(R.id.buttonContainer);//generar botones


        // Datos para obtener menú
        int empresaId = DatosGlobalesSingleton.getInstance().getEmpresaModel().getCodigo();
        String usuario = DatosGlobalesSingleton.getInstance().getUsuarioEmpresaModel().getNombre();

        new ObtenerMenuTask().execute(empresaId, usuario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            String clave = menuItemIdToClaveMap.get(item.getItemId());
            if (clave != null) {
                handleMenuItemClick(clave);
            } else if (item.getItemId() == R.id.nav_configuracion) {
                showThemeSelectionDialog();
            } else {
                Toast.makeText(MainActivity.this, "Opción no reconocida.", Toast.LENGTH_SHORT).show();
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void showThemeSelectionDialog() {
        ThemeSelectionDialogFragment dialogFragment = new ThemeSelectionDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "theme_selection");
    }

    private void handleMenuItemClick(String clave) {
        // Realiza acciones específicas basadas en la clave del ítem del menú
        String message = "Seleccionaste: " + clave;
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

        int empresaId = DatosGlobalesSingleton.getInstance().getEmpresaModel().getCodigo();
        String usuario = DatosGlobalesSingleton.getInstance().getUsuarioEmpresaModel().getNombre();
        String token = DatosGlobalesSingleton.getInstance().getLoginResponseModel().getToken();


        // Ejecutar la tarea asincrónica para obtener las opciones de menú
        new ObtenerMenuOpcionesTask(this).execute(empresaId, usuario, clave, token);


    }

    private class ObtenerMenuTask extends AsyncTask<Object, Void, List<MenuItemModel>> {

        @Override
        protected List<MenuItemModel> doInBackground(Object... params) {
            int empresaId = (int) params[0];
            String usuario = (String) params[1];

            MenuService menuService = new MenuService();
            String token = DatosGlobalesSingleton.getInstance().getLoginResponseModel().getToken();
            return menuService.obtenerMenu(empresaId, usuario, token);
        }

        @Override
        protected void onPostExecute(List<MenuItemModel> menuList) {
            NavigationView navigationView = findViewById(R.id.navigation_view);
            Menu menu = navigationView.getMenu();

            // Limpia los ítems actuales en el menú, manteniendo solo el ítem de configuración
            menu.clear();
            menu.add(Menu.NONE, R.id.nav_configuracion, Menu.NONE, getString(R.string.nav_configuracion))
                    .setIcon(R.drawable.ic_settings);

            if (menuList != null) {
                menuItemIdToClaveMap.clear(); // Limpia el mapa

                for (MenuItemModel item : menuList) {
                    // Usa un ID único basado en el índice del ítem en la lista
                    int itemId = View.generateViewId(); // Genera un ID único para cada ítem
                    MenuItem menuItem = menu.add(Menu.NONE, itemId, Menu.NONE, item.getNombre())
                            .setIcon(R.drawable.ic_launcher_foreground); // Usa el ícono adecuado

                    // Mapea el ID del ítem al valor de la clave
                    menuItemIdToClaveMap.put(itemId, item.getClave());
                }
            } else {
                Toast.makeText(MainActivity.this, "No se pudo obtener el menú.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class ObtenerMenuOpcionesTask extends AsyncTask<Object, Void, List<MenuOpcionModel>> {
        private final MainActivity context;

        public ObtenerMenuOpcionesTask(MainActivity context) {
            this.context = context;
        }

        @Override
        protected List<MenuOpcionModel> doInBackground(Object... params) {
            int empresaId = (int) params[0];
            String usuario = (String) params[1];
            String padre = (String) params[2];
            String token = (String) params[3];

            MenuOpcionService menuOpcionService = new MenuOpcionService();
            return menuOpcionService.obtenerMenuOpciones(empresaId, usuario, padre, token);
        }

        @Override
        protected void onPostExecute(List<MenuOpcionModel> result) {
            if (result != null && !result.isEmpty()) {
                // Aquí puedes hacer lo que necesites con la lista resultante, por ejemplo:
                // actualizar un RecyclerView, Spinner, o cualquier otro componente de UI
                // con los datos de las opciones de menú.


                List<MenuOpcionModel> menuOpciones = result;

                generarBotones(menuOpciones);
//                // Ejemplo: Mostrar los nombres de las opciones en un Spinner
//                List<String> menuNombres = new ArrayList<>();
//                for (MenuOpcionModel menuOpcion : result) {
//                    //menuNombres.add(menuOpcion.getNombre());
//                    Toast.makeText(context, menuOpcion.getNombre(), Toast.LENGTH_SHORT).show();
//                }

                //ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, menuNombres);
                //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //context.menuOpcionSpinner.setAdapter(adapter); // Reemplaza menuOpcionSpinner con tu Spinner

            } else {
                Toast.makeText(context, "No se pudo obtener las opciones de menú.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    //metodo para generar botones y manejar las acciones correspondientes

    private void generarBotones(List<MenuOpcionModel> menuOpciones) {
        LinearLayout buttonContainer = findViewById(R.id.buttonContainer);

        // Limpia los botones existentes si los hay
        buttonContainer.removeAllViews();

        // Crear un objeto de LayoutParams para los botones
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 16, 0, 16); // Márgenes para los botones

        // Estilo para los botones
        int padding = getResources().getDimensionPixelSize(R.dimen.button_padding);
        int cornerRadius = getResources().getDimensionPixelSize(R.dimen.button_corner_radius);

        for (MenuOpcionModel menuOpcion : menuOpciones) {
            Button button = new Button(this);
            button.setText(menuOpcion.getNombre());
            button.setLayoutParams(params);

            // Aplicar estilo a los botones
            button.setPadding(padding, padding, padding, padding);
            button.setBackgroundResource(R.drawable.button_background);


            //Utiliza el método getActivityClass para obtener la clase de la actividad basada en la acción:

            button.setOnClickListener(v -> {
                String ventana = menuOpcion.getAccion();
                Class<?> activityClass = MenuActionMapper.getActivityClass(ventana);

                if (activityClass != null) {
                    Intent intent = new Intent(MainActivity.this, activityClass);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No se encontró la actividad para la acción: " + ventana, Toast.LENGTH_SHORT).show();
                }
            });


            buttonContainer.addView(button);
        }
    }


}
