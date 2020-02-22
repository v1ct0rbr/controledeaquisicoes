/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.view.internal;

import br.gov.pb.der.config.Configurator;
import br.gov.pb.der.dao.factory._DAOFactory;
import br.gov.pb.der.dao.interfaces.EmpresaDao;
import br.gov.pb.der.dao.interfaces.FormaPagamentoDao;
import br.gov.pb.der.dao.interfaces.ItemDao;
import br.gov.pb.der.dao.interfaces.NotaFiscalDao;
import br.gov.pb.der.dao.interfaces.SetorDao;
import br.gov.pb.der.filtros.AbstractFilter;
import br.gov.pb.der.filtros.FiltroNota;
import br.gov.pb.der.modelo.Aquisicao;
import br.gov.pb.der.modelo.Empresa;
import br.gov.pb.der.modelo.FormaPagamento;
import br.gov.pb.der.modelo.ItemNota;
import br.gov.pb.der.modelo.Setor;
import br.gov.pb.der.services.MessageService;
import br.gov.pb.der.utils.EmissaoRelatorio;
import br.gov.pb.der.utils.Funcoes;
import br.gov.pb.der.utils.JTextFieldLimit;
import br.gov.pb.der.utils.MyJInternalFrame;
import br.gov.pb.der.utils.NumberRenderer;
import br.gov.pb.der.utils.SplashScreen;
import br.gov.pb.der.utils.TableCellListener;
import br.gov.pb.der.view.dialog.JDItem;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author victorqueiroga
 */
public class JIFcad_Notafiscal extends MyJInternalFrame {

    /**
     * Creates new form jIfcad_Notafiscal
     */
    public static final int TAB_LIST = 1;
    public static final int TAB_FORM = 0;
    public static final int QUANT_COLUMNS_TB_ITENS = 5;
    public static final int TB_ITENS_COL_COD = 0;
    public static final int TB_ITENS_COL_NOME = 1;
    public static final int TB_ITENS_COL_SETOR = 2;
    public static final int TB_ITENS_COL_QUANT = 3;
    public static final int TB_ITENS_COL_VALOR = 4;

    private static final int caracteresRestantes = 512;

    private static JIFcad_Notafiscal jifcadNotaFiscal;
    private EmpresaDao daoEmpresa = _DAOFactory.getFactory().getEmpresaDao();
    private NotaFiscalDao daoNota = _DAOFactory.getFactory().getNotaFiscalDao();
    private SetorDao daoSetor = _DAOFactory.getFactory().getSetorDao();
    private ItemDao daoItem = _DAOFactory.getFactory().getItemDao();
    private FormaPagamentoDao daoForma = _DAOFactory.getFactory().getFormaPagamentoDao();
    DefaultListModel listModel;
    private DefaultTableModel modeloNotaFiscal;
    JComboBox jComboSetores = new JComboBox<String>();
    /////////////////////////////////////
    private static Aquisicao notafiscal = new Aquisicao();
    public static DefaultTableModel modeloItemNota;
    public static double valorTotal = 0;
    //////////////////////////////////
    Action selecionaEmpresa;
    Action editItemtTable;
    FiltroNota filtroNota;
    byte[] arquivo;
    File arq;
    File arqTemp;
    ItemNota itemNota = new ItemNota();
    Empresa empresaNotaFiscal;
    List<String> setoresCombo;
    List<Long> itensMarcadosParaExclusao = new ArrayList<>();
    //////////////
    TableCellListener tcl;

    public static JIFcad_Notafiscal getInstance() {
        if (jifcadNotaFiscal == null) {
            jifcadNotaFiscal = new JIFcad_Notafiscal();
        }

        return jifcadNotaFiscal;
    }

    public static JIFcad_Notafiscal getInstance(int tab) {
        if (jifcadNotaFiscal == null) {
            jifcadNotaFiscal = new JIFcad_Notafiscal();
        }
        jTabbedPanel.setSelectedIndex(tab);
        return jifcadNotaFiscal;
    }

    public static void freeInstance() {
        jifcadNotaFiscal = null;
    }

    public JIFcad_Notafiscal() {
        initComponents();
        initComboBoxes();
        super.setFrameIndex(Configurator.IFRAME_NOTAS);
        modeloNotaFiscal = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        modeloItemNota = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 ? false : true;
            }

        };
        jtfNota.setDocument(new JTextFieldLimit(50));
        jtaDescricao.setDocument(new JTextFieldLimit(Aquisicao.MAX_CARACTERES_DESCRICAO));
        jlbCaracteresRestantes.setText(Aquisicao.MAX_CARACTERES_DESCRICAO + "");
        listModel = new DefaultListModel();
        jlbNomeArquivo.setVisible(false);
        jlbNomeArquivo.setText("");
        jpArquivo.setVisible(false);
        selecionaEmpresa = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selecionarEmpresa();
            }
        };
        carregaEmpresas();
        carregaFormasPagamento();
        carregaSetores();
        carregaFiltroNota();
        filtraNotas();
        preparaTabelaItem();
        editItemtTable = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
//                TableCellListener tcl = (TableCellListener) e.getSource();
//                tcl.getRow(); tcl.getColumn(); tcl.getOldValue(); tcl.getNewValue();
                if (tcl.getColumn() == 3) {
                    try {
                        int teste = Integer.parseInt(tcl.getNewValue().toString().trim());
                        jtbItemNota.getColumnModel().getColumn(4).setCellRenderer(NumberRenderer.getCurrencyRenderer());
                        calcularValorTotal();
                    } catch (NumberFormatException ex) {
                        MessageService.errorMessage("Forneça uma quantidade válida!", "Erro", JIFcad_Notafiscal.getInstance(TAB_FORM));
                        modeloItemNota.setValueAt(tcl.getOldValue().toString(), tcl.getRow(), tcl.getColumn());
                    }
                }

                if (tcl.getColumn() == 4) {
                    try {
                        Double teste = Funcoes.formatMoedaFloat(tcl.getNewValue().toString().trim());
                        modeloItemNota.setValueAt(tcl.getNewValue().toString().trim(), tcl.getRow(), tcl.getColumn());
                        calcularValorTotal();
                    } catch (NumberFormatException ex) {
                        MessageService.errorMessage("Forneça um valor válido!", "Erro", JIFcad_Notafiscal.getInstance(TAB_FORM));
                        modeloItemNota.setValueAt(tcl.getOldValue().toString(), tcl.getRow(), tcl.getColumn());
                    }

                }

            }
        };
        tcl = new TableCellListener(jtbItemNota, editItemtTable);

    }

    public static void adicionarItem(Object[] item) {
        modeloItemNota.addRow(item);
        calcularValorTotal();
    }

    public static void calcularValorTotal() {
        valorTotal = 0;
        for (int i = 0; i < modeloItemNota.getRowCount(); i++) {
            valorTotal += Integer.parseInt(modeloItemNota.getValueAt(i, TB_ITENS_COL_QUANT).toString()) * Funcoes.formatMoedaFloat(modeloItemNota.getValueAt(i, TB_ITENS_COL_VALOR).toString());
        }
        jlbValorTotal.setText(Funcoes.formatMoedaToString(valorTotal));
    }

    public void carregaSetores() {
        setoresCombo = new ArrayList<>();
        setoresCombo = daoSetor.findAllCombo();
//        String[] arrSetores = new String[setoresCombo.size() + 1];
//
//        arrSetores = setoresCombo.toArray(arrSetores);
//        jComboSetores = new JComboBox(arrSetores);
        jComboSetores = new JComboBox(Funcoes.toComboList(setoresCombo));

    }

    public void initComboBoxes() {
        jcbFiltroMes.addItem("Todos");
        for (int mes = 1; mes <= 12; mes++) {
            jcbFiltroMes.addItem(mes);
        }
        jcbFiltroAno.addItem("Todos");

        for (int years = Calendar.getInstance().get(Calendar.YEAR); years > 1980; years--) {
            jcbFiltroAno.addItem(years);
        }
    }

    public void carregaItensNota() {
        preparaTabelaItem();
        if (notafiscal.getItens() != null && !notafiscal.getItens().isEmpty()) {
            for (ItemNota it : notafiscal.getItens()) {
                Object[] item = new Object[QUANT_COLUMNS_TB_ITENS];
                item[0] = it.getId();
                item[1] = it.getNome();
                item[2] = it.getId() + "-" + it.getSetorDestino();
                item[3] = it.getQuantidade();
                item[4] = it.getValor();
                adicionarItem(item);
            }
        }
    }

    public void carregaEmpresas() {
        List<String> empresasCombo = new ArrayList<>();
        empresasCombo = daoEmpresa.findAllCombo();
        jcbEmpresasCombo.removeAllItems();
        jcbEmpresasCombo.addItem("Selecione");
        jcbFiltroEmpresasCombo.removeAllItems();
        jcbFiltroEmpresasCombo.addItem("Todos");
        for (String emp : empresasCombo) {
            jcbEmpresasCombo.addItem(emp);
            jcbFiltroEmpresasCombo.addItem(emp);
        }
        if (notafiscal != null && notafiscal.getEmpresa() != null) {
            empresaNotaFiscal = daoEmpresa.findById(notafiscal.getEmpresa().getId());
            if (empresaNotaFiscal != null) {
                jcbEmpresasCombo.setSelectedItem(empresaNotaFiscal.getNome());
            }
        }

    }

    public void carregaFormasPagamento() {
        List<FormaPagamento> formas = new ArrayList<FormaPagamento>();
        formas = daoForma.list();
        jcbFormasPagamento.removeAllItems();
        if (!formas.isEmpty()) {
            for (FormaPagamento formaPagamento : formas) {
                jcbFormasPagamento.addItem(formaPagamento);
            }
            if (notafiscal != null && notafiscal.getFormaPagamento() != null) {
                jcbFormasPagamento.setSelectedItem(notafiscal.getFormaPagamento());
            }
        }
    }

    public void carregaNotas(List<Object[]> notas) {
        for (Object[] nota : notas) {
            modeloNotaFiscal.addRow(nota);
        }
    }

    public void carregaFiltroNota() {
        filtroNota = new FiltroNota();
        if (!jtfFiltroNumero.getText().trim().isEmpty()) {
            filtroNota.setNumeroNota(jtfFiltroNumero.getText().trim());
        }

        if (jcbFiltroEmpresasCombo.getSelectedIndex() > 0) {
            filtroNota.setNomeEmpresa(jcbFiltroEmpresasCombo.getSelectedItem().toString());
        }

        if (jcbFiltroMes.getSelectedIndex() > 0) {
            filtroNota.setMes(Integer.parseInt(jcbFiltroMes.getSelectedItem().toString()));
        }
        if (jcbFiltroAno.getSelectedIndex() > 0) {
            filtroNota.setAno(Integer.parseInt(jcbFiltroAno.getSelectedItem().toString()));
        }
        if (jcbFiltroOrdenacao.getSelectedIndex() > 0) {
            filtroNota.setOrdenacao(jcbFiltroOrdenacao.getSelectedIndex());
            filtroNota.setFormaOrdenacao(jcbFiltroFormaOrdenacao.getSelectedIndex());
        }

        System.out.println("\n\nMes: " + jcbFiltroAno.getSelectedItem().toString() + "; Ano: " + jcbFiltroMes.getSelectedItem().toString());

        filtroNota.changePage(Configurator.MAX_RESULTS, AbstractFilter.FIRST);
        filtroNota.setTotalRegistros(daoNota.countByFiltro(filtroNota));
        filtroNota.setNumPages((int) ((filtroNota.getTotalRegistros() + (filtroNota.getMaxResults() - 1)) / (filtroNota.getMaxResults())));
    }

    public boolean excluirNota(Long codNota) {
        Aquisicao nota;
        boolean exclusaoOk = false;
        if (MessageService.confirmDialog("Excluir Nota selecionada?", "Atenção", this) == MessageService.YES_OPTION) {
            try {
                daoNota.open(true);
                nota = daoNota.findByIdNoConnection(codNota);
                if (nota != null) {
                    daoNota.delete(nota);
                    jbtNovo.doClick();
                }
                daoEmpresa.commit();
                MessageService.informationMessage("Exclusão realizada com sucesso!", "Atenção", this);
                carregaFiltroNota();
                filtraNotas();
                jtbNotas.requestFocusInWindow();
                exclusaoOk = true;
            } catch (Exception e) {
                MessageService.errorMessage("Erro na exclusão da Nota: \n" + e.getMessage(), "Erro", this);
                daoEmpresa.rollback();
            } finally {
                daoEmpresa.close();
            }
        }
        return exclusaoOk;
    }

    /**
     * botão de filtrar notas
     */
    public void filtraNotas() {
        List<Object[]> _notas;
        //carregaFiltroNota();
        preparaTabelaNota();
        _notas = daoNota.listarPorFiltro(filtroNota);
        if (_notas != null && !_notas.isEmpty()) {
            carregaNotas(_notas);
        }
        jlbPaginationAtual.setText("" + (filtroNota.getCurrentPage() + 1));
        if (filtroNota.getNumPages() == 0) {
            filtroNota.setNumPages(1);
        }
        jlbNumPaginas.setText("" + filtroNota.getNumPages());
    }

    public List<ItemNota> getItensFromTable() {
        List<ItemNota> itens = new ArrayList<>();
        for (int i = 0; i < modeloItemNota.getRowCount(); i++) {
            ItemNota item = new ItemNota();
            Setor set = null;
            item = new ItemNota();
            if (!modeloItemNota.getValueAt(i, 0).toString().equals("-")) {
                item.setId(Long.parseLong(modeloItemNota.getValueAt(i, TB_ITENS_COL_COD).toString()));
                item = daoItem.findByIdNoConnection(item.getId());
            }
            if (!modeloItemNota.getValueAt(i, TB_ITENS_COL_SETOR).toString().equals("Selecione")) {
                item.setSetorDestino(daoSetor.findByIdNoConnection(Funcoes.getCodFromString(modeloItemNota.getValueAt(i, TB_ITENS_COL_SETOR).toString(), 1)));
            } else {
                item.setSetorDestino(null);
            }
            item.setNome(modeloItemNota.getValueAt(i, TB_ITENS_COL_NOME).toString());
            item.setQuantidade(Integer.parseInt(modeloItemNota.getValueAt(i, TB_ITENS_COL_QUANT).toString()));
            item.setValor(Funcoes.formatMoedaFloat(modeloItemNota.getValueAt(i, TB_ITENS_COL_VALOR).toString()));
            item.setNotaFiscal(notafiscal);
            itens.add(item);

        }
        return itens;
    }

    public void preparaTabelaItem() {
        jtbItemNota.setModel(modeloItemNota);
        modeloItemNota.addColumn("CÓDIGO");
        modeloItemNota.addColumn("DESCRIÇÃO");
        modeloItemNota.addColumn("SETOR DE DESTINO");
        modeloItemNota.addColumn("QUANTIDADE");
        modeloItemNota.addColumn("(R$) VAL. UNITÁRIO");
        modeloItemNota.setNumRows(0);
        modeloItemNota.setColumnCount(QUANT_COLUMNS_TB_ITENS);
        jtbItemNota.getColumnModel().getColumn(0).setPreferredWidth(5);
        jtbItemNota.getColumnModel().getColumn(1).setPreferredWidth(200);
        jtbItemNota.getColumnModel().getColumn(2).setPreferredWidth(200);
        jtbItemNota.getColumnModel().getColumn(3).setPreferredWidth(5);
        jtbItemNota.getColumnModel().getColumn(4).setPreferredWidth(10);
//        jtbItemNota.getColumnModel().getColumn(4).setCellRenderer(NumberRenderer.getCurrencyRenderer());
        TableColumn tc = jtbItemNota.getColumnModel().getColumn(2);
        tc.setCellEditor(new DefaultCellEditor(jComboSetores));

    }

    public void preparaTabelaNota() {
        jtbNotas.setModel(modeloNotaFiscal);
        modeloNotaFiscal.addColumn("ID");
        modeloNotaFiscal.addColumn("CÓDIGO - NF");
        modeloNotaFiscal.addColumn("EMPRESA");
        modeloNotaFiscal.addColumn("DATA");
        modeloNotaFiscal.addColumn("VALOR");
        modeloNotaFiscal.setNumRows(0);
        modeloNotaFiscal.setColumnCount(5);
        jtbNotas.getColumnModel().getColumn(0).setPreferredWidth(5);
        jtbNotas.getColumnModel().getColumn(1).setPreferredWidth(100);
        jtbNotas.getColumnModel().getColumn(2).setPreferredWidth(200);
        jtbNotas.getColumnModel().getColumn(3).setPreferredWidth(100);
        jtbNotas.getColumnModel().getColumn(4).setPreferredWidth(100);
        jtbNotas.getColumnModel().getColumn(4).setCellRenderer(NumberRenderer.getCurrencyRenderer());
//        jtbNotas.getColumnModel().getColumn(3).setCellRenderer(NumberRenderer.getCurrencyRenderer());
        //jtbNotas.setAutoCreateRowSorter(true);

    }

    public void prepararAquivoParaSalvar() {

        if (arq != null) {
            arquivo = new byte[(int) arq.length()];
            try {
                FileInputStream is;
                is = new FileInputStream(arq);
                arquivo = new byte[(int) arq.length()];
                int offset = 0;
                int numRead = 0;
                while (offset < arquivo.length
                        && (numRead = is.read(arquivo, offset, arquivo.length - offset)) >= 0) {
                    offset += numRead;
                }
            } catch (IOException ex) {
                Logger.getLogger(JIFcad_Notafiscal.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void prepararFormulario() {
        preparaTabelaItem();
        jlbValorTotal.setText("R$ " + 0);
        if (notafiscal != null && notafiscal.getId() != null) {
            jtfCodNota.setText("" + notafiscal.getId());
            jtfNota.setText(notafiscal.getNumero());
            if (notafiscal.getDescricao() != null) {
                jlbCaracteresRestantes.setText((Aquisicao.MAX_CARACTERES_DESCRICAO - notafiscal.getDescricao().length()) + "");
            }
            jcbEmpresasCombo.setSelectedItem(notafiscal.getEmpresa().getNome());
            jdcData.setCalendar(notafiscal.getDataEmissao());
            jbtCadastrarNota.setText("Atualizar");

            if (notafiscal.getFormaPagamento() != null) {
                jcbFormasPagamento.setSelectedItem(notafiscal.getFormaPagamento());
            }
            if (notafiscal.getArquivo() != null && notafiscal.getArquivo().length > 0) {
                jpArquivo.setVisible(true);
            } else {
                jpArquivo.setVisible(false);
            }
            for (ItemNota it : notafiscal.getItens()) {
                Object[] ob = new Object[QUANT_COLUMNS_TB_ITENS];
                ob[TB_ITENS_COL_COD] = it.getId();
                ob[TB_ITENS_COL_NOME] = it.getNome();
                if (it.getSetorDestino() != null) {
                    ob[TB_ITENS_COL_SETOR] = it.getSetorDestino().getId() + "-" + it.getSetorDestino().getNome();
                } else {
                    ob[TB_ITENS_COL_SETOR] = "Selecione";
                }
                ob[TB_ITENS_COL_QUANT] = it.getQuantidade();
                ob[TB_ITENS_COL_VALOR] = Funcoes.formatMoedaFloat(it.getValor());
                modeloItemNota.addRow(ob);
            }
            calcularValorTotal();
            jbtExcluirNota.setEnabled(true);

        } else {
            jtfNota.setText("");
            jcbEmpresasCombo.setSelectedIndex(0);
            jdcData.setCalendar(null);
            jtaDescricao.setText("");
            jlbCaracteresRestantes.setText(Aquisicao.MAX_CARACTERES_DESCRICAO + "");
            jtfValorTotal.setText("");
            jbtCadastrarNota.setText("Cadastrar");
            jtfCodNota.setText("");
            jtfNota.requestFocusInWindow();
            jpArquivo.setVisible(false);
            jlbNomeArquivo.setVisible(false);
            jlbNomeArquivo.setText("");
        }

    }

    public void preparaListaEmpresa() {
        listModel.removeAllElements();
    }

    public void selecionarEmpresa() {
        if (jcbEmpresasCombo.getSelectedIndex() > 0) {
            String empresaSelecionada = jcbEmpresasCombo.getSelectedItem().toString();
            try {
                daoEmpresa.open();
                empresaNotaFiscal = daoEmpresa.findByName(empresaSelecionada);
                if (empresaNotaFiscal != null) {
                    jtfCodEmpresa.setText(empresaNotaFiscal.getId() + "");
                } else {
                    MessageService.errorMessage("Empresa não foi encontrada", "Erro", this);
                    carregaEmpresas();
                }
            } catch (Exception ex) {
                MessageService.errorMessage("Erro (" + ex.getMessage(), "Erro", this);
            } finally {
                daoEmpresa.close();
            }
        }
    }

    public boolean validarCampos() {
        String erros = "";

        if (jtfNota.getText().trim().isEmpty()) {
            erros += " - O numero da nota fiscal deve ser fornecido\n";
        }

//        if (modeloItemNota.getRowCount() == 0) {
//            erros += " - A nota deve ter pelo menos um item cadastrado\n";
//        }
        if (jtfCodEmpresa.getText().isEmpty()) {
            erros += " - Selecione uma Empresa\n";
        }

        if (jdcData.getCalendar() == null) {
            erros += " - Forneça a data de emissão Data\n";
        }
        if (jtfCodNota.getText().trim().isEmpty() && !jtfNota.getText().trim().isEmpty() && !jtfCodEmpresa.getText().isEmpty()) {
            if (daoNota.notaFiscalJaExiste(jtfNota.getText().trim(), Long.parseLong(jtfCodEmpresa.getText().trim()))) {
                erros += " - Nota fiscal de número \""
                        + jtfNota.getText().trim() + "\" para a empresa \""
                        + jcbEmpresasCombo.getSelectedItem().toString() + "\" já existe no banco de dados\n";
            }
        }

        if (!erros.isEmpty()) {
            MessageService.errorMessage(erros, "Erros foram encontrados", this);
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane4 = new javax.swing.JScrollPane();
        jTabbedPanel = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jbtNovo = new javax.swing.JButton();
        jbtCadastrarNota = new javax.swing.JButton();
        jbtExcluirNota = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtfNota = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jdcData = new com.toedter.calendar.JDateChooser();
        jtfCodNota = new javax.swing.JTextField();
        jtfCodEmpresa = new javax.swing.JTextField();
        jcbEmpresasCombo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jbtRecarregarEmpresas = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jdcDataFinal = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jcbFormasPagamento = new javax.swing.JComboBox();
        jbtRecarregarFormasPagamento = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jtfValorTotal = new javax.swing.JFormattedTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaDescricao = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        jlbCaracteresRestantes = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jbtSelecionaArquivo = new javax.swing.JButton();
        jlbNomeArquivo = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtbItemNota = new javax.swing.JTable();
        jbtItemAdicionar = new javax.swing.JButton();
        jbtItemExcluir = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jlbValorTotal = new javax.swing.JLabel();
        jpArquivo = new javax.swing.JPanel();
        jbtVisualizarArquivo = new javax.swing.JButton();
        jbtExcluirArquivo = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbNotas = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jbtFirst = new javax.swing.JButton();
        jbtPrevious = new javax.swing.JButton();
        jbtNext = new javax.swing.JButton();
        jbtLast = new javax.swing.JButton();
        jlbNumPaginas = new javax.swing.JLabel();
        jlbPaginationAtual = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jtfFiltroNumero = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jcbFiltroMes = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jcbFiltroAno = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jbtFiltrarNotas = new javax.swing.JButton();
        jbtLimpar = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jcbFiltroOrdenacao = new javax.swing.JComboBox();
        jcbFiltroFormaOrdenacao = new javax.swing.JComboBox();
        jcbFiltroEmpresasCombo = new javax.swing.JComboBox<>();
        jbtRecarregarEmpresas1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jbtEdit = new javax.swing.JButton();
        jbtDelete = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Controle de Aquisições");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jScrollPane4.setPreferredSize(new java.awt.Dimension(1414, 632));

        jTabbedPanel.setPreferredSize(new java.awt.Dimension(1212, 600));

        jbtNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/new.png"))); // NOI18N
        jbtNovo.setText("Limpar campos");
        jbtNovo.setToolTipText("");
        jbtNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtNovoActionPerformed(evt);
            }
        });

        jbtCadastrarNota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/disk.png"))); // NOI18N
        jbtCadastrarNota.setText("Cadastrar");
        jbtCadastrarNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCadastrarNotaActionPerformed(evt);
            }
        });

        jbtExcluirNota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/delete.png"))); // NOI18N
        jbtExcluirNota.setText("Excluir nota");
        jbtExcluirNota.setEnabled(false);
        jbtExcluirNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtExcluirNotaActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "INFORMAÇÕES PRINCIPAIS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("NotaFiscal");

        jtfNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNotaActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Dt. de Emissão");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jdcData.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jdcData.setDate(Calendar.getInstance().getTime());
        jdcData.setMinSelectableDate(new java.util.Date(315547287000L));

        jtfCodNota.setEnabled(false);
        jtfCodNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfCodNotaActionPerformed(evt);
            }
        });

        jtfCodEmpresa.setEditable(false);

        jcbEmpresasCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbEmpresasComboActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Empresa");

        jbtRecarregarEmpresas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/refresh.png"))); // NOI18N
        jbtRecarregarEmpresas.setToolTipText("recarregar empresas");
        jbtRecarregarEmpresas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtRecarregarEmpresasActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Dt. Final");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jdcDataFinal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jdcDataFinal.setMinSelectableDate(new java.util.Date(315547287000L));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("Forma de Pagamento");
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jbtRecarregarFormasPagamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/refresh.png"))); // NOI18N
        jbtRecarregarFormasPagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtRecarregarFormasPagamentoActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Valo da Nota (R$)");

        jtfValorTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jtfValorTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jtfValorTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jtaDescricao.setColumns(20);
        jtaDescricao.setRows(5);
        jtaDescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtaDescricaoKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jtaDescricao);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setText("Caracteres restantes:");

        jlbCaracteresRestantes.setText("0");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel17.setText("Observações");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(jtfValorTotal))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(71, 71, 71)
                                .addComponent(jdcDataFinal, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(34, 34, 34)
                                .addComponent(jdcData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jcbFormasPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(59, 59, 59)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jcbEmpresasCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtRecarregarEmpresas, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jtfNota))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfCodNota, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfCodEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtRecarregarFormasPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlbCaracteresRestantes))
                    .addComponent(jLabel17))
                .addGap(25, 25, 25))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jtfNota)
                                    .addComponent(jtfCodNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jcbEmpresasCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel3)))
                                    .addComponent(jtfCodEmpresa)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jbtRecarregarEmpresas, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jdcData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jcbFormasPagamento)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jdcDataFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtRecarregarFormasPagamento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtfValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jlbCaracteresRestantes))))
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ARQUIVO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jbtSelecionaArquivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/select_file.png"))); // NOI18N
        jbtSelecionaArquivo.setText("Upload de arquivo");
        jbtSelecionaArquivo.setToolTipText("");
        jbtSelecionaArquivo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jbtSelecionaArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSelecionaArquivoActionPerformed(evt);
            }
        });

        jlbNomeArquivo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlbNomeArquivo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlbNomeArquivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/preview.png"))); // NOI18N
        jlbNomeArquivo.setToolTipText("Abrir arquivo selecionado");
        jlbNomeArquivo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlbNomeArquivo.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jlbNomeArquivo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlbNomeArquivoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtSelecionaArquivo)
                .addGap(18, 18, 18)
                .addComponent(jlbNomeArquivo, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
                .addGap(160, 160, 160))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtSelecionaArquivo)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jlbNomeArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ITENS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jScrollPane3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jScrollPane3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane3MouseClicked(evt);
            }
        });

        jtbItemNota.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtbItemNota.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jtbItemNota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbItemNotaMouseClicked(evt);
            }
        });
        jtbItemNota.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jtbItemNotaPropertyChange(evt);
            }
        });
        jScrollPane3.setViewportView(jtbItemNota);

        jbtItemAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/add.png"))); // NOI18N
        jbtItemAdicionar.setText("Incluir Item");
        jbtItemAdicionar.setAlignmentX(0.5F);
        jbtItemAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtItemAdicionarActionPerformed(evt);
            }
        });

        jbtItemExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/delete.png"))); // NOI18N
        jbtItemExcluir.setText("Excluir Item");
        jbtItemExcluir.setEnabled(false);
        jbtItemExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtItemExcluirActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Soma dos Itens:");

        jlbValorTotal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jlbValorTotal.setForeground(new java.awt.Color(0, 51, 153));
        jlbValorTotal.setText("R$ 0");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jbtItemAdicionar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtItemExcluir)
                        .addGap(132, 132, 132)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlbValorTotal)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtItemAdicionar)
                    .addComponent(jbtItemExcluir)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlbValorTotal))
                .addContainerGap())
        );

        jbtVisualizarArquivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/pdf.png"))); // NOI18N
        jbtVisualizarArquivo.setToolTipText("Arquivo salvo");
        jbtVisualizarArquivo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtVisualizarArquivo.setMaximumSize(new java.awt.Dimension(65, 37));
        jbtVisualizarArquivo.setMinimumSize(new java.awt.Dimension(65, 37));
        jbtVisualizarArquivo.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtVisualizarArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtVisualizarArquivoActionPerformed(evt);
            }
        });

        jbtExcluirArquivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/delete_32x32.png"))); // NOI18N
        jbtExcluirArquivo.setToolTipText("Excluir arquivo salvo");
        jbtExcluirArquivo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtExcluirArquivo.setMaximumSize(new java.awt.Dimension(83, 29));
        jbtExcluirArquivo.setMinimumSize(new java.awt.Dimension(83, 29));
        jbtExcluirArquivo.setPreferredSize(new java.awt.Dimension(83, 29));
        jbtExcluirArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtExcluirArquivoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpArquivoLayout = new javax.swing.GroupLayout(jpArquivo);
        jpArquivo.setLayout(jpArquivoLayout);
        jpArquivoLayout.setHorizontalGroup(
            jpArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpArquivoLayout.createSequentialGroup()
                .addComponent(jbtVisualizarArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtExcluirArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jSeparator3)
        );
        jpArquivoLayout.setVerticalGroup(
            jpArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpArquivoLayout.createSequentialGroup()
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtVisualizarArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtExcluirArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbtNovo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtCadastrarNota, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtExcluirNota, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpArquivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(177, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jbtNovo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbtCadastrarNota)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbtExcluirNota)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jpArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        jTabbedPanel.addTab("Cadastrar", jPanel1);

        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 450));

        jtbNotas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtbNotas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jtbNotas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbNotasMouseClicked(evt);
            }
        });
        jtbNotas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtbNotasKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtbNotas);

        jbtFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/resultset_first.png"))); // NOI18N
        jbtFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtFirstActionPerformed(evt);
            }
        });

        jbtPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/resultset_previous.png"))); // NOI18N
        jbtPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtPreviousActionPerformed(evt);
            }
        });

        jbtNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/resultset_next.png"))); // NOI18N
        jbtNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtNextActionPerformed(evt);
            }
        });

        jbtLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/resultset_last.png"))); // NOI18N
        jbtLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtLastActionPerformed(evt);
            }
        });

        jlbNumPaginas.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlbNumPaginas.setText("2");
        jlbNumPaginas.setToolTipText("Número de páginas");

        jlbPaginationAtual.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlbPaginationAtual.setText("0");
        jlbPaginationAtual.setToolTipText("Página atual");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("/");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtFirst)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtPrevious)
                .addGap(23, 23, 23)
                .addComponent(jlbPaginationAtual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlbNumPaginas)
                .addGap(18, 18, 18)
                .addComponent(jbtNext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtLast)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jlbPaginationAtual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlbNumPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbtFirst)
                        .addComponent(jbtPrevious)
                        .addComponent(jbtNext)
                        .addComponent(jbtLast)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "FILTRAGEM", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("Número da Nota");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Mês/Ano de Emissão");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("/");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Nome da Empresa");

        jbtFiltrarNotas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/filter.png"))); // NOI18N
        jbtFiltrarNotas.setText("Filtrar");
        jbtFiltrarNotas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtFiltrarNotasActionPerformed(evt);
            }
        });
        jbtFiltrarNotas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jbtFiltrarNotasKeyReleased(evt);
            }
        });

        jbtLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/clean.png"))); // NOI18N
        jbtLimpar.setText("Limpar");
        jbtLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtLimparActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setText("Ordenar por");

        jcbFiltroOrdenacao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Empresa", "Data", "Valor" }));

        jcbFiltroFormaOrdenacao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Crescente", "Decrescente" }));

        jcbFiltroEmpresasCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jbtRecarregarEmpresas1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/refresh.png"))); // NOI18N
        jbtRecarregarEmpresas1.setToolTipText("recarregar empresas");
        jbtRecarregarEmpresas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtRecarregarEmpresas1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jtfFiltroNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jcbFiltroMes, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jcbFiltroAno, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel9))
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jcbFiltroOrdenacao, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jcbFiltroFormaOrdenacao, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jcbFiltroEmpresasCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jbtRecarregarEmpresas1)))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jbtLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtFiltrarNotas, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel13))
                .addContainerGap(372, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbtRecarregarEmpresas1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jtfFiltroNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jcbFiltroEmpresasCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel13))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcbFiltroMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcbFiltroAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcbFiltroOrdenacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcbFiltroFormaOrdenacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtFiltrarNotas)))
                    .addComponent(jbtLimpar))
                .addContainerGap())
        );

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/add.png"))); // NOI18N
        jButton2.setText("adicionar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jbtEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/page_edit.png"))); // NOI18N
        jbtEdit.setText("editar selecionado");
        jbtEdit.setEnabled(false);
        jbtEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtEditActionPerformed(evt);
            }
        });

        jbtDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/delete.png"))); // NOI18N
        jbtDelete.setText("excluir selecionado");
        jbtDelete.setEnabled(false);
        jbtDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtDelete)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jbtEdit)
                    .addComponent(jbtDelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/printer.png"))); // NOI18N
        jButton1.setText("Gerar relatório baseado no filtro");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        jTabbedPanel.addTab("Listar", jPanel2);

        jScrollPane4.setViewportView(jTabbedPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1248, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        // TODO add your handling code here:
        freeInstance();
    }//GEN-LAST:event_formInternalFrameClosed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        jTabbedPanel.setSelectedIndex(TAB_FORM);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jbtDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDeleteActionPerformed
        // TODO add your handling code here:

        if (jtbNotas.getSelectedRow() > -1) {
            Aquisicao nota;
            Long codNota;
            int linhaAtual = jtbNotas.getSelectedRow();
            jtbNotas.changeSelection(linhaAtual, linhaAtual, false, false);
            codNota = Long.parseLong(jtbNotas.getValueAt(linhaAtual, 0).toString());
            excluirNota(codNota);
        } else {
            MessageService.warningMessage("Você deve selecionar uma nota fiscal primeiro", "Erro", this);
        }
    }//GEN-LAST:event_jbtDeleteActionPerformed

    private void jbtEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtEditActionPerformed
        // TODO add your handling code here:
        Long codNota;
        if (jtbNotas.getSelectedRow() > -1) {
            int linhaAtual = jtbNotas.getSelectedRow();
            jtbNotas.changeSelection(linhaAtual, linhaAtual, false, false);
            codNota = Long.parseLong(jtbNotas.getValueAt(linhaAtual, 0).toString());
            jbtNovo.doClick();
            notafiscal = daoNota.findById(codNota);
            prepararFormulario();
            if (notafiscal != null) {
                jTabbedPanel.setSelectedIndex(TAB_FORM);
                jtfNota.requestFocusInWindow();
            } else {
                MessageService.errorMessage("Nota fiscal não foi encontrada", "Atenção", this);
            }

        }
    }//GEN-LAST:event_jbtEditActionPerformed

    private void jbtFiltrarNotasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtFiltrarNotasActionPerformed
        // TODO add your handling code here:
        carregaFiltroNota();
        filtraNotas();
    }//GEN-LAST:event_jbtFiltrarNotasActionPerformed

    private void jbtLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLastActionPerformed
        // TODO add your handling code here:
        if (filtroNota.getCurrentPage() < (filtroNota.getNumPages() - 1)) {
            filtroNota.changePage(Configurator.MAX_RESULTS, AbstractFilter.LAST);
            filtraNotas();
        }
    }//GEN-LAST:event_jbtLastActionPerformed

    private void jbtNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtNextActionPerformed
        // TODO add your handling code here:
        if (filtroNota.getCurrentPage() < (filtroNota.getNumPages() - 1)) {
            filtroNota.changePage(Configurator.MAX_RESULTS, AbstractFilter.NEXT);
            filtraNotas();
        }
    }//GEN-LAST:event_jbtNextActionPerformed

    private void jbtPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtPreviousActionPerformed
        // TODO add your handling code here:
        if (filtroNota.getCurrentPage() > 0) {
            filtroNota.changePage(Configurator.MAX_RESULTS, AbstractFilter.PREVIOUS);
            filtraNotas();
        }
    }//GEN-LAST:event_jbtPreviousActionPerformed

    private void jbtFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtFirstActionPerformed
        // TODO add your handling code here:
        if (filtroNota.getCurrentPage() > 0) {
            filtroNota.changePage(Configurator.MAX_RESULTS, AbstractFilter.FIRST);
            filtraNotas();
        }
    }//GEN-LAST:event_jbtFirstActionPerformed

    private void jbtLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLimparActionPerformed
        // TODO add your handling code here:
        jcbFiltroEmpresasCombo.setSelectedIndex(0);
        jtfFiltroNumero.setText("");
        jcbFiltroMes.setSelectedIndex(0);
        jcbFiltroAno.setSelectedIndex(0);
        jcbFiltroOrdenacao.setSelectedIndex(0);
        jcbFiltroFormaOrdenacao.setSelectedIndex(0);
    }//GEN-LAST:event_jbtLimparActionPerformed

    private void jtbNotasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbNotasMouseClicked
        // TODO add your handling code here:
        jbtEdit.setEnabled(true);
        jbtDelete.setEnabled(true);
        if (evt.getClickCount() == 2) {
            jbtEdit.doClick();
        }
    }//GEN-LAST:event_jtbNotasMouseClicked

    private void jtbNotasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtbNotasKeyReleased
        // TODO add your handling code here:
        if (jtbNotas.getSelectedRow() > -1) {
            if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                jbtDelete.doClick();
            }
        }
    }//GEN-LAST:event_jtbNotasKeyReleased

    private void jbtFiltrarNotasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jbtFiltrarNotasKeyReleased
        // TODO add your handling code here:
        jbtFiltrarNotas.requestFocusInWindow();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtraNotas();
        }
    }//GEN-LAST:event_jbtFiltrarNotasKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        carregaFiltroNota();
        EmissaoRelatorio.relatorioDeNotasFiscaisPorFiltro(filtroNota);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jbtRecarregarEmpresas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtRecarregarEmpresas1ActionPerformed
        // TODO add your handling code here:
        carregaEmpresas();
    }//GEN-LAST:event_jbtRecarregarEmpresas1ActionPerformed

    private void jlbNomeArquivoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlbNomeArquivoMouseClicked
        // TODO add your handling code here:
        if (arq != null) {
            try {
                SplashScreen.getInstance().atualizarStatus("Carregando arquivo. Aguarde...");
                SplashScreen.getInstance().setVisible(true);
                java.awt.Desktop.getDesktop().open(arq);
            } catch (IOException e) {
                MessageService.errorMessage("Arquivo não encontrado", "Atenção", this);
            } finally {
                SplashScreen.finishInstance();
            }
        }
    }//GEN-LAST:event_jlbNomeArquivoMouseClicked

    private void jbtExcluirArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtExcluirArquivoActionPerformed
        // TODO add your handling code here:
        if (MessageService.confirmDialog("Tem certeza que quer excluir o arquivo?", "Confirmação", this) == MessageService.YES_OPTION) {
            if (notafiscal != null) {
                notafiscal.setArquivo(null);
                jbtVisualizarArquivo.setVisible(false);
                jbtExcluirArquivo.setVisible(false);
                MessageService.warningMessage("Você precisa atualizar para excluir definitivamente o arquivo!", "Atenção", this);
            }
        }
    }//GEN-LAST:event_jbtExcluirArquivoActionPerformed

    private void jbtVisualizarArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtVisualizarArquivoActionPerformed
        // TODO add your handling code here:
        SplashScreen splash = SplashScreen.getInstance();
        splash.atualizarStatus("Carregando arquivo");
        splash.showSplash();
        arqTemp = notafiscal.getFile(notafiscal.getArquivo(), notafiscal.getNumero());
        if (arqTemp != null) {
            try {
                java.awt.Desktop.getDesktop().open(arqTemp);
                splash.setVisible(false);
            } catch (IOException e) {
                MessageService.errorMessage("Arquivo não encontrado", "Atenção", this);
            }
        }
    }//GEN-LAST:event_jbtVisualizarArquivoActionPerformed

    private void jbtSelecionaArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSelecionaArquivoActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
        Action details = fc.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "pdf");
        fc.setFileFilter(filter);
        int res = fc.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            arq = fc.getSelectedFile();
            if (arq.length() > Aquisicao.TAMANHO_MAXIMO_ARQUIVO) {
                MessageService.errorMessage("Tamanho de arquivo muito grande. \n Forneça arquivos de, no máximo 3 MB.", "Erro", this);
                arq = null;
                jlbNomeArquivo.setVisible(false);
                jlbNomeArquivo.setText("");

            } else {
                jlbNomeArquivo.setVisible(true);
                jlbNomeArquivo.setText(arq.getName());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Voce nao selecionou nenhum arquivo.");
        }
    }//GEN-LAST:event_jbtSelecionaArquivoActionPerformed

    private void jtbItemNotaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jtbItemNotaPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jtbItemNotaPropertyChange

    private void jbtItemExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtItemExcluirActionPerformed
        // TODO add your handling code here:
        int row = jtbItemNota.getSelectedRow();
        if (row > -1) {
            if (MessageService.confirmDialog("Exluir item?", "Atenção", this) == MessageService.YES_OPTION) {
                try {
                    long id = Long.parseLong(modeloItemNota.getValueAt(row, TB_ITENS_COL_COD).toString());
                    itensMarcadosParaExclusao.add(id);
                } catch (NumberFormatException ex) {
                    System.out.println("não existe no banco de dados");
                }

                modeloItemNota.removeRow(row);
                calcularValorTotal();
            }
        }
    }//GEN-LAST:event_jbtItemExcluirActionPerformed

    private void jbtItemAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtItemAdicionarActionPerformed
        // TODO add your handling code here:
        JDItem ditem = JDItem.getInstance();
        ditem.setVisible(true);

        //        if (validarCamposItem()) {
        //            if (itemNota == null) {
        //                itemNota = new ItemNota();
        //            }
        //            itemNota.setId((!jtfItemCod.getText().isEmpty() ? Long.parseLong(jtfItemCod.getText()) : null));
        //            itemNota.setNome(jtfItemNome.getText());
        //            if (jcbItemSetor.getSelectedIndex() > 0) {
        //                Setor setor = new Setor();
        //                daoSetor.open();
        //                setor = daoSetor.findById(Integer.parseInt(jtfItemSetorCod.getText()));
        //                if (setor != null) {
        //                    itemNota.setSetorDestino(setor);
        //                }
        //            }
        //            itemNota.setQuantidade(Integer.parseInt(jtfItemQuantidade.getText()));
        //            itemNota.setValor(Funcoes.formatMoedaFloat(jtfItemValor.getText()));
        //            if (notafiscal.getItens() == null) {
        //                notafiscal.setItens(new ArrayList<>());
        //            }
        //            notafiscal.getItens().add(itemNota);
        //            valorTotal += itemNota.getQuantidade() * itemNota.getValor();
        //            jlbValorTotal.setText("R$ " + valorTotal);
        //            Object[] itemObject = new Object[QUANT_COLUMNS_TB_ITENS];
        //            itemObject[0] = (itemNota.getId() != null && itemNota.getId() > 0 ? itemNota.getId() : "-");
        //            itemObject[1] = itemNota.getNome();
        //            itemObject[2] = itemNota.getSetorDestino().getNome();
        //            itemObject[3] = itemNota.getQuantidade();
        //            itemObject[4] = itemNota.getValor();
        //            modeloItemNota.addRow(itemObject);
        //        }
    }//GEN-LAST:event_jbtItemAdicionarActionPerformed

    private void jbtCadastrarNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCadastrarNotaActionPerformed
        // TODO add your handling code here:
        //Empresa emp;
        notafiscal = new Aquisicao();
        try {
            daoNota.open(true);
            if (validarCampos()) {
                if (!jtfCodNota.getText().isEmpty()) {
                    notafiscal = daoNota.findByIdNoConnection(Long.parseLong(jtfCodNota.getText()));
                }

                if (jtfCodEmpresa.getText().length() > 0) {
                    empresaNotaFiscal = daoEmpresa.findByIdNoConnection(Long.parseLong(jtfCodEmpresa.getText()));
                    notafiscal.setEmpresa(empresaNotaFiscal);
                }
                if (jcbFormasPagamento.getItemCount() > 0) {
                    FormaPagamento forma = daoForma.findByName(jcbFormasPagamento.getSelectedItem().toString());
                    if (forma != null) {
                        notafiscal.setFormaPagamento(forma);
                    } else {
                        notafiscal.setFormaPagamento(null);
                    }
                }

                notafiscal.setDataEmissao(jdcData.getCalendar());
                notafiscal.setNumero(jtfNota.getText().trim());

                if (!jtaDescricao.getText().trim().isEmpty()) {
                    notafiscal.setDescricao(jtaDescricao.getText().trim());
                } else {
                    notafiscal.setDescricao("");
                }

                if (!jtfValorTotal.getText().trim().isEmpty()) {
                    notafiscal.setValor(Funcoes.formatMoedaFloat(jtfValorTotal.getText().trim()));
                } else {
                    notafiscal.setValor(valorTotal);
                }
                notafiscal.setItens(getItensFromTable());

                if (arq != null) {
                    notafiscal.readFile(arq);
                } else {
                    notafiscal.setArquivo(null);
                }

                if (!jtfCodNota.getText().isEmpty() && MessageService.confirmDialog("Atualizar nota fiscal?", "Atenção", this) == MessageService.YES_OPTION) {
                    if (!itensMarcadosParaExclusao.isEmpty()) {
                        ItemNota itNota = null;
                        for (Long idItem : itensMarcadosParaExclusao) {
                            itNota = daoItem.findByIdNoConnection(idItem);
                            if (itNota != null) {
                                daoItem.delete(itNota);
                            }
                        }
                    }
                    daoNota.update(notafiscal);
                    daoNota.commit();
                    MessageService.informationMessage("Nota fiscal '" + notafiscal.getNumero() + "' atualizada com sucesso!", "Sucesso", this);
                    prepararFormulario();

                } else if (jtfCodNota.getText().isEmpty() && MessageService.confirmDialog("Cadastrar Nota?", "Atenção", this) == MessageService.YES_OPTION) {
                    daoNota.persist(notafiscal);
                    daoNota.commit();
                    MessageService.informationMessage("Nota fiscal '" + notafiscal.getNumero() + "' cadastrada com sucesso!", "Sucesso", this);
                    prepararFormulario();
                }
                jbtFiltrarNotas.doClick();
            }
        } catch (Exception e) {
            daoNota.rollback();
            MessageService.errorMessage("Erro na inserção da nota", "Erro", this);
            e.printStackTrace();
        } finally {
            daoNota.close();
        }
    }//GEN-LAST:event_jbtCadastrarNotaActionPerformed

    private void jbtNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtNovoActionPerformed
        // TODO add your handling code here:
        notafiscal = new Aquisicao();
        arq = null;
        arqTemp = null;
        arquivo = null;
        itensMarcadosParaExclusao = new ArrayList<>();
        jbtExcluirNota.setEnabled(false);
        prepararFormulario();
    }//GEN-LAST:event_jbtNovoActionPerformed

    private void jbtRecarregarEmpresasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtRecarregarEmpresasActionPerformed
        // TODO add your handling code here:
        carregaEmpresas();
    }//GEN-LAST:event_jbtRecarregarEmpresasActionPerformed

    private void jcbEmpresasComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbEmpresasComboActionPerformed
        // TODO add your handling code here:
        if (jcbEmpresasCombo.getSelectedIndex() == 0 || jcbEmpresasCombo.getItemCount() == 0) {
            jtfCodEmpresa.setText("");
        } else {
            Long idEmpresa = (Long) daoEmpresa.findIdByName(jcbEmpresasCombo.getSelectedItem().toString());
            if (idEmpresa != null && idEmpresa > 0) {
                jtfCodEmpresa.setText(idEmpresa.toString());
            }
        }
    }//GEN-LAST:event_jcbEmpresasComboActionPerformed

    private void jtfCodNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfCodNotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfCodNotaActionPerformed

    private void jtfNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNotaActionPerformed

    private void jbtExcluirNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtExcluirNotaActionPerformed
        // TODO add your handling code here:
        if (!jtfCodNota.getText().trim().isEmpty()) {
            try {
                Long codNota = Long.parseLong(jtfCodNota.getText());
                if (excluirNota(codNota)) {
                    jbtNovo.doClick();
                }
            } catch (NumberFormatException e) {
                MessageService.warningMessage("Nenhuma nota foi selecionada", "Atenção", this);
            }
        }

    }//GEN-LAST:event_jbtExcluirNotaActionPerformed

    private void jScrollPane3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane3MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jScrollPane3MouseClicked

    private void jtbItemNotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbItemNotaMouseClicked
        // TODO add your handling code here:
        if (jtbItemNota.getSelectedRow() > -1) {
            jbtItemExcluir.setEnabled(true);
        }
    }//GEN-LAST:event_jtbItemNotaMouseClicked

    private void jtaDescricaoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtaDescricaoKeyReleased
        // TODO add your handling code here:
        int totalRestantes = Aquisicao.MAX_CARACTERES_DESCRICAO - jtaDescricao.getText().length();
        jlbCaracteresRestantes.setText(totalRestantes + "");
    }//GEN-LAST:event_jtaDescricaoKeyReleased

    private void jbtRecarregarFormasPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtRecarregarFormasPagamentoActionPerformed
        // TODO add your handling code here:
        carregaFormasPagamento();
    }//GEN-LAST:event_jbtRecarregarFormasPagamentoActionPerformed

    public static Aquisicao getNotafiscal() {
        return notafiscal;
    }

    public static void setNotafiscal(Aquisicao notafiscal) {
        JIFcad_Notafiscal.notafiscal = notafiscal;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private static javax.swing.JTabbedPane jTabbedPanel;
    private javax.swing.JButton jbtCadastrarNota;
    private javax.swing.JButton jbtDelete;
    private javax.swing.JButton jbtEdit;
    private javax.swing.JButton jbtExcluirArquivo;
    private javax.swing.JButton jbtExcluirNota;
    private javax.swing.JButton jbtFiltrarNotas;
    private javax.swing.JButton jbtFirst;
    private javax.swing.JButton jbtItemAdicionar;
    private javax.swing.JButton jbtItemExcluir;
    private javax.swing.JButton jbtLast;
    private javax.swing.JButton jbtLimpar;
    private javax.swing.JButton jbtNext;
    private javax.swing.JButton jbtNovo;
    private javax.swing.JButton jbtPrevious;
    private javax.swing.JButton jbtRecarregarEmpresas;
    private javax.swing.JButton jbtRecarregarEmpresas1;
    private javax.swing.JButton jbtRecarregarFormasPagamento;
    private javax.swing.JButton jbtSelecionaArquivo;
    private javax.swing.JButton jbtVisualizarArquivo;
    private javax.swing.JComboBox jcbEmpresasCombo;
    private javax.swing.JComboBox jcbFiltroAno;
    private javax.swing.JComboBox<String> jcbFiltroEmpresasCombo;
    private javax.swing.JComboBox jcbFiltroFormaOrdenacao;
    private javax.swing.JComboBox jcbFiltroMes;
    private javax.swing.JComboBox jcbFiltroOrdenacao;
    private javax.swing.JComboBox jcbFormasPagamento;
    private com.toedter.calendar.JDateChooser jdcData;
    private com.toedter.calendar.JDateChooser jdcDataFinal;
    private javax.swing.JLabel jlbCaracteresRestantes;
    private javax.swing.JLabel jlbNomeArquivo;
    private javax.swing.JLabel jlbNumPaginas;
    private javax.swing.JLabel jlbPaginationAtual;
    private static javax.swing.JLabel jlbValorTotal;
    private javax.swing.JPanel jpArquivo;
    private javax.swing.JTextArea jtaDescricao;
    public static javax.swing.JTable jtbItemNota;
    private javax.swing.JTable jtbNotas;
    private javax.swing.JTextField jtfCodEmpresa;
    private javax.swing.JTextField jtfCodNota;
    private javax.swing.JTextField jtfFiltroNumero;
    private javax.swing.JTextField jtfNota;
    private javax.swing.JFormattedTextField jtfValorTotal;
    // End of variables declaration//GEN-END:variables

}
