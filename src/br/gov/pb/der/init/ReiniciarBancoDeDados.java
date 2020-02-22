package br.gov.pb.der.init;

//import br.com.certiproject.utils.Funcoes;
//import br.com.certiproject.utils.Parametro;
import br.gov.pb.der.config.Configurator;
import br.gov.pb.der.utils.Funcoes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;

public class ReiniciarBancoDeDados {

//    public static final String DB_TYPE = "FIREBIRD";
    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
       
        try {
            // STEP 2: Register JDBC driver
            // Class.forName("com.mysql.jdbc.Driver");

            System.out.println("Connecting to " + Configurator.BD_CONN_TYPE
                    + " ....");
            conn = getConnection();
            conn.setAutoCommit(true);
            // STEP 4: Execute a query
            System.out.println("Creating database...");
            stmt = conn.createStatement();
            String sql = "";
            String userPassword;
            switch (Configurator.DB_TYPE) {
                case Configurator.DB_TYPE_POSTGRES:
                    userPassword = Configurator.BD_CONN_PASS + Configurator.BD_CONN_USER;
                    sql = "DO " + " $body$"
                            + " BEGIN "
                            + " IF NOT EXISTS ("
                            + " SELECT *"
                            + " FROM pg_catalog.pg_user"
                            + " WHERE  usename = '" + Configurator.BD_CONN_USER + "') THEN ";

                    sql += "CREATE ROLE " + Configurator.BD_CONN_USER + " LOGIN ENCRYPTED PASSWORD 'md5"
                            + DigestUtils.md5Hex(userPassword) + "'"
                            + " NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;";
                    sql += " END IF; "
                            + " END"
                            + " $body$;";
                    stmt.executeUpdate(sql);

                    sql = "ALTER ROLE " + Configurator.BD_CONN_USER + " WITH ENCRYPTED PASSWORD 'md5"
                            + DigestUtils.md5Hex(userPassword) + "'";
                    stmt.executeUpdate(sql);

                    sql = "DROP DATABASE IF EXISTS " + Configurator.BD_NAME;
                    stmt.executeUpdate(sql);

                    sql = "CREATE DATABASE " + Configurator.BD_NAME + " WITH OWNER = "
                            + Configurator.BD_CONN_USER + " ENCODING = '"
                            + Funcoes.noHyphen(Configurator.CHARSET) + "'";
                    stmt.executeUpdate(sql);
                    break;
                case Configurator.DB_TYPE_FIREBIRD:
                    System.exit(0);
                    break;
            }

//            sql = "DROP ROLE IF EXISTS " + Configurator.CONN_USER;
//            stmt.executeUpdate(sql);
            System.out.println("Database '" + Configurator.BD_NAME + "' created");

            Dados.iniciarDados();

            // conn.commit();
//            System.out.println("connecting in new database '"
//                    + Parametro.BD_NAME + "' ....");
//            conn = getConnection(true);
//            System.out.println("Connection established");
//            conn.close();
//            conn.setAutoCommit(true);
            // STEP 4: Execute a query
//            stmt = conn.createStatement();
//            System.out.println("Creating schemas...");
            /*
             * 
             * Cria��o de SCHEMAS DENTRO DO BANCO DE DADOS
             */
//            sql = "CREATE SCHEMA IF NOT EXISTS " + Parametro.schema_documentos
//                    + " AUTHORIZATION " + Parametro.conn_user;
//            stmt.executeUpdate(sql);
//
//            stmt.close();
//            conn.close();
            JOptionPane.showMessageDialog(null, "Database and schemas created successfully...", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ReiniciarBancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        JOptionPane.showMessageDialog(null, "Fim do Código", "Atenção",
                JOptionPane.INFORMATION_MESSAGE);
    }// end main

    public static Connection getConnection() throws ClassNotFoundException, SQLException {

        Class.forName(Configurator.BD_CONN_DRIVER_CLASS);

        // STEP 3: Open a connection
        return DriverManager.getConnection((Configurator.BD_CONN_URL_NO_BD), Configurator.BD_CONN_USER, Configurator.BD_CONN_PASS);

    }

}// end JDBCExample
