/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.modelo;

import br.gov.pb.der.config.Configurator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.commons.io.FileUtils;
/**
 *
 * @author victorqueiroga
 */
@Entity
@Table(name = Configurator.BD_TABLE_PREFIX + "Aquisicao")
public class Aquisicao implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String COL_PREFIX = "NF_";
    private static final String SEQUENCE = "SEQ_NOTAFISCAL";
    public static final int TAMANHO_MAXIMO_ARQUIVO = 3000000;
    public static final int MAX_CARACTERES_DESCRICAO = 512;

    private Long id;

    private String numero;

    private Calendar dataEmissao;

    private Calendar dataConclusao;

    private String descricao;

    private Empresa empresa;

    private byte[] arquivo;

    private String filePath;

    private Collection<ItemNota> itens;

    private double valor;

    private FormaPagamento formaPagamento;

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

    @Column(name = "numero", length = 40, nullable = false)
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "data_emissao")
    public Calendar getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Calendar dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "data_conclusao")
    public Calendar getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(Calendar dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    @Column(name = "descricao", length = MAX_CARACTERES_DESCRICAO, nullable = true)
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "EMPRESA_ID")
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Column(name = "FILE", unique = false, nullable = true, length = TAMANHO_MAXIMO_ARQUIVO)
    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notaFiscal", fetch = FetchType.EAGER)
    public Collection<ItemNota> getItens() {
        return itens;
    }

    public void setItens(Collection<ItemNota> itens) {
        this.itens = itens;
    }

    @Transient
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Column(name = "valor")
    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "forma_pagamento_id")
    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.numero);
        hash = 61 * hash + Objects.hashCode(this.empresa);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Aquisicao other = (Aquisicao) obj;
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.empresa, other.empresa)) {
            return false;
        }
        return true;
    }

    public double calculaValorTotal() {
        double valorTotal = 0;
        if (this.itens != null) {
            for (ItemNota it : itens) {
                valorTotal += it.getQuantidade() * it.getValor();
            }
        }
        return valorTotal;
    }

    public File getFile(byte[] arq, String nomeArquivo) {
        try {
            //converte o array de bytes em file
            //create a temp file
            File f = File.createTempFile(nomeArquivo, ".pdf");
            FileUtils.writeByteArrayToFile(f, arq);
            return f;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void readFile(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException(
                        "EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
            }
        }
        this.arquivo = buffer;
    }
}
