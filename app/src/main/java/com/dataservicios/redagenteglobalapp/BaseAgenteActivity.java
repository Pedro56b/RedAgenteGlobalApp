package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dataservicios.librerias.GlobalConstant;
import com.dataservicios.librerias.SessionManager;
import com.dataservicios.SQLite.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import adapter.NavDrawerListAdapter;
import app.AppController;
import model.NavDrawerItem;

/**
 * Created by user on 06/02/2015.
 */
public class BaseAgenteActivity extends Activity
{
    private Activity MyActivity;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private android.support.v4.app.ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    private ProgressDialog pDialog;

    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private SessionManager session;
    private DatabaseHelper db;




    protected void onCreateDrawer(){
        MyActivity = (Activity) this;
        session = new SessionManager(getApplicationContext());
        mTitle = mDrawerTitle = getTitle();
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_agentes_items);
        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_agente_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        // adding nav drawer items to array
        // Lista de agentes
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Editar Agente
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Checklist
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Reclamos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // Pedidos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // Transacciones
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        // facturacion
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        // Foto
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        //Cerrar auditoria
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new android.support.v4.app.ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_salir:

                session.logoutUser();
               // getAct
                finishAffinity();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_salir).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Bundle bundle = getIntent().getExtras();
        String id_agente = bundle.getString("id");

        // getting values from selected ListItem
        String aid = id_agente;

        switch (position) {
            case 0:
                // Starting new intent

                finish();

                break;
            case 1:
                // Starting new intent
                Intent i = new Intent( this , EditAgenteActivity.class);
                Bundle bolsa = new Bundle();
                bolsa.putString("id", aid);
                i.putExtras(bolsa);
                startActivity(i);

                break;
            case 2:
                // Starting new intent
                Intent i_1 = new Intent( this , ChecklistActivity.class);
                Bundle bolsa_1 = new Bundle();
                bolsa_1.putString("id", aid);
                i_1.putExtras(bolsa_1);
                startActivity(i_1);
                break;
            case 3:// Starting new intent
                Intent i_2 = new Intent( this , ListofReclamosActivity.class);
                Bundle bolsa_2 = new Bundle();
                bolsa_2.putString("id", aid);
                i_2.putExtras(bolsa_2);
                startActivity(i_2);
                break;
            case 4:
                //fragment = new CommunityFragment();
                Intent i_3 = new Intent( this , ListaPedido.class);
                Bundle bolsa_3 = new Bundle();
                bolsa_3.putString("id", aid);
                i_3.putExtras(bolsa_3);
                startActivity(i_3);
                break;
            case 5:
                // Intent i_4 = new Intent( this , TransaccionesAgenteActivity.class);
                Intent i_4 = new Intent( this , Transacciones.class);
                Bundle bolsa_4 = new Bundle();
                bolsa_4.putString("id", aid);
                i_4.putExtras(bolsa_4);
                startActivity(i_4);
                break;
            case 6:
                // Intent i_4 = new Intent( this , TransaccionesAgenteActivity.class);
                Intent i_5 = new Intent( this , Facturacion.class);
                Bundle bolsa_5 = new Bundle();
                bolsa_5.putString("id", aid);
                i_5.putExtras(bolsa_5);
                startActivity(i_5);
                break;
            case 7:
                Intent i_6 = new Intent( this , AndroidCustomGalleryActivity.class);
                Bundle bolsa_6 = new Bundle();
                bolsa_6.putString("id", aid);
                i_6.putExtras(bolsa_6);
                startActivity(i_6);
                break;
            case 8:
               // GlobalConstant.inicio = strDate;
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = sdf.format(c.getTime());
                GlobalConstant.fin = strDate;


                Log.i("INICIO", GlobalConstant.inicio);
                Log.i("FIN", GlobalConstant.fin);


                insertaTiemporAuditoria(id_agente, GlobalConstant.inicio, GlobalConstant.fin);
                finish();
                break;



            default:
                break;
        }
    }

    private void insertaTiemporAuditoria(String idAgent, String inicio, String fin) {
        db = new DatabaseHelper(MyActivity);
        db.updateStatusAndFech(inicio,fin,idAgent);


        JSONObject params_pedido = new JSONObject();
        try {
            params_pedido.put("agent_id", idAgent);
            params_pedido.put("inicio", inicio);
            params_pedido.put("fin", fin);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , "http://redagentesyglobalnet.com/updateStatusAgent" ,params_pedido,
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
                                Log.d("DATAAAA", response.toString());
                                Toast.makeText(MyActivity, "Se actualizo su visita", Toast.LENGTH_LONG).show();

                                finish();

                            } else {

                                Toast.makeText(MyActivity, "No se ha podido enviar la información, intentelo mas tarde ",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MyActivity, "No se ha podido enviar la información, intentelo mas tarde ",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


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
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}