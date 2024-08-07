package com.example.sai;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sai.Data.DatosGlobalesSingleton;
import com.example.sai.Model.ClienteModel;
import com.example.sai.Model.ProductoModel;
import com.example.sai.Service.ProductoService;
import com.google.android.material.internal.EdgeToEdgeUtils;

import java.util.ArrayList;
import java.util.List;

public class FrmFacturaDetalleActivity extends AppCompatActivity {

    private Button buscarbutton;
    private EditText productoEditext;
    private  EditText descripcionEditext;
    private  EditText casaEditext;
    private  ListView productolistview;

    private  EditText productodescipcionEditext;
    private  EditText precioproductoEditext;
    private  EditText cantidadproductoEditext;
    private  EditText totalproductoEditext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_frm_factura_detalle);

        //obtener instancia de los controles.

        buscarbutton = findViewById(R.id.Buscarbutton);
        productoEditext = findViewById(R.id.codigoeditTextText);
        descripcionEditext = findViewById(R.id.descripcioneditTextText);
        casaEditext = findViewById(R.id.casaeditTextText);

        productolistview = findViewById(R.id.productoListViewControl);

        productodescipcionEditext = findViewById(R.id.prodcutoeditTextText);
        precioproductoEditext = findViewById(R.id.precioeditTextText);
        cantidadproductoEditext = findViewById(R.id.cantidadeditTextText);
        totalproductoEditext = findViewById(R.id.totaleditTextText);


        //parametros para la busqueda
        String token = DatosGlobalesSingleton.getInstance().getLoginResponseModel().getToken();//token
        int empresa = DatosGlobalesSingleton.getInstance().getUsuarioEmpresaModel().getPredeterminada();
        int tipoclientemodel = DatosGlobalesSingleton.getInstance().getClienteModel().getTipo();


        int tipocliente; // aqui tengo que hacer un case para asignar bien los tipos de precio segun el cliente.

        if(tipoclientemodel == 1 || tipoclientemodel == 5 || tipoclientemodel == 2)//precio publico
        {
            tipocliente = 1;

        }else if(tipoclientemodel == 4 || tipoclientemodel == 3)//precio mayoriste
        {
            tipocliente = 2;
        }else if(tipoclientemodel == 6)//precios especiales
        {
            tipocliente = 3;
        }else if(tipoclientemodel == 7)//precio ruta
        {
            tipocliente = 4;

        }else{
            tipocliente = 0;
        }


        int cliente = DatosGlobalesSingleton.getInstance().getClienteModel().getCodigo();
        int bodega = DatosGlobalesSingleton.getInstance().getBodegaModel().getCodigo();




        //buscar el producto
        buscarbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String producto = productoEditext.getText().toString().trim();
                String descripcion = descripcionEditext.getText().toString().trim();
                String casa = casaEditext.getText().toString().trim();
                // Contadores para verificar si los campos están llenos
                int filledFields = 0;
                if (!producto.isEmpty()) filledFields++;
                if (!descripcion.isEmpty()) filledFields++;
                if (!casa.isEmpty()) filledFields++;
                // Verificar las condiciones
                if (filledFields == 0) {
                    // Todos los campos están vacíos
                    Toast.makeText(FrmFacturaDetalleActivity.this, "Debe llenar al menos un campo para buscar.", Toast.LENGTH_LONG).show();
                }else if (filledFields > 1) {
                    // Más de un campo está lleno
                    Toast.makeText(FrmFacturaDetalleActivity.this, "Solo se puede buscar por un campo.", Toast.LENGTH_SHORT).show();
                } else {

                    if(!producto.isEmpty()){
                        //buscar por producto
                        new ObtenerProductosTask(token).execute(empresa, tipocliente, cliente,bodega, null, producto, null);

                    } else if(!descripcion.isEmpty()){

                        new ObtenerProductosTask(token).execute(empresa, tipocliente, cliente,bodega, descripcion, null, null);

                    } else if (!casa.isEmpty()){

                        new ObtenerProductosTask(token).execute(empresa, tipocliente, cliente,bodega, null, null, casa);

                    }

                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    public class ObtenerProductosTask extends AsyncTask<Object, Void, List<ProductoModel>> {

        private final String token; // Asume que el token es necesario y se pasa como parámetro

        public ObtenerProductosTask(String token) {
            this.token = token;
        }

        @Override
        protected List<ProductoModel> doInBackground(Object... params) {
            int empresaId = (int) params[0];
            int tipoCliente = (int) params[1];
            int clienteId = (int) params[2];
            int bodegaId = (int) params[3];
            String descripcionGeneral = (String) params[4];
            String producto = (String) params[5];
            String casa = (String) params[6];

            ProductoService productoService = new ProductoService();
            return productoService.obtenerProductos(empresaId, tipoCliente, clienteId, bodegaId, descripcionGeneral, producto, casa, token);
        }

        @Override
        protected void onPostExecute(List<ProductoModel> result) {
            if (result != null && !result.isEmpty()) {
                // Aquí puedes actualizar la UI con los datos obtenidos
                // Ejemplo: actualizar un ListView o RecyclerView
                //Toast.makeText(FrmFacturaDetalleActivity.this, "Productos obtenidos exitosamente", Toast.LENGTH_SHORT).show();

                // Crear una lista de cadenas que representen la información que deseas mostrar
                List<String> productoInfoList = new ArrayList<>();
                for (ProductoModel productoModel : result) {
                    // Aquí puedes ajustar la representación del producto como desees
                    productoInfoList.add(productoModel.getProducto() +" - "+productoModel.getDescripcionGeneral() + " - " + productoModel.getDisponible());
                }

                // Crear un ArrayAdapter usando el layout simple_spinner_item y la lista de información de los productos
                ArrayAdapter<String> adapter = new ArrayAdapter<>(FrmFacturaDetalleActivity.this, android.R.layout.simple_list_item_1, productoInfoList);

                // Asignar el adaptador al ListView
                productolistview.setAdapter(adapter);

                // Establecer un listener para capturar el clic en los elementos de la lista
                productolistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        ProductoModel productoModelselecciondo = result.get(position);
                        Toast.makeText(FrmFacturaDetalleActivity.this, productoModelselecciondo.getDescripcionGeneral(), Toast.LENGTH_SHORT).show();
                        //cargo los datos al modelo para luego usarlos en la factura.
                        //DatosGlobalesSingleton.getInstance().setClienteModel(clienteSeleccionado);
                        //clienteselectEditText.setText(DatosGlobalesSingleton.getInstance().getClienteModel().getNombre());
                        productodescipcionEditext.setText(productoModelselecciondo.getDescripcionGeneral());
                        double precio = productoModelselecciondo.getPrecio();
                        precioproductoEditext.setText(String.valueOf(precio));




                    }
                });





            } else {
                Toast.makeText(FrmFacturaDetalleActivity.this, "No se pudo obtener los productos.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}