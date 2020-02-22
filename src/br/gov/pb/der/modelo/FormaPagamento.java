/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.modelo;

import br.gov.pb.der.config.Configurator;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author victo
 */
@Entity
@Table(name = Configurator.BD_TABLE_PREFIX + "FormaPagamento")
public class FormaPagamento implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String COL_PREFIX = "FP_";

    public static final int FP_BOLETO = 1;
    public static final int FP_DEPOSITO = 2;
    public static final int FP_DINHEIRO = 3;
    public static final int FP_CARTAO = 4;
    public static final int FP_PRAZO = 5;
    public static final int FP_INDEFINIDA = 6;

    private Integer id;

    private String nome;

    public FormaPagamento() {
    }

    public FormaPagamento(String nome) {
        this.nome = nome;
    }

    public FormaPagamento(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    @Id
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "nome", unique = true, nullable = false, length = 100)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.nome);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FormaPagamento other = (FormaPagamento) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
