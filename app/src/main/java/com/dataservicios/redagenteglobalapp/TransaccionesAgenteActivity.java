package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.widget.ListViewCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ListView;

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

import adapter.TransaccionesAdapter;
import app.AppController;
import model.Transacciones;

/**
 * Created by user on 08/02/2015.
 */
public class TransaccionesAgenteActivity extends Activity {
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    // Progress Dialog
    private ProgressDialog pDialog;
    //ArrayList<HashMap<String, String>> agentesList;

    private JSONObject params;
    private List<Transacciones> transaccioneslist = new ArrayList<Transacciones>();
    private TransaccionesAdapter adapter;

    // JSON Node names
    private static final String TAG_ID = "id";
    private static String url_transacciones_agente = "http://192.168.1.43:8080/RedAgenteGlobalApp/get_transacciones.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_trans_pos);
        //super.onCreateDrawer();
        getActionBar().setDisplayHomeAsUpEnabled(true);

        params = new JSONObject();

        Bundle bundle = getIntent().getExtras();
        String id_agente = bundle.getString(TAG_ID);
        try {
            params.put("id_agente", id_agente);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get listview
        ListView listView = (ListView) findViewById(R.id.list_items);

        adapter = new TransaccionesAdapter(this, transaccioneslist);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        cargaTransacciones();
        listView.setAdapter(adapter);

    }


    private void cargaTransacciones() {
        showpDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_transacciones_agente ,params,
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
                                agentesObjJson = response.getJSONArray("transacciones");
                                for (int i = 0; i < agentesObjJson.length(); i++) {
                                    JSONObject obj = agentesObjJson.getJSONObject(i);
                                    // Storing each json item in variable
                                    Transacciones transacciones = new Transacciones();
                                    transacciones.setId(obj.getString("id"));
                                    transacciones.setdesc_mes_1(obj.getString("desc_mes_1"));
                                    transacciones.setdesc_mes_2(obj.getString("desc_mes_2"));
                                    transacciones.setdesc_mes_3(obj.getString("desc_mes_3"));
                                    transacciones.setdesc_mes_4(obj.getString("desc_mes_4"));
                                    transacciones.setdesc_mes_5(obj.getString("desc_mes_5"));
                                    transacciones.setdesc_mes_6(obj.getString("desc_mes_6"));
                                    transacciones.settrxs_mes_1(Integer.valueOf(obj.getString("trxs_mes_1")));
                                    transacciones.settrxs_mes_2(Integer.valueOf(obj.getString("trxs_mes_2")));
                                    transacciones.settrxs_mes_3(Integer.valueOf(obj.getString("trxs_mes_3")));
                                    transacciones.settrxs_mes_4(Integer.valueOf(obj.getString("trxs_mes_4")));
                                    transacciones.settrxs_mes_5(Integer.valueOf(obj.getString("trxs_mes_5")));
                                    transacciones.settrxs_mes_6(Integer.valueOf(obj.getString("trxs_mes_6")));
                                    transacciones.settrxs_lun(Integer.valueOf(obj.getString("trxs_lun")));
                                    transacciones.settrxs_mar(Integer.valueOf(obj.getString("trxs_mar")));
                                    transacciones.settrxs_mie(Integer.valueOf(obj.getString("trxs_mie")));
                                    transacciones.settrxs_jue(Integer.valueOf(obj.getString("trxs_jue")));
                                    transacciones.settrxs_vie(Integer.valueOf(obj.getString("trxs_vie")));
                                    transacciones.settrxs_sab(Integer.valueOf(obj.getString("trxs_sab")));
                                    transacciones.settrxs_dom(Integer.valueOf(obj.getString("trxs_dom")));
                                    transaccioneslist.add(transacciones);
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
