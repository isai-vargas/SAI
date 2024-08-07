package com.example.sai;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sai.Data.DatosGlobalesSingleton;
import com.example.sai.Model.EmpresaModel;
import com.example.sai.Model.LoginResponseModel;
import com.example.sai.Model.UsuarioEmpresaModel;
import com.example.sai.Service.AuthService;
import com.example.sai.Service.EmpresaService;
import com.example.sai.Service.UsuarioEmpresaService;

public class LoginActivity extends AppCompatActivity {
    //cuadro de texto de usuario
    private EditText usernameEditText;
    private EditText empresaEditext;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;

    private static int codigoempresa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //inicializo los controles de mi interfas de usuario
        usernameEditText = findViewById(R.id.username);
        empresaEditext = findViewById(R.id.empresa);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);

        usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // El EditText ha perdido el foco
                    String username = usernameEditText.getText().toString().trim();
                    if (!username.isEmpty()) {
                        // Inicia la tarea de red para obtener datos
                        new ObtenerUsuarioEmpresaTask().execute(username);
                        //int empresaId = DatosGlobalesSingleton.getInstance().getUsuarioEmpresaModel().getPredeterminada(); // ID de la empresa que deseas obtener

                    }
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (!username.isEmpty()) {
                    //login(username);
                    if(!password.isEmpty()){

                        if(codigoempresa != -1){


                            new AuthTask(username, password, codigoempresa).execute();

                        }else{
                            Toast.makeText(LoginActivity.this, "Por favor debe de seleccionar una empresa", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(LoginActivity.this, "Por favor ingrese su contraseña", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Por favor ingrese su nombre de usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Clase interna para realizar la solicitud de red en segundo plano de los datos del usuario
    private class ObtenerUsuarioEmpresaTask extends AsyncTask<String, Void, UsuarioEmpresaModel> {
        @Override
        protected UsuarioEmpresaModel doInBackground(String... params) {
            String username = params[0];

            UsuarioEmpresaService usuarioEmpresaService = new UsuarioEmpresaService();
            return usuarioEmpresaService.obtenerUsuarioEmpresa(username);


        }

        @Override
        protected void onPostExecute(UsuarioEmpresaModel usuarioEmpresa) {
            if (usuarioEmpresa != null) {

                //Toast.makeText(LoginActivity.this, "Usuario: "+ usuarioEmpresa.getPredeterminada(), Toast.LENGTH_SHORT).show();

                DatosGlobalesSingleton.getInstance().setUsuarioEmpresaModel(usuarioEmpresa);
                codigoempresa = usuarioEmpresa.getPredeterminada();
                new ObtenerEmpresaTask().execute(codigoempresa);



            } else {

                Toast.makeText(LoginActivity.this, "No se encontraron datos para el usuario.", Toast.LENGTH_LONG).show();

            }
        }
    }


    //obtener los datos de la empresa del usuario
    private class ObtenerEmpresaTask extends AsyncTask<Integer, Void, EmpresaModel> {

        @Override
        protected EmpresaModel doInBackground(Integer... params) {
            // Obtener el ID de la empresa del primer parámetro
            int empresaId = params[0];

            // Crear una instancia del servicio para obtener los datos de la empresa
            EmpresaService empresaService = new EmpresaService();
            return empresaService.obtenerEmpresa(empresaId);
        }

        @Override
        protected void onPostExecute(EmpresaModel empresa) {
            if (empresa != null) {
                //Toast.makeText(LoginActivity.this, "No se encontró la empresa."+empresa.getNombre(), Toast.LENGTH_SHORT).show();

                empresaEditext.setText(empresa.getNombre());
                DatosGlobalesSingleton.getInstance().setEmpresaModel(empresa);

            } else {
                // Manejar el caso donde no se encontró la empresa o hubo un error
                Toast.makeText(LoginActivity.this, "No se encontró la empresa.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //funcion para el login
    public class AuthTask extends AsyncTask<Void, Void, LoginResponseModel> {

        //inicio el progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Mostrar el ProgressBar antes de iniciar la tarea
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);
            loginButton.setAlpha(0.5f);
        }


        private String nombre;
        private String clave;
        private int predeterminada;
        private AuthService authService;

        public AuthTask(String nombre, String clave, int predeterminada) {
            this.nombre = nombre;
            this.clave = clave;
            this.predeterminada = predeterminada;
            this.authService = new AuthService();
        }

        @Override
        protected LoginResponseModel doInBackground(Void... voids) {
            return authService.authenticate(nombre, clave, predeterminada);
        }

        @Override
        protected void onPostExecute(LoginResponseModel response) {
            super.onPostExecute(response);
            // Ocultar el ProgressBar después de que la tarea se complete
            progressBar.setVisibility(View.GONE);
            //activar el boton
            loginButton.setEnabled(true);
            loginButton.setAlpha(1.0f);

            if (response != null) {
                // Manejar la respuesta aquí
                //System.out.println("Token: " + response.getToken());
                //System.out.println("Expiration: " + response.getExpiration());
                // Puedes usar el token para autenticar otras solicitudes o almacenar el token
                //Toast.makeText(LoginActivity.this, "No se encontró la empresa.", Toast.LENGTH_SHORT).show();

                //llamar main activity despues que se aiga logueado el usuario
                DatosGlobalesSingleton.getInstance().setLoginResponseModel(response);//almacena los datos

                //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //startActivity(intent);
                //finish();

                Intent intent = new Intent(LoginActivity.this, SucursalBodega.class);
                startActivity(intent);
                finish();


            } else {

                System.out.println("Error en la autenticación.");
                Toast.makeText(LoginActivity.this, "No se logro autenticar el usuario", Toast.LENGTH_SHORT).show();
                loginButton.setEnabled(true);
                loginButton.setAlpha(1.0f);
            }
        }
    }


}
