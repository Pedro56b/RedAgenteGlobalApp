package com.dataservicios.librerias;
/**
 * Created by usuario on 11/11/2014.
 */
public final class GlobalConstant {

    public static final String DATABASE_NAME = "agenteGlobalNet";
    private static String dominio = "http://www.redagentesyglobalnet.com";
    //Testeo Local:
    //private static final String LOGIN_URL = "http://192.168.1.45/webservice/login.php";
    //Testeo real server:
    public static final String LOGIN_URL = dominio + "/loginUser" ;
    public static final String AGENT_LIST = dominio +  "/JsonAgentList";
    public static final String URL_IMAGES_AGENT = dominio +  "/images/agentes/";

    //Probando

}
