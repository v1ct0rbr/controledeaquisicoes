/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.modelo.view;

/**
 *
 * @author victorqueiroga
 */
public class ItemRelatorioView {

    /* ---------------------- Relacionados a nota ---------------------- */
    private String numero;
    private String empresa;
    private String dataEmissao;
    private Double valor;
    /* ---------------------- Relacionados ao item --------------------- */
    private String descricao;
    private String setorDestino;
    private int quantidade;
    private double valorUnitario;

    public ItemRelatorioView(String numero, String empresa, String dataEmissao, Double valor, String descricao, String setorDestino, int quantidade, double valorUnitario) {
        this.numero = numero;
        this.empresa = empresa;
        this.dataEmissao = dataEmissao;
        this.valor = valor;
        this.descricao = descricao;
        this.setorDestino = setorDestino;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSetorDestino() {
        return setorDestino;
    }

    public void setSetorDestino(String setorDestino) {
        this.setorDestino = setorDestino;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

}
