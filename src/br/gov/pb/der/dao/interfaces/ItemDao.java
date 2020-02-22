/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.gov.pb.der.dao.interfaces;

import br.gov.pb.der.modelo.ItemNota;
import br.gov.pb.der.modelo.Setor;
import java.io.Serializable;

/**
 *
 * @author victorqueiroga
 */
public interface ItemDao extends GenericDAO<ItemNota, Serializable>{
    
    public boolean verificaItensPorSetor(Setor setor);
}
