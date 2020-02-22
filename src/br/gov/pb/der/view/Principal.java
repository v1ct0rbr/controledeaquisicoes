/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.view;

import br.gov.pb.der.config.Configurator;
import br.gov.pb.der.services.MessageService;
import br.gov.pb.der.utils.Funcoes;
import br.gov.pb.der.utils.MyJInternalFrame;
import br.gov.pb.der.utils.SplashScreen;
import br.gov.pb.der.view.dialog.JDSobre;
import br.gov.pb.der.view.internal.JIFEmpresas;
import br.gov.pb.der.view.internal.JIFSetores;
import br.gov.pb.der.view.internal.JIFcad_Notafiscal;
import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.persistence.PersistenceException;
import javax.swing.DefaultDesktopManager;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import static javax.swing.JLayeredPane.POPUP_LAYER;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import org.hibernate.exception.JDBCConnectionException;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.postgresql.util.PSQLException;

/**
 *
 * @author victorqueiroga
 */
public class Principal extends javax.swing.JFrame {

    private static String beautyeye;
    /**
     * Creates new form NewJFrame
     */
    DefaultDesktopManager ddM;
    JDesktopPane jDesktopPane;
    JScrollPane jScrollPane;
    public static Properties props;
    BufferedImage background = null;
    Image icone;

    public static Principal principal;

    public static Principal getInstance() {
        if (principal == null) {
            principal = new Principal();
            principal.setTitle("Controle de Aquisições Empresarial");
        }
        return principal;
    }

    public static Properties getProperties() throws IOException {
        if (props == null) {
            props = new Properties();
            props.load(new FileInputStream("config/customizacao.properties"));
        }
        return props;
    }

    public static Principal getInstance2() {
        return principal;
    }

    public static void setInstance(Principal operador) {
        principal = operador;
    }

    public Principal() {

//        InputStream teste = new BufferedInputStream(new FileInputStream(Configurator.PATH_IMAGES + ""));
        try {
            props = getProperties();
            InputStream is = new FileInputStream(Configurator.PATH_IMAGES + props.getProperty("background"));
            background = ImageIO.read(is);
            is = new FileInputStream(Configurator.PATH_IMAGES + props.getProperty("frame_icone"));
            icone = ImageIO.read(is);
            setIconImage(icone);

        } catch (IOException ex) {
            MessageService.warningMessage("Configuração de customização/imagem não encontrada", "Atenção", null);
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
        jlbEmpresa.setText(props.getProperty("empresa_nome"));
        jlbEquipe.setText(jlbEquipe.getText() + Configurator.ADMIN_NAME_DEVELOPER);
        jDesktopPane = new JDesktopPane() {

            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(background.getWidth(), background.getHeight());
            }
        };
//        jDesktopPane.putClientProperty("JDesktopPane.dragMode", "outline");
        jScrollPane = new JScrollPane(jDesktopPane);
//       
        ddM = new DefaultDesktopManager() {
            @Override
            public void iconifyFrame(JInternalFrame jif) {
                int lyr = jif.getLayer();
                jif.setLayer(POPUP_LAYER);
                super.iconifyFrame(jif);
                jif.setLayer(lyr);
            }
        };

        jDesktopPane.setDesktopManager(ddM);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("/br/gov/pb/der/images/icons/main_icon.png"));
        HashSet backup = new HashSet(this.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        HashSet conj = (HashSet) backup.clone();
        conj.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jPanel2, BorderLayout.WEST);
        getContentPane().add(jDesktopPane, BorderLayout.CENTER);
        getContentPane().add(jPanel1, BorderLayout.SOUTH);
        this.setExtendedState(MAXIMIZED_BOTH);
    }

    public boolean minimizeAllWindowsExceptThis(JInternalFrame jif) throws PropertyVetoException {
        boolean alreadyHasComponent = false;
        for (JInternalFrame comp : jDesktopPane.getAllFrames()) {
            if (jif.equals(comp)) {
                alreadyHasComponent = true;
                continue;
            }
            comp.setIcon(true);
            ddM.minimizeFrame(comp);
        }
        return alreadyHasComponent;
    }

    public void removeAllInternalFrames() {
        for (JInternalFrame jif : jDesktopPane.getAllFrames()) {
            jif.dispose();
        }
        jDesktopPane.removeAll();
    }

    public void addInternalframe(JInternalFrame jif, boolean maximize) throws PropertyVetoException {
//        boolean alreadyHasComponent = false;
        if (jif != null) {
            if (minimizeAllWindowsExceptThis(jif)) {
                if (maximize) {
                    ddM.maximizeFrame(jif);
                    jif.setMaximum(true);
                }
            } else {
                jDesktopPane.add(jif);
                jif.setVisible(true);
                try {
                    if (maximize) {
                        jif.setMaximum(true);
                    }
                } catch (PropertyVetoException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            jDesktopPane.getDesktopManager().deiconifyFrame(jif);
            jif.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Componente não foi inicializado", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jbtNotas = new javax.swing.JButton();
        jbtCalc = new javax.swing.JButton();
        jbtEmpresas = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jbtChangeWindow = new javax.swing.JButton();
        jbtSetores = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jlbEmpresa = new javax.swing.JLabel();
        jlbEquipe = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jmiNotas = new javax.swing.JMenuItem();
        jmiEmpresas = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jmSair = new javax.swing.JMenuItem();
        jmTools = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jmiAlternarEntreJanelas = new javax.swing.JMenuItem();
        jmiCloseJanelaAtual = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        jMenuItem2.setText("jMenuItem2");

        jMenuItem3.setText("jMenuItem3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(icone);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jbtNotas.setBackground(new java.awt.Color(255, 255, 255));
        jbtNotas.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jbtNotas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/nota_fiscal64x64.png"))); // NOI18N
        jbtNotas.setText("Notas Fiscais");
        jbtNotas.setToolTipText("");
        jbtNotas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtNotas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtNotas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtNotasActionPerformed(evt);
            }
        });

        jbtCalc.setBackground(new java.awt.Color(255, 255, 255));
        jbtCalc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jbtCalc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/calculadora.png"))); // NOI18N
        jbtCalc.setText("Calculadora");
        jbtCalc.setToolTipText("Abrir Calculadora");
        jbtCalc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtCalc.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCalcActionPerformed(evt);
            }
        });

        jbtEmpresas.setBackground(new java.awt.Color(255, 255, 255));
        jbtEmpresas.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jbtEmpresas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/company64x64.png"))); // NOI18N
        jbtEmpresas.setText("Empresas");
        jbtEmpresas.setToolTipText("");
        jbtEmpresas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtEmpresas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtEmpresas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtEmpresasActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/close.png"))); // NOI18N
        jButton2.setText("Fechar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jbtChangeWindow.setBackground(new java.awt.Color(255, 255, 255));
        jbtChangeWindow.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jbtChangeWindow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/alternar_entre_janelas.png"))); // NOI18N
        jbtChangeWindow.setText("Alternar janela");
        jbtChangeWindow.setToolTipText("");
        jbtChangeWindow.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtChangeWindow.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtChangeWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtChangeWindowActionPerformed(evt);
            }
        });

        jbtSetores.setBackground(new java.awt.Color(255, 255, 255));
        jbtSetores.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jbtSetores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/department.png"))); // NOI18N
        jbtSetores.setText("Setores");
        jbtSetores.setToolTipText("");
        jbtSetores.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSetores.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSetores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSetoresActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbtSetores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtEmpresas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtNotas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtCalc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtChangeWindow, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtNotas)
                .addGap(4, 4, 4)
                .addComponent(jbtEmpresas)
                .addGap(4, 4, 4)
                .addComponent(jbtSetores, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtChangeWindow)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtCalc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jlbEmpresa.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlbEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlbEmpresa.setText("Empresa");

        jlbEquipe.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlbEquipe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlbEquipe.setText("Desenvolvido por: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jlbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlbEquipe, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbEmpresa)
                    .addComponent(jlbEquipe))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("Arquivo");

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/nota_fiscal_16x16.png"))); // NOI18N
        jMenu4.setText("Controle de Notas");

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Cadastro de Nota fiscal");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jmiNotas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        jmiNotas.setText("Lista de Notas fiscal");
        jmiNotas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiNotasActionPerformed(evt);
            }
        });
        jMenu4.add(jmiNotas);

        jMenu1.add(jMenu4);

        jmiEmpresas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jmiEmpresas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/company16x16.png"))); // NOI18N
        jmiEmpresas.setText("Controle de Empresas");
        jmiEmpresas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiEmpresasActionPerformed(evt);
            }
        });
        jMenu1.add(jmiEmpresas);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("Controle de setores");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jmSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jmSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/gov/pb/der/images/icons/close16x16.png"))); // NOI18N
        jmSair.setText("Sair");
        jmSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmSairActionPerformed(evt);
            }
        });
        jMenu1.add(jmSair);

        jMenuBar1.add(jMenu1);

        jmTools.setText("Ferramentas");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Calculadora");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jmTools.add(jMenuItem4);

        jMenu3.setText("Janelas");

        jmiAlternarEntreJanelas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_MASK));
        jmiAlternarEntreJanelas.setText("Alternar entre janelas");
        jmiAlternarEntreJanelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiAlternarEntreJanelasActionPerformed(evt);
            }
        });
        jMenu3.add(jmiAlternarEntreJanelas);

        jmiCloseJanelaAtual.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jmiCloseJanelaAtual.setText("Fechar janela atual");
        jmiCloseJanelaAtual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiCloseJanelaAtualActionPerformed(evt);
            }
        });
        jMenu3.add(jmiCloseJanelaAtual);

        jmTools.add(jMenu3);

        jMenuBar1.add(jmTools);

        jMenu2.setText("Ajuda");

        jMenuItem1.setText("Sobre");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 955, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCalcActionPerformed
        // TODO add your handling code here:
        Funcoes.abrirCalc();
    }//GEN-LAST:event_jbtCalcActionPerformed

    private void jbtNotasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtNotasActionPerformed
        // TODO add your handling code here:
        try {
            addInternalframe(JIFcad_Notafiscal.getInstance(), true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jbtNotasActionPerformed

    private void jmSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmSairActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jmSairActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (MessageService.confirmDialog("Sair da Aplicação", "Confirmação", this) == MessageService.YES_OPTION) {
            jmSair.doClick();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jbtEmpresasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtEmpresasActionPerformed
        // TODO add your handling code here:
        try {
            addInternalframe(JIFEmpresas.getInstance(), true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jbtEmpresasActionPerformed

    private void jmiEmpresasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiEmpresasActionPerformed
        // TODO add your handling code here:
        jbtEmpresas.doClick();
    }//GEN-LAST:event_jmiEmpresasActionPerformed

    private void jmiNotasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiNotasActionPerformed
        // TODO add your handling code here:
        try {
            addInternalframe(JIFcad_Notafiscal.getInstance(JIFcad_Notafiscal.TAB_LIST), true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jmiNotasActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        JDSobre.getInstance().setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        jbtCalc.doClick();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jbtChangeWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtChangeWindowActionPerformed
        // TODO add your handling code here:

        int selectedFrameIndex = 0;
        int frameIndex = Configurator.IFRAME_NOTAS;

        List<Integer> frameIndexes = new ArrayList<>();
        for (JInternalFrame frame : jDesktopPane.getAllFrames()) {
            int currIndex = ((MyJInternalFrame) frame).getFrameIndex();
            frameIndexes.add(currIndex);
        }

        if (jDesktopPane.getSelectedFrame() != null) {
            selectedFrameIndex = frameIndexes.indexOf(((MyJInternalFrame) jDesktopPane.getSelectedFrame()).getFrameIndex());
        }

        selectedFrameIndex += 1;

        try {
            frameIndex = frameIndexes.get(selectedFrameIndex);
        } catch (IndexOutOfBoundsException ex) {
            selectedFrameIndex = 0;
            frameIndex = frameIndexes.get(selectedFrameIndex);
        }

        for (JInternalFrame frame : jDesktopPane.getAllFrames()) {
            if (((MyJInternalFrame) frame).getFrameIndex() == frameIndex) {
                try {
                    addInternalframe(frame, true);
                } catch (PropertyVetoException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
    }//GEN-LAST:event_jbtChangeWindowActionPerformed

    private void jmiAlternarEntreJanelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiAlternarEntreJanelasActionPerformed
        // TODO add your handling code here:
        jbtChangeWindow.doClick();
    }//GEN-LAST:event_jmiAlternarEntreJanelasActionPerformed

    private void jmiCloseJanelaAtualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiCloseJanelaAtualActionPerformed
        // TODO add your handling code here:
        JInternalFrame jif = jDesktopPane.getSelectedFrame();
        if (jif != null) {
            jif.dispose();
        }
    }//GEN-LAST:event_jmiCloseJanelaAtualActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        try {
            addInternalframe(JIFcad_Notafiscal.getInstance(JIFcad_Notafiscal.TAB_FORM), true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jbtSetoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSetoresActionPerformed
        // TODO add your handling code here:
        try {
            addInternalframe(JIFSetores.getInstance(), true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jbtSetoresActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        jbtSetores.doClick();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
        
         */

        try {
            SplashScreen.getInstance().atualizarStatus("Testando banco de dados...");
            Funcoes.testarBD();
        } catch (JDBCConnectionException | PersistenceException | PSQLException | NullPointerException ex) {
            MessageService.errorMessage("Verifique as configurações com o banco de dados", "Aviso", null);
            System.exit(0);
        }
        String lookAndFeel = beautyeye;
        try {
            SplashScreen.getInstance().atualizarStatus("Carregando estilo...");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.setLookAndFeel("joxy.JoxyLookAndFeel");
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
//          UIManager.setLookAndFeel(
//          UIManager.getSystemLookAndFeelClassName());
//          UIManager.setLookAndFeel(beautyeye);

        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SplashScreen.getInstance().atualizarStatus("Carregando dados...");
                Principal.getInstance().setVisible(false);
                getInstance().setVisible(false);
                SplashScreen.getInstance().atualizarStatus("Finalizando..");
                SplashScreen.finishInstance();
                Principal.getInstance().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtCalc;
    private javax.swing.JButton jbtChangeWindow;
    private javax.swing.JButton jbtEmpresas;
    private javax.swing.JButton jbtNotas;
    private javax.swing.JButton jbtSetores;
    private javax.swing.JLabel jlbEmpresa;
    private javax.swing.JLabel jlbEquipe;
    private javax.swing.JMenuItem jmSair;
    private javax.swing.JMenu jmTools;
    private javax.swing.JMenuItem jmiAlternarEntreJanelas;
    private javax.swing.JMenuItem jmiCloseJanelaAtual;
    private javax.swing.JMenuItem jmiEmpresas;
    private javax.swing.JMenuItem jmiNotas;
    // End of variables declaration//GEN-END:variables

}
