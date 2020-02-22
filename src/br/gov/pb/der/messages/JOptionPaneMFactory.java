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
public class JOptionPaneMFactory extends _MFactory {

    @Override
    public MyMessages getMessages() {
        return new JOPMessages();
    }

    @Override
    public int yesOption() {
        return JOptionPane.YES_OPTION;
    }

    @Override
    public int noOption() {
        return JOptionPane.NO_OPTION;
    }

}
