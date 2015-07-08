package com.dataservicios.librerias;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.File;

/**
 * Created by usuario on 11/11/2014.
 */
public final class GlobalConstant {

    public static final String DATABASE_NAME = "agenteGlobalNet";
    public static final String DOMINIO = "http://redagentesyglobalnet.com";
    //Testeo Local:
    //private static final String LOGIN_URL = "http://192.168.1.45/webservice/login.php";
    //Testeo real server:
    public static final String LOGIN_URL = DOMINIO + "/loginUser" ;
    public static final String AGENT_LIST = DOMINIO +  "/JsonAgentList";
    public static final String URL_IMAGES_AGENT = DOMINIO +  "/images/agentes/";


    //Probando
    public static final String TABLE_AGENTS = "agentes";

    public static  double latitude_open, longitude_open;

    public static String inicio,fin;

    public static  int status;

}
