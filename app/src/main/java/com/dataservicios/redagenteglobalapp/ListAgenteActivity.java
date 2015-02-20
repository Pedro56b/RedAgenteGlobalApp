package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import adapter.NavDrawerListAdapter;
import model.NavDrawerItem;

/**
 * Created by usuario on 08/11/2014.
 */
public class ListAgenteActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_agentes);

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));


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

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
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

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
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
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                //fragment = new FindPeopleFragment();
                break;
            case 2:
                //fragment = new PhotosFragment();
                break;
            case 3:
                //fragment = new CommunityFragment();
                break;
            case 4:
                //fragment = new PagesFragment();
                break;
            case 5:
                //fragment = new WhatsHotFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
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

//    Intent intent ;
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_base, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
//        switch(id) {
//            case R.id.action_profile:
////                intent = new Intent(this,MainActivity.class);
////                finish();
////                startActivity(intent);
////                break;
//                Toast.makeText(BaseActivity.this, "Activiyy Profile" , Toast.LENGTH_LONG).show();
//            case R.id.action_change_password:
////                intent = new Intent(this,PrimerActivity.class);
////                finish();
////                startActivity(intent);
//                Toast.makeText(BaseActivity.this, "Activiyy Password" , Toast.LENGTH_LONG).show();
//
//                break;
//
//
//            case R.id.action_exit:
//                //Creando una instacia a la clase alerta dialogo
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage("Esta seguro que decea sali de la Aplicación?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // MyActivity.this.finish(); //Close  this Activity for example: MyActivity.java
//                                System.exit(0);
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // some code if you want
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alert = builder.create();
//                alert.show();
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    // TextView nombre;
   // @Override
    //protected void onCreate(Bundle savedInstanceState) {
     //   super.onCreate(savedInstanceState);
      //  setContentView(R.layout.list_agentes);
//        nombre=(TextView) findViewById(R.id.tvNombre);
//        Bundle bolsa = getIntent().getExtras();
//        nombre.setText(bolsa.getString("NOMBRE"));
//
//        //LLenando el ListView
//        ListView lv = (ListView)findViewById(R.id.listView);
//
//        ArrayList<ItemCompra> itemsCompra = obtenerItems();
//
//        ItemCompraAdapter adapter = new ItemCompraAdapter(this, itemsCompra);
//
//        lv.setAdapter(adapter);
//    private ArrayList<ItemCompra> obtenerItems() {
//        ArrayList<ItemCompra> items = new ArrayList<ItemCompra>();
//        items.add(new ItemCompra(1, "Agente Pepito", "Lorem ipsum Lorem ipsumLorem ipsumLorem ipsum", "drawable/agente"));
//        items.add(new ItemCompra(2, "Agente Juan", "Lorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsum", "drawable/agente"));
//        items.add(new ItemCompra(3, "Agente Lorena", "Lorem ipsumLorem ipsumLorem ipsumLorem ipsum", "drawable/agente"));
//        items.add(new ItemCompra(1, "Agente Pepito", "Lorem ipsum Lorem ipsumLorem ipsumLorem ipsum", "drawable/agente"));
//        items.add(new ItemCompra(2, "Agente Juan", "Lorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsum", "drawable/agente"));
//        items.add(new ItemCompra(3, "Agente Lorena", "Lorem ipsumLorem ipsumLorem ipsumLorem ipsum", "drawable/agente"));
//        items.add(new ItemCompra(1, "Agente Pepito", "Lorem ipsum Lorem ipsumLorem ipsumLorem ipsum", "drawable/agente"));
//        items.add(new ItemCompra(2, "Agente Juan", "Lorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsum", "drawable/agente"));
//        items.add(new ItemCompra(3, "Agente Lorena", "Lorem ipsumLorem ipsumLorem ipsumLorem ipsum", "drawable/agente"));
//        items.add(new ItemCompra(1, "Agente Pepito", "Lorem ipsum Lorem ipsumLorem ipsumLorem ipsum", "drawable/agente"));
//        items.add(new ItemCompra(2, "Agente Juan", "Lorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsum", "drawable/agente"));
//        items.add(new ItemCompra(3, "Agente Lorena", "Lorem ipsumLorem ipsumLorem ipsumLorem ipsum", "drawable/agente"));
//        return items;
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_profile) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
