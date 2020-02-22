/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author victo
 */
public class JDBCUtils {

    private String _sqlString;
    private Map<Integer, Object> _parameters;

    public JDBCUtils(String sql) {
        _sqlString = sql;
    }

    public void setParameters(Map<Integer, Object> param) {
        _parameters = param;
    }

    /* 
     * I'm assuming you already have a method like this
     */
    public ResultSet processQuery(Connection connection) throws SQLException {
        //List results = new ArrayList();
        /* 
         * establish connection here
         */
        PreparedStatement preparedStatement = connection.prepareStatement(this._sqlString);

        if (_parameters != null) {
            /* 
             * Iterate over the map to set parameters 
             */
            for (Integer key : _parameters.keySet()) {
                preparedStatement.setObject(key, _parameters.get(key));
            }
        }

        ResultSet rs = preparedStatement.executeQuery();
        return rs;
        /*
         * process the ResultSet
         */
        //return results;
    }

}
