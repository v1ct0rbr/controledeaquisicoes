/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.view.internal;

import br.gov.pb.der.config.Configurator;
import br.gov.pb.der.dao.factory._DAOFactory;
import br.gov.pb.der.dao.interfaces.EmpresaDao;
import br.gov.pb.der.filtros.AbstractFilter;
import br.gov.pb.der.filtros.FiltroEmpresa;
import br.gov.pb.der.modelo.Empresa;
import br.gov.pb.der.services.MessageService;
import br.gov.pb.der.utils.EmissaoRelatorio;
import br.gov.pb.der.utils.JTextFieldLimit;
import br.gov.pb.der.utils.MyJInternalFrame;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author victo
 */
public class JIFEmpresas extends MyJInternalFrame {

    /**
     * Creates new form JIFEmpresas
     */
    private static final int CARACTERES_RESTANTES = 512;
    private static JIFEmpresas jifEmpresa;
    private DefaultTableModel modeloLocal;
    private EmpresaDao daoEmpresa = _DAOFactory.getFactory().getEmpresaDao();
    private FiltroEmpresa filtroEmpresa;

    public static JIFEmpresas getInstance() {
        if (jifEmpresa == null) {
            jifEmpresa = new JIFEmpresas();
        }
        return jifEmpresa;
    }

    public JIFEmpresas() {
        initComponents();
        super.setFrameIndex(Configurator.IFRAME_EMPRESAS);

        modeloLocal = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false; //To change body of generated methods, choose Tools | Templates.
            }
        };
        jlbEmpresaSelecionada.setVisible(false);
        jtfFiltroNome.setDocument(new JTextFieldLimit(100));
        jtfNome.setDocument(new JTextFieldLimit(100));
        jtaDescricao.setDocument(new JTextFieldLimit(512));
        jlbCaracteresRestantes.setText(CARACTERES_RESTANTES + "");
        filtraEmpresas();
    }

    public void cadastrarEmpresa() {
        Empresa empresaCadastro = new Empresa();
        if (validaCamposEmpresa()) {
            try {
                daoEmpresa.open();
                daoEmpresa.openTransaction();

                if (jtfCodEmpresa.getText().isEmpty() && MessageService.confirmDialog("Cadastrar nova Empresa?", "Atenção", this) == MessageService.YES_OPTION) {
                    empresaCadastro.setNome(jtfNome.getText());
                    empresaCadastro.setDescricao(jtaDescricao.getText());
                    daoEmpresa.persist(empresaCadastro);
                    daoEmpresa.commit();
                    MessageService.informationMessage("Cadastro da empresa \"" + empresaCadastro.getNome() + "\" efetuado com sucesso. ", "Informação", this);
                } else if (MessageService.confirmDialog("Atualizar Empresa?", "Atenção", this) == MessageService.YES_OPTION) {
                    empresaCadastro = daoEmpresa.findByIdNoConnection(Long.parseLong(jtfCodEmpresa.getText()));
                    empresaCadastro.setNome(jtfNome.getText());
                    empresaCadastro.setDescricao(jtaDescricao.getText());
                    daoEmpresa.update(empresaCadastro);
                    daoEmpresa.commit();
                    MessageService.informationMessage("Atualizacao realizada com sucesso.", "Informação", this);
                }
                if (empresaCadastro.getId() != null) {
                    jtfCodEmpresa.setText("" + empresaCadastro.getId());
                    jbtCadastrar.setText("Atualizar");
                }
                limparFiltroEmpresa();
                filtraEmpresas();
            } catch (Exception ex) {
                MessageService.errorMessage("Erro na operação: \n" + ex.getMessage(), "Erro", this);
                daoEmpresa.rollback();
            } finally {
                daoEmpresa.close();
            }
        }
    }

    public void carregaEmpresas(List<Object[]> locais) {
        for (Object[] local : locais) {
            modeloLocal.addRow(local);
        }
    }

    public void carregaFiltroEmpresa() {
        filtroEmpresa = new FiltroEmpresa();
        if (!jtfFiltroNome.getText().trim().isEmpty()) {
            filtroEmpresa.setNome(jtfFiltroNome.getText());
        }
        filtroEmpresa.changePage(10, AbstractFilter.FIRST);
        filtroEmpresa.setTotalRegistros(daoEmpresa.countByFiltro(filtroEmpresa));
        filtroEmpresa.setNumPages((int) ((filtroEmpresa.getTotalRegistros() + (filtroEmpresa.getMaxResults() - 1)) / (filtroEmpresa.getMaxResults())));
    }

    public void filtraEmpresas() {
        List<Object[]> _empresas;
        carregaFiltroEmpresa();
        preparaTabelaEmpresa();
        _empresas = daoEmpresa.listarPorFiltro(filtroEmpresa);
        if (_empresas != null && !_empresas.isEmpty()) {
            carregaEmpresas(_empresas);
        }
        jlbPaginationAtual.setText("" + (filtroEmpresa.getCurrentPage() + 1));
        if (filtroEmpresa.getNumPages() == 0) {
            filtroEmpresa.setNumPages(1);
        }
        jlbNumPaginas.setText("" + filtroEmpresa.getNumPages());
    }

    public void limparFiltroEmpresa() {
        jtfFiltroNome.setText("");
    }

    public void preparaTabelaEmpresa() {
        jtbEmpresas.setModel(modeloLocal);
        modeloLocal.addColumn("ID");
        modeloLocal.addColumn("NOME");
        modeloLocal.setNumRows(0);
        modeloLocal.setColumnCount(2);
        jtbEmpresas.getColumnModel().getColumn(0).setPreferredWidth(5);
        jtbEmpresas.getColumnModel().getColumn(1).setPreferredWidth(250);
    }

    public boolean validaCamposEmpresa() {
        String erros = "";
        if (jtfNome.getText().trim().isEmpty()) {
            erros += " - Preencha o nome da Empresa corretamente";
        }
        if (!erros.isEmpty()) {
            MessageService.errorMessage(erros, "Erro", this);
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

        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaDescricao = new javax.swing.JTextArea();
        jlbNome = new javax.swing.JLabel();
        jtfNome = new javax.swing.JTextField();
        jlbDescricao = new javax.swing.JLabel();
        jbtCadastrar = new javax.swing.JButton();
        jbtLimpar = new javax.swing.JButton();
        jtfCodEmpresa = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jlbCaracteresRestantes = new javax.swing.JLabel();
        jlbEmpresaSelecionada = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbEmpresas = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jbtFirst = new javax.swing.JButton();
        jbtPrevious = new javax.swing.JButton();
        jbtNext = new javax.swing.JButton();
        jbtLast = new javax.swing.JButton();
        jlbNumPaginas = new javax.swing.JLabel();
        jlbPaginationAtual = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jbtDelete = new javax.swing.JButton();
        jbtEdit = new javax.swing.JButton();
        jlbNome1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jlbFiltroNome = new javax.swing.JLabel();
        jtfFiltroNome = new javax.swing.JTextField();
        jbtFiltrar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setClosable(true);
        setIconifiable(true);
        setTitle("Controle de Empresas");

        jPanel1.setPreferredSize(new java.awt.Dimension(950, 412));

        jtaDescricao.setColumns(20);
        jtaDescricao.setRows(5);
        jtaDescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtaDescricaoKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jtaDescricao);

        jlbNome.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlbNome.setText("Nome");

        jlbDescricao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlbDescricao.setText("Descrição");

        jbtCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/disk.png"))); // NOI18N
        jbtCadastrar.setText("Cadastrar");
        jbtCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCadastrarActionPerformed(evt);
            }
        });

        jbtLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/new.png"))); // NOI18N
        jbtLimpar.setText("Novo");
        jbtLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtLimparActionPerformed(evt);
            }
        });

        jtfCodEmpresa.setEditable(false);
        jtfCodEmpresa.setToolTipText("Código da empresa");
        jtfCodEmpresa.setEnabled(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel1.setText("Caracteres restantes:");

        jlbCaracteresRestantes.setText("jLabel2");

        jlbEmpresaSelecionada.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlbEmpresaSelecionada.setForeground(new java.awt.Color(51, 102, 0));
        jlbEmpresaSelecionada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbEmpresaSelecionada.setText("EMPRESA SELECIONADA");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jlbDescricao)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jbtCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jlbEmpresaSelecionada, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlbCaracteresRestantes))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jlbNome)
                        .addGap(316, 316, 316))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jtfNome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfCodEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbEmpresaSelecionada, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlbNome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfCodEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jlbDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbCaracteresRestantes)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtCadastrar)
                    .addComponent(jbtLimpar))
                .addContainerGap())
        );

        jtbEmpresas.setModel(new javax.swing.table.DefaultTableModel(
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
        jtbEmpresas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jtbEmpresas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbEmpresasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtbEmpresas);

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

        jbtDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/delete.png"))); // NOI18N
        jbtDelete.setText("excluir selecionado");
        jbtDelete.setToolTipText("");
        jbtDelete.setEnabled(false);
        jbtDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDeleteActionPerformed(evt);
            }
        });

        jbtEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/page_edit.png"))); // NOI18N
        jbtEdit.setText("editar selecionado");
        jbtEdit.setToolTipText("");
        jbtEdit.setEnabled(false);
        jbtEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jbtEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtDelete))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbtDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jlbPaginationAtual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlbNumPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jbtFirst)
                                .addComponent(jbtPrevious)
                                .addComponent(jbtNext)
                                .addComponent(jbtLast)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jlbNome1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlbNome1.setText("Lista de Empresas");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "FILTRAGEM", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jlbFiltroNome.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlbFiltroNome.setText("Nome");

        jbtFiltrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/filter.png"))); // NOI18N
        jbtFiltrar.setText("filtrar");
        jbtFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtFiltrarActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/printer.png"))); // NOI18N
        jButton1.setText("Relatório de Empresas");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbFiltroNome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtfFiltroNome, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtFiltrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbFiltroNome)
                    .addComponent(jtfFiltroNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtFiltrar)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jlbNome1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbNome1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        jScrollPane3.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1058, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtaDescricaoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtaDescricaoKeyReleased
        // TODO add your handling code here:
        int totalRestantes = CARACTERES_RESTANTES - jtaDescricao.getText().length();
        jlbCaracteresRestantes.setText(totalRestantes + "");
    }//GEN-LAST:event_jtaDescricaoKeyReleased

    private void jbtCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCadastrarActionPerformed
        // TODO add your handling code here:
        cadastrarEmpresa();
    }//GEN-LAST:event_jbtCadastrarActionPerformed

    private void jbtLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLimparActionPerformed
        // TODO add your handling code here:
        jtfCodEmpresa.setText("");
        jtfNome.setText("");
        jtaDescricao.setText("");
        jbtCadastrar.setText("Cadastrar");
        jlbEmpresaSelecionada.setVisible(false);
    }//GEN-LAST:event_jbtLimparActionPerformed

    private void jtbEmpresasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbEmpresasMouseClicked
        // TODO add your handling code here:

        jbtEdit.setEnabled(true);
        jbtDelete.setEnabled(true);
        if (evt.getClickCount() == 2) {
            jbtEdit.doClick();
        }
    }//GEN-LAST:event_jtbEmpresasMouseClicked

    private void jbtFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtFiltrarActionPerformed
        // TODO add your handling code here:
        filtraEmpresas();
    }//GEN-LAST:event_jbtFiltrarActionPerformed

    private void jbtFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtFirstActionPerformed
        // TODO add your handling code here:
        if (filtroEmpresa.getCurrentPage() > 0) {
            filtroEmpresa.changePage(Configurator.MAX_RESULTS, AbstractFilter.FIRST);
            filtraEmpresas();
        }
    }//GEN-LAST:event_jbtFirstActionPerformed

    private void jbtPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtPreviousActionPerformed
        // TODO add your handling code here:
        if (filtroEmpresa.getCurrentPage() > 0) {
            filtroEmpresa.changePage(Configurator.MAX_RESULTS, AbstractFilter.PREVIOUS);
            filtraEmpresas();
        }
    }//GEN-LAST:event_jbtPreviousActionPerformed

    private void jbtNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtNextActionPerformed
        // TODO add your handling code here:
        if (filtroEmpresa.getCurrentPage() < (filtroEmpresa.getNumPages() - 1)) {
            filtroEmpresa.changePage(Configurator.MAX_RESULTS, AbstractFilter.NEXT);
            filtraEmpresas();
        }
    }//GEN-LAST:event_jbtNextActionPerformed

    private void jbtLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLastActionPerformed
        // TODO add your handling code here:
        if (filtroEmpresa.getCurrentPage() < (filtroEmpresa.getNumPages() - 1)) {
            filtroEmpresa.changePage(Configurator.MAX_RESULTS, AbstractFilter.LAST);
            filtraEmpresas();
        }
    }//GEN-LAST:event_jbtLastActionPerformed

    private void jbtDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDeleteActionPerformed
        // TODO add your handling code here:
        if (jtbEmpresas.getSelectedRow() > -1) {
            Empresa empresa;
            Long codEmpresa;
            int linhaAtual = jtbEmpresas.getSelectedRow();
            jtbEmpresas.changeSelection(linhaAtual, linhaAtual, false, false);
            codEmpresa = Long.parseLong(jtbEmpresas.getValueAt(linhaAtual, 0).toString());
            if (MessageService.confirmDialog("Excluir item selecionado", "Atenção", this) == MessageService.YES_OPTION) {
                try {
                    daoEmpresa.open(true);
                    empresa = daoEmpresa.findByIdNoConnection(codEmpresa);
                    if (empresa != null) {
                        if (!daoEmpresa.temNotasDependentes(empresa)) {
                            daoEmpresa.delete(empresa);
                            jbtLimpar.doClick();
                            daoEmpresa.commit();
                            MessageService.informationMessage("Empresa excluída com sucesso", "Sucesso", this);
                            filtraEmpresas();
                        } else {
                            MessageService.warningMessage("A empresa tem notas dependentes. Não é possível excluir a mesma!!", "Atenção", this);
                        }
                    } else {
                        MessageService.informationMessage("Empresa inexistente", "Sucesso", this);
                        filtraEmpresas();
                    }

                } catch (Exception e) {
                    MessageService.errorMessage("Erro na exclusão do setor: \n" + e.getMessage(), "Erro", this);
                    daoEmpresa.rollback();
                } finally {
                    daoEmpresa.close();
                }
            }
        } else {
            MessageService.warningMessage("Você deve selecionar uma empresa primeiro", "Erro", this);
        }
    }//GEN-LAST:event_jbtDeleteActionPerformed

    private void jbtEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtEditActionPerformed
        // TODO add your handling code here:
        Long codSetor;
        if (jtbEmpresas.getSelectedRow() > -1) {
            Empresa empresa;
            int linhaAtual = jtbEmpresas.getSelectedRow();
            jtbEmpresas.changeSelection(linhaAtual, linhaAtual, false, false);
            codSetor = Long.parseLong(jtbEmpresas.getValueAt(linhaAtual, 0).toString());
            empresa = daoEmpresa.findById(codSetor);
            if (empresa != null) {
                jtfCodEmpresa.setText("" + empresa.getId());
                jtfNome.setText(empresa.getNome());
                jtaDescricao.setText(empresa.getDescricao());
                jlbCaracteresRestantes.setText((CARACTERES_RESTANTES - empresa.getDescricao().length()) + "");
                jbtCadastrar.setText("Atualizar");
                jlbEmpresaSelecionada.setVisible(true);
                jtfNome.requestFocusInWindow();
            }
        }
    }//GEN-LAST:event_jbtEditActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            EmissaoRelatorio.relatorioDeEmpresas();
        } catch (SQLException ex) {
            MessageService.errorMessage("Erro inesperado no banco de dados!", "Atenção", this);
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtCadastrar;
    private javax.swing.JButton jbtDelete;
    private javax.swing.JButton jbtEdit;
    private javax.swing.JButton jbtFiltrar;
    private javax.swing.JButton jbtFirst;
    private javax.swing.JButton jbtLast;
    private javax.swing.JButton jbtLimpar;
    private javax.swing.JButton jbtNext;
    private javax.swing.JButton jbtPrevious;
    private javax.swing.JLabel jlbCaracteresRestantes;
    private javax.swing.JLabel jlbDescricao;
    private javax.swing.JLabel jlbEmpresaSelecionada;
    private javax.swing.JLabel jlbFiltroNome;
    private javax.swing.JLabel jlbNome;
    private javax.swing.JLabel jlbNome1;
    private javax.swing.JLabel jlbNumPaginas;
    private javax.swing.JLabel jlbPaginationAtual;
    private javax.swing.JTextArea jtaDescricao;
    private javax.swing.JTable jtbEmpresas;
    private javax.swing.JTextField jtfCodEmpresa;
    private javax.swing.JTextField jtfFiltroNome;
    private javax.swing.JTextField jtfNome;
    // End of variables declaration//GEN-END:variables
}
