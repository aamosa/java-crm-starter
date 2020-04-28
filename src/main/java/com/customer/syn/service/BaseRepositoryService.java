package com.customer.syn.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public abstract class BaseRepositoryService<T extends Serializable, I> implements BasicRepository<T, I> {
    
    private static final Logger log = Logger.getLogger(BaseRepositoryService.class.getName());
    
    @PersistenceContext(name = "syn")
    private EntityManager em;
    
    private final Class<T> clazz;
    
    
    // -------------------------------------------------------------------- constructors
    
    public BaseRepositoryService(Class<T> clazz) {
        this.clazz = clazz;
//        parameterizedClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
//                .getActualTypeArguments()[0];
    }

    @Override
    public Optional<T> findByID(I id) {
        log.info("Find by ID: [ " + id + " ]");
        return Optional.ofNullable(em.find(clazz, id)); 
    }

    @Override
    public List<T> fetchAll() {
        return em.createQuery("from " + clazz.getName(), clazz)
                .getResultList();
    }

    @Override
    public void save(T entity) {
        em.persist(entity);
    }

    @Override
    public void delete(T entity) {
        em.remove(entity);
    }

    @Override
    public void update(T entity) {
        em.merge(entity);
    }

}
