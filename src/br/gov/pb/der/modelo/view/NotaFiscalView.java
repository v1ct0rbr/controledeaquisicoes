/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.modelo.view;

import br.gov.pb.der.modelo.ItemNota;
import java.util.Collection;

/**
 *
 * @author victo
 */
public class NotaFiscalView {

    private String numero;
    private String empresa;
    private String dataEmissao;
    ////////
    private String descricaoItem;
    private String setorDestino;
    private String setorResponsavel;
    private int quantidade;
    private double valorUnitario;
    private double valorItem;
    private double valorTotal;

    public NotaFiscalView(String numero, String empresa, String dataEmissao, String descricaoItem, String setorDestino, String setorResponsavel, int quantidade, double valorUnitario, double valorItem, double valorTotal) {
        this.numero = numero;
        this.empresa = empresa;
        this.dataEmissao = dataEmissao;
        this.descricaoItem = descricaoItem;
        this.setorDestino = setorDestino;
        this.setorResponsavel = setorResponsavel;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorItem = valorItem;
        this.valorTotal = valorTotal;
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

    public String getDescricaoItem() {
        return descricaoItem;
    }

    public void setDescricaoItem(String descricaoItem) {
        this.descricaoItem = descricaoItem;
    }

    public String getSetorDestino() {
        return setorDestino;
    }

    public void setSetorDestino(String setorDestino) {
        this.setorDestino = setorDestino;
    }

    public String getSetorResponsavel() {
        return setorResponsavel;
    }

    public void setSetorResponsavel(String setorResponsavel) {
        this.setorResponsavel = setorResponsavel;
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

    public double getValorItem() {
        return valorItem;
    }

    public void setValorItem(double valorItem) {
        this.valorItem = valorItem;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

}
