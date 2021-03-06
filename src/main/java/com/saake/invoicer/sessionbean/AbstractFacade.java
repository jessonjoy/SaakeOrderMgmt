/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.sessionbean;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.apache.commons.logging.Log;

/**
 *
 * @author jn
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;
        
    @Inject
    private Log log;
    
    @Inject
    private EntityManager em;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected EntityManager getEntityManager(){
        return em;
    }

    public void create(T entity) {
        getEntityManager().persist(entity);
//        getEntityManager().flush();
    }

    public void clearCache() {
        getEntityManager().clear();
        getEntityManager().getEntityManagerFactory().getCache().evict(entityClass);
    }
    
    public void clearEntityFromCache() {
        getEntityManager().getEntityManagerFactory().getCache().evict(entityClass);
    }

    public T edit(T entity) {               
        return getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
