package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.SQLite.DatabaseHelper;
import com.dataservicios.librerias.ConexionInternet;
import com.dataservicios.librerias.GlobalConstant;
import com.dataservicios.librerias.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.Pedido;
import model.Producto;
import model.Publicidad;
import model.PublicidadDetalle;
import model.TipoReclamo;


public class MainActivity extends Activity {
    private int splashTime = 3000;
    private Thread thread;
    private ProgressBar mSpinner;
    private TextView tvCargando ;
    private ConexionInternet cnInternet ;
    private Activity MyActivity;
    private JSONParser jsonParser;
    // Database Helper
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyActivity = (Activity) this;
        mSpinner = (ProgressBar) findViewById(R.id.Splash_ProgressBar);
        mSpinner.setIndeterminate(true);
        tvCargando = (TextView) findViewById(R.id.tvCargando);
        cnInternet=new ConexionInternet(MyActivity);
        db = new DatabaseHelper(getApplicationContext());
        if (cnInternet.isOnline()){
            if (checkDataBase(MyActivity)){
                CargarLogin();
            }else{
                db.deleteAllPedido();
                db.deleteAllPublicidad();
                db.deleteAllPublicidadDetalle();


                new cargaTipoPedido().execute();
            }

        }else  {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Conexion a internet?");
            alertDialogBuilder
                    .setMessage("No se encontro conexion a Internet!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }

    /**
     * Carga el activity del login
     * **/
    private void CargarLogin()
    {
        Intent intent = new Intent("com.dataservicios.redagenteglobalapp.LOGIN");
        startActivity(intent);
        finish();
    }


    class cargaTipoPedido extends AsyncTask<String, String, String> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            tvCargando.setText("Cargando Modulo de tipo pedidos...");
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            cargaTipoPedido();

//            Intent intent = new Intent("com.dataservicios.redagenteglobalapp.LOGIN");
//            startActivity(intent);
//            finish();
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            new cargaPublicidad().execute();
            if (file_url != null){
                Toast.makeText(MyActivity, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

    class cargaPublicidad extends AsyncTask<String, String, String> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            tvCargando.setText("Cargando Modulo de tipo Publicidad...");
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            cargaPublicidad();

//            Intent intent = new Intent("com.dataservicios.redagenteglobalapp.LOGIN");
//            startActivity(intent);
//            finish();
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            new cargaPublicidadDetalle().execute();
            if (file_url != null){
                Toast.makeText(MyActivity, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

    class cargaPublicidadDetalle extends AsyncTask<String, String, String> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            tvCargando.setText("Cargando Modulo Detalles de Publicidad...");
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            cargaPublicidadDetalle();

//            Intent intent = new Intent("com.dataservicios.redagenteglobalapp.LOGIN");
//            startActivity(intent);
//            finish();
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            new cargaTipoReclamo().execute();
            if (file_url != null){
                Toast.makeText(MyActivity, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }


    class cargaTipoReclamo extends AsyncTask<String, String, String> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            tvCargando.setText("Cargando Modulo de Reclamos ...");
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            cargaTipoReclamo();

//            Intent intent = new Intent("com.dataservicios.redagenteglobalapp.LOGIN");
//            startActivity(intent);
//            finish();
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            CargarLogin();
            if (file_url != null){
                Toast.makeText(MyActivity, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
    public void cargaTipoPedido() {
        int success;
        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idcel", "1"));
            JSONParser jsonParser = new JSONParser();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest("http://redagentesyglobalnet.com/JsonTipoPedido" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            success = json.getInt("success");
            if (success == 1) {
                JSONArray ObjJson;
                ObjJson = json.getJSONArray("type_orders");

                if(ObjJson.length() > 0) {
                    for (int i = 0; i < ObjJson.length(); i++) {
                        try {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            Pedido pedido = new Pedido();
                            pedido.setId(Integer.valueOf(obj.getString("id")));
                            pedido.setNombre(obj.getString("fullname"));
                            db.createPedido(pedido);
                            //pedido.setDescripcion(obj.getString("descripcion"));
                            // adding movie to movies array
                            // tipoPedidoList.add(pedido);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //poblandoSpinnerTipoPedido();
                    Log.d("Tag Count", "Tag Count: " + db.getAllPedidos());
                }
            }else{
                Log.d("Login Failure!", json.getString("message"));
                // return json.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void cargaPublicidad() {
        int success;
        try {
            //tvCargando.setText("Cargando Modulo de Productos");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idcel", "1"));
            JSONParser jsonParser = new JSONParser();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest("http://redagentesyglobalnet.com/JsonPublicitiesTotal" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            success = json.getInt("success");
            if (success == 1) {
                JSONArray ObjJson;
                ObjJson = json.getJSONArray("publicities");
                if(ObjJson.length() > 0) {
                    for (int i = 0; i < ObjJson.length(); i++) {
                        try {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            Publicidad publicidad = new Publicidad();
                            publicidad.setId(Integer.valueOf(obj.getString("id")));
                            publicidad.setIdPedido(Integer.valueOf(obj.getString("type_orders_id")));
                            publicidad.setNombre(obj.getString("fullname"));
                            // adding movie to movies array
                            // tipoPedidoList.add(pedido);
                            db.createPublicidad(publicidad);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //poblandoSpinnerTipoPedido();
                }
//                Intent intent = new Intent("com.dataservicios.redagenteglobalapp.LOGIN");
//                startActivity(intent);
//                finish();
                //return json.getString("message");
            }else{
                Log.d("Login Failure!", json.getString("message"));
                // return json.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void cargaPublicidadDetalle() {
        int success;
        try {
            //tvCargando.setText("Cargando Modulo de Productos");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idcel", "1"));
            JSONParser jsonParser = new JSONParser();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest("http://redagentesyglobalnet.com/JsonPublicitiesDetailTotal" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            success = json.getInt("success");
            if (success == 1) {
                JSONArray ObjJson;
                ObjJson = json.getJSONArray("publicities_details");
                if(ObjJson.length() > 0) {
                    for (int i = 0; i < ObjJson.length(); i++) {
                        try {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            PublicidadDetalle publicidadDetalle = new PublicidadDetalle();
                            publicidadDetalle.setId(Integer.valueOf(obj.getString("id")));
                            publicidadDetalle.setIdPublicidad(Integer.valueOf(obj.getString("publicities_id")));
                            publicidadDetalle.setNombre(obj.getString("fullname"));
                            // adding movie to movies array
                            // tipoPedidoList.add(pedido);
                            db.createPublicidadDetalle(publicidadDetalle);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //poblandoSpinnerTipoPedido();
                    Log.d("getAllPublicidadDetalle", "Tag Count: getAllPublicidadDetalle :" + db.getAllPublicidadDetalle().size());
                }
//                Intent intent = new Intent("com.dataservicios.redagenteglobalapp.LOGIN");
//                startActivity(intent);
//                finish();
                //return json.getString("message");
            }else{
                Log.d("Login Failure!", json.getString("message"));
                // return json.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void cargaTipoReclamo() {
        int success;
        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idcel", "1"));
            JSONParser jsonParser = new JSONParser();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest("http://redagentesyglobalnet.com/JsonTipoReclamo" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            success = json.getInt("success");
            if (success == 1) {
                JSONArray ObjJson;
                ObjJson = json.getJSONArray("type_claims");

                if(ObjJson.length() > 0) {
                    for (int i = 0; i < ObjJson.length(); i++) {
                        try {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            TipoReclamo tipoReclamo = new TipoReclamo();
                            tipoReclamo.setId(Integer.valueOf(obj.getString("id")));
                            tipoReclamo.setNombre(obj.getString("fullname"));
                            db.createTipoReclamo(tipoReclamo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }else{
                Log.d("Login Failure!", json.getString("message"));
                // return json.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void createPedido(){
        db = new DatabaseHelper(getApplicationContext());
        // Creating tags
        Pedido pedido1 = new Pedido();
        pedido1.setId(1);
        pedido1.setNombre("Externo");
        Pedido pedido2 = new Pedido();
        pedido1.setId(2);
        pedido1.setNombre("Externo");
        // Inserting tags in db
        long tag1_id = db.createPedido(pedido1);
        long tag2_id = db.createPedido(pedido2);

        Log.d("Tag Count", "Tag Count: " + db.getAllPedidos().size());
    }

    private boolean checkDataBase(Context context) {
        SQLiteDatabase checkDB = null;
        try {
            File database=context.getDatabasePath(GlobalConstant.DATABASE_NAME);
            if (database.exists()) {
                Log.i("Database", "Found");
                String myPath = database.getAbsolutePath();
                Log.i("Database Path", myPath);
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
                //return true;
            } else {
                // Database does not exist so copy it from assets here
                Log.i("Database", "Not Found");
                //return false;
            }
        } catch(SQLiteException e) {
            Log.i("Database", "Not Found");
        } finally {
            if(checkDB != null) {
                checkDB.close();
            }
        }
        return checkDB != null ? true : false;
    }

}
