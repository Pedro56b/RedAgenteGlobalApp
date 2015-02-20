package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import app.AppController;

/**
 * Created by user on 08/02/2015.
 */


//extends BaseAgenteActivity

public class ChecklistActivity extends Activity {

    EditText fechaEditText, horaEditText, extEditText, intEditText, otroagenteEditText, factEditText, tiendafrecEditText, derivEditText, relevEditText;
    Spinner contactoSpinner;
    Switch extSwitch, intSwitch, otroagenteSwitch, factSwitch;
    Button guardarchecklistButton;
    RadioGroup tiporadioGroup;
    RadioButton derivacionButton, influenciaButton, periferiaButton;


    int mYear, mMonth, mDay, hour, minute;
    private String array_spinner[], contacto_ids[];
    private ProgressDialog pDialog;
    private JSONObject params, paramsdata;
    Spinner spn_tipo;

    private AlertDialog.Builder builder;

    private static String url_nuevo_checklist = "http://redagentesyglobalnet.com/insertJsonCheck";
    private static String url_get_contactos = "http://redagentesyglobalnet.com/JsonContactsAgent";

    public ChecklistActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist);
        //super.onCreateDrawer();
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String id_agente = bundle.getString("id");


        params = new JSONObject();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        try {
            params.put("id", id_agente);
            Log.d("params",params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cargarContactos();

        //Get Spinner
        spn_tipo = (Spinner) findViewById(R.id.spContacto);


        fechaEditText = (EditText) findViewById(R.id.edtFecha);
        horaEditText = (EditText) findViewById(R.id.edtHora);
        extEditText = (EditText) findViewById(R.id.edtExt);
        intEditText = (EditText) findViewById(R.id.edtInt);
        otroagenteEditText = (EditText) findViewById(R.id.edtotroagente);
        factEditText = (EditText) findViewById(R.id.edtfact);
        tiendafrecEditText = (EditText) findViewById(R.id.edttiendafrec);
        derivEditText = (EditText) findViewById(R.id.edtderiv);
        relevEditText = (EditText) findViewById(R.id.edtrelev);
        contactoSpinner = (Spinner) findViewById(R.id.spContacto);
        extSwitch = (Switch) findViewById(R.id.swExt);
        intSwitch = (Switch) findViewById(R.id.swInt);
        otroagenteSwitch = (Switch) findViewById(R.id.swotroagente);
        factSwitch = (Switch) findViewById(R.id.swfact);
        guardarchecklistButton = (Button) findViewById(R.id.btGuardarChecklist);
        tiporadioGroup = (RadioGroup) findViewById(R.id.tipogrupo);
        derivacionButton = (RadioButton) findViewById(R.id.Btnderivacion);
        influenciaButton = (RadioButton) findViewById(R.id.Btninfluencia);
        periferiaButton = (RadioButton) findViewById(R.id.Btnperiferia);

        Calendar mcurrentDate=Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        hour = mcurrentDate.get(Calendar.HOUR_OF_DAY);
        minute = mcurrentDate.get(Calendar.MINUTE);

        fechaEditText.setText(String.format("%02d",mDay)+"/"+String.format("%02d",mMonth)+"/"+String.format("%02d",mYear) );
        horaEditText.setText( String.format("%02d",hour) + ":" +String.format("%02d",minute) );

        fechaEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker=new DatePickerDialog(ChecklistActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        mYear = selectedyear;
                        mMonth = selectedmonth;
                        mDay = selectedday;
                        fechaEditText.setText(String.format("%02d",mDay)+"/"+String.format("%02d",mMonth)+"/"+String.format("%02d",mYear) );
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Fecha de Visita");
                mDatePicker.show();
            }
        });

        horaEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog  mTimePicker = new TimePickerDialog(ChecklistActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        horaEditText.setText( String.format("%02d",hour) + ":" +String.format("%02d",minute) );
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Hora de Visita");
                mTimePicker.show();
            }
        });

        guardarchecklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ChecklistActivity.this ,2)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("AGENTE")
                        .setMessage("¿Confirmar?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                params = new JSONObject();

                                Bundle bundle = getIntent().getExtras();
                                String id_agente = bundle.getString("id");

                                pDialog = new ProgressDialog(ChecklistActivity.this);
                                pDialog.setMessage("Please wait...");
                                pDialog.setCancelable(false);

                                builder = new AlertDialog.Builder(ChecklistActivity.this,2);

                                try {

                                    int selectedId,tipo_agente,ext_val,int_val,otro_agente_val, fact_pend_val ;

                                    selectedId = tiporadioGroup.getCheckedRadioButtonId();
                                    if(selectedId == derivacionButton.getId()) {
                                        tipo_agente = 0;
                                    } else if(selectedId == influenciaButton.getId()) {
                                        tipo_agente = 1;
                                    } else {
                                        tipo_agente = 2;
                                    }

                                    if (extSwitch.isChecked())
                                        ext_val = 1;
                                    else
                                        ext_val = 0;

                                    if (intSwitch.isChecked())
                                        int_val = 1;
                                    else
                                        int_val = 0;

                                    if (otroagenteSwitch.isChecked())
                                        otro_agente_val = 1;
                                    else
                                        otro_agente_val = 0;


                                    if (factSwitch.isChecked())
                                        fact_pend_val = 1;
                                    else
                                        fact_pend_val = 0;

                                    params.put("id_agente", id_agente );
                                    params.put("fec_desc", fechaEditText.getText() );
                                    params.put("hor_desc", horaEditText.getText() );
                                    params.put("ext_desc", extEditText.getText() );
                                    params.put("int_desc", intEditText.getText() );
                                    params.put("otro_agente_desc", otroagenteEditText.getText() );
                                    params.put("fact_pend_desc", factEditText.getText()  );
                                    params.put("tienda_frec_desc", tiendafrecEditText.getText()  );
                                    params.put("tipo_agente_desc", derivEditText.getText() );
                                    params.put("datos_relev", relevEditText.getText() );
                                    params.put("contacto_id", contacto_ids[contactoSpinner.getSelectedItemPosition()]  );
                                    params.put("ext_val",  ext_val   );
                                    params.put("int_val",   int_val  );
                                    params.put("otro_agente_val",  otro_agente_val   );
                                    params.put("fact_pend_val",  fact_pend_val   );
                                    params.put("tipo_agente_val", tipo_agente  );


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                 paramsdata = new JSONObject();

                                try {
                                    paramsdata.put("data_json",params.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                guardarchecklist();
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


    private void guardarchecklist() {
        showpDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_nuevo_checklist , paramsdata,
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
                                                Intent i = new Intent( ChecklistActivity.this , AgenteDetailActivity.class);
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




    private void cargarContactos() {
        showpDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_get_contactos , params,
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
                                agentesObjJson = response.getJSONArray("contactos");
                                array_spinner=new String[agentesObjJson.length()];
                                contacto_ids =new String[agentesObjJson.length()];
                                for (int i = 0; i < agentesObjJson.length(); i++) {
                                    JSONObject obj = agentesObjJson.getJSONObject(i);
                                    // Storing each json item in variable
                                    array_spinner[i]= obj.getString("contacto") ;
                                    contacto_ids[i] = obj.getString("id");
                                }

                                Spinner s = (Spinner) findViewById(R.id.spContacto);
                                ArrayAdapter adapter = new ArrayAdapter(ChecklistActivity.this,
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
