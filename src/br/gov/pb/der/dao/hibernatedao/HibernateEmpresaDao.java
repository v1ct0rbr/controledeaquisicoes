/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.dao.hibernatedao;

import br.gov.pb.der.dao.interfaces.EmpresaDao;
import br.gov.pb.der.filtros.AbstractFilter;
import br.gov.pb.der.filtros.FiltroEmpresa;
import br.gov.pb.der.modelo.Empresa;
import br.gov.pb.der.utils.Funcoes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author victorqueiroga
 */
public class HibernateEmpresaDao extends HibernateDao<Empresa, Serializable> implements EmpresaDao {

    public HibernateEmpresaDao() {
        super(Empresa.class);
    }

    @Override
    public long countByFiltro(AbstractFilter fs) {
        FiltroEmpresa filtro = (FiltroEmpresa) fs;
        Long resultado;
        try {
            open();
            CriteriaBuilder builder = getEm().getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
            Root<Empresa> empresa = criteriaQuery.from(Empresa.class);

            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getNome() != null) {
                ParameterExpression<String> paramValor = builder.parameter(String.class, "nome");
                predicates.add(builder.like(builder.lower(empresa.<String>get("nome")), paramValor));
            }
            criteriaQuery.select(builder.count(empresa.get("id")));
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            TypedQuery<Long> query = getEm().createQuery(criteriaQuery);

            if (filtro.getNome() != null) {
                query.setParameter("nome", Funcoes.toLowerCaseForLike(filtro.getNome()));
            }

//            query.setFirstResult((int) filtro.getFirstResult());
//            query.setMaxResults((int) filtro.getMaxResults());
            resultado = query.getSingleResult();
        } finally {
            close();
        }
        return resultado;
    }

    @Override
    public List<Object[]> listarPorFiltro(AbstractFilter fs) {
        FiltroEmpresa filtro = (FiltroEmpresa) fs;

        List<Object[]> resultado = new ArrayList<>();
        try {
            open();
            CriteriaBuilder builder = getEm().getCriteriaBuilder();
            CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
            Root<Empresa> empresa = criteriaQuery.from(Empresa.class);

            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getNome() != null) {
                ParameterExpression<String> paramValor = builder.parameter(String.class, "nome");
                predicates.add(builder.like(builder.lower(empresa.<String>get("nome")), paramValor));
            }

            Order order = builder.asc(empresa.<String>get("nome"));
            criteriaQuery.multiselect(empresa.<Integer>get("id"), empresa.<String>get("nome"));
            criteriaQuery.orderBy(order);
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            TypedQuery<Object[]> query = getEm().createQuery(criteriaQuery);

            if (filtro.getNome() != null) {
                query.setParameter("nome", Funcoes.toLowerCaseForLike(filtro.getNome()));
            }

            query.setFirstResult((int) filtro.getFirstResult());
            query.setMaxResults((int) filtro.getMaxResults());
            resultado = query.getResultList();
        } finally {
            close();
        }
        return resultado;
    }

    @Override
    public Empresa findByName(String name) {
        TypedQuery<Empresa> query = getEm().createQuery("SELECT e FROM Empresa e where e.nome = :name", Empresa.class);
        query.setParameter("name", name);
        Empresa empresa;
        try {
            empresa = query.getSingleResult();
        } catch (NoResultException ex) {
            System.err.println("não encontrado...ok");
            empresa = null;
        }
        return empresa;
    }

    @Override
    public List<String> findAllCombo() {
        List<String> empresasCombo = new ArrayList<>();
        try {
            open();
            Query query = getEm().createQuery("SELECT e.nome FROM Empresa e order by e.nome", String.class);
            empresasCombo = query.getResultList();
        } catch (NoResultException ex) {
            System.err.println("Lista de empresas vazia...");
        } finally {
            close();
        }
        return empresasCombo;
    }

    @Override
    public Serializable findIdByName(String name) {
        Long id = null;
        try {
            open();
            Query query = getEm().createQuery("SELECT e.id FROM Empresa e where e.nome = :name", Long.class);
            query.setParameter("name", name);
            id = (Long) query.getSingleResult();
        } catch (NoResultException ex) {
            System.err.println("Lista de empresas vazia...");
        } finally {
            close();
        }
        return id;
    }

    @Override
    public List<Empresa> reportByFilter(AbstractFilter fs) {
        FiltroEmpresa filtro = (FiltroEmpresa) fs;

        List<Empresa> resultado = new ArrayList<>();
        try {
            open();
            CriteriaBuilder builder = getEm().getCriteriaBuilder();
            CriteriaQuery<Empresa> criteriaQuery = builder.createQuery(Empresa.class);
            Root<Empresa> empresa = criteriaQuery.from(Empresa.class);

            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getNome() != null) {
                ParameterExpression<String> paramValor = builder.parameter(String.class, "nome");
                predicates.add(builder.like(builder.lower(empresa.<String>get("nome")), paramValor));
            }

            Order order = builder.asc(empresa.<String>get("nome"));
            criteriaQuery.multiselect(empresa.<Integer>get("id"), empresa.<String>get("nome"));
            criteriaQuery.orderBy(order);
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            TypedQuery<Empresa> query = getEm().createQuery(criteriaQuery);

            if (filtro.getNome() != null) {
                query.setParameter("nome", Funcoes.toLowerCaseForLike(filtro.getNome()));
            }

            query.setFirstResult((int) filtro.getFirstResult());
            query.setMaxResults((int) filtro.getMaxResults());
            resultado = query.getResultList();
        } finally {
            close();
        }
        return resultado;

    }

    @Override
    public boolean temNotasDependentes(Empresa empresa) {
        Query query = getEm().createQuery("SELECT COUNT(n.id) FROM NotaFiscal n where n.empresa.id = :empresa_id");
        query.setParameter("empresa_id", empresa.getId());
        Long codigo;
        boolean temNotasDependentes = false;
        try {
            codigo = (Long) query.getSingleResult();
            if (codigo != null && codigo > 0) {
                temNotasDependentes = true;
            }
        } catch (NoResultException ex) {
            System.err.println("não encontrado...ok");
        }
        return temNotasDependentes;
    }

}
