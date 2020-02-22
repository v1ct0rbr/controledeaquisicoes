/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.dao.interfaces;

import br.gov.pb.der.filtros.AbstractFilter;
import br.gov.pb.der.modelo.Aquisicao;
import br.gov.pb.der.modelo.view.ItemRelatorioView;
import br.gov.pb.der.modelo.view.NotaFiscalView;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author victorqueiroga
 */
public interface NotaFiscalDao extends GenericDAO<Aquisicao, Serializable> {

//    public List<Object[]> listarPorFiltro(AbstractFilter filtro);
    public List<NotaFiscalView> reportItensPorFiltro(AbstractFilter filtro);

    public List<NotaFiscalView> reportNotasPorFiltro(AbstractFilter filtro);

    public boolean notaFiscalJaExiste(String numero, Long codEmpresa);

}
