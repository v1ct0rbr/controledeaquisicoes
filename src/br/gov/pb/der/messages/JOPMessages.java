/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.messages;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Victor_2
 */
public class JOPMessages implements MyMessages {

    @Override
    public void informationMessage(String valor, String titulo, Object parent) {
        JOptionPane.showMessageDialog((Component) parent, valor, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void errorMessage(String valor, String titulo, Object parent) {
        JOptionPane.showMessageDialog((Component) parent, valor, titulo, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void warningMessage(String valor, String titulo, Object parent) {
        JOptionPane.showMessageDialog((Component) parent, valor, titulo, JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public int confirmDialog(String valor, String titulo, Object parent) {
        return JOptionPane.showConfirmDialog((Component) parent, valor, titulo, JOptionPane.YES_NO_OPTION);
    }
}
