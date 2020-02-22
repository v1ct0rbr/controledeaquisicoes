package br.gov.pb.der.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import javax.swing.*;

public class ConectarBancoDeNotas {

    private static final String driver = "org.postgresql.Driver";
    //*****
    //Banco Local 
    private static final String url = "jdbc:postgresql://localhost:5432/NotaFiscalDER";
    //*****
    //Banco Remoto - no Servidor de Arquivos DER
    //*****
    private static final String usuario = "notafiscal";
    private static final String senha = "ditc.2012";
    private Connection conexao;
//    public Statement stm;
//    public ResultSet rs;

    private static ConectarBancoDeNotas rep;

    private ConectarBancoDeNotas() {
    }

    public static synchronized ConectarBancoDeNotas getInstance() {
        if (rep == null) {
            rep = new ConectarBancoDeNotas();
        }
        return rep;
    }

    public static Connection getConexao() throws SQLException, IOException {
        Connection conexao = null;
        Properties props = new Properties();
        props.load(new FileInputStream("config/bdconfig.properties"));

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Drive do Postgres - " + ex + " não Localizado!");
        }
        try {
            conexao = DriverManager.getConnection(props.getProperty("javax.persistence.jdbc.url"), usuario, senha);
        } catch (SQLException Fonte) {
            JOptionPane.showMessageDialog(null, "Servidor fora dor ar, entre em contato com a DIT");
        }
        return conexao;
    }

    public static void desConecta(Connection conexao) throws SQLException {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException erroSql) {
            JOptionPane.showMessageDialog(null, "Não Foi Possivel Fechar o Banco de Dados - " + erroSql);
        }
    }

}
