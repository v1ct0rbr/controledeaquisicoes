/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.filtros;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;

/**
 *
 * @author victorqueiroga
 */
public class FiltroNota extends AbstractFilter {

    private String nomeEmpresa;
    private String numeroNota;
    private Integer mes;
    private Integer ano;
    private Integer ordenacao;
    private Integer formaOrdenacao;
    private Order ordem;

    public static final int ORD_TODOS = 0;
    public static final int ORD_EMPRESA = 1;
    public static final int ORD_DATA = 2;
    public static final int ORD_VALOR = 3;
    public static final int FORMA_CRESCENTE = 0;
    public static final int FORMA_DECRESCENTE = 1;

    
    

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {
        this.numeroNota = numeroNota;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getOrdenacao() {
        return ordenacao;
    }

    public void setOrdenacao(Integer ordenacao) {
        this.ordenacao = ordenacao;
    }

    public Integer getFormaOrdenacao() {
        return formaOrdenacao;
    }

    public void setFormaOrdenacao(Integer formaOrdenacao) {
        this.formaOrdenacao = formaOrdenacao;
    }

    @Override
    public String toString() {
        return "FiltroNota{" + "nomeEmpresa=" + nomeEmpresa + ", codigoNota=" + numeroNota + '}';
    }

}
