/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.utils;

import br.gov.pb.der.dao.factory._DAOFactory;
import br.gov.pb.der.dao.interfaces.SetorDao;
import br.gov.pb.der.modelo.Setor;
import br.gov.pb.der.modelo.view.SetorView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author victorqueiroga
 */
public class FacadePatrimonio {

    ConectarBancoPatrimonio connBD;
    Connection conn;

    public FacadePatrimonio() {
        connBD = ConectarBancoPatrimonio.getInstance();
    }

    public synchronized void sincronizarSetoresPatrimonio() throws SQLException {
//        List<SetorView> setoresDoPatrimonio = new ArrayList<>();
        List<Setor> setoresPatrimonio = new ArrayList<>();
        SetorDao daoSetor = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = connBD.getConexao();
            String sql = "select id, descricao from setor order by id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                setoresPatrimonio.add(new Setor(rs.getInt(1), rs.getString(2)));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            rs.close();
            pstmt.close();
            conn.rollback();
            e.printStackTrace();
        } finally {
            connBD.desConecta(conn);
        }

        try {
            daoSetor = _DAOFactory.getFactory().getSetorDao();
            daoSetor.open(true);
            for (Setor sp : setoresPatrimonio) {
                Setor searchSetor = daoSetor.findByIdNoConnection(sp.getId());
                if (searchSetor != null && !searchSetor.getNome().equals(sp.getNome())) {
                    searchSetor.setNome(sp.getNome());
                    daoSetor.update(searchSetor);
                } else {
                    daoSetor.persist(sp);
                }
            }
            daoSetor.commit();
        } catch (Exception ex) {
            daoSetor.rollback();
            ex.printStackTrace();
        } finally {
            daoSetor.close();
        }

    }
}
