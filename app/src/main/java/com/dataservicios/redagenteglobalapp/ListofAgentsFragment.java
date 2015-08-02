package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dataservicios.SQLite.DatabaseHelper;
import com.dataservicios.librerias.GPSTracker;
import com.dataservicios.librerias.GlobalConstant;
import com.dataservicios.librerias.JSONParser;
import com.dataservicios.librerias.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import adapter.AgentesAdapter;
import app.AppController;
import model.Agentes;

/**
 * Created by user on 09/01/2015.
 */
public class ListofAgentsFragment extends Fragment {
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    // Progress Dialog
    private ProgressDialog pDialog;
    //ArrayList<HashMap<String, String>> agentesList;
    private List<Agentes> agentesList = new ArrayList<Agentes>();
    private List<Agentes> listadoAgentes = new ArrayList<Agentes>();
    private ListView listView;
    private AgentesAdapter adapter;
    private Activity MyActivity ;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_AGENTES = "agentes";
    private static final String TAG_ID = "id";
    private static final String TAG_TIENDA = "nombre_comercial";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_STATUS = "status";
    private static final String TAG_INICIO = "inicio";
    private static final String TAG_FIN = "fin";
    private static String url_all_agentes = GlobalConstant.DOMINIO + "/JsonAgentList";
    private SessionManager session;
    private String code_user, id_user, name_user;
    private String idtienda;
    private Bundle args;
    private JSONObject params;

    private DatabaseHelper db;
    // products JSONArray
    JSONArray agentes = null;

    public ListofAgentsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Setting ActionBar Name
        getActivity().getActionBar().setTitle("Mis Agentes");
        session = new SessionManager(getActivity());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        name_user = user.get(SessionManager.KEY_NAME);
        // email
        code_user = user.get(SessionManager.KEY_USER);
        // id
        id_user = user.get(SessionManager.KEY_ID_USER);


        return inflater.inflate(R.layout.fragment_list_of_agents, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new DatabaseHelper(getActivity());
        int countReg;

        // Hashmap for ListView
        // agentesList = new ArrayList<HashMap<String, String>>();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        GlobalConstant.inicio = strDate;
        Log.i("FECHA",strDate);


        Bundle bundle = getArguments();
        String search;
        //here is your list array
        params = new JSONObject();
        if (bundle != null) {
            search = bundle.getString("search");

            try {
                params.put("id", id_user);
                params.put("search",search);
                //idtienda = search;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            search="";
            try {
                params.put("id", id_user);
                params.put("search",search);
                //idtienda = search;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Loading products in Background Thread
        // new LoadAllProducts().execute(search);
        // Get listview
        ListView listView = (ListView) getActivity().findViewById(R.id.list_items);
        // Get Edittex
        final EditText search_edt = (EditText) getActivity().findViewById(R.id.edt_search);
        // on seleting single product
        // launching Edit Product Screen

        adapter = new AgentesAdapter(getActivity(), agentesList);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        //listView.setAdapter(adapter);
        countReg = db.getCountTable(GlobalConstant.TABLE_AGENTS);

        if (countReg==0){
            cargaAgentes();
        }else{
            if (search == ""){
                adapter = new AgentesAdapter(getActivity(), db.getAllAgents(Integer.valueOf(id_user)));
                adapter.notifyDataSetChanged();
            } else {
                //Log.i("SEARCH....","hay datos para Buscar");
                adapter = new AgentesAdapter(getActivity(), db.getSearcAgents(Integer.valueOf(id_user),search));
                adapter.notifyDataSetChanged();
            }
        }

       // listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                //Obteniendo Ubicacion
                GPSTracker gps = new GPSTracker(getActivity());
                // Verificar si GPS esta habilitado
                if(gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    GlobalConstant.latitude_open = latitude;
                    GlobalConstant.longitude_open = longitude;
                    //Toast toast = Toast.makeText(getApplicationContext(), "Lat: " + String.valueOf(latitude) + "Long: " + String.valueOf(longitude), Toast.LENGTH_SHORT);
                    //toast.show();
                }else{
                    // Indicar al Usuario que Habilite su GPS
                    gps.showSettingsAlert();
                }

                // getting values from selected ListItem
                String aid = ((TextView) view.findViewById(R.id.id)).getText().toString();
                String status = ((TextView) view.findViewById(R.id.tvEstatus)).getText().toString();
                GlobalConstant.status = Integer.valueOf(status)  ;
                // Starting new intent
                Intent i = new Intent( getActivity() , AgenteDetailActivity.class);
                Bundle bolsa = new Bundle();
                bolsa.putString(TAG_ID, aid);
                //bolsa.putString("status", status);
                i.putExtras(bolsa);
                startActivity(i);
            }
        });
//
//        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                parent.setEnabled(false);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        listView.setAdapter(adapter);
        search_edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Starting new intent
                    Fragment fragment = null;
                    fragment = new ListofAgentsFragment();
                    // sending pid to next activity
                    args = new Bundle();
                    args.putString("search", search_edt.getText().toString());
                    fragment.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("tag").commit();
                    return true;
                }
                return false;
            }
        });


    }

    class LoadAllProducts extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Cargando Agentes...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("search", args[0]));
            params.add(new BasicNameValuePair("id", id_user));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_agentes, "POST", params);

            // Check your log cat for JSON reponse
            Log.d("All Agentes: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products

                    agentes = json.getJSONArray(TAG_AGENTES);
                    // looping through All Products
                    for (int i = 0; i < agentes.length(); i++) {
                        JSONObject obj = agentes.getJSONObject(i);
                        // Storing each json item in variable
                        String id = obj.getString(TAG_ID);
                        String name = obj.getString(TAG_TIENDA);
                        String direccion = obj.getString(TAG_DIRECCION);
                        // creating new HashMap
//                        HashMap<String, String> map = new HashMap<String, String>();
//                        // adding each child node to HashMap key => value
//                        map.put(TAG_ID, id);
//                        map.put(TAG_TIENDA, name);
//                        map.put(TAG_DIRECCION, direccion);
//                        map.put("Flag", Integer.toString(R.drawable.agente));

                        Agentes agentes = new Agentes();
                        agentes.setId(Integer.valueOf(obj.getString("id")));
                        agentes.setThumbnailUrl("http://api.androidhive.info/json/movies/2.jpg");
                        agentes.setDireccion(obj.getString("direccion"));
                        agentes.setNombreAgente(obj.getString("nombre_comercial"));
                        // agentes.setStatus(obj.getInt("status"));
                        // adding HashList to ArrayList
                        agentesList.add(agentes);
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    // no products found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            if (file_url != null) {
                Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG).show();
            }

            // dismiss the dialog after getting all products
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
//                    ListAdapter adapter = new SimpleAdapter(
//                                                            getActivity().getApplicationContext(),
//                                                            agentesList,
//                                                            R.layout.list_detail,
//                                                            new String[] { "Flag",
//                                                                            TAG_ID,
//                                                                            TAG_TIENDA,
//                                                                            TAG_DIRECCION
//                                                                            },
//                                                                            new int[] {
//                                                                                        R.id.Flag,
//                                                                                        R.id.id,
//                                                                                        R.id.tienda,
//                                                                                        R.id.direccion});
//                    // updating listview
//                    ListView lv = (ListView) getActivity().findViewById(R.id.list_items);
//                    lv.setAdapter(adapter);
                    listView.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();
                }
            });
            pDialog.dismiss();
        }


    }


    private void cargaAgentes() {
        showpDialog();


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_all_agentes ,params,
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
                                JSONArray agentesObjJson;
                                agentesObjJson = response.getJSONArray("agentes");
                                // looping through All Products
                                for (int i = 0; i < agentesObjJson.length(); i++) {
                                    JSONObject obj = agentesObjJson.getJSONObject(i);
                                    // Storing each json item in variable id_user
                                    Agentes agentes = new Agentes();
                                    agentes.setId(Integer.valueOf(obj.getString(TAG_ID)));
                                    agentes.setThumbnailUrl( GlobalConstant.URL_IMAGES_AGENT + obj.getString("photo"));
                                    agentes.setDireccion(obj.getString(TAG_DIRECCION));
                                    agentes.setNombreAgente(obj.getString(TAG_TIENDA));
                                    agentes.setRazonSocial(obj.getString("nombre_comercial"));
                                    agentes.setIdUser(Integer.valueOf(id_user));
                                    agentes.setStatus(obj.getInt(TAG_STATUS));
                                    agentes.setInicio(obj.getString(TAG_INICIO));
                                    agentes.setFin(obj.getString(TAG_FIN));
                                    // agentes.setStatus(obj.getInt("status"));
                                    // adding HashList to ArrayList
                                    db.ingresarAgentes(agentes);
                                    agentesList.add(agentes);
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

};





