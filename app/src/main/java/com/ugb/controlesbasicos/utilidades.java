package com.ugb.controlesbasicos;

import java.util.Base64;

public class utilidades {
    static String url_consulta = "http://192.168.134.218:5984/amigos/_design/amigos/_view/amigos";
    static String url_mto = "http://192.168.134.218:5984/amigos";
    static String user = "Isamar";
    static String passwd = "CLAROSUGB2022";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user + ":" + passwd).getBytes());
    public String generarIdUnico(){

        return java.util.UUID.randomUUID().toString();
    }
}
