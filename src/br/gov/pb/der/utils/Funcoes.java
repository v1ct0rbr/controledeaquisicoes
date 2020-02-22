/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.utils;

import br.gov.pb.der.config.Configurator;
import br.gov.pb.der.dao.factory._DAOFactory;
import br.gov.pb.der.dao.interfaces.EmpresaDao;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.persistence.PersistenceException;
import javax.swing.JTextField;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.exception.JDBCConnectionException;
import org.postgresql.util.PSQLException;

/**
 *
 * @author victorqueiroga
 */
public class Funcoes {

    public static int[] keys = {KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_DOWN};

    public static void abrirCalc() {
        try {
            Runtime rt = Runtime.getRuntime();
            //Process pr = rt.exec("cmd /c dir");
            Process pr = rt.exec("calc");
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = null;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            int exitVal = pr.waitFor();
            System.out.println("Exited with error code " + exitVal);

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    public static String calendarToString(Calendar calendardate) {
        String strdate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        if (calendardate != null) {
            strdate = sdf.format(calendardate.getTime());
        }
        return strdate;
    }

    public static String cryptPasswordSha256(String pass) {
        return DigestUtils.sha256Hex(pass);
    }

    /**
     * formata valor retirando o ponto e substituindo por um caractere vazio e a
     * vírgula por um ponto, dessa forma converte o valor em string para double
     *
     * @param valor
     * @return
     * @throws NumberFormatException
     */
    public static Double formatMoedaFloat(String valor) throws NumberFormatException {
        Double teste = Double.parseDouble(valor.replaceAll("\\.", "").replaceAll(",", "."));
        //MessageService.informationMessage("valor formatado: " + teste, "teste", null);
        return teste;
    }

    public static String formatMoedaFloat(double valor) throws NumberFormatException {
        String teste = "" + valor;
        teste = teste.replaceAll("\\.", ",");
        //MessageService.informationMessage("valor formatado: " + teste, "teste", null);
        return teste;
    }

    public static String formatMoedaToString(double money) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(money);
        return moneyString;
    }

    /**
     * type 1 - Integer; 2 - Long
     *
     * @param s
     * @param type
     * @return
     */
    public static Serializable getCodFromString(String s, int type) {
        Serializable id = null;
        if (s != null) {
            int index = s.indexOf('-');
            String part = s.substring(0, index);
            try {
                switch (type) {
                    case 1:
                        id = Integer.parseInt(part);
                        break;
                    case 2:
                        id = Long.parseLong(part);
                        break;
                }
            } catch (NumberFormatException ex) {
                System.err.println("Impossível pegar código da string");
            }
        }
        return id;
    }

    public static boolean isInArray(int num, int[] array) {
        for (int i : array) {
            if (num == i) {
                return true;
            }
        }
        return false;

    }

    public static String noHyphen(String teste) {
        return teste.replace("-", "");
    }

    public static void testarBD() throws JDBCConnectionException, PersistenceException, PSQLException, NullPointerException {
        EmpresaDao dao = _DAOFactory.getFactory().getEmpresaDao();
        dao.open(false);
        dao.close();
    }

    public static String[] toComboList(List<String> dados) {
        String[] arr;
        if (dados.size() > 0) {
            arr = new String[dados.size() + 1];
        } else {
            arr = new String[1];
        }
        arr[0] = "Selecione";
        int i = 1;
        for (String nome : dados) {
            arr[i] = nome;
            i++;
        }
        return arr;
    }

    public static String toLowerCaseForLike(String value) {
        return "%" + value.toLowerCase(new Locale(Configurator.LANGUAGE, Configurator.COUNTRY)) + "%";
    }

    public static boolean verificarUltimaPosicaoCursor(JTextField field) {
        if (field.getCaretPosition() > field.getText().length() - 1) {
            return true;
        }
        return false;
    }

}
