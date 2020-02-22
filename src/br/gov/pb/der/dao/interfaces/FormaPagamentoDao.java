/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.dao.interfaces;

import br.gov.pb.der.modelo.FormaPagamento;
import java.io.Serializable;

/**
 *
 * @author victo
 */
public interface FormaPagamentoDao extends GenericDAO<FormaPagamento, Serializable> {

    public FormaPagamento findByName(String name);
}
