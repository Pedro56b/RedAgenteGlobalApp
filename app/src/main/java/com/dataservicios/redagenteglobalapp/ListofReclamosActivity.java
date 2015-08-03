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
import com.dataservicios.librerias.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import adapter.ReclamosAdapter;
import app.AppController;
import model.Reclamos;

/**
 * Created by user on 08/02/2015.
 */

//extends BaseAgenteActivity

public class ListofReclamosActivity extends Activity {
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    // Progress Dialog
    private ProgressDialog pDialog;
    //ArrayList<HashMap<String, String>> agentesList;


    private List<Reclamos> reclamosList = new ArrayList<Reclamos>();
    private ListView listView;
    private ReclamosAdapter adapter;

    private JSONObject params;

    // JSON Node names
    private static final String TAG_ID = "id";
    private static String url_all_reclamos = "http://redagentesyglobalnet.com/JsonClaimsList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_reclamos);
        //super.onCreateDrawer();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //here is your list array
        params = new JSONObject();

        Bundle bundle = getIntent().getExtras();
        String id_agente = bundle.getString(TAG_ID);
        try {
            params.put("agent_id", id_agente);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Get listview
        ListView listView = (ListView) findViewById(R.id.list_items);
        // Get Button
        Button btn_new_reclamo = (Button) findViewById(R.id.btn_nuevoreclamo);
        adapter = new ReclamosAdapter(this, reclamosList);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        cargaReclamos();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String aid = ((TextView) view.findViewById(R.id.id)).getText().toString();
            }
        });
        listView.setAdapter(adapter);

        btn_new_reclamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getIntent().getExtras();
                String id_agente = bundle.getString(TAG_ID);
                Intent i = new Intent( ListofReclamosActivity.this , NewReclamoActivity.class);
                Bundle bolsa = new Bundle();
                bolsa.putString("id", id_agente);
                i.putExtras(bolsa);
                startActivity(i);
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



    private void cargaReclamos() {
        showpDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_all_reclamos ,params,
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
                                agentesObjJson = response.getJSONArray("claims");
                                for (int i = 0; i < agentesObjJson.length(); i++) {
                                    JSONObject obj = agentesObjJson.getJSONObject(i);
                                    // Storing each json item in variable
                                    Reclamos reclamos = new Reclamos();
                                    reclamos.setId(Integer.valueOf(obj.getString("id")));
                                    reclamos.setReclamo(obj.getString("content"));
                                    reclamos.setTipo(obj.getString("Tipo"));
                                    reclamos.setDir(obj.getString("dir"));
                                    reclamos.setFecha(obj.getString("created_at"));
                                    reclamos.setEstado(obj.getString("state"));
                                    reclamos.setComentario(obj.getString("comment"));
                                    reclamosList.add(reclamos);
                                }
                                adapter.notifyDataSetChanged();
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
