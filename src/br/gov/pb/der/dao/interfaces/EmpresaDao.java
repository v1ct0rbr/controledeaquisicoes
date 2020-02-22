/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.dao.interfaces;

import br.gov.pb.der.filtros.AbstractFilter;
import br.gov.pb.der.modelo.Empresa;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Victor_2
 */
public interface EmpresaDao extends GenericDAO<Empresa, Serializable> {

    public Empresa findByName(String name);

    public Serializable findIdByName(String name);

    public boolean temNotasDependentes(Empresa empresa);

}
