/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.filtros;

/**
 *
 * @author Victor_2
 */
public class FiltroEmpresa extends AbstractFilter {

    private String nome;

    public FiltroEmpresa() {
        super();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "FiltroEmpresa{" + "nome=" + nome + '}';
    }

}
