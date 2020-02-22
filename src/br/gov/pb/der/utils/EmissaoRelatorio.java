/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.utils;

import br.gov.pb.der.config.Configurator;
import br.gov.pb.der.dao.factory._DAOFactory;
import br.gov.pb.der.dao.interfaces.NotaFiscalDao;
import br.gov.pb.der.filtros.FiltroNota;
import br.gov.pb.der.modelo.view.NotaFiscalView;
import br.gov.pb.der.services.MessageService;
import br.gov.pb.der.view.Principal;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author victo
 */
public class EmissaoRelatorio {

    private static Connection conn;
    private static Properties props;
    private static Map parametros;

    public static void carregaParametros(String titulo) throws IOException {
        props = Principal.getProperties();
        if (parametros == null) {
            parametros = new HashMap();
        }
        parametros.put("titulo_relatorio", titulo);
        parametros.put("empresa_nome", props.getProperty("empresa_nome"));
        parametros.put("empresa_setor_nome", props.getProperty("empresa_setor_nome"));
        parametros.put("empresa_localidade", props.getProperty("empresa_localidade"));
        parametros.put("empresa_endereco", props.getProperty("empresa_endereco"));
        parametros.put("empresa_logo", Configurator.PATH_IMAGES + props.getProperty("empresa_logo"));
        parametros.put("empresa_telefone", props.getProperty("empresa_telefone"));
        parametros.put("report_background", Configurator.PATH_IMAGES + props.getProperty("report_background"));
        parametros.put("equipe", props.getProperty("equipe"));
        parametros.put("SUBREPORT_DIR", Configurator.PATH_RELATORIOS + "subreport/");

    }

    public static void relatorioDeEmpresas() throws SQLException {
        //ConectarBancoDeNotas conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        JRResultSetDataSource jrRS;
        //JRBeanCollectionDataSource jrDS; 
        try {
            String query = null;
            //gerando o jasper design
            ////////////////////Coletando os dados////////////////////////
            conn = ConectarBancoDeNotas.getInstance().getConexao();
            query = "select nome, descricao from " + Configurator.BD_TABLE_PREFIX + "EMPRESA";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            //////////////////////////////////////////////////////////////
            jrRS = new JRResultSetDataSource(rs);

            carregaParametros("Empresas Cadastradas");
            JasperReport relatorio;
            relatorio = (JasperReport) JRLoader.loadObject(new FileInputStream(Configurator.PATH_RELATORIOS + "empresas.jasper"));
            JasperPrint impressao = JasperFillManager.fillReport(relatorio, parametros, jrRS);
            //exibe o resultado
            JasperViewer viewer = new JasperViewer(impressao, false);
            viewer.setTitle("Empresas");
            viewer.setVisible(true);

        } catch (IOException ex) {
            MessageService.errorMessage("Arquivo não encontrado: " + ex.getMessage(), "Erro", Principal.getInstance());
        } catch (SQLException ex) {
            MessageService.errorMessage("Erro de SQL: " + ex.getSQLState(), "Erro", Principal.getInstance());
        } catch (JRException ex) {
            MessageService.errorMessage("Erro de relatório: " + ex.getMessage(), "Erro", Principal.getInstance());
            ex.printStackTrace();
        } finally {
            ConectarBancoDeNotas.desConecta(conn);
        }

    }

    public static void relatorioDeNotasFiscaisPorFiltro(FiltroNota filtro) {
        JRDataSource jrDS;
        NotaFiscalDao daoNota = null;
        try {
            daoNota = _DAOFactory.getFactory().getNotaFiscalDao();
            List<NotaFiscalView> itensRelatorio = daoNota.reportNotasPorFiltro(filtro);
            jrDS = new JRBeanCollectionDataSource(itensRelatorio, false);
            carregaParametros("Notas Fiscais");
            JasperReport relatorio;
            relatorio = (JasperReport) JRLoader.loadObject(new FileInputStream(Configurator.PATH_RELATORIOS + "notas_fiscais.jasper"));
            JasperPrint impressao = JasperFillManager.fillReport(relatorio, parametros, jrDS);
            JasperViewer viewer = new JasperViewer(impressao, false);
            viewer.setTitle("Notas Fiscais");
            viewer.setVisible(true);
        } catch (IOException ex) {
            MessageService.errorMessage("Arquivo não encontrado: " + ex.getMessage(), "Erro", Principal.getInstance());
        } catch (JRException ex) {
            MessageService.errorMessage("Erro de relatório: " + ex.getMessage(), "Erro", Principal.getInstance());
            ex.printStackTrace();
        } finally {
            daoNota.close();
        }
    }

}
