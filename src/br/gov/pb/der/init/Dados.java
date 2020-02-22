/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.init;

import br.gov.pb.der.config.Configurator;
import br.gov.pb.der.dao.factory._DAOFactory;
import br.gov.pb.der.dao.interfaces.FormaPagamentoDao;
import br.gov.pb.der.modelo.FormaPagamento;
import br.gov.pb.der.utils.Funcoes;
import java.util.ArrayList;

/**
 *
 * @author Victor_2
 */
public class Dados {

    public static void iniciarDados() {
        FormaPagamentoDao daoForma = _DAOFactory.getFactory().getFormaPagamentoDao();
        try {
            FormaPagamento forma1 = new FormaPagamento(FormaPagamento.FP_BOLETO, "Boleto");
            FormaPagamento forma2 = new FormaPagamento(FormaPagamento.FP_CARTAO, "Cartão");
            FormaPagamento forma3 = new FormaPagamento(FormaPagamento.FP_DEPOSITO, "Depósito/Transferência");
            FormaPagamento forma4 = new FormaPagamento(FormaPagamento.FP_DINHEIRO, "Dinheiro");
            FormaPagamento forma5 = new FormaPagamento(FormaPagamento.FP_PRAZO, "Prazo");
            FormaPagamento forma6 = new FormaPagamento(FormaPagamento.FP_INDEFINIDA, "Indefinida");
            daoForma.open(true);
            daoForma.persist(forma1);
            daoForma.persist(forma2);
            daoForma.persist(forma3);
            daoForma.persist(forma4);
            daoForma.persist(forma5);
            daoForma.persist(forma6);
            daoForma.commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            daoForma.close();
        }
    }

}
