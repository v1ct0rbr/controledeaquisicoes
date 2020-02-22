/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.dao.hibernatedao;

import br.gov.pb.der.dao.interfaces.SetorDao;
import br.gov.pb.der.filtros.AbstractFilter;
import br.gov.pb.der.filtros.FiltroSetor;
import br.gov.pb.der.modelo.Empresa;
import br.gov.pb.der.modelo.Aquisicao;
import br.gov.pb.der.modelo.Setor;
import br.gov.pb.der.utils.Funcoes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author victorqueiroga
 */
public class HibernateSetorDao extends HibernateDao<Setor, Serializable> implements SetorDao {

    public HibernateSetorDao() {
        super(Setor.class);
    }

    @Override
    public long countByFiltro(AbstractFilter filtro) {
        FiltroSetor fs = (FiltroSetor) filtro;
        Long resultado;

        try {
            open();
            CriteriaBuilder builder = getEm().getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
            Root<Setor> nota = criteriaQuery.from(Setor.class);
            List<Predicate> predicates = new ArrayList<>();

            if (fs.getId() != null) {
                ParameterExpression<Integer> paramValor = builder.parameter(Integer.class, "id");
                predicates.add(builder.equal(nota.<Integer>get("id"), paramValor));
            }
            if (fs.getNome() != null) {
                ParameterExpression<String> paramValor = builder.parameter(String.class, "nome");
                predicates.add(builder.like(builder.lower(nota.<String>get("nome")), paramValor));
            }

            criteriaQuery.select(builder.count(nota.get("id")));
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            TypedQuery<Long> query = getEm().createQuery(criteriaQuery);
            if (fs.getId() != null) {
                query.setParameter("id", fs.getId());
            }
            if (fs.getNome() != null) {
                query.setParameter("nome", Funcoes.toLowerCaseForLike(fs.getNome()));
            }
            resultado = query.getSingleResult();

        } finally {
            close();
        }
        return resultado;
    }

    @Override
    public List<Setor> reportByFilter(AbstractFilter filtro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Object[]> listarPorFiltro(AbstractFilter filtro) {
        FiltroSetor fs = (FiltroSetor) filtro;
        List<Object[]> resultado = new ArrayList<>();

        try {
            open();
            CriteriaBuilder builder = getEm().getCriteriaBuilder();
            CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
            Root<Setor> setor = criteriaQuery.from(Setor.class);
            List<Predicate> predicates = new ArrayList<>();

            if (fs.getId() != null) {
                ParameterExpression<Integer> paramValor = builder.parameter(Integer.class, "id");
                predicates.add(builder.equal(setor.<Integer>get("id"), paramValor));
            }
            if (fs.getNome() != null) {
                ParameterExpression<String> paramValor = builder.parameter(String.class, "nome");
                predicates.add(builder.like(builder.lower(setor.<String>get("nome")), paramValor));
            }

            Order order = builder.asc(setor.<String>get("id"));
            criteriaQuery.multiselect(setor.<Long>get("id"), setor.<String>get("nome"), setor.<String>get("responsavel"));
            criteriaQuery.orderBy(order);
            criteriaQuery.where(predicates.toArray(new Predicate[0]));

            TypedQuery<Object[]> query = getEm().createQuery(criteriaQuery);
            if (fs.getId() != null) {
                query.setParameter("id", fs.getId());
            }
            if (fs.getNome() != null) {
                query.setParameter("nome", Funcoes.toLowerCaseForLike(fs.getNome()));
            }
            query.setFirstResult((int) fs.getFirstResult());
            query.setMaxResults((int) fs.getMaxResults());

            resultado = query.getResultList();

        } finally {
            close();
        }
        return resultado;
    }

    @Override
    public Setor findByName(String name) {
        TypedQuery<Setor> query = getEm().createQuery("SELECT s FROM Setor s where s.nome = :name", Setor.class);
        query.setParameter("name", name);
        Setor setor;
        try {
            setor = query.getSingleResult();
        } catch (NoResultException ex) {
            System.err.println("não encontrado...ok");
            setor = null;
        }
        return setor;
    }

    @Override
    public List<String> findAllCombo() {
        List<String> empresasCombo = new ArrayList<>();
        try {
            open();
            Query query = getEm().createQuery("SELECT CONCAT(s.id,'-',s.nome) FROM Setor s order by s.id", String.class);
            empresasCombo = query.getResultList();
        } catch (NoResultException ex) {
            System.err.println("Lista de empresas vazia...");
        } finally {
            close();
        }
        return empresasCombo;
    }

    

}
