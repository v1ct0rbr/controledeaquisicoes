/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.dao.hibernatedao;

/**
 *
 * @author Victor_2
 */
import br.gov.pb.der.dao.interfaces.GenericDAO;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

/**
 *
 * @author Victor_2
 * @param <T>
 * @param <Type>
 */
public abstract class HibernateDao<T, Type extends Serializable> implements GenericDAO<T, Type> {

    private Class<T> entityClass;
    private HibernateConnection conn;

    public HibernateDao(Class<T> entityClass) {
        this.conn = HibernateConnection.getInstance();
        this.entityClass = entityClass;
    }

    @Override
    public void open() throws NullPointerException {
        conn.open(false);
    }

    @Override
    public void open(boolean withTransaction) throws NullPointerException {
        conn.open(withTransaction);

    }

    @Override
    public void openTransaction() throws PersistenceException {
        conn.getTx().begin();
    }

    @Override
    public void closeTransaction() {
        conn.closeTransaction();
    }

    @Override
    public void commit() throws NullPointerException {
        conn.commit();
    }

    @Override
    public void close() throws NullPointerException {
        conn.close();
    }

    @Override
    public void rollback() {
        conn.rollback();
    }

    @Override
    public void refresh(T entity) {
        conn.getEm().refresh(entity);
    }

    @Override
    public boolean persist(T entity) {
        try {
            conn.getEm().persist(entity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean update(T entity) {
        try {

            conn.getEm().merge(entity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean delete(T entity) {
        try {
            conn.getEm().remove(entity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<T> list() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Object[]> listAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T findById(Serializable id) {
        T object;
        try {
            open();
            object = conn.getEm().find(entityClass, id);
        } finally {
            close();
        }
        return object;

    }

    @Override
    public T findByIdNoConnection(Serializable id) {
        T entity;
        entity = conn.getEm().find(entityClass, id);
        return entity;
    }

    public EntityManager getEm() {
        return this.conn.getEm();
    }

    @Override
    public void detach(T entity) {
        conn.getEm().detach(entity);
    }

    @Override
    public List<String> findAllCombo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
//    public long countByFiltro(AbstractFilter filtro) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
