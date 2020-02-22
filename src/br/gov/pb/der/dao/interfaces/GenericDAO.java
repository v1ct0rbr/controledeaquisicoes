package br.gov.pb.der.dao.interfaces;

import br.gov.pb.der.filtros.AbstractFilter;
import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, Type extends Serializable> {

    public void open() throws NullPointerException;

    public void open(boolean withTransaction) throws NullPointerException;

    public void openTransaction();

    public void closeTransaction();

    public void detach(T entity);

    void commit();

    void close() throws NullPointerException;

    void rollback();

    void refresh(T entity);

    public boolean persist(T entity);

    boolean update(T entity);

    boolean delete(T entity);

    public List<T> list();

    public List<String> findAllCombo();

    public T findById(Serializable id);

    public T findByIdNoConnection(Serializable id);
    
    public List<Object[]> listAll();

    public long countByFiltro(AbstractFilter filtro);
    
    public List<T> reportByFilter(AbstractFilter filtro);
    
    public List<Object[]> listarPorFiltro(AbstractFilter filtro);
    
    

}
