package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dataservicios.SQLite.DatabaseHelper;
import com.dataservicios.librerias.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.PedidoAdapter;
import app.AppController;
import model.Pedido;
import model.Producto;
import model.Publicidad;
import model.PublicidadDetalle;

/**
 * Created by usuario on 06/02/2015.
 */
public class Padidos extends Activity {

    private Activity MyActivity;
    private SessionManager session;
    private String code_user, id_user, name_user;
    private String array_spinner_tipo[];
    private Spinner spTipo, spPrducto, spDir;
    private String array_spinner[], dir_ids[];


    private ProgressDialog pDialog;
    private JSONObject params;

    private List<Pedido> tipoPedidoList = new ArrayList<Pedido>();
    private List<Publicidad> tipoProductoList = new ArrayList<Publicidad>();
    private List<PublicidadDetalle> tipoPEList = new ArrayList<PublicidadDetalle>();
    private Button btEnviar;
    private RadioGroup rgTipo ;
    private RadioButton rbNuevo, rbRenovacion;
    private EditText etComent;
    private int idTipo, idProducto, idPE ,idAgente;
    private String valor , comentario;


    private static String url_get_dirs  = "http://redagentesyglobalnet.com/JsonDirsAgent";

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        db = new DatabaseHelper(getApplicationContext());

        MyActivity = (Activity) this;

        MyActivity.getActionBar().setTitle("Pedidos");
        session = new SessionManager(MyActivity);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        name_user = user.get(SessionManager.KEY_NAME);
        // email
        code_user = user.get(SessionManager.KEY_USER);
        // id
        id_user = user.get(SessionManager.KEY_ID_USER);


        Bundle bundle = getIntent().getExtras();
        idAgente = Integer.valueOf(bundle.getString("id")) ;

        spTipo = (Spinner) findViewById(R.id.spTipo);
        spDir = (Spinner) findViewById(R.id.spDir);
        spPrducto = (Spinner) findViewById(R.id.spProducto);
        btEnviar = (Button) findViewById(R.id.btEnviar);
        rgTipo = (RadioGroup) findViewById(R.id.rgTipo);
        rbNuevo = (RadioButton)findViewById(R.id.rbNuevo);
        rbRenovacion = (RadioButton)findViewById(R.id.rbRenovacion);
        etComent = (EditText) findViewById(R.id.etComentario);


        comentario = etComent.getText().toString();

//        spPrducto.setEnabled(true);
//        spPrducto.setAlpha(0.4f);
//        spPrducto.setClickable(false);

//        spPE.setEnabled(true);
//        spPE.setAlpha(0.4f);
//        spPE.setClickable(false);


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
        cargaDirs();



        params = new JSONObject();
        try {
            params.put("id", id_user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();
                int b=tipoPedidoList.get(position).getId();
                idTipo = b;
                String d=String.valueOf(b);
                // Showing selected spinner item
                //Toast.makeText(parent.getContext(), "Seleciono: " + label + " ID: " + d,Toast.LENGTH_LONG).show();
                enabledControl();
                JSONObject paramsTipo = new JSONObject();
                try {
                    //paramsTipo.put("id", id_user);
                    paramsTipo.put("id", d);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    cargaProducto(paramsTipo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPrducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();
                int b=tipoProductoList.get(position).getId();
                idProducto = b;
                String d=String.valueOf(b);
                // Showing selected spinner item
                //Toast.makeText(parent.getContext(), "You selected: " + label + " ID: " + d,Toast.LENGTH_LONG).show();
                enabledControl();
                JSONObject paramsTipo = new JSONObject();
                try {
                    //paramsTipo.put("id", id_user);
                    paramsTipo.put("id", d);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPedido();
            }
        });

        cargaTipoPedido();


    }


    private void cargaTipoPedido(){

        //showpDialog();
        //List<Pedido> tipoPedidoLista = new ArrayList<Pedido>();
        tipoPedidoList=db.getAllPedidos();
        poblandoSpinnerTipoPedido();


//        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , "http://redagentesyglobalnet.com/JsonTipoPedido" ,params,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response)
//                    {
//                        Log.d("DATAAAA", response.toString());
//                        //adapter.notifyDataSetChanged();
//                        try {
//                            //String agente = response.getString("agentes");
//                            int success =  response.getInt("success");
//                            if (success == 1) {
////
//                                JSONArray ObjJson;
//                                ObjJson = response.getJSONArray("type_orders");
//                                if(ObjJson.length() > 0) {
//                                    for (int i = 0; i < ObjJson.length(); i++) {
//                                        try {
//                                            JSONObject obj = ObjJson.getJSONObject(i);
//                                            Pedido pedido = new Pedido();
//                                            pedido.setId(Integer.valueOf(obj.getString("id")));
//                                            pedido.setTipo(obj.getString("fullname"));
//                                            //pedido.setDescripcion(obj.getString("descripcion"));
//                                            // adding movie to movies array
//                                            tipoPedidoList.add(pedido);
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                    poblandoSpinnerTipoPedido();
//                                }
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        hidepDialog();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
//                        hidepDialog();
//                    }
//                }
//        );
//
//        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }


    private void cargaProducto(JSONObject paramsTipo) throws JSONException {

           // showpDialog();
            tipoPedidoList=db.getAllPedidos();
            tipoProductoList=db.getAllPublicidadTipo(paramsTipo.getInt("id"));
            poblandoSpinnerProducto();
//            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , "http://redagentesyglobalnet.com/JsonPublicities" , paramsTipo,
//                    new Response.Listener<JSONObject>()
//                    {
//                        @Override
//                        public void onResponse(JSONObject response)
//                        {
//                            Log.d("DATAAAA", response.toString());
//                            //adapter.notifyDataSetChanged();
//                            try {
//                                //String agente = response.getString("agentes");
//                                int success =  response.getInt("success");
//                                if (success == 1) {
//                                    JSONArray ObjJson;
//                                    ObjJson = response.getJSONArray("publicities");
//                                    tipoProductoList.clear();
//                                    if(ObjJson.length() > 0) {
//                                        for (int i = 0; i < ObjJson.length(); i++) {
//                                            try {
//                                                JSONObject obj = ObjJson.getJSONObject(i);
//                                                Producto producto = new Producto();
//                                                producto.setId(Integer.valueOf(obj.getString("id")));
//                                                producto.setNombre(obj.getString("fullname"));
//                                                //pedido.setDescripcion(obj.getString("descripcion"));
//                                                // adding movie to movies array
//                                                tipoProductoList.add(producto);
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                        poblandoSpinnerProducto();
//                                    }
//
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            hidepDialog();
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            //VolleyLog.d(TAG, "Error: " + error.getMessage());
//                            hidepDialog();
//                        }
//                    }
//            );
//
//            AppController.getInstance().addToRequestQueue(jsObjRequest);


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
                                ArrayAdapter adapter = new ArrayAdapter(Padidos.this,
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



    //Lllenado spiner tipo pedido
    private void poblandoSpinnerTipoPedido() {
        List<String> lables = new ArrayList<String>();
        //lables.add(tipoPedidoList.get(0).getTipo());
        for (int i = 0; i < tipoPedidoList.size(); i++) {
            lables.add(tipoPedidoList.get(i).getNombre());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spTipo.setAdapter(spinnerAdapter);
    }

    //Lllenado spiner tipo Producto
    private void poblandoSpinnerProducto() {

        spPrducto.setAdapter(null);
        List<String> lables = new ArrayList<String>();
        //lables.add(tipoPedidoList.get(0).getTipo());
        for (int i = 0; i < tipoProductoList.size(); i++) {
            lables.add(tipoProductoList.get(i).getNombre());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner

        spPrducto.setAdapter(spinnerAdapter);
    }
    //Lllenado spiner tipo ProductoEspecifico

   private void enviarPedido(){

       showpDialog();
       //String valor;
       int id = rgTipo.getCheckedRadioButtonId();
       if (id == -1){
           //no item selected
           valor ="";
       }
       else{
           if (id == rbNuevo.getId()){
               //Do something with the button
               valor = "new";
           } else if(id == rbRenovacion.getId()){
               valor = "renewal";
           }
       }
       comentario = etComent.getText().toString();
       JSONObject params_pedido = new JSONObject();
       try {
           params_pedido.put("id", id_user);
           params_pedido.put("agent_id", idAgente);
           params_pedido.put("type_orders_id", idTipo);
           params_pedido.put("publicities_id", idProducto);
           params_pedido.put("dir", spDir.getSelectedItem());
           params_pedido.put("state", valor);
           params_pedido.put("comentario",comentario);

           Log.d("params",params_pedido.toString());
           Log.d("params",params_pedido.toString());

       } catch (JSONException e) {
           e.printStackTrace();
       }

       JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , "http://redagentesyglobalnet.com/insertJsonOrder" ,params_pedido,
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
                               Log.d("DATAAAA", response.toString());
                               Toast.makeText(MyActivity, "Se  envió correctamente su pedido",Toast.LENGTH_LONG).show();
                               String aid = String.valueOf(idAgente) ;
                               Intent intent = new Intent(MyActivity, ListaPedido.class);
                               Bundle bolsa = new Bundle();
                               bolsa.putString("id", aid);
                               intent.putExtras(bolsa);
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                               startActivity(intent);
                               finish();
                           } else {
                               Toast.makeText(MyActivity, "No se ha podido enviar la información, intentelo mas tarde ",Toast.LENGTH_LONG).show();
                           }
                       } catch (JSONException e) {
                           Toast.makeText(MyActivity, "No se ha podido enviar la información, intentelo mas tarde ",Toast.LENGTH_LONG).show();
                           e.printStackTrace();
                       }

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
                Intent intent = new Intent(this, ListaPedido.class);
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

    private void enabledControl(){



        rbNuevo.setClickable(true);
        rbNuevo.setEnabled(true);
        rbRenovacion.setClickable(true);
        rbRenovacion.setEnabled(true);
        rbRenovacion.setChecked(false);
        rbNuevo.setChecked(false);
    }
    private void disableControl(){
        rbNuevo.setClickable(false);
        rbNuevo.setEnabled(false);

        rbRenovacion.setClickable(false);
        rbRenovacion.setEnabled(false);
        rbRenovacion.setChecked(false);
        rbNuevo.setChecked(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }


    private void hidePDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
