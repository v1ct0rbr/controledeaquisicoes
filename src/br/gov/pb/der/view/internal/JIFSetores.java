/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.view.internal;

import br.gov.pb.der.config.Configurator;
import br.gov.pb.der.dao.factory._DAOFactory;
import br.gov.pb.der.dao.interfaces.ItemDao;
import br.gov.pb.der.dao.interfaces.SetorDao;
import br.gov.pb.der.filtros.AbstractFilter;
import br.gov.pb.der.filtros.FiltroSetor;
import br.gov.pb.der.modelo.Setor;
import br.gov.pb.der.services.MessageService;
import br.gov.pb.der.utils.FacadePatrimonio;
import br.gov.pb.der.utils.MyJInternalFrame;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author victo
 */
public class JIFSetores extends MyJInternalFrame {

    /**
     * Creates new form JIFSetores2
     */
    private DefaultTableModel modeloSetores;
    FiltroSetor filtroSetor;
    private SetorDao daoSetor = _DAOFactory.getFactory().getSetorDao();
    private ItemDao daoItem = _DAOFactory.getFactory().getItemDao();
    private Setor setor;
    private static JIFSetores jifSetores;

    public static JIFSetores getInstance() {
        if (jifSetores == null) {
            jifSetores = new JIFSetores();
        }
        return jifSetores;
    }

    public JIFSetores() {
        initComponents();
        super.setFrameIndex(Configurator.IFRAME_SETORES);
        modeloSetores = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false; //To change body of generated methods, choose Tools | Templates.
            }
        };
        jlbSetorSelecionado.setVisible(false);
        jpCadastroSetor.getRootPane().setDefaultButton(jbtCadastrar);
        jpFiltragem.getRootPane().setDefaultButton(jbtFiltrarSetor);
        carregaFiltroSetor();
        filtraSetores();
    }

    public void carregaFiltroSetor() {
        filtroSetor = new FiltroSetor();
        if (!jtfFitroCodigo.getText().trim().isEmpty()) {
            try {
                filtroSetor.setId(Integer.parseInt(jtfFitroCodigo.getText().trim()));
            } catch (NumberFormatException e) {
                filtroSetor.setId(null);
            }
        }
        if (!jtfFitroSetor.getText().trim().isEmpty()) {
            filtroSetor.setNome(jtfFitroSetor.getText().trim());
        }
        filtroSetor.changePage(Configurator.MAX_RESULTS, AbstractFilter.FIRST);
        filtroSetor.setTotalRegistros(daoSetor.countByFiltro(filtroSetor));
        filtroSetor.setNumPages((int) ((filtroSetor.getTotalRegistros() + (filtroSetor.getMaxResults() - 1)) / (filtroSetor.getMaxResults())));
    }

    public void carregaSetores(List<Object[]> notas) {
        for (Object[] nota : notas) {
            modeloSetores.addRow(nota);
        }
    }

    public void filtraSetores() {
        List<Object[]> _setores;
        preparaTabelaSetor();
        _setores = daoSetor.listarPorFiltro(filtroSetor);
        if (_setores != null && !_setores.isEmpty()) {
            carregaSetores(_setores);
        }
        jlbPaginationAtual.setText("" + (filtroSetor.getCurrentPage() + 1));
        if (filtroSetor.getNumPages() == 0) {
            filtroSetor.setNumPages(1);
        }
        jlbNumPaginas.setText("" + filtroSetor.getNumPages());
    }

    public void limparCamposFiltragem() {
        jtfFitroCodigo.setText("");
        jtfFitroSetor.setText("");
    }

    public void preparaTabelaSetor() {
        jtbSetores.setModel(modeloSetores);
        modeloSetores.addColumn("ID");
        modeloSetores.addColumn("Nome");
        modeloSetores.addColumn("Responsável");
        modeloSetores.setNumRows(0);
        modeloSetores.setColumnCount(3);
        jtbSetores.getColumnModel().getColumn(0).setPreferredWidth(5);
        jtbSetores.getColumnModel().getColumn(1).setPreferredWidth(200);
        jtbSetores.getColumnModel().getColumn(2).setPreferredWidth(200);
    }

    public void prepararFormulario() {
        if (setor != null && setor.getId() != null) {
            jtfCodigo.setText("" + setor.getId());
        } else {
            jtfCodigo.setText("");
            jtfNomeSetor.setText("");
            jtfNomeResponsavel.setText("");
        }

    }

    public boolean validarCampos() {
        String erros = "";

        if (jtfCodigo.getText().trim().isEmpty()) {
            erros += " - O código deve ser informado\n";
        }

        if (jtfNomeSetor.getText().trim().isEmpty()) {
            erros += " - O nome do setor deve ser informado.\n";
        }

        if (!erros.isEmpty()) {
            MessageService.errorMessage("Campos obrigatórios não informados: \n\n" + erros, "Erros foram encontrados", this);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jpCadastroSetor = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jtfCodigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtfNomeSetor = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jtfNomeResponsavel = new javax.swing.JTextField();
        jbtNovo = new javax.swing.JButton();
        jbtCadastrar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jlbSetorSelecionado = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jbtFirst = new javax.swing.JButton();
        jbtPrevious = new javax.swing.JButton();
        jbtNext = new javax.swing.JButton();
        jbtLast = new javax.swing.JButton();
        jlbNumPaginas = new javax.swing.JLabel();
        jlbPaginationAtual = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jbtEditar = new javax.swing.JButton();
        jbtExcluir = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtbSetores = new javax.swing.JTable();
        jpFiltragem = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jtfFitroSetor = new javax.swing.JTextField();
        jbtFiltrarSetor = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jtfFitroCodigo = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();

        setClosable(true);
        setIconifiable(true);
        setTitle("Controle de Setores");

        jpCadastroSetor.setPreferredSize(new java.awt.Dimension(950, 390));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Código");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Nome do setor");

        jtfNomeSetor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNomeSetorActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Responsável");

        jbtNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/clean.png"))); // NOI18N
        jbtNovo.setText("Novo");
        jbtNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtNovoActionPerformed(evt);
            }
        });

        jbtCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/disk.png"))); // NOI18N
        jbtCadastrar.setText("Cadastrar / Atualizar");
        jbtCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCadastrarActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/refresh.png"))); // NOI18N
        jButton1.setText("Sincronizar com banco do patrimônio");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jlbSetorSelecionado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlbSetorSelecionado.setForeground(new java.awt.Color(51, 102, 0));
        jlbSetorSelecionado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbSetorSelecionado.setText("SETOR SELECIONADO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfNomeResponsavel, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1)
                            .addComponent(jlbSetorSelecionado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(jtfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(194, 194, 194))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jtfNomeSetor, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jbtNovo)
                        .addGap(32, 32, 32)
                        .addComponent(jbtCadastrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jlbSetorSelecionado, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(8, 8, 8)
                .addComponent(jtfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(6, 6, 6)
                .addComponent(jtfNomeSetor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(6, 6, 6)
                .addComponent(jtfNomeResponsavel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtNovo)
                    .addComponent(jbtCadastrar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap())
        );

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

        jbtEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/page_edit.png"))); // NOI18N
        jbtEditar.setText("Editar selecionado");
        jbtEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtEditarActionPerformed(evt);
            }
        });

        jbtExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/delete.png"))); // NOI18N
        jbtExcluir.setText("Excluir selecionado");
        jbtExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtExcluirActionPerformed(evt);
            }
        });

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jbtEditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtExcluir)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbtEditar)
                        .addComponent(jbtExcluir)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtbSetores.setModel(new javax.swing.table.DefaultTableModel(
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
        jtbSetores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbSetoresMouseClicked(evt);
            }
        });
        jtbSetores.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtbSetoresKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jtbSetores);

        jpFiltragem.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtragem", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Nome do setor:");

        jbtFiltrarSetor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/filter.png"))); // NOI18N
        jbtFiltrarSetor.setText("Filtrar");
        jbtFiltrarSetor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtFiltrarSetorActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Código");

        jtfFitroCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfFitroCodigoKeyPressed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/clean.png"))); // NOI18N
        jButton2.setText("Limpar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpFiltragemLayout = new javax.swing.GroupLayout(jpFiltragem);
        jpFiltragem.setLayout(jpFiltragemLayout);
        jpFiltragemLayout.setHorizontalGroup(
            jpFiltragemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFiltragemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpFiltragemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpFiltragemLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(69, 69, 69)
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jpFiltragemLayout.createSequentialGroup()
                        .addComponent(jtfFitroCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jtfFitroSetor, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtFiltrarSetor)))
                .addContainerGap())
        );
        jpFiltragemLayout.setVerticalGroup(
            jpFiltragemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFiltragemLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jpFiltragemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpFiltragemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtFiltrarSetor)
                    .addComponent(jtfFitroSetor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfFitroCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpFiltragem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jpFiltragem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jpCadastroSetorLayout = new javax.swing.GroupLayout(jpCadastroSetor);
        jpCadastroSetor.setLayout(jpCadastroSetorLayout);
        jpCadastroSetorLayout.setHorizontalGroup(
            jpCadastroSetorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCadastroSetorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpCadastroSetorLayout.setVerticalGroup(
            jpCadastroSetorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCadastroSetorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCadastroSetorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jpCadastroSetor);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtfNomeSetorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNomeSetorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNomeSetorActionPerformed

    private void jbtCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCadastrarActionPerformed
        // TODO add your handling code here:

        setor = new Setor();
        try {
            daoSetor.open(true);
            if (validarCampos()) {
                if (!jtfCodigo.getText().isEmpty()) {
                    setor = daoSetor.findByIdNoConnection(Integer.parseInt(jtfCodigo.getText()));
                    if (setor == null) {
                        setor = new Setor();
                    }
                } else {
                    setor = new Setor();
                }
                setor.setId(Integer.parseInt(jtfCodigo.getText()));
                setor.setNome(jtfNomeSetor.getText().trim());
                setor.setResponsavel(jtfNomeResponsavel.getText().trim());
                //                notafiscal.setValor(Funcoes.formatMoedaFloat(jtfValor.getText()));
                if (MessageService.confirmDialog("Salvar informações do setor " + setor.getNome() + "?", "Atenção", this) == MessageService.YES_OPTION) {
                    daoSetor.persist(setor);
                    daoSetor.commit();
                    MessageService.informationMessage("Setor '" + setor.getNome() + "' salvo com sucesso!", "Sucesso", this);
                }
                jbtFiltrarSetor.doClick();
                prepararFormulario();
            }
        } catch (Exception e) {
            daoSetor.rollback();
            MessageService.errorMessage("Erro na inserção da nota", "Erro", this);
            e.printStackTrace();
        } finally {
            daoSetor.close();
        }
    }//GEN-LAST:event_jbtCadastrarActionPerformed

    private void jbtNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtNovoActionPerformed
        // TODO add your handling code here:
        jtfCodigo.setText("");
        jtfNomeSetor.setText("");
        jtfNomeResponsavel.setText("");
        jlbSetorSelecionado.setVisible(false);
        setor = new Setor();
    }//GEN-LAST:event_jbtNovoActionPerformed

    private void jbtFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtFirstActionPerformed
        // TODO add your handling code here:
        if (filtroSetor.getCurrentPage() > 0) {
            filtroSetor.changePage(Configurator.MAX_RESULTS, AbstractFilter.FIRST);
            filtraSetores();
        }
    }//GEN-LAST:event_jbtFirstActionPerformed

    private void jbtPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtPreviousActionPerformed
        // TODO add your handling code here:
        if (filtroSetor.getCurrentPage() > 0) {
            filtroSetor.changePage(Configurator.MAX_RESULTS, AbstractFilter.PREVIOUS);
            filtraSetores();
        }
    }//GEN-LAST:event_jbtPreviousActionPerformed

    private void jbtNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtNextActionPerformed
        // TODO add your handling code here:
        if (filtroSetor.getCurrentPage() < (filtroSetor.getNumPages() - 1)) {
            filtroSetor.changePage(Configurator.MAX_RESULTS, AbstractFilter.NEXT);
            filtraSetores();
        }
    }//GEN-LAST:event_jbtNextActionPerformed

    private void jbtLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLastActionPerformed
        // TODO add your handling code here:
        if (filtroSetor.getCurrentPage() < (filtroSetor.getNumPages() - 1)) {
            filtroSetor.changePage(Configurator.MAX_RESULTS, AbstractFilter.LAST);
            filtraSetores();
        }
    }//GEN-LAST:event_jbtLastActionPerformed

    private void jtbSetoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbSetoresMouseClicked
        // TODO add your handling code here:
        jbtEditar.setEnabled(true);
        jbtExcluir.setEnabled(true);
        if (evt.getClickCount() == 2) {
            jbtEditar.doClick();
        }
    }//GEN-LAST:event_jtbSetoresMouseClicked

    private void jtbSetoresKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtbSetoresKeyReleased
        // TODO add your handling code here:
        if (jtbSetores.getSelectedRow() > -1) {
            if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                jbtExcluir.doClick();
            }
        }
    }//GEN-LAST:event_jtbSetoresKeyReleased

    private void jbtFiltrarSetorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtFiltrarSetorActionPerformed
        // TODO add your handling code here:
        carregaFiltroSetor();
        filtraSetores();
    }//GEN-LAST:event_jbtFiltrarSetorActionPerformed

    private void jtfFitroCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfFitroCodigoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            MessageService.informationMessage("teste", "ok", this);
        }
    }//GEN-LAST:event_jtfFitroCodigoKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        limparCamposFiltragem();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jbtEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtEditarActionPerformed
        // TODO add your handling code here:
        Integer codNota;
        if (jtbSetores.getSelectedRow() > -1) {
            int linhaAtual = jtbSetores.getSelectedRow();
            jtbSetores.changeSelection(linhaAtual, linhaAtual, false, false);
            codNota = Integer.parseInt(jtbSetores.getValueAt(linhaAtual, 0).toString());
            //            jbtNovo.doClick();
            setor = daoSetor.findById(codNota);
            if (setor != null) {
                jtfCodigo.setText("" + setor.getId());
                jtfNomeSetor.setText(setor.getNome());
                jtfNomeResponsavel.setText(setor.getResponsavel());
                jlbSetorSelecionado.setVisible(true);
                jtfNomeSetor.requestFocusInWindow();

            } else {
                MessageService.errorMessage("Setor não foi encontrado", "Atenção", this);
            }
        }
    }//GEN-LAST:event_jbtEditarActionPerformed

    private void jbtExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtExcluirActionPerformed
        // TODO add your handling code here:

        if (jtbSetores.getSelectedRow() > -1) {
            Setor setor;
            Integer codSetor;
            int linhaAtual = jtbSetores.getSelectedRow();
            jtbSetores.changeSelection(linhaAtual, linhaAtual, false, false);
            codSetor = Integer.parseInt(jtbSetores.getValueAt(linhaAtual, 0).toString());
            if (MessageService.confirmDialog("Excluir item selecionado", "Atenção", this) == MessageService.YES_OPTION) {
                try {
                    daoSetor.open(true);
                    setor = daoSetor.findByIdNoConnection(codSetor);

                    if (setor != null) {
                        if (!daoItem.verificaItensPorSetor(setor)) {
                            daoSetor.delete(setor);
                            daoSetor.commit();
                            jbtNovo.doClick();
                            MessageService.informationMessage("Exclusão realizada com sucesso!", "Atenção", this);
                            carregaFiltroSetor();
                            filtraSetores();
                            jtbSetores.requestFocusInWindow();
                        } else {
                            MessageService.warningMessage("Não é possível excluir o setor, pois existem itens dependentes!", "Atenção", this);
                        }
                    } else {
                        MessageService.warningMessage("O setor não existe ou foi excluído recentemente!", "Atenção", this);
                    }

                } catch (Exception e) {
                    MessageService.errorMessage("Erro na exclusão do setor: \n" + e.getMessage(), "Erro", this);
                    daoSetor.rollback();
                } finally {
                    daoSetor.close();
                }
            }
        } else {
            MessageService.warningMessage("Você deve selecionar uma nota fiscal primeiro", "Erro", this);
        }
    }//GEN-LAST:event_jbtExcluirActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (MessageService.confirmDialog("Isso irá sincronizar os setores do banco de dados de patrimônio com o de notas fiscais.\n Tem certeza que quer continuar?", "Atenção", this) == MessageService.YES_OPTION) {
            FacadePatrimonio fp = new FacadePatrimonio();
            try {
                fp.sincronizarSetoresPatrimonio();
                limparCamposFiltragem();
                filtraSetores();
                MessageService.informationMessage("Sincronização finalizada", "Atenção", this);
            } catch (SQLException ex) {
                MessageService.errorMessage("Erro na conexão com o patrimonio! \n(" + ex.getMessage() + ")", "Atenção", this);
                Logger.getLogger(JIFSetores.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton jbtCadastrar;
    private javax.swing.JButton jbtEditar;
    private javax.swing.JButton jbtExcluir;
    private javax.swing.JButton jbtFiltrarSetor;
    private javax.swing.JButton jbtFirst;
    private javax.swing.JButton jbtLast;
    private javax.swing.JButton jbtNext;
    private javax.swing.JButton jbtNovo;
    private javax.swing.JButton jbtPrevious;
    private javax.swing.JLabel jlbNumPaginas;
    private javax.swing.JLabel jlbPaginationAtual;
    private javax.swing.JLabel jlbSetorSelecionado;
    private javax.swing.JPanel jpCadastroSetor;
    private javax.swing.JPanel jpFiltragem;
    private javax.swing.JTable jtbSetores;
    private javax.swing.JTextField jtfCodigo;
    private javax.swing.JTextField jtfFitroCodigo;
    private javax.swing.JTextField jtfFitroSetor;
    private javax.swing.JTextField jtfNomeResponsavel;
    private javax.swing.JTextField jtfNomeSetor;
    // End of variables declaration//GEN-END:variables
}
