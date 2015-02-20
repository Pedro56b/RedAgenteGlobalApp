package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dataservicios.librerias.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.PedidoAdapter;
import app.AppController;
import model.Pedido;

/**
 * Created by usuario on 06/02/2015 By Data International Derechos Reservados 2015.-20-02-2015
 */
public class ListaPedido extends Activity {

    private Activity MyActivity;
    private SessionManager session;
    private String code_user, id_user, name_user;

    private int idAgente;

    private ProgressDialog pDialog;
    private List<Pedido> pedidosList = new ArrayList<Pedido>();
    private ListView listView;
    private PedidoAdapter adapter;
    private JSONObject params;
    private Button btNuevo ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_pedido);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("Lista de Pedidos");


        MyActivity = (Activity) this;
        session = new SessionManager(MyActivity);
        // obtiene datos desde la sesión
        HashMap<String, String> user = session.getUserDetails();
        // name
        name_user = user.get(SessionManager.KEY_NAME);
        // email
        code_user = user.get(SessionManager.KEY_USER);
        // id
        id_user = user.get(SessionManager.KEY_ID_USER);

        Bundle bundle = getIntent().getExtras();
        idAgente = Integer.valueOf(bundle.getString("id")) ;

        //Ventana de de carga----------------------
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);




        params = new JSONObject();
        try {
            params.put("agent_id", idAgente);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btNuevo =(Button)findViewById(R.id.btNuevo);
        listView = (ListView) findViewById(R.id.list);
        adapter = new PedidoAdapter(this, pedidosList);

        listView.setAdapter(adapter);
        cargaListaPedidos();

        btNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getIntent().getExtras();
                String id_agente = bundle.getString("id");
                // getting values from selected ListItem
                String aid = id_agente;
                Intent intent = new Intent("com.dataservicios.redagenteglobalapp.PEDIDOS");
                Bundle bolsa = new Bundle();
                bolsa.putString("id", aid);
                intent.putExtras(bolsa);
                startActivity(intent);
            }
        });


    }

    private void cargaListaPedidos(){
        showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , "http://redagentesyglobalnet.com/JsonOrdersList" ,params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("DATAAAA", response.toString());
                        //adapter.notifyDataSetChanged();
                        try {
                            //String agente = response.getString("agentes");
                            int success =  response.getInt("success");
                            if (success == 1) {
//
                                JSONArray ObjJson;
                                ObjJson = response.getJSONArray("orders");
                                // looping through All Products
                                if(ObjJson.length() > 0) {
                                    for (int i = 0; i < ObjJson.length(); i++) {
                                        try {
                                            JSONObject obj = ObjJson.getJSONObject(i);
                                            Pedido pedido = new Pedido();
                                            pedido.setId(Integer.valueOf(obj.getString("id")));

                                            pedido.setTipo(obj.getString("tipo"));
                                            pedido.setDescripcion(obj.getString("comment"));
                                            pedido.setNombre(obj.getString("content"));
                                            pedido.setPedido(obj.getString("Pedido"));
                                            pedido.setPublicidado(obj.getString("Publicidad"));
                                            pedido.setEstado(obj.getString("estado"));
                                            pedido.setPublicidadDetalle(obj.getString("Publicidad_detalle"));
                                            pedido.setFecha(obj.getString("created_at"));

                                            // adding movie to movies array
                                            pedidosList.add(pedido);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidepDialog();
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
}
