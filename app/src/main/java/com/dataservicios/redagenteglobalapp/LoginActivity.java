package com.dataservicios.redagenteglobalapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dataservicios.SQLite.DatabaseHelper;
import com.dataservicios.librerias.GPSTracker;
import com.dataservicios.librerias.GlobalConstant;
import com.dataservicios.librerias.GlobalMessage;
import com.dataservicios.librerias.JSONParser;
import com.dataservicios.librerias.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.User;

/**
 * Created by usuario on 05/11/2014.
 */
public class LoginActivity extends ActionBarActivity  implements View.OnClickListener {
    Button ingresar;
    Button llamar;
    Button ubicar;
    EditText usuario,contrasena;
    String IdDevice;
    // Database Helper
    private DatabaseHelper db;

    // Progress Dialog
    private ProgressDialog pDialog;

    SessionManager session;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ingresar = (Button) findViewById(R.id.btIngresar);
        llamar = (Button) findViewById(R.id.btLlamar);
        ubicar = (Button) findViewById(R.id.btUbicar);
        usuario =   (EditText) findViewById(R.id.etUsuario);
        contrasena = (EditText) findViewById(R.id.etContrasena);
        ingresar.setOnClickListener(this);
        llamar.setOnClickListener(this);
        ubicar.setOnClickListener(this);

        db = new DatabaseHelper(getApplicationContext());
        //Obteniendo el ID device
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IdDevice=telephonyManager.getDeviceId();
        if(db.getUserCount() > 0) {
            User users = new User();
            users = db.getUser(1);
            usuario.setText(users.getNombre());
            contrasena.setText(users.getPassword());
        }


        //Obteniedo user y pasword de la base de datos local si ya se ha logeado ateriormente


        // Session Manager
        session = new SessionManager(getApplicationContext());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btIngresar:
                //String nombre = usuario.getText().toString();

//                if (usuario.getText().toString().equals("jaime") && contrasena.getText().toString().equals("jaime"))
//                {
//                    Intent intent = new Intent("com.dataservicios.redagenteglobalapp.LISTGENTE");
//                    Bundle bolsa = new Bundle();
//                    bolsa.putString("NOMBRE", nombre);
//                    intent.putExtras(bolsa);
//                    startActivity(intent);
//                } else {
//                    Toast toast = Toast.makeText(this , "La contraseña o usario es incorrecto", Toast.LENGTH_SHORT );
//                    toast.show();
//                }
//
                if (usuario.getText().toString().trim().equals("") )
                {
                    Toast toast = Toast.makeText(this , "Ingrese un Usuario ", Toast.LENGTH_SHORT );
                    toast.show();
                    usuario.requestFocus();
                }else if (contrasena.getText().toString().trim().equals("")) {
                    Toast toast = Toast.makeText(this , "Ingrese una Contraseña ", Toast.LENGTH_SHORT );
                    toast.show();
                    contrasena.requestFocus();
                }else {
                    new AttemptLogin().execute();

                }
                break;
            case R.id.btLlamar:
                makeCall("948337893");
                break;
            case R.id.btUbicar:
//                GPSTracker gps = new GPSTracker(this);
//                double latitude = gps.getLatitude();
//                double longitude = gps.getLongitude();
//                String url = String.format("geo:%f, %f", latitude, longitude);
//                Intent i = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse(url));
//                startActivity(i);

                Intent intent = new Intent("com.dataservicios.redagenteglobalapp.UBICACION");
                startActivity(intent);
                //finish();
                break;

        }
    }

    protected void makeCall(String telefono) {
        Log.i("Make call", "");
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
        phoneIntent.setData(Uri.parse("tel:" + telefono));
        startActivity(phoneIntent);
    }
    class AttemptLogin extends AsyncTask<String, String, String> {

        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Iniciando Sesión...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Comprobando si es exito
            int success;
            String id_user;

            String username = usuario.getText().toString();
            String password = contrasena.getText().toString();
            try {
                // Construyendo los parametros
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("iddevice", IdDevice));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.LOGIN_URL ,"POST", params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success, tag que retorna el json
                success = json.getInt("success");
                id_user = String.valueOf(json.getInt("id")) ;

                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    Intent i = new Intent(LoginActivity.this, PanelAdmin.class);

                    db.deleteAllUser();
                    User users = new User();
                    users.setId(1);
                    users.setNombre( usuario.getText().toString());
                    users.setPassword(contrasena.getText().toString());
                    db.createUser(users);
                    //Enviando los datos usando Bundle a otro activity
                    session.createLoginSession("Sin Nombre",id_user, username);
                    Bundle bolsa = new Bundle();
                    bolsa.putString("NOMBRE", username);
                    i.putExtras(bolsa);
                    finish();
                    startActivity(i);
                    return json.getString("message");
                }else{
                    Log.d("Login Failure!", json.getString("message"));
                    return json.getString("message");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }
}