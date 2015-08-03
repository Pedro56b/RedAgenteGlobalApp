package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dataservicios.SQLite.DatabaseHelper;
import com.dataservicios.librerias.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.ReclamosAdapter;
import app.AppController;
import model.PublicidadDetalle;
import model.Reclamos;
import model.TipoReclamo;

/**
 * Created by user on 08/02/2015.
 */

//extends BaseAgenteActivity

public class NewReclamoActivity extends Activity {

    private String array_spinner[], dir_ids[];
    private ProgressDialog pDialog;
    private JSONObject params;
    Spinner spn_tipo, spn_dir;
    Button btn_guardar_reclamo;
    EditText edt_comentario;
    private SessionManager session;
    private String code_user, id_user, name_user;

    private AlertDialog.Builder builder;
    List<TipoReclamo> tipoReclamoList = new ArrayList<TipoReclamo>();
    private DatabaseHelper db;

    private static String url_nuevo_reclamo = "http://redagentesyglobalnet.com/insertJsonClaim";
    private static String url_get_tipo_reclamos = "http://redagentesyglobalnet.com/JsonTipoReclamo";
    private static String url_get_dirs  = "http://redagentesyglobalnet.com/JsonDirsAgent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_reclamo);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //super.onCreateDrawer();
        db = new DatabaseHelper(getApplicationContext());
        session = new SessionManager(this);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        name_user = user.get(SessionManager.KEY_NAME);
        // email
        code_user = user.get(SessionManager.KEY_USER);
        // id
        id_user = user.get(SessionManager.KEY_ID_USER);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        params = new JSONObject();
        try {
            Bundle bundle = getIntent().getExtras();
            String id_agente = bundle.getString("id");
            params.put("agent_id", id_agente);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cargaDirs();
        cargaTipoReclamos();

        //Get Spinner
        spn_tipo = (Spinner) findViewById(R.id.spTipo);
        spn_dir = (Spinner) findViewById(R.id.spDir);

        // Get Button
        btn_guardar_reclamo = (Button) findViewById(R.id.btGuardarReclamo);
        //Get Edittext
        edt_comentario = (EditText) findViewById(R.id.edtcomentario);

        btn_guardar_reclamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(NewReclamoActivity.this ,2)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("AGENTE")
                        .setMessage("¿Confirmar?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                params = new JSONObject();

                                Bundle bundle = getIntent().getExtras();
                                String id_agente = bundle.getString("id");

                                pDialog = new ProgressDialog(NewReclamoActivity.this);
                                pDialog.setMessage("Please wait...");
                                pDialog.setCancelable(false);

                                builder = new AlertDialog.Builder(NewReclamoActivity.this, 2);

                                try {
                                    params.put("agent_id", id_agente);
                                    params.put("id",id_user);
                                    params.put("type_claims_id", tipoReclamoList.get(spn_tipo.getSelectedItemPosition()).getId() );
                                    params.put("dir", spn_dir.getSelectedItem()  );
                                    params.put("comentario", edt_comentario.getText());

                                    Log.d("params", params.toString());


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                guardarReclamo();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });





    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = getIntent().getExtras();
        String id_agente = bundle.getString("id");

        // getting values from selected ListItem
        String aid = id_agente;
        switch (item.getItemId()) {
            case android.R.id.home:
                // go to previous screen when app icon in action bar is clicked
                Intent intent = new Intent(this, ListofReclamosActivity.class);
                Bundle bolsa = new Bundle();
                bolsa.putString("id", aid);
                intent.putExtras(bolsa);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void guardarReclamo() {
        showpDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_nuevo_reclamo , params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("DATAAAA", response.toString());
                        try {
                            int success =  response.getInt("success");
                            if (success == 1) {
                                builder.setMessage("Se guardo Correctamente el Reclamo")
                                        .setTitle("AGENTE")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Bundle bundle = getIntent().getExtras();
                                                String id_agente = bundle.getString("id");

                                                // getting values from selected ListItem
                                                String aid = id_agente;
                                                // Starting new intent
                                                Intent i = new Intent( NewReclamoActivity.this , ListofReclamosActivity.class);
                                                Bundle bolsa = new Bundle();
                                                bolsa.putString("id", aid);
                                                i.putExtras(bolsa);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                                // 3. Get the AlertDialog from create()
                                AlertDialog dialog = builder.create();
                                // 4. Display the AlertDialog
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidePDialog();
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }




    private void cargaTipoReclamos() {

        tipoReclamoList = db.getAllTipoReclamo();

        Spinner spn_tipo = (Spinner) findViewById(R.id.spTipo);

        //spn_tipo.setAdapter(null);
        List<String> lables = new ArrayList<String>();
        //lables.add(tipoPedidoList.get(0).getTipo());
        for (int i = 0; i < tipoReclamoList.size(); i++) {
            lables.add(tipoReclamoList.get(i).getNombre());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spn_tipo.setAdapter(spinnerAdapter);

//        showpDialog();
//
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_get_tipo_reclamos , null,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response)
//                    {
//                        Log.d("DATAAAA", response.toString());
//                        try {
//                            int success =  response.getInt("success");
//                            if (success == 1) {
//                                JSONArray agentesObjJson;
//                                agentesObjJson = response.getJSONArray("type_claims");
//                                array_spinner=new String[agentesObjJson.length()];
//                                tipo_ids =new String[agentesObjJson.length()];
//                                for (int i = 0; i < agentesObjJson.length(); i++) {
//                                    JSONObject obj = agentesObjJson.getJSONObject(i);
//                                    // Storing each json item in variable
//                                    array_spinner[i]= obj.getString("fullname") ;
//                                    tipo_ids[i] = obj.getString("id");
//                                }
//
//                                Spinner s = (Spinner) findViewById(R.id.spTipo);
//                                ArrayAdapter adapter = new ArrayAdapter(NewReclamoActivity.this,
//                                        android.R.layout.simple_spinner_item, array_spinner);
//                                s.setAdapter(adapter);
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        hidePDialog();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
//                        hidePDialog();
//                    }
//                }
//        );
//
//        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }


    private void cargaDirs() {

        showpDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_get_dirs , params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("DATAAAA", response.toString());
                        try {
                            int success =  response.getInt("success");
                            if (success == 1) {
                                JSONArray agentesObjJson;
                                agentesObjJson = response.getJSONArray("dirs");
                                array_spinner=new String[agentesObjJson.length()];
                                dir_ids =new String[agentesObjJson.length()];
                                for (int i = 0; i < agentesObjJson.length(); i++) {
                                    JSONObject obj = agentesObjJson.getJSONObject(i);
                                    // Storing each json item in variable
                                    array_spinner[i]= obj.getString("dir") ;
                                    dir_ids[i] = obj.getString("dir");
                                }

                                Spinner s = (Spinner) findViewById(R.id.spDir);
                                ArrayAdapter adapter = new ArrayAdapter(NewReclamoActivity.this,
                                        android.R.layout.simple_spinner_item, array_spinner);
                                s.setAdapter(adapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidePDialog();
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidePDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
