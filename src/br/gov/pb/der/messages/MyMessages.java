/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.messages;

import javax.swing.JOptionPane;

/**
 *
 * @author Victor_2
 */
public interface MyMessages {

    public void informationMessage(String valor, String titulo, Object parent);

    public void errorMessage(String valor, String titulo, Object parent);

    public void warningMessage(String valor, String titulo, Object parent);

    public int confirmDialog(String valor, String titulo, Object parent);
}
