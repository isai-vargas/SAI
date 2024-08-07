package com.example.sai;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sai.Data.DatosGlobalesSingleton;
import com.example.sai.Model.BodegaModel;
import com.example.sai.Model.SucursalModel;
import com.example.sai.Service.BodegaService;
import com.example.sai.Service.SucursalService;

import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.List;

public class SucursalBodega extends AppCompatActivity {


    private Spinner sucursalSpinner;
    private Spinner bodegaSpinner;
    private Button siguienteButton;
    private List<SucursalModel> sucursalesList; // Lista para almacenar sucursales
    private List<BodegaModel> bodegasList; //lista de para almacenar bidegas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursal_bodega);

        // Obtén una referencia al TextView
        TextView texviewempresa = findViewById(R.id.empresatextview);
        texviewempresa.setText(DatosGlobalesSingleton.getInstance().getEmpresaModel().getNombre());
        TextView textusuario = findViewById(R.id.nombreusertexview);
        textusuario.setText(DatosGlobalesSingleton.getInstance().getUsuarioEmpresaModel().getNombreLargo());

        //sucursales y bodegas
        sucursalSpinner = findViewById(R.id.sucursalspinner);
        sucursalesList = new ArrayList<>(); // Inicializa la lista

        bodegaSpinner = findViewById(R.id.bodegaspinner);
        bodegasList = new ArrayList<>();

        siguienteButton = findViewById(R.id.siguientebutton);

        String token = DatosGlobalesSingleton.getInstance().getLoginResponseModel().getToken();
        int empresaId = DatosGlobalesSingleton.getInstance().getEmpresaModel().getCodigo();
        String usuario = DatosGlobalesSingleton.getInstance().getUsuarioEmpresaModel().getNombre();

        new ObtenerSucursalesTask().execute(empresaId, usuario, token);

        // Establecer el listener para la selección de elementos del Spinner
        sucursalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Obtener la sucursal seleccionada
                SucursalModel selectedSucursal = sucursalesList.get(position);
                String centro = selectedSucursal.getCentro();

                //almacenar los datos de la sucursal seleccionada para poder usarlos despues
                DatosGlobalesSingleton.getInstance().setSucursalModel(selectedSucursal);

                //aqui mandaremos a llamar para cargar la bodegas
                new ObtenerBodegasTask().execute(empresaId, usuario, centro, token);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SucursalBodega.this, "No a seleccionado ninguna sucursal", Toast.LENGTH_SHORT).show();
            }
        });


        bodegaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Obtener la sucursal seleccionada
                BodegaModel selectedBodega = bodegasList.get(position);

                //almacenar los datos de la sucursal seleccionada para poder usarlos despues
                DatosGlobalesSingleton.getInstance().setBodegaModel(selectedBodega);

                //Toast.makeText(SucursalBodega.this, "No a seleccionado ninguna bodega" + DatosGlobalesSingleton.getInstance().getBodegaModel().getDescripcion(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SucursalBodega.this, "No a seleccionado ninguna bodega", Toast.LENGTH_SHORT).show();
            }
        });



        siguienteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SucursalBodega.this, "hola gisela", Toast.LENGTH_SHORT).show();
                //nos lleva hacia la otra ventana
                int sucursal = DatosGlobalesSingleton.getInstance().getSucursalModel().getCodigo();
                int bodega = DatosGlobalesSingleton.getInstance().getBodegaModel().getCodigo();
                if(sucursal != -1){

                    if(bodega !=-1){
                        Intent intent = new Intent(SucursalBodega.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(SucursalBodega.this, "No puede continuar no ay bodega seleccionada", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(SucursalBodega.this, "No puede continuar no ay sucursal seleccionada", Toast.LENGTH_SHORT).show();
                }


            }

        });
    }



    //obtener las sucursal de la api y cargarlas en el spinner
    private class ObtenerSucursalesTask extends AsyncTask<Object, Void, List<SucursalModel>> {
        @Override
        protected List<SucursalModel> doInBackground(Object... params) {
            int empresaId = (int) params[0];
            String usuario = (String) params[1];
            String token = (String) params[2];

            SucursalService sucursalService = new SucursalService();
            return sucursalService.obtenerSucursales(empresaId, usuario, token);
        }

        @Override
        protected void onPostExecute(List<SucursalModel> result) {
            if (result != null && !result.isEmpty()) {
                sucursalesList.clear();
                sucursalesList.addAll(result);

                List<String> sucursalNames = new ArrayList<>();
                for (SucursalModel sucursal : sucursalesList) {
                    sucursalNames.add(sucursal.getDescripcion());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(SucursalBodega.this, android.R.layout.simple_spinner_item, sucursalNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sucursalSpinner.setAdapter(adapter);

            } else {
                Toast.makeText(SucursalBodega.this, "No se pudo obtener las sucursales.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    // Obtener las bodegas de la API y cargarlas en el spinner
    private class ObtenerBodegasTask extends AsyncTask<Object, Void, List<BodegaModel>> {
        @Override
        protected List<BodegaModel> doInBackground(Object... params) {
            int empresaId = (int) params[0];
            String usuario = (String) params[1];
            String centro = (String) params[2];
            String token = (String) params[3];

            BodegaService bodegaService = new BodegaService();
            return bodegaService.obtenerBodegas(empresaId, usuario, centro, token);
        }

        @Override
        protected void onPostExecute(List<BodegaModel> result) {
            if (result != null && !result.isEmpty()) {
                // Aquí asumiendo que tienes una lista `bodegasList` para almacenar los resultados
                bodegasList.clear();
                bodegasList.addAll(result);

                List<String> bodegaNames = new ArrayList<>();
                for (BodegaModel bodega : bodegasList) {
                    bodegaNames.add(bodega.getDescripcion());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(SucursalBodega.this, android.R.layout.simple_spinner_item, bodegaNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                bodegaSpinner.setAdapter(adapter);

            } else {
                Toast.makeText(SucursalBodega.this, "No se pudo obtener las bodegas.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}