/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.dao.hibernatedao;

import br.gov.pb.der.dao.interfaces.ItemDao;
import br.gov.pb.der.filtros.AbstractFilter;
import br.gov.pb.der.modelo.ItemNota;
import br.gov.pb.der.modelo.Setor;
import java.io.Serializable;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author victorqueiroga
 */
public class HibernateItemNotaDao extends HibernateDao<ItemNota, Serializable> implements ItemDao {

    public HibernateItemNotaDao() {
        super(ItemNota.class);
    }

    @Override
    public long countByFiltro(AbstractFilter filtro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ItemNota> reportByFilter(AbstractFilter filtro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Object[]> listarPorFiltro(AbstractFilter filtro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean verificaItensPorSetor(Setor setor) {
        Query query = getEm().createQuery("SELECT COUNT(it.id) FROM ItemNota it where it.setorDestino.id = :setor_id");
        query.setParameter("setor_id", setor.getId());
        Long codigo;
        boolean temItensDependentes = false;
        try {
            codigo = (Long) query.getSingleResult();
            if (codigo != null && codigo > 0) {
                temItensDependentes = true;
            }
        } catch (NoResultException ex) {
            System.err.println("não encontrado...ok");
        }
        return temItensDependentes;
    }

}
