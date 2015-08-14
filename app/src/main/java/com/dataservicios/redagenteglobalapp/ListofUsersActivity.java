package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.AgentesAdapter;
import adapter.UserAdapter;
import app.AppController;
import model.Reclamos;
import model.User;

/**
 * Created by PEDRO QUISPE ALVAREZ on 08/08/2015.
 */
public class ListofUsersActivity extends Activity {

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    // Progress Dialog
    private ProgressDialog pDialog;

    private List<User> usersList = new ArrayList<User>();
    private ListView listView;
    private UserAdapter adapter;
    private static String url_dependusers = GlobalConstant.DOMINIO + "/JsonListUsersNivel";
    private SessionManager session;
    private String code_user, id_user, name_user, type_user;
    private String idtienda;
    private Bundle args;
    private JSONObject params;


    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    // JSON parser class
    JSONParser jsonParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_users);

        getActionBar().setTitle("Lista de Usuarios");
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        name_user = user.get(SessionManager.KEY_NAME);
        // email
        code_user = user.get(SessionManager.KEY_USER);
        // id
        id_user = user.get(SessionManager.KEY_ID_USER);

        type_user=user.get(SessionManager.KEY_TYPE_USER);



        listView = (ListView) findViewById(R.id.list_slidermenu);
        TextView nombres = (TextView) findViewById(R.id.nombres);
        TextView idinterbank = (TextView) findViewById(R.id.idinterbank);

        nombres.setText(name_user);
        idinterbank.setText(code_user);


        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.thumbnail);

        // thumbnail image
        thumbNail.setImageUrl("http://redagentesyglobalnet.com/images/users/"+code_user+".jpg", imageLoader);
        thumbNail.setErrorImageResId(R.drawable.ic_user);

        Log.d("code_user",code_user);
        Log.d("name_user",name_user);
        Log.d("id_user",id_user);


        Log.d("User_id",id_user);

        adapter = new UserAdapter(this, usersList);
        params = new JSONObject();
        try {
            params.put("user_id", id_user);
            //idtienda = search;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        cargaUsers();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {


                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            // getting values from selected ListItem
                            String uid = ((TextView) view.findViewById(R.id.id)).getText().toString();
                            // Starting new intent
                            Intent i ;
                            //Your code goes here
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("user_id", uid));
                            JSONObject json = jsonParser.makeHttpRequest(url_dependusers ,"POST", params);
                            Integer success = json.getInt("success");

                            Log.d("USER-ID",success+"");
                            Log.d("USER-ID",""+(success == 1));

                            if (success == 1 ){
                                i = new Intent( ListofUsersActivity.this , ListofUsersActivity.class);
                            }else{
                                i = new Intent( ListofUsersActivity.this , PanelAdmin.class);

                            }
                            Bundle bolsa = new Bundle();
                            bolsa.putString("user_id", uid);
                            //session.logoutUser();
                            session.createLoginSession(name_user, uid, code_user,type_user);
                            //bolsa.putString("status", status);
                            bolsa.putString("NOMBRE", code_user);
                            i.putExtras(bolsa);
                            startActivity(i);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();


            }
        });



    }


    private void cargaUsers() {
        showpDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , url_dependusers ,params,
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
                                agentesObjJson = response.getJSONArray("users");
                                for (int i = 0; i < agentesObjJson.length(); i++) {
                                    JSONObject obj = agentesObjJson.getJSONObject(i);
                                    // Storing each json item in variable
                                    User user = new User();
                                    //users.setThumbnailUrl(GlobalConstant.URL_IMAGES_AGENT + obj.getString("photo"));
                                    user.setId(Integer.parseInt(obj.getString("iduser")));
                                    user.setNombre(obj.getString("nombres"));
                                    user.setIdInterbank(obj.getString("idInterbank"));
                                    user.setThumbnailUrl("http://redagentesyglobalnet.com/images/users/"+obj.getString("photo"));
                                    usersList.add(user);
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
