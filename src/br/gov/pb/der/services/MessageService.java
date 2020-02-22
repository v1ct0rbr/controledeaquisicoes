/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.services;


import br.gov.pb.der.messages.MyMessages;
import br.gov.pb.der.messages._MFactory;

/**
 *
 * @author Victor_2
 */
public class MessageService {

    private static MyMessages messages = _MFactory.getFactory().getMessages();
    public static final int YES_OPTION = _MFactory.getFactory().yesOption();
    public static final int NO_OPTION = _MFactory.getFactory().noOption();

    public static void informationMessage(String valor, String titulo, Object parent) {
        messages.informationMessage(valor, titulo, parent);
    }

    public static void errorMessage(String valor, String titulo, Object parent) {
        messages.errorMessage(valor, titulo, parent);
    }

    public static void warningMessage(String valor, String titulo, Object parent) {
        messages.warningMessage(valor, titulo, parent);
    }

    public static int confirmDialog(String valor, String titulo, Object parent) {
        return messages.confirmDialog(valor, titulo, parent);
    }
    
    public static void cadastroRealizado(Object parent){
        informationMessage("Cadastro realizado com sucesso", "INFO", parent);
    }
    public static void atualizacaoRealizada(Object parent){
        informationMessage("Atualização realizada com sucesso", "INFO", parent);
    }

}
