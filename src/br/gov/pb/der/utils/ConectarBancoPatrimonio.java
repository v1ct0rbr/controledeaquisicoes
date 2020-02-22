/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author victorqueiroga
 */
public class ConectarBancoPatrimonio {

    private static final String driver = "org.firebirdsql.jdbc.FBDriver";
//    private static final String url = "jdbc:firebirdsql:localhost:C:\\Patrimonio\\PatrimonioSQL\\dados\\PATRIMONIOSQL.FDB";
    private static final String url = "jdbc:firebirdsql:10.10.1.1:C:\\DMP\\SAP\\dados\\PATRIMONIOSQL.FDB";

    private static final String usuario = "SYSDBA";
    private static final String senha = "masterkey";

//    private Connection conexao;
//    public Statement stm;
//    public ResultSet rs;
    private static ConectarBancoPatrimonio rep = new ConectarBancoPatrimonio();

    public static synchronized ConectarBancoPatrimonio getInstance() {
        if (rep == null) {
            rep = new ConectarBancoPatrimonio();
        }
        return rep;
    }

    public static Connection getConexao() throws SQLException {
        Connection conexao = null;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Drive do FireBird - " + ex + " não Localizado!");
        }
        try {
            conexao = DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException Fonte) {
            JOptionPane.showMessageDialog(null, "Servidor fora dor ar ou sem conexão com a rede, entre em contato com a DIT");
        }
        return conexao;
    }

    public static void desConecta(Connection conexao) throws SQLException {
        try {
            conexao.close();
        } catch (SQLException erroSql) {
            JOptionPane.showMessageDialog(null, "Não Foi Possivel Fechar o Banco de Dados - " + erroSql);
        }
    }

}
