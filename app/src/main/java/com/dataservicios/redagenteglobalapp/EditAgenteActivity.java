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
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dataservicios.librerias.JSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppController;

/**
 * Created by user on 06/02/2015.
 */

//extends BaseAgenteActivity
public class EditAgenteActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // single product url
    private static final String url_agente_detials = "http://redagentesyglobalnet.com/JsonAgentDetail";
    // single product edit url
    private static final String url_update_agente_detail = "http://redagentesyglobalnet.com/updateJsonAgent";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_AGENTE= "agente";
    private static final String TAG_ID = "id";
    private static final String TAG_TIENDA = "tienda";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_REPRESENTANTE = "representante";
    private static final String TAG_TELEFONO = "telefono";

    Button btn_save_agente;
    EditText edt_tienda;
    EditText edt_direccion;
    EditText edt_representante;
    EditText edt_telefono_detail;

    private AlertDialog.Builder builder;
    private JSONObject params;



    public EditAgenteActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agente_edit);
        //super.onCreateDrawer();
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // EditText Text
        edt_tienda = (EditText) findViewById(R.id.tienda_edt);
        edt_direccion = (EditText) findViewById(R.id.direccion_edt);
        edt_representante = (EditText) findViewById(R.id.representante_edt);
        edt_telefono_detail = (EditText) findViewById(R.id.telefono_edt);


        btn_save_agente = (Button) findViewById(R.id.save_agente_btn);

        btn_save_agente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new AlertDialog.Builder(EditAgenteActivity.this ,2)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("AGENTE")
                        .setMessage("¿Confirmar?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Bundle bundle = getIntent().getExtras();
                                String id_agente = bundle.getString(TAG_ID);

                                pDialog = new ProgressDialog(EditAgenteActivity.this);
                                pDialog.setMessage("Please wait...");
                                pDialog.setCancelable(false);

                                builder = new AlertDialog.Builder(EditAgenteActivity.this,2);

                                params = new JSONObject();
                                try {
                                    params.put("id", id_agente);
                                    params.put(TAG_TIENDA, edt_tienda.getText().toString());
                                    params.put(TAG_DIRECCION, edt_direccion.getText().toString());
                                    params.put(TAG_REPRESENTANTE, edt_representante.getText().toString());
                                    params.put(TAG_TELEFONO, edt_telefono_detail.getText().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                UpdateAgente();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        String id_agente = bundle.getString(TAG_ID);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        cargaDetalleAgente(id_agente);

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
                Intent intent = new Intent(this, AgenteDetailActivity.class);
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


    private void UpdateAgente() {
        showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_update_agente_detail ,params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("DATAAAA", response.toString());
                        //adapter.notifyDataSetChanged();
                        try {

                            int success =  response.getInt("success");

                            if (success == 1) {

                                builder.setMessage("Se guardo Correctamente los Cambios")
                                        .setTitle("AGENTE")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
//                                            Bundle bundle = getIntent().getExtras();
//                                            String id_agente = bundle.getString(TAG_ID);
//
//                                            // getting values from selected ListItem
//                                            String aid = id_agente;
//                                            // Starting new intent
//                                            Intent i = new Intent( EditAgenteActivity.this , AgenteDetailActivity.class);
//                                            Bundle bolsa = new Bundle();
//                                            bolsa.putString(TAG_ID, aid);
//                                            i.putExtras(bolsa);
//                                            startActivity(i);
//                                            finish();

                                                onBackPressed();
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


    private void cargaDetalleAgente(String agente_id) {
        showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET , url_agente_detials+"?id="+agente_id ,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("DATAAAA", response.toString());
                        //adapter.notifyDataSetChanged();
                        try {
                            JSONObject agente;
                            //String agente = response.getString("agentes");
                            int success =  response.getInt(TAG_SUCCESS);
                            if (success == 1) {
                                JSONArray agentesObjJson;
                                agentesObjJson = response.getJSONArray(TAG_AGENTE);
                                JSONObject obj = agentesObjJson.getJSONObject(0);
                                // Storing each json item in variable
                                edt_tienda.setText(obj.getString(TAG_TIENDA));
                                edt_direccion.setText(obj.getString(TAG_DIRECCION));
                                edt_representante.setText(obj.getString("ruc"));
                                edt_telefono_detail.setText(obj.getString("telefono"));
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


    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

        //a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        Bundle argPDV = new Bundle();
//        argPDV.putInt("idPDV", idPDV );
//        argPDV.putInt("idRuta", idRuta );
//        argPDV.putString("fechaRuta",fechaRuta);
//        Intent a = new Intent(this,DetallePdv.class);
//        a.putExtras(argPDV);
//
//        startActivity(a);

    }

}
