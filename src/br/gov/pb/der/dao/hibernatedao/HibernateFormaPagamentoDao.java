/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.dao.hibernatedao;

import br.gov.pb.der.dao.interfaces.FormaPagamentoDao;
import br.gov.pb.der.filtros.AbstractFilter;
import br.gov.pb.der.modelo.FormaPagamento;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author victo
 */
public class HibernateFormaPagamentoDao extends HibernateDao<FormaPagamento, Serializable> implements FormaPagamentoDao {

    public HibernateFormaPagamentoDao() {
        super(FormaPagamento.class);
    }

    @Override
    public long countByFiltro(AbstractFilter filtro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<FormaPagamento> reportByFilter(AbstractFilter filtro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Object[]> listarPorFiltro(AbstractFilter filtro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<FormaPagamento> list() {
        List<FormaPagamento> formasPagamento = new ArrayList<>();
        try {
            open();
            Query query;
            query = getEm().createQuery("SELECT f FROM FormaPagamento f");
            formasPagamento = query.getResultList();
        } catch (NoResultException ex) {
            System.err.println("Lista de empresas vazia...");
            formasPagamento = new ArrayList<>();
        } finally {
            close();
        }
        return formasPagamento; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FormaPagamento findByName(String name) {
        FormaPagamento forma = null;
        Query query = getEm().createQuery("SELECT f FROM FormaPagamento f where f.nome = :name");
        query.setParameter("name", name);
        try {
            forma = (FormaPagamento) query.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("Nenhum forma de pagamento encontrada!!");
        }
        return forma;
    }

}
