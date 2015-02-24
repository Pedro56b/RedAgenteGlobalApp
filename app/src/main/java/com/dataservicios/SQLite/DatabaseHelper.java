package com.dataservicios.SQLite;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.Pedido;
import model.Publicidad;
import model.PublicidadDetalle;
import model.TipoReclamo;

/**
 * Created by usuario on 12/02/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";
    // Database Version
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "agenteGlobalNet";
    // Table Names
    private static final String TABLE_PEDIDO = "pedido";
    private static final String TABLE_PUBLICIDAD = "publicidad";
    private static final String TABLE_PUBLICIDAD_DETALLE = "publicidad_detalle";
    private static final String TABLE_TIPO_RECLAMO = "tipo_reclamo";
    //Nombre de columnas en comun
    private static final String KEY_ID = "id";
    private static final String KEY_NOMBRE = "nombre";
    // TABLE_PUBLICIDAD_DETALLE Table - column names
    private static final String KEY_PEDIDO_ID = "id_pedido";
    private static final String KEY_PUBLICIDAD_ID = "id_publicidad";
    // Table Create Statements
    // Pedido table create statement
    private static final String CREATE_TABLE_PEDIDO = "CREATE TABLE "
            + TABLE_PEDIDO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOMBRE
            + " TEXT" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_PUBLICIDAD = "CREATE TABLE " + TABLE_PUBLICIDAD
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PEDIDO_ID + " INTEGER,"
            + KEY_NOMBRE + " TEXT " + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_PUBLICIDAD_DETALLE = "CREATE TABLE "
            + TABLE_PUBLICIDAD_DETALLE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PUBLICIDAD_ID + " INTEGER,"
            + KEY_NOMBRE + " TEXT " + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_TIPO_RECLAMO= "CREATE TABLE "
            + TABLE_TIPO_RECLAMO + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NOMBRE + " TEXT " + ")";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null , DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_PEDIDO);
        db.execSQL(CREATE_TABLE_PUBLICIDAD);
        db.execSQL(CREATE_TABLE_PUBLICIDAD_DETALLE);
        db.execSQL(CREATE_TABLE_TIPO_RECLAMO);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUBLICIDAD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUBLICIDAD_DETALLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPO_RECLAMO);
        // create new tables
        onCreate(db);
    }


    // ------------------------ "Pedido" table methods ----------------//

    /*
     * Creating a todo
     */
    public long createPedido(Pedido pedido) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, pedido.getId());
        values.put(KEY_NOMBRE, pedido.getNombre());

        // insert row
        //long todo_id = db.insert(TABLE_PEDIDO, null, values);
         db.insert(TABLE_PEDIDO, null, values);

        long todo_id = pedido.getId();
        return todo_id;
    }

    /*
     * get single Pedido
     */
    public Pedido getPedido(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_PEDIDO + " WHERE "
                + KEY_ID + " = " + id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Pedido pd = new Pedido();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setNombre((c.getString(c.getColumnIndex(KEY_NOMBRE))));
        return pd;
    }

    /**
     * getting all Pedido
     * */
    public List<Pedido> getAllPedidos() {
        List<Pedido> pedidos = new ArrayList<Pedido>();
        String selectQuery = "SELECT  * FROM " + TABLE_PEDIDO;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Pedido pd = new Pedido();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setNombre((c.getString(c.getColumnIndex(KEY_NOMBRE))));

                // adding to todo list
                pedidos.add(pd);
            } while (c.moveToNext());
        }
        return pedidos;
    }

    /*
     * getting Publicidad count
     */
    public int getPedidoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PEDIDO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }
    /*
     * Updating a Pedido
     */
    public int updatePedido(Pedido pedido) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, pedido.getNombre());
        // updating row
        return db.update(TABLE_PEDIDO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(pedido.getId()) });
    }

    /*
     * Deleting a Pedido
     */
    public void deletePedido(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PEDIDO, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /*
     * Deleting all Pedido
     */
    public void deleteAllPedido() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PEDIDO, null, null );

    }

    // ------------------------ "publicidad" table methods ----------------//
    /*
     * Creating a publicidad
     */
    public long createPublicidad(Publicidad publicidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, publicidad.getId());
        values.put(KEY_PEDIDO_ID, publicidad.getIdPedido());
        values.put(KEY_NOMBRE, publicidad.getNombre());
        // insert row
        //long todo_id = db.insert(TABLE_PEDIDO, null, values);
        db.insert(TABLE_PUBLICIDAD, null, values);
        long todo_id = publicidad.getId();
        return todo_id;
    }

    /*
     * get single publicidad
     */
    public Publicidad getPublicidad(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_PUBLICIDAD+ " WHERE "
                + KEY_ID + " = " + id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Publicidad pd = new Publicidad();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setNombre((c.getString(c.getColumnIndex(KEY_NOMBRE))));
        return pd;
    }
    /**
     * getting all Publicidad por Tipo
     * */
    public List<Publicidad> getAllPublicidadTipo(long idTipo) {
        List<Publicidad> publicidad = new ArrayList<Publicidad>();
        String selectQuery = "SELECT  * FROM " + TABLE_PUBLICIDAD + " WHERE "
                + KEY_PEDIDO_ID + " = " + idTipo;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Publicidad pd = new Publicidad();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setIdPedido(c.getInt((c.getColumnIndex(KEY_PEDIDO_ID))));
                pd.setNombre((c.getString(c.getColumnIndex(KEY_NOMBRE))));

                // adding to todo list
                publicidad.add(pd);
            } while (c.moveToNext());
        }
        return publicidad;
    }
    /**
     * getting all Publicidad
     * */
    public List<Publicidad> getAllPublicidad() {
        List<Publicidad> publicidad = new ArrayList<Publicidad>();
        String selectQuery = "SELECT  * FROM " + TABLE_PUBLICIDAD;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Publicidad pd = new Publicidad();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setNombre((c.getString(c.getColumnIndex(KEY_NOMBRE))));

                // adding to todo list
                publicidad.add(pd);
            } while (c.moveToNext());
        }
        return publicidad;
    }

    /*
     * getting publicidad count
     */
    public int getPublicidadCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PUBLICIDAD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }
    /*
    * Deleting all Publicidad
    */
    public void deleteAllPublicidad() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PUBLICIDAD, null, null );

    }
    // ------------------------ "PublicidadDetalle" table methods ----------------//
    /*
     * Creating a PublicidadDetalle
     */
    public long createPublicidadDetalle(PublicidadDetalle publicidadDetalle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, publicidadDetalle.getId());
        values.put(KEY_PUBLICIDAD_ID, publicidadDetalle.getIdPublicidad());
        values.put(KEY_NOMBRE, publicidadDetalle.getNombre());
        // insert row
        //long todo_id = db.insert(TABLE_PEDIDO, null, values);
        db.insert(TABLE_PUBLICIDAD_DETALLE, null, values);
        long todo_id = publicidadDetalle.getId();
        return todo_id;
    }

    /*
     * get single publicidad
     */
    public PublicidadDetalle getPublicidadDetalle(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_PUBLICIDAD_DETALLE+ " WHERE "
                + KEY_ID + " = " + id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        PublicidadDetalle pd = new PublicidadDetalle();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setIdPublicidad(c.getInt(c.getColumnIndex(KEY_PUBLICIDAD_ID)));
        pd.setNombre((c.getString(c.getColumnIndex(KEY_NOMBRE))));
        return pd;
    }
    /**
     * getting all Publicidad por Tipo
     * */
    public List<PublicidadDetalle> getAllPublicidadDetalle(long idPublicidad) {
        List<PublicidadDetalle> publicidadDetalle = new ArrayList<PublicidadDetalle>();
        String selectQuery = "SELECT  * FROM " + TABLE_PUBLICIDAD_DETALLE + " WHERE "
                + KEY_PUBLICIDAD_ID + " = " + idPublicidad;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PublicidadDetalle pd = new PublicidadDetalle();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setIdPublicidad(c.getInt((c.getColumnIndex(KEY_PUBLICIDAD_ID))));
                pd.setNombre((c.getString(c.getColumnIndex(KEY_NOMBRE))));

                // adding to todo list
                publicidadDetalle.add(pd);
            } while (c.moveToNext());
        }
        return publicidadDetalle;
    }
    /**
     * getting all Publicidad
     * */
    public List<PublicidadDetalle> getAllPublicidadDetalle() {
        List<PublicidadDetalle> publicidadDetalle = new ArrayList<PublicidadDetalle>();
        String selectQuery = "SELECT  * FROM " + TABLE_PUBLICIDAD_DETALLE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PublicidadDetalle pd = new PublicidadDetalle();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setIdPublicidad(c.getInt((c.getColumnIndex(KEY_PUBLICIDAD_ID))));
                pd.setNombre((c.getString(c.getColumnIndex(KEY_NOMBRE))));

                // adding to todo list
                publicidadDetalle.add(pd);
            } while (c.moveToNext());
        }
        return publicidadDetalle;
    }
    /*
     * getting publicidad count
     */
    public int getPublicidadDetalleCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PUBLICIDAD_DETALLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }
    /*
    * Deleting all Publicidad
    */
    public void deleteAllPublicidadDetalle() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PUBLICIDAD_DETALLE, null, null );

    }




    // ------------------------ "Tipo_Reclamo" table methods ----------------//

    /*
     * Creating a Tipo_Reclamo
     */
    public long createTipoReclamo(TipoReclamo tipoReclamo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, tipoReclamo.getId());
        values.put(KEY_NOMBRE, tipoReclamo.getNombre());

        // insert row
        //long todo_id = db.insert(TABLE_PEDIDO, null, values);
        db.insert(TABLE_TIPO_RECLAMO, null, values);

        long todo_id = tipoReclamo.getId();
        return todo_id;
    }

    /*
     * get single Pedido
     */
    public TipoReclamo getTipoReclamo(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_TIPO_RECLAMO + " WHERE "
                + KEY_ID + " = " + id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        TipoReclamo pd = new TipoReclamo();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setNombre((c.getString(c.getColumnIndex(KEY_NOMBRE))));
        return pd;
    }

    /**
     * getting all Pedido
     * */
    public List<TipoReclamo> getAllTipoReclamo() {
        List<TipoReclamo> tipoReclamo = new ArrayList<TipoReclamo>();
        String selectQuery = "SELECT  * FROM " + TABLE_TIPO_RECLAMO;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TipoReclamo pd = new TipoReclamo();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setNombre((c.getString(c.getColumnIndex(KEY_NOMBRE))));

                // adding to todo list
                tipoReclamo.add(pd);
            } while (c.moveToNext());
        }
        return tipoReclamo;
    }

    /*
     * getting Publicidad count
     */
    public int getTipoReclamoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TIPO_RECLAMO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }
    /*
     * Updating a Pedido
     */
    public int updateTipoReclamo(TipoReclamo tipoReclamo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, tipoReclamo.getNombre());
        // updating row
        return db.update(TABLE_TIPO_RECLAMO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(tipoReclamo.getId()) });
    }

    /*
     * Deleting a Pedido
     */
    public void deleteTipoReclamo(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TIPO_RECLAMO, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
    /*
     * Deleting all Pedido
     */
    public void deleteAllTipoReclamo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TIPO_RECLAMO, null, null );
    }






    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
