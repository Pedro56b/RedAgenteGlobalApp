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
import com.dataservicios.librerias.GlobalConstant;
import com.dataservicios.librerias.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import app.AppController;

/**
 * Created by PEDRO QUISPE ALVAREZ on 09/07/2015.
 */
public class EditContactoActivity extends Activity{

    private ProgressDialog pDialog;
    private JSONObject params;
    Button btn_guardar_contacto, btn_eliminar_contacto;
    EditText edt_contacto, edt_celular, edt_email;
    private SessionManager session;
    private String code_user, id_user, name_user;
    private AlertDialog.Builder builder;

    private static String url_editar_contacto =  GlobalConstant.DOMINIO +  "/updateContactAgent";
    private static String url_eliminar_contacto = GlobalConstant.DOMINIO +  "/deleteContactAgent";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_contacto);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Editar Contacto");

        Bundle bundle = getIntent().getExtras();


        session = new SessionManager(this);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        name_user = user.get(SessionManager.KEY_NAME);
        // email
        code_user = user.get(SessionManager.KEY_USER);
        // id
        id_user = user.get(SessionManager.KEY_ID_USER);


        // Get Button
        btn_guardar_contacto = (Button) findViewById(R.id.btGuardarContacto);
        btn_eliminar_contacto = (Button) findViewById(R.id.btEliminarContacto);


        // Get Inputs
        edt_celular = (EditText) findViewById(R.id.edtcelular);
        edt_contacto =(EditText) findViewById(R.id.edtcontacto);
        edt_email = (EditText) findViewById(R.id.edtemail);

        edt_celular.setText(bundle.getString("celular"));
        edt_contacto.setText(bundle.getString("contacto"));
        edt_email.setText(bundle.getString("email"));

        btn_guardar_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EditContactoActivity.this ,2)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("AGENTE")
                        .setMessage("¿Confirmar?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                params = new JSONObject();

                                Bundle bundle = getIntent().getExtras();
                                String id_agente = bundle.getString("id");
                                String id_contacto = bundle.getString("cid");

                                pDialog = new ProgressDialog(EditContactoActivity.this);
                                pDialog.setMessage("Please wait...");
                                pDialog.setCancelable(false);

                                builder = new AlertDialog.Builder(EditContactoActivity.this, 2);

                                try {
                                    params.put("contact_id", id_contacto);
                                    params.put("agent_id", id_agente);
                                    params.put("celular", edt_celular.getText());
                                    params.put("contacto", edt_contacto.getText());
                                    params.put("email", edt_email.getText());

                                    Log.d("params", params.toString());


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                guardarContacto();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


        btn_eliminar_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EditContactoActivity.this ,2)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("AGENTE")
                        .setMessage("¿Esta seguro de eliminar el Contacto?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                params = new JSONObject();

                                Bundle bundle = getIntent().getExtras();
                                String id_agente = bundle.getString("id");
                                String id_contacto = bundle.getString("cid");

                                pDialog = new ProgressDialog(EditContactoActivity.this);
                                pDialog.setMessage("Please wait...");
                                pDialog.setCancelable(false);

                                builder = new AlertDialog.Builder(EditContactoActivity.this, 2);

                                try {
                                    params.put("contact_id", id_contacto);

                                    Log.d("params", params.toString());


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                eliminarContacto();
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
                Intent intent = new Intent(this, ListaContactos.class);
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

    private void guardarContacto() {
        showpDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_editar_contacto , params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("DATAAAA", response.toString());
                        try {
                            int success =  response.getInt("success");
                            if (success == 1) {
                                builder.setMessage("Se Actualizo Correctamente el Contacto")
                                        .setTitle("AGENTE")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Bundle bundle = getIntent().getExtras();
                                                String id_agente = bundle.getString("id");

                                                // getting values from selected ListItem
                                                String aid = id_agente;
                                                // Starting new intent
                                                Intent i = new Intent( EditContactoActivity.this , ListaContactos.class);
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


    private void eliminarContacto() {
        showpDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_eliminar_contacto , params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("DATAAAA", response.toString());
                        try {
                            int success =  response.getInt("success");
                            if (success == 1) {
                                builder.setMessage("Se Elimino Correctamente el Contacto")
                                        .setTitle("AGENTE")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Bundle bundle = getIntent().getExtras();
                                                String id_agente = bundle.getString("id");

                                                // getting values from selected ListItem
                                                String aid = id_agente;
                                                // Starting new intent
                                                Intent i = new Intent( EditContactoActivity.this , ListaContactos.class);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(pDialog != null)
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
