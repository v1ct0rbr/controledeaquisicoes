/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.dao.interfaces;

import br.gov.pb.der.modelo.Setor;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author victorqueiroga
 */
public interface SetorDao extends GenericDAO<Setor, Serializable> {

    public Setor findByName(String name);

}
