package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dataservicios.librerias.GPSTracker;
import com.dataservicios.librerias.GlobalConstant;
import com.dataservicios.librerias.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.ContactoAdapter;
import app.AppController;
import model.Contacto;

/**
 * Created by PEDRO QUISPE ALVAREZ on 08/07/2015.
 */
public class ListaContactos extends Activity {
    private Activity MyActivity;
    private SessionManager session;
    private String code_user, id_user, name_user;

    private int idAgente;

    private static String url_get_contactos = "http://redagentesyglobalnet.com/JsonContactsAgent";

    private ProgressDialog pDialog;
    private List<Contacto> contactosList = new ArrayList<Contacto>();
    private ListView listView;
    private ContactoAdapter adapter;
    private JSONObject params;
    private Button btNuevo ;

    private String prueba ; //Nueva variable
    private String Mayprueba ; //Nueva variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_contactos);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("Lista de Contactos");


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
            params.put("id", idAgente);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btNuevo =(Button)findViewById(R.id.btNuevo);
        listView = (ListView) findViewById(R.id.list);
        adapter = new ContactoAdapter(this, contactosList);

        listView.setAdapter(adapter);
        cargarContactos();

        btNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getIntent().getExtras();
                String id_agente = bundle.getString("id");
                // getting values from selected ListItem
                String aid = id_agente;
                Intent intent = new Intent( getBaseContext() , NewContactoActivity.class);
                Bundle bolsa = new Bundle();
                bolsa.putString("id", aid);
                intent.putExtras(bolsa);
                startActivity(intent);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = getIntent().getExtras();
                String id_agente = bundle.getString("id");
                // getting values from selected ListItem
                String aid = id_agente;
                String cid = ((TextView) view.findViewById(R.id.tvId)).getText().toString();
                String celular = ((TextView) view.findViewById(R.id.tvcelular)).getText().toString();
                String contacto = ((TextView) view.findViewById(R.id.tvcontacto)).getText().toString();
                String email = ((TextView) view.findViewById(R.id.tvemail)).getText().toString();
                // Starting new intent
                Intent i = new Intent( getBaseContext(), EditContactoActivity.class);
                Bundle bolsa = new Bundle();
                bolsa.putString("cid", cid);
                bolsa.putString("id", aid);
                bolsa.putString("celular", celular);
                bolsa.putString("contacto", contacto);
                bolsa.putString("email", email);
                //bolsa.putString("status", status);
                i.putExtras(bolsa);
                startActivity(i);
            }
        });




    }

    private void cargarContactos(){
        showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_get_contactos  ,params,
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
                                ObjJson = response.getJSONArray("contactos");
                                // looping through All Products
                                if(ObjJson.length() > 0) {
                                    for (int i = 0; i < ObjJson.length(); i++) {
                                        try {
                                            JSONObject obj = ObjJson.getJSONObject(i);
                                            Contacto contacto = new Contacto();
                                            contacto.setId(obj.getString("id"));
                                            contacto.setContato(obj.getString("contacto"));
                                            contacto.setCelular(obj.getString("celular"));
                                            contacto.setEmail(obj.getString("email"));

                                            // adding movie to movies array
                                            contactosList.add(contacto);
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
