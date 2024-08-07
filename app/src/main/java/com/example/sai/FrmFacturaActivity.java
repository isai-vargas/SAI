package com.example.sai;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sai.Data.DatosGlobalesSingleton;
import com.example.sai.Model.ClienteModel;
import com.example.sai.Model.SerieModel;
import com.example.sai.Model.SucursalModel;
import com.example.sai.Service.ClienteService;
import com.example.sai.Service.SerieService;

import java.util.ArrayList;
import java.util.List;

public class FrmFacturaActivity extends AppCompatActivity {

    private Spinner seriesSpinner;
    private TextView correlativotextview;
    private Button buttonbuscar;
    private Button buttonsiguiente;
    private EditText nombreEditText;
    private  EditText codigoEditText;
    private  EditText nitEditText;
    private  EditText clienteselectEditText;
    private List<SerieModel> serieModelList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_frm_factura);

        seriesSpinner = findViewById(R.id.seriespinner);
        serieModelList = new ArrayList<>();

        correlativotextview = findViewById(R.id.correlativotextView);
        buttonbuscar = findViewById(R.id.Buscarbutton);
        buttonsiguiente = findViewById(R.id.siguientebutton);

        // Obtener referencias a los EditText
        codigoEditText = findViewById(R.id.codigoeditTextText);
        nombreEditText = findViewById(R.id.nombreeditTextText);
        nitEditText = findViewById(R.id.niteditTextText);
        clienteselectEditText = findViewById(R.id.clienteSelectText);

        listView = findViewById(R.id.ClientesListViewControl);

        // Para limpiar el clienteModel y la serie.
        //con esto me aseguro que todoestara limpio cuando se inicie la actividad.
        DatosGlobalesSingleton.getInstance().clearClienteModel();
        DatosGlobalesSingleton.getInstance().clearSerieModel();


        int sucursal = DatosGlobalesSingleton.getInstance().getSucursalModel().getCodigo(); // Reemplaza con el valor real
        int bodega = DatosGlobalesSingleton.getInstance().getBodegaModel().getCodigo();// Reemplaza con el valor real
        int empresa = DatosGlobalesSingleton.getInstance().getUsuarioEmpresaModel().getPredeterminada();  // Reemplaza con el valor real
        int tipo = 4;     // Reemplaza con el valor real
        String token = DatosGlobalesSingleton.getInstance().getLoginResponseModel().getToken(); // Reemplaza con el token real

        new ObtenerSeriesTask(this).execute(sucursal, bodega, empresa, tipo, token);

        //evento para seleccionar la serie
        seriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //obtener la seria seleccionada
                SerieModel serieselect = serieModelList.get(position);

                //almaceno todp de la seria seleccionada
                DatosGlobalesSingleton.getInstance().setSerieModel(serieselect);

                long correlativo = DatosGlobalesSingleton.getInstance().getSerieModel().getCorrelativo();
                // Convierte el long a String
                String correlativoString = String.valueOf(correlativo);
                // Actualiza el TextView con el valor convertido

                correlativotextview.setText(correlativoString);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(FrmFacturaActivity.this, "No se han selecionado una seria", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        buttonbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Obtener los textos de los EditText y aplicar trim para eliminar espacios en blanco
                String codigo = codigoEditText.getText().toString().trim();
                String nombre = nombreEditText.getText().toString().trim();
                String nit = nitEditText.getText().toString().trim();

                // Contadores para verificar si los campos están llenos
                int filledFields = 0;
                if (!codigo.isEmpty()) filledFields++;
                if (!nombre.isEmpty()) filledFields++;
                if (!nit.isEmpty()) filledFields++;

                // Verificar las condiciones
                if (filledFields == 0) {
                    // Todos los campos están vacíos
                    Toast.makeText(FrmFacturaActivity.this, "Debe llenar al menos un campo para buscar.", Toast.LENGTH_SHORT).show();
                } else if (filledFields > 1) {
                    // Más de un campo está lleno
                    Toast.makeText(FrmFacturaActivity.this, "Solo se puede buscar por un campo.", Toast.LENGTH_SHORT).show();
                } else {

                    // Exactamente un campo está lleno, proceder con la búsqueda
                    if (!codigo.isEmpty()) {
                        // Buscar por código
                        int codigoint = Integer.parseInt(codigo);
                        new ObtenerClientesTask().execute(codigoint, null, null, token);

                    } else if (!nombre.isEmpty()) {
                        // Buscar por nombre
                        new ObtenerClientesTask().execute(null, nombre, null, token);


                    } else if (!nit.isEmpty()) {
                        // Buscar por nit
                        new ObtenerClientesTask().execute(null, null, nit, token);


                    }
                }


            }
        });


        buttonsiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClienteModel cliente = DatosGlobalesSingleton.getInstance().getClienteModel();
                SerieModel serie = DatosGlobalesSingleton.getInstance().getSerieModel();

                if(cliente != null){

                    if (serie!= null){

                        Intent intent = new Intent(FrmFacturaActivity.this, FrmFacturaDetalleActivity.class);
                        startActivity(intent);

                    }else {
                        Toast.makeText(FrmFacturaActivity.this, "No puede continuar sin haber seleccionado una serie", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(FrmFacturaActivity.this, "No puede continuar sin haber seleccionado un cliente", Toast.LENGTH_LONG).show();
                }


            }

        });




    }



    //tarea asyncrona para traer las series
    private class ObtenerSeriesTask extends AsyncTask<Object, Void, List<SerieModel>> {
        private Context context;

        public ObtenerSeriesTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<SerieModel> doInBackground(Object... params) {
            int sucursal = (int) params[0];
            int bodega = (int) params[1];
            int empresa = (int) params[2];
            int tipo = (int) params[3];
            String token = (String) params[4];

            return SerieService.obtenerSeries(sucursal, bodega, empresa, tipo, token);
        }

        @Override
        protected void onPostExecute(List<SerieModel> result) {
            if (result != null && !result.isEmpty()) {
                serieModelList.clear();
                serieModelList.addAll(result);

                List<String> series = new ArrayList<>();
                for (SerieModel serie : serieModelList){
                    series.add(serie.getSerie());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(FrmFacturaActivity.this, android.R.layout.simple_spinner_item,series);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                seriesSpinner.setAdapter(adapter);

            } else {
                Toast.makeText(context, "No se pudieron obtener las series.", Toast.LENGTH_SHORT).show();
            }
        }

    }


    //tarea asyncrona para buscar clientes
    private class ObtenerClientesTask extends AsyncTask<Object, Void, List<ClienteModel>> {
        @Override
        protected List<ClienteModel> doInBackground(Object... params) {
            Integer codigo = (Integer) params[0];
            String nombre = (String) params[1];
            String nit = (String) params[2];
            String token = (String) params[3];

            ClienteService clienteService = new ClienteService();
            return clienteService.obtenerClientes(codigo, nombre, nit, token);
        }

        @Override
        protected void onPostExecute(List<ClienteModel> result) {
            if (result != null && !result.isEmpty()) {
                // Crear una lista de cadenas que representen la información que deseas mostrar
                List<String> clienteInfoList = new ArrayList<>();
                for (ClienteModel cliente : result) {
                    // Aquí puedes ajustar la representación del cliente como desees
                    clienteInfoList.add(cliente.getCodigo() +" - "+cliente.getNombre() + " - " + cliente.getNit());
                }

                // Crear un ArrayAdapter usando el layout simple_spinner_item y la lista de información de los clientes
                ArrayAdapter<String> adapter = new ArrayAdapter<>(FrmFacturaActivity.this, android.R.layout.simple_list_item_1, clienteInfoList);

                // Asignar el adaptador al ListView
                listView.setAdapter(adapter);

                // Establecer un listener para capturar el clic en los elementos de la lista
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Obtener el objeto ClienteModel correspondiente a la posición seleccionada
                        ClienteModel clienteSeleccionado = result.get(position);
                        //cargo los datos al modelo para luego usarlos en la factura.
                        DatosGlobalesSingleton.getInstance().setClienteModel(clienteSeleccionado);

                        clienteselectEditText.setText(DatosGlobalesSingleton.getInstance().getClienteModel().getNombre());

                    }
                });


            } else {
                Toast.makeText(FrmFacturaActivity.this, "No se pudo obtener los clientes.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}