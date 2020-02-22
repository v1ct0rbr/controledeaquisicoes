/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.messages;

/**
 *
 * @author Victor_2
 */
public abstract class _MFactory {

    public static final Class FACTORY_CLASS = JOptionPaneMFactory.class;

    public abstract int yesOption();

    public abstract int noOption();

    public static _MFactory getFactory() {
        try {

            return (_MFactory) FACTORY_CLASS.newInstance();

        } catch (InstantiationException e) {

            // TODO Auto-generated catch block
            throw new RuntimeException();

        } catch (IllegalAccessException e) {

            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public abstract MyMessages getMessages();

}
