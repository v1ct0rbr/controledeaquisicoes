/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.modelo;

import br.gov.pb.der.config.Configurator;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author victo
 */
@Entity
@Table(name = Configurator.BD_TABLE_PREFIX + "ItemNota")
public class ItemNota implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String COL_PREFIX = "ITN_";
    private static final String SEQUENCE = "SEQ_ITEMNOTA";

    private Long id;
    private String nome;
    private int quantidade;
    private double valor;
    private Aquisicao notaFiscal;
    private Setor setorDestino;
    private String setorDestinoTemp;

    public ItemNota() {
        setorDestino = new Setor();
    }

    @Id
    @SequenceGenerator(name = SEQUENCE, sequenceName = COL_PREFIX + "pk_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(length = 100, nullable = false, unique = false, name = "nome")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "quantidade", nullable = false)
    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Column(name = "valor", nullable = false)
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @ManyToOne
    @JoinColumn(name = "SETOR_ID")
    public Setor getSetorDestino() {
        return setorDestino;
    }

    public void setSetorDestino(Setor setorDestino) {
        this.setorDestino = setorDestino;
    }

    @ManyToOne
    @JoinColumn(name = "nota_id")
    public Aquisicao getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(Aquisicao notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    @Transient
    public String getSetorDestinoTemp() {
        return setorDestinoTemp;
    }

    public void setSetorDestinoTemp(String setorDestinoTemp) {
        this.setorDestinoTemp = setorDestinoTemp;
    }

}
