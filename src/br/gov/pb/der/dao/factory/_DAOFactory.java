package br.gov.pb.der.dao.factory;

import br.gov.pb.der.config.Configurator;
import br.gov.pb.der.dao.interfaces.EmpresaDao;
import br.gov.pb.der.dao.interfaces.FormaPagamentoDao;
import br.gov.pb.der.dao.interfaces.ItemDao;
import br.gov.pb.der.dao.interfaces.NotaFiscalDao;
import br.gov.pb.der.dao.interfaces.SetorDao;

public abstract class _DAOFactory {
    // List of DAO types supported by the factory

    // There will be a method for each DAO that can be
    // created. The concrete factories will have to
    // implement these methods.
    public static _DAOFactory getFactory() {
        try {
            return (_DAOFactory) Configurator.FACTORY_CLASS.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public abstract EmpresaDao getEmpresaDao();

    public abstract NotaFiscalDao getNotaFiscalDao();

    public abstract SetorDao getSetorDao();

    public abstract ItemDao getItemDao();
    
    public abstract FormaPagamentoDao getFormaPagamentoDao();

//    
//    public abstract FuncaoDao getFuncaoDao();
//
}
