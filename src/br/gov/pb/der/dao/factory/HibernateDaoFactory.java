/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.dao.factory;

import br.gov.pb.der.dao.hibernatedao.HibernateEmpresaDao;
import br.gov.pb.der.dao.hibernatedao.HibernateFormaPagamentoDao;
import br.gov.pb.der.dao.hibernatedao.HibernateItemNotaDao;
import br.gov.pb.der.dao.hibernatedao.HibernateNotaFiscalDao;
import br.gov.pb.der.dao.hibernatedao.HibernateSetorDao;
import br.gov.pb.der.dao.interfaces.EmpresaDao;
import br.gov.pb.der.dao.interfaces.FormaPagamentoDao;
import br.gov.pb.der.dao.interfaces.ItemDao;
import br.gov.pb.der.dao.interfaces.NotaFiscalDao;
import br.gov.pb.der.dao.interfaces.SetorDao;

/**
 *
 * @author Victor_2
 */
public class HibernateDaoFactory extends _DAOFactory {

    @Override
    public EmpresaDao getEmpresaDao() {
        return new HibernateEmpresaDao();
    }

    @Override
    public NotaFiscalDao getNotaFiscalDao() {
        return new HibernateNotaFiscalDao(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SetorDao getSetorDao() {
        return new HibernateSetorDao(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ItemDao getItemDao() {
        return new HibernateItemNotaDao();
    }

    @Override
    public FormaPagamentoDao getFormaPagamentoDao() {
        return new HibernateFormaPagamentoDao();
    }

}
