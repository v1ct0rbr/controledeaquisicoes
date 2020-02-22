/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.dao.hibernatedao;

import br.gov.pb.der.dao.interfaces.NotaFiscalDao;
import br.gov.pb.der.filtros.AbstractFilter;
import br.gov.pb.der.filtros.FiltroNota;
import br.gov.pb.der.modelo.Empresa;
import br.gov.pb.der.modelo.ItemNota;
import br.gov.pb.der.modelo.Aquisicao;
import br.gov.pb.der.modelo.Setor;
import br.gov.pb.der.modelo.view.NotaFiscalView;
import br.gov.pb.der.utils.Funcoes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
 *
 */
public class HibernateNotaFiscalDao extends HibernateDao<Aquisicao, Serializable> implements NotaFiscalDao {

    public HibernateNotaFiscalDao() {
        super(Aquisicao.class);
    }

    @Override
    public long countByFiltro(AbstractFilter filtro) {
        FiltroNota fs = (FiltroNota) filtro;
        Long resultado;
        try {
            open();
            CriteriaBuilder builder = getEm().getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
            Root<Aquisicao> nota = criteriaQuery.from(Aquisicao.class);
            Join<Aquisicao, Empresa> empresa = nota.join("empresa", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();

            if (fs.getNumeroNota() != null) {
                ParameterExpression<String> paramValor = builder.parameter(String.class, "numero");
                predicates.add(builder.like(builder.lower(nota.<String>get("numero")), paramValor));
            }
            if (fs.getNomeEmpresa() != null) {
                ParameterExpression<String> paramValor = builder.parameter(String.class, "nome");
                predicates.add(builder.like(builder.lower(empresa.<String>get("nome")), paramValor));
            }

            if (fs.getMes() != null) {
                ParameterExpression<Integer> paramValor = builder.parameter(Integer.class, "mes");
                predicates.add(builder.equal(builder.function("month", Integer.class, nota.<Calendar>get("dataEmissao")), paramValor));
            }
            if (fs.getAno() != null) {
                ParameterExpression<Integer> paramValor = builder.parameter(Integer.class, "ano");
                predicates.add(builder.equal(builder.function("year", Integer.class, nota.<Calendar>get("dataEmissao")), paramValor));
            }

            criteriaQuery.select(builder.count(nota.get("id")));
            criteriaQuery.where(predicates.toArray(new Predicate[0]));

            TypedQuery<Long> query = getEm().createQuery(criteriaQuery);

            if (fs.getNumeroNota() != null) {
                query.setParameter("numero", Funcoes.toLowerCaseForLike(fs.getNumeroNota()));
            }
            if (fs.getNomeEmpresa() != null) {
                query.setParameter("nome", Funcoes.toLowerCaseForLike(fs.getNomeEmpresa()));
            }
            if (fs.getMes() != null) {
                query.setParameter("mes", fs.getMes());
            }
            if (fs.getAno() != null) {
                query.setParameter("ano", fs.getAno());
            }
            //    query.setFirstResult((int) filtro.getFirstResult());
//            query.setMaxResults((int) filtro.getMaxResults());
            resultado = query.getSingleResult();
        } finally {
            close();
        }
        return resultado;
    }

    @Override
    public List<Object[]> listarPorFiltro(AbstractFilter filtro) {
        FiltroNota fs = (FiltroNota) filtro;
        List<Object[]> resultado = new ArrayList<>();
        try {
            open();
            CriteriaBuilder builder = getEm().getCriteriaBuilder();
            CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
            Root<Aquisicao> nota = criteriaQuery.from(Aquisicao.class);
            Join<Aquisicao, Empresa> empresa = nota.join("empresa", JoinType.INNER);
            Join<Aquisicao, ItemNota> item = nota.join("itens", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (fs.getNumeroNota() != null) {
                ParameterExpression<String> paramValor = builder.parameter(String.class, "numero");
                predicates.add(builder.like(builder.lower(nota.<String>get("numero")), paramValor));
            }
            if (fs.getNomeEmpresa() != null) {
                ParameterExpression<String> paramValor = builder.parameter(String.class, "nome");
                predicates.add(builder.like(builder.lower(empresa.<String>get("nome")), paramValor));
            }

            if (fs.getMes() != null) {
                ParameterExpression<Integer> paramValor = builder.parameter(Integer.class, "mes");
                predicates.add(builder.equal(builder.function("month", Integer.class, nota.<Calendar>get("dataEmissao")), paramValor));
            }
            if (fs.getAno() != null) {
                ParameterExpression<Integer> paramValor = builder.parameter(Integer.class, "ano");
                predicates.add(builder.equal(builder.function("year", Integer.class, nota.<Calendar>get("dataEmissao")), paramValor));
            }
            Order order = builder.asc(nota.<String>get("numero"));
            criteriaQuery.multiselect(nota.<Long>get("id"), nota.<String>get("numero"), empresa.<String>get("nome"),
                    builder.function("TO_CHAR", String.class, nota.<Calendar>get("dataEmissao"), builder.literal("dd/MM/yyyy")),
                    builder.sum(builder.prod(item.<Integer>get("quantidade"), item.<Double>get("valor"))).alias("total"));
            criteriaQuery.orderBy(order);
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            criteriaQuery.groupBy(nota.<Long>get("id"), empresa.<String>get("nome"));

            if (fs.getOrdenacao() != null && fs.getOrdenacao() > 0) {
                Order ordem = null;
                switch (fs.getOrdenacao()) {
                    case FiltroNota.ORD_EMPRESA:
                        ordem = (fs.getFormaOrdenacao() == FiltroNota.FORMA_CRESCENTE ? builder.asc(empresa.get("nome")) : builder.desc(empresa.get("nome")));
                        break;
                    case FiltroNota.ORD_DATA:
                        ordem = (fs.getFormaOrdenacao() == FiltroNota.FORMA_CRESCENTE ? builder.asc(nota.get("dataEmissao")) : builder.desc(nota.get("dataEmissao")));
                        break;
                    case FiltroNota.ORD_VALOR:
                        ordem = (fs.getFormaOrdenacao() == FiltroNota.FORMA_CRESCENTE ? builder.asc(nota.get("valor")) : builder.desc(nota.get("valor")));
                        break;
                }
                criteriaQuery.orderBy(ordem);
            }

            TypedQuery<Object[]> query = getEm().createQuery(criteriaQuery);

            if (fs.getNumeroNota() != null) {
                query.setParameter("numero", Funcoes.toLowerCaseForLike(fs.getNumeroNota()));
            }
            if (fs.getNomeEmpresa() != null) {
                query.setParameter("nome", Funcoes.toLowerCaseForLike(fs.getNomeEmpresa()));
            }
            if (fs.getMes() != null) {
                query.setParameter("mes", fs.getMes());
            }
            if (fs.getAno() != null) {
                query.setParameter("ano", fs.getAno());
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
    public List<NotaFiscalView> reportItensPorFiltro(AbstractFilter filtro) {
        FiltroNota fs = (FiltroNota) filtro;
        List<NotaFiscalView> notas = new ArrayList<>();
        try {
            open();
            CriteriaBuilder builder = getEm().getCriteriaBuilder();
            CriteriaQuery<NotaFiscalView> criteriaQuery = builder.createQuery(NotaFiscalView.class);
            Root<ItemNota> item = criteriaQuery.from(ItemNota.class);
            Join<ItemNota, Aquisicao> nota = item.join("notaFiscal", JoinType.INNER);
            Join<Aquisicao, Empresa> empresa = nota.join("empresa", JoinType.INNER);
            Join<ItemNota, Setor> setor = item.join("setorDestino", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (fs.getNumeroNota() != null) {
                ParameterExpression<String> paramValor = builder.parameter(String.class, "numero");
                predicates.add(builder.like(builder.lower(nota.<String>get("numero")), paramValor));
            }
            if (fs.getNomeEmpresa() != null) {
                ParameterExpression<String> paramValor = builder.parameter(String.class, "nome");
                predicates.add(builder.like(builder.lower(empresa.<String>get("nome")), paramValor));
            }
            if (fs.getMes() != null) {
                ParameterExpression<Integer> paramValor = builder.parameter(Integer.class, "mes");
                predicates.add(builder.equal(builder.function("month", Integer.class, nota.<Calendar>get("dataEmissao")), paramValor));
            }
            if (fs.getAno() != null) {
                ParameterExpression<Integer> paramValor = builder.parameter(Integer.class, "ano");
                predicates.add(builder.equal(builder.function("year", Integer.class, nota.<Calendar>get("dataEmissao")), paramValor));
            }

            Order order = builder.desc(nota.<String>get("dataEmissao"));
            criteriaQuery.select(builder.construct(NotaFiscalView.class, nota.<String>get("numero"), empresa.<String>get("nome"),
                    builder.function("TO_CHAR", String.class, nota.<Calendar>get("dataEmissao"), builder.literal("dd/MM/yyyy")),
                    item.<String>get("nome"), setor.<String>get("nome"), item.<Integer>get("quantidade"), item.<Double>get("valor"),
                    builder.prod(item.<Integer>get("quantidade"), item.<Double>get("valor")), nota.<Double>get("valor")));
            criteriaQuery.orderBy(order);
            criteriaQuery.where(predicates.toArray(new Predicate[0]));

            if (fs.getOrdenacao() != null && fs.getOrdenacao() > 0) {
                Order ordem = null;
                switch (fs.getOrdenacao()) {
                    case FiltroNota.ORD_EMPRESA:
                        ordem = (fs.getFormaOrdenacao() == FiltroNota.FORMA_CRESCENTE ? builder.asc(empresa.get("nome")) : builder.desc(empresa.get("nome")));
                        break;
                    case FiltroNota.ORD_DATA:
                        ordem = (fs.getFormaOrdenacao() == FiltroNota.FORMA_CRESCENTE ? builder.asc(nota.get("data")) : builder.desc(nota.get("data")));
                        break;
                    case FiltroNota.ORD_VALOR:
                        ordem = (fs.getFormaOrdenacao() == FiltroNota.FORMA_CRESCENTE ? builder.asc(nota.get("valor")) : builder.desc(nota.get("valor")));
                        break;
                }
                criteriaQuery.orderBy(ordem);
            }

            TypedQuery<NotaFiscalView> query = getEm().createQuery(criteriaQuery);

            if (fs.getNumeroNota() != null) {
                query.setParameter("numero", Funcoes.toLowerCaseForLike(fs.getNumeroNota()));
            }
            if (fs.getNomeEmpresa() != null) {
                query.setParameter("nome", Funcoes.toLowerCaseForLike(fs.getNomeEmpresa()));
            }
            if (fs.getMes() != null) {
                query.setParameter("mes", fs.getMes());
            }
            if (fs.getAno() != null) {
                query.setParameter("ano", fs.getAno());
            }
            query.setFirstResult(0);
            query.setMaxResults(1000);

            notas = query.getResultList();

        } finally {
            close();
        }
        return notas;
    }

    @Override
    public List<NotaFiscalView> reportNotasPorFiltro(AbstractFilter filtro) {
        FiltroNota fs = (FiltroNota) filtro;
        List<NotaFiscalView> notas = new ArrayList<>();
        try {
            open();
            CriteriaBuilder builder = getEm().getCriteriaBuilder();
            CriteriaQuery<NotaFiscalView> criteriaQuery = builder.createQuery(NotaFiscalView.class);
            Root<Aquisicao> nota = criteriaQuery.from(Aquisicao.class);
            Join<Aquisicao, ItemNota> item = nota.join("itens", JoinType.INNER);
            Join<Aquisicao, Empresa> empresa = nota.join("empresa", JoinType.INNER);
            Join<ItemNota, Setor> setor = item.join("setorDestino", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (fs.getNumeroNota() != null) {
                ParameterExpression<String> paramValor = builder.parameter(String.class, "numero");
                predicates.add(builder.like(builder.lower(nota.<String>get("numero")), paramValor));
            }
            if (fs.getNomeEmpresa() != null) {
                ParameterExpression<String> paramValor = builder.parameter(String.class, "nome");
                predicates.add(builder.like(builder.lower(empresa.<String>get("nome")), paramValor));
            }
            if (fs.getMes() != null) {
                ParameterExpression<Integer> paramValor = builder.parameter(Integer.class, "mes");
                predicates.add(builder.equal(builder.function("month", Integer.class, nota.<Calendar>get("dataEmissao")), paramValor));
            }
            if (fs.getAno() != null) {
                ParameterExpression<Integer> paramValor = builder.parameter(Integer.class, "ano");
                predicates.add(builder.equal(builder.function("year", Integer.class, nota.<Calendar>get("dataEmissao")), paramValor));
            }

            Order order = builder.desc(nota.<String>get("dataEmissao"));
            criteriaQuery.select(builder.construct(NotaFiscalView.class, nota.<String>get("numero"), empresa.<String>get("nome"),
                    builder.function("TO_CHAR", String.class, nota.<Calendar>get("dataEmissao"), builder.literal("dd/MM/yyyy")),
                    item.<String>get("nome"), setor.<String>get("nome"), setor.<String>get("responsavel"), item.<Integer>get("quantidade"), item.<Double>get("valor"),
                    builder.prod(item.<Integer>get("quantidade"), item.<Double>get("valor")), nota.<Double>get("valor")));
            criteriaQuery.orderBy(order);
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            if (fs.getOrdenacao() != null && fs.getOrdenacao() > 0) {
                Order ordem = null;
                switch (fs.getOrdenacao()) {
                    case FiltroNota.ORD_EMPRESA:
                        ordem = (fs.getFormaOrdenacao() == FiltroNota.FORMA_CRESCENTE ? builder.asc(empresa.get("nome")) : builder.desc(empresa.get("nome")));
                        break;
                    case FiltroNota.ORD_DATA:
                        ordem = (fs.getFormaOrdenacao() == FiltroNota.FORMA_CRESCENTE ? builder.asc(nota.get("dataEmissao")) : builder.desc(nota.get("dataEmissao")));
                        break;
                    case FiltroNota.ORD_VALOR:
                        ordem = (fs.getFormaOrdenacao() == FiltroNota.FORMA_CRESCENTE ? builder.asc(nota.get("valor")) : builder.desc(nota.get("valor")));
                        break;
                }
                criteriaQuery.orderBy(ordem);
            }

            TypedQuery<NotaFiscalView> query = getEm().createQuery(criteriaQuery);

            if (fs.getNumeroNota() != null) {
                query.setParameter("numero", Funcoes.toLowerCaseForLike(fs.getNumeroNota()));
            }
            if (fs.getNomeEmpresa() != null) {
                query.setParameter("nome", Funcoes.toLowerCaseForLike(fs.getNomeEmpresa()));
            }
            if (fs.getMes() != null) {
                query.setParameter("mes", fs.getMes());
            }
            if (fs.getAno() != null) {
                query.setParameter("ano", fs.getAno());
            }
            query.setFirstResult(0);
            query.setMaxResults(1000);

            notas = query.getResultList();

        } finally {
            close();
        }
        return notas;
    }

    @Override
    public List<Aquisicao> reportByFilter(AbstractFilter filtro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean notaFiscalJaExiste(String numero, Long codEmpresa) {
        Query query = getEm().createQuery("SELECT count(n.id) FROM Aquisicao n where n.numero = :numero and n.empresa.id = :empresa_id");
        query.setParameter("numero", numero);
        query.setParameter("empresa_id", codEmpresa);
        Long codigo;
        boolean jaExiste = false;
        try {
            codigo = (Long) query.getSingleResult();
            if (codigo != null && codigo > 0) {
                jaExiste = true;
            }
        } catch (NoResultException ex) {
            System.err.println("não encontrado...ok");
        }
        return jaExiste;
    }

}
