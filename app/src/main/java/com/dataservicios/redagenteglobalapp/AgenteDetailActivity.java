package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.dataservicios.librerias.GPSTracker;
import com.dataservicios.librerias.GlobalConstant;
import com.dataservicios.librerias.JSONParser;
import com.dataservicios.librerias.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import adapter.NavDrawerListAdapter;
import app.AppController;
import model.Agentes;
import model.NavDrawerItem;

/**
 * Created by usuario on 28/01/2015.
 */
public class AgenteDetailActivity extends BaseAgenteActivity {

    public static MapView mapView;
    public static GoogleMap map;
    // Progress Dialog
    private ProgressDialog pDialog;
    // single product url
    private static final String url_agente_detials = GlobalConstant.DOMINIO + "/JsonAgentDetail";
    // single product edit url
    private static final String url_update_agente =  GlobalConstant.DOMINIO + "/updatePositionAgent";

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_AGENTE= "agente";
    private static final String TAG_ID = "id";
    private static final String TAG_TIENDA = "tienda";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_REPRESENTANTE = "representante";
    private static final String TAG_TELEFONO = "telefono";
    private static final String TAG_LATITUD = "latitud";
    private static final String TAG_LONGITUD = "longitud";
    private Integer status;

    private JSONObject params;

    TextView txt_tienda_detail;
    TextView txt_direccion_detail;
    TextView txt_ruc_detail;
    TextView txt_telefono_detail;
    TextView txt_referencia_detail;
    TextView txt_distrito_detail;
    TextView txt_departamento_detail;
    TextView txt_latitud_detail;
    TextView txt_longitud_detail;
    TextView txt_id;

    private AlertDialog.Builder builder;


    public AgenteDetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agente_detail);
        super.onCreateDrawer();

        getActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            Bundle bundle = getIntent().getExtras();
            String id_agente = bundle.getString(TAG_ID);
            //status = Integer.valueOf(bundle.getString("status")) ;


            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            cargaDetalleAgente(id_agente);
            // EditText Text
            txt_tienda_detail = (TextView) findViewById(R.id.tienda_detail);
            txt_direccion_detail = (TextView) findViewById(R.id.direccion_detail);
            txt_ruc_detail = (TextView) findViewById(R.id.ruc_detail);
            txt_telefono_detail = (TextView) findViewById(R.id.telefono_detail);
//            txt_latitud_detail = (TextView) findViewById(R.id.latitud_detail);
//            txt_longitud_detail = (TextView) findViewById(R.id.longitud_detail);
             txt_referencia_detail =(TextView) findViewById(R.id.referencia_detail);
            txt_distrito_detail=(TextView) findViewById(R.id.distrito_detail);
            txt_departamento_detail=(TextView) findViewById(R.id.departamento_detail);
            txt_id = (TextView) findViewById(R.id.id_detail);

            Button gps_button = (Button) findViewById(R.id.btn_gps);
            Button photo_button = (Button) findViewById(R.id.btn_photo);
            Button pedido_button = (Button) findViewById(R.id.btn_pedido);
            Button reclamo_button = (Button) findViewById(R.id.btn_reclamo);
            Button edit_button = (Button) findViewById(R.id.btn_edit_agente);
            Button phono_button = (Button) findViewById(R.id.btn_detalle_phono);
            Button message_button = (Button) findViewById(R.id.btn_detalle_message);
            Button contacts_button = (Button) findViewById(R.id.btn_contacts);
            /*
            // thumbnail image
            thumbNail_1.setImageUrl("http://localhost:8080/redagenteglobalapp/images/agentes/000001-Agente_foto_20150208_002512", imageLoader);
            // thumbnail image
            thumbNail_2.setImageUrl("http://localhost:8080/redagenteglobalapp/images/agentes/000001-Agente_foto_20150208_004858.jpg", imageLoader);
            // thumbnail image
            thumbNail_3.setImageUrl("http://localhost:8080/redagenteglobalapp/images/agentes/000001-Agente_foto_20150208_005140.jpg", imageLoader);
            */

            if(GlobalConstant.status==1){
                 gps_button.setEnabled(false);
                 photo_button.setEnabled(false);
                pedido_button.setEnabled(false);
                contacts_button.setEnabled(false);
                reclamo_button.setEnabled(false);
                edit_button.setEnabled(false);

            }
            //phono_button.setEnabled(false);
           // message_button.setEnabled(false);


            edit_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Bundle bundle = getIntent().getExtras();
                    String id_agente = bundle.getString(TAG_ID);

                    // getting values from selected ListItem
                    String aid = id_agente;
                    // Starting new intent
                    Intent i = new Intent( AgenteDetailActivity.this , EditAgenteActivity.class);
                    Bundle bolsa = new Bundle();
                    bolsa.putString(TAG_ID, aid);
                    i.putExtras(bolsa);
                    startActivity(i);
                }
            });

            contacts_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Bundle bundle = getIntent().getExtras();
                    String id_agente = bundle.getString(TAG_ID);

                    // getting values from selected ListItem
                    String aid = id_agente;
                    // Starting new intent
                    Intent i = new Intent( AgenteDetailActivity.this , ListaContactos.class);
                    Bundle bolsa = new Bundle();
                    bolsa.putString(TAG_ID, aid);
                    i.putExtras(bolsa);
                    startActivity(i);
                }
            });



            phono_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i("Make call", "");
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                    phoneIntent.setData(Uri.parse("tel:" + txt_telefono_detail.getText()));
                    startActivity(phoneIntent);
                }
            });

            message_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i("Make call", "");
                    Intent phoneIntent = new Intent(Intent.ACTION_VIEW);
                    phoneIntent.setData(Uri.parse("sms:" + txt_telefono_detail.getText()));
                    startActivity(phoneIntent);
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + txt_telefono_detail.getText())));
                }
            });

            gps_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new AlertDialog.Builder(AgenteDetailActivity.this ,2)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("AGENTE")
                            .setMessage("¿Confirmar?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getGPSLocation();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });

            photo_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    takePhoto();
                }
            });


            pedido_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    /*
                    Bundle bundle = getIntent().getExtras();
                    String id_agente = bundle.getString(TAG_ID);

                    // getting values from selected ListItem
                    String aid = id_agente;
                    // Starting new intent
                    Intent i = new Intent( AgenteDetailActivity.this , ListofPedidosActivity.class);
                    Bundle bolsa = new Bundle();
                    bolsa.putString(TAG_ID, aid);
                    i.putExtras(bolsa);
                    startActivity(i);
                    */

                    //fragment = new CommunityFragment();

                    Bundle bundle = getIntent().getExtras();
                    String id_agente = bundle.getString(TAG_ID);

                    // getting values from selected ListItem
                    String aid = id_agente;
                    Intent i_3 = new Intent( AgenteDetailActivity.this , ListaPedido.class);
                    Bundle bolsa_3 = new Bundle();
                    bolsa_3.putString("id", aid);
                    i_3.putExtras(bolsa_3);
                    startActivity(i_3);
                }
            });

            reclamo_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Bundle bundle = getIntent().getExtras();
                    String id_agente = bundle.getString(TAG_ID);

                    // getting values from selected ListItem
                    String aid = id_agente;
                    // Starting new intent
                    Intent i = new Intent( AgenteDetailActivity.this , ListofReclamosActivity.class);
                    Bundle bolsa = new Bundle();
                    bolsa.putString(TAG_ID, aid);
                    i.putExtras(bolsa);
                    startActivity(i);
                }
            });

            // Gets the MapView from the XML layout and creates it
            mapView = (MapView) findViewById(R.id.mapview_detail);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            // Gets to GoogleMap from the MapView and does initialization stuff
            map = mapView.getMap();
            // Changing map type
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            // Showing / hiding your current location
            map.setMyLocationEnabled(true);
            // Enable / Disable zooming controls
            map.getUiSettings().setZoomControlsEnabled(true);
            // Enable / Disable my location button
            map.getUiSettings().setMyLocationButtonEnabled(true);
            // Enable / Disable Compass icon
            map.getUiSettings().setCompassEnabled(true);
            // Enable / Disable Rotate gesture
            map.getUiSettings().setRotateGesturesEnabled(true);
            // Enable / Disable zooming functionality
            map.getUiSettings().setZoomGesturesEnabled(true);
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            System.out.println(e);
        }

    }



    // Camera
    private void takePhoto() {
        /*Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

        startActivityForResult(intent, 100);
        */

        Bundle bundle = getIntent().getExtras();
        String id_agente = bundle.getString(TAG_ID);

        // getting values from selected ListItem
        String aid = id_agente;
        // Starting new intent
        Intent i = new Intent( AgenteDetailActivity.this , AndroidCustomGalleryActivity.class);
        Bundle bolsa = new Bundle();
        bolsa.putString(TAG_ID, aid);

        i.putExtras(bolsa);
        startActivity(i);


    }


    private void getGPSLocation() {

        GPSTracker gps = new GPSTracker(this);

        // Verificar si GPS esta habilitado
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            map.clear();
            //txt_latitud_detail.setText(latitude+"");
            //txt_longitud_detail.setText(longitude+"");
            LatLng agente_latlng = new LatLng(latitude,longitude) ;
            //Añade el marker al mapa
            Marker agente_marker = map.addMarker(new MarkerOptions()
                    .position(agente_latlng)
                    .title(txt_tienda_detail.getText().toString())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_map))
                    .snippet(txt_direccion_detail.getText().toString()));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(agente_latlng, 15));
            map.setMyLocationEnabled(true);
            //Mostrando información del Market ni bien carga el mapa
            agente_marker.showInfoWindow();

            Bundle bundle = getIntent().getExtras();
            String id_agente = bundle.getString(TAG_ID);

            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);

            builder = new AlertDialog.Builder(this,2);

            params = new JSONObject();
                try {
                params.put("id", id_agente);
                params.put("latitud",latitude);
                params.put("longitud",longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            UpdatePosition();

        }else{
            // Indicar al Usuario que Habilite su GPS
            gps.showSettingsAlert();
        }
    }

    private void UpdatePosition() {
        showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_update_agente ,params,
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
                            int success =  response.getInt("success");

                            if (success == 1) {

                                builder.setMessage("Se guardo Correctamente la Ubicacion")
                                        .setTitle("AGENTE")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User clicked OK button
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
                            int success =  response.getInt("success");

                            if (success == 1) {
                                JSONArray agentesObjJson;
                                agentesObjJson = response.getJSONArray(TAG_AGENTE);
                                JSONObject obj = agentesObjJson.getJSONObject(0);
                                // Storing each json item in variable
                                txt_id.setText(obj.getString(TAG_ID));
                                txt_tienda_detail.setText(obj.getString(TAG_TIENDA));
                                //txt_direccion_detail.setText("Dirección: " + obj.getString(TAG_DIRECCION));
                                txt_direccion_detail.setText("Dirección: " + obj.getString(TAG_DIRECCION));
                                txt_ruc_detail.setText(obj.getString("ruc"));

                                txt_referencia_detail.setText(obj.getString("referencia"));
                                txt_distrito_detail.setText(obj.getString("distrito"));
                                txt_departamento_detail.setText(obj.getString("departamento"));

                                txt_telefono_detail.setText(obj.getString("telefono"));
                                //txt_latitud_detail.setText(obj.getString("latitud"));
                                //txt_longitud_detail.setText(obj.getString("longitud"));

                                Double latitud;
                                Double longitud;

                                if (obj.getString(TAG_LATITUD) != "null") {
                                    latitud = Double.parseDouble(obj.getString(TAG_LATITUD));
                                }else
                                {
                                    latitud = Double.parseDouble("0");
                                }

                                if (obj.getString(TAG_LATITUD) != "null") {
                                    longitud = Double.parseDouble(obj.getString(TAG_LONGITUD));
                                }else
                                {
                                    longitud = Double.parseDouble("0");
                                }

                                LatLng agente_position = new LatLng(latitud,longitud) ;

                                //Añade el marker al mapa
                                Marker agente_marker = map.addMarker(new MarkerOptions()
                                        .position(agente_position)
                                        .title(obj.getString(TAG_TIENDA))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_map))
                                        .snippet(obj.getString(TAG_DIRECCION)));

                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(agente_position, 15));

                                //Mostrando información del Market ni bien carga el mapa
                                agente_marker.showInfoWindow();

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
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        finish();
        startActivity(getIntent());
    }






}
