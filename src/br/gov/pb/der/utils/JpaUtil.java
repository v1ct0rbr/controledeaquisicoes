/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.utils;

import br.gov.pb.der.config.Configurator;
import br.gov.pb.der.services.MessageService;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import org.hibernate.exception.JDBCConnectionException;

/**
 *
 * @author Victor_2
 */
public class JpaUtil {

    private static final String TYPE_CONN = "javax.persistence.jdbc";
    private static EntityManagerFactory factory;

    static {
        try {
            //factory = Persistence.createEntityManagerFactory(Configurator.PERSISTENCE_UNIT);
            Properties props = new Properties();
            props.load(new FileInputStream("config/bdconfig.properties"));
            props.put(TYPE_CONN + ".user", Configurator.BD_CONN_USER);
            props.put(TYPE_CONN + ".password", Configurator.BD_CONN_PASS);
            factory = Persistence.createEntityManagerFactory(Configurator.PERSISTENCE_UNIT, props);
        } catch (JDBCConnectionException | PersistenceException ex) {
            MessageService.errorMessage("Erro de conexão: \n" + ex.getMessage(), "Erro de Banco", null);
        } catch (IOException ex) {
            MessageService.errorMessage("Arquivo de configuracao inexistente ou inválido: \n", "Erro de Banco", null);
        }
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void close() {
        factory.close();
    }

}
