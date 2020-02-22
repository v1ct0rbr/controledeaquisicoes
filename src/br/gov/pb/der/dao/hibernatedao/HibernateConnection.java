/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.dao.hibernatedao;


import br.gov.pb.der.utils.JpaUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

/**
 *
 * @author Victor_2
 */
public class HibernateConnection {

    private EntityManager em;
    public static HibernateConnection conn;

    public static HibernateConnection getInstance() {
        if (conn == null) {
            conn = new HibernateConnection();
        }
        return conn;
    }

    public void open(boolean withTransaction) throws NullPointerException {
        em = JpaUtil.getEntityManager();
        
        if (withTransaction && !getTx().isActive()) {
            getTx().begin();
            
        }
    }

    public void commit() throws NullPointerException {
        if (em.isOpen() && getTx().isActive()) {
            getTx().commit();
        } else {
            throw new NullPointerException("Não existem transações ativas");
        }
    }

    public EntityTransaction getTx() {
        return em.getTransaction();
    }

    public void close() throws NullPointerException {
        closeTransaction();
        if (em.isOpen()) {
            em.clear();
            em.close();
        }
    }

    public void openTransaction() throws PersistenceException {
        getTx().begin();
    }

    public void closeTransaction() {
        if (getTx() != null && getTx().isActive()) {
            getTx().commit();
        }
    }

    public void rollback() {
        if (getTx() != null && getTx().isActive()) {
            getTx().rollback();
        }
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
