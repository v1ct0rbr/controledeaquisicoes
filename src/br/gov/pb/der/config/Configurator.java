/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.config;

import br.gov.pb.der.dao.factory.HibernateDaoFactory;

/**
 *
 * @author Victor_2
 */
public class Configurator {

    ////////////////////////////////////////////////////////////////////
    public static final Class FACTORY_CLASS = HibernateDaoFactory.class;
    ////////////////////////////////////////////////////////////////////
    public static final String DB_TYPE_POSTGRES = "POSTGRES";
    public static final String DB_TYPE_FIREBIRD = "FIREBIRD";
    /////////////////////////SGBD USADO///////////////////
    public static final String DB_TYPE = DB_TYPE_POSTGRES;
    //////////////////////////////////////////////////////
    public static final String PERSISTENCE_UNIT = "NotaFiscalDERPU";
    ///////////////////////////////////////////////////////////////////
    /**
     * **********************CONFIGURAÇÃO POSTGRES**************************
     */
    public static final String BD_TABLE_PREFIX = "NFDER_";
    public static final String BD_NAME = "controledeaquisicoes";
    public static final String BD_HOST = "localhost";
    public static final String BD_CONN_DRIVER_CLASS = "org.postgresql.Driver";
    public static final String BD_CONN_TYPE = "postgresql";
    public static final String BD_CONN_PORT = "5432";
    public static final String BD_CONN_USER = "victor";
    public static final String BD_CONN_PASS = "victorqueiroga";
    public static final String BD_CONN_URL = "jdbc:" + BD_CONN_TYPE + "://" + BD_HOST + ":" + BD_CONN_PORT + "/" + BD_NAME;
    public static final String BD_CONN_URL_NO_BD = "jdbc:" + BD_CONN_TYPE + "://" + BD_HOST + ":" + BD_CONN_PORT + "/";
    /**
     * **********************CONFIGURAÇÃO FIREBIRD**************************
     */
//    public static final String BD_TABLE_PREFIX = "CDC_";
//    public static final String BD_NAME = "controledechaves";
//    public static final String BD_HOST = "localhost";
//    public static final String BD_CONN_DRIVER_CLASS = "org.firebirdsql.jdbc.FBDriver";
//    public static final String BD_CONN_TYPE = "firebirdsql";
//    public static final String BD_CONN_PORT = "3050";
//    public static final String BD_CONN_USER = "victor";
//    public static final String BD_CONN_PASS = "ditc.2012";
//    public static final String BD_CONN_ADMIN_USER = "SYSDBA";
//    public static final String BD_CONN_ADMIN_PASS = "masterkey";
//    public static final String BD_PATH = "C:/" + BD_NAME;
//    public static final String BD_CONN_URL = "jdbc:" + BD_CONN_TYPE + "://" + BD_HOST + ":" + BD_CONN_PORT + "/" + BD_NAME;
//    public static final String BD_CONN_URL_NO_BD = "jdbc:" + BD_CONN_TYPE + "://" + BD_HOST + ":" + BD_CONN_PORT + "/";

    //////////////////////////////////////////////////////////////////////////
    public static final String ADMIN_NAME = "Victor Queiroga";
//    public static final String ADMIN_USER = "admin";
//    public static final String ADMIN_PASS = "admin";
    public static final String ADMIN_EMAIL = "contato@vivasoft.com.br";
    public static final String ADMIN_NAME_DEVELOPER = "Victor Henrique Queiroga de Oliveira";
    public static final String CHARSET = "UTF-8";
    public static final String LANGUAGE = "pt";
    public static final String COUNTRY = "BR";
    public static final String RESOURCES = "resources";
    public static final String PATH_IMAGES = RESOURCES + "/imagens/";
    public static final String PATH_RELATORIOS = RESOURCES + "/relatorios/";
    //////////////////////////////////////////////////////////////////////////
    public static final String DOMINIO = "br.gov.pb.der";

    public static final int MAX_RESULTS = 10;

    ///////////////FRAMES//////////////
    public static final int IFRAME_NOTAS = 1;
    public static final int IFRAME_EMPRESAS = 2;
    public static final int IFRAME_SETORES = 3;

}
