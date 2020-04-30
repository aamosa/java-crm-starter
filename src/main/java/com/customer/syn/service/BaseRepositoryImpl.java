package com.customer.syn.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public abstract class BaseRepositoryImpl<E, I> implements BasicRepository<E, I> {
    
    private static final Logger log = Logger.getLogger(BaseRepositoryImpl.class.getName());
    
    @PersistenceContext(name = "syn")
    private EntityManager em;
    
    private Class<E> clazz;
    
    
    // ---------------------------------------------------------------- constructors
    
    @SuppressWarnings("unchecked")
    public BaseRepositoryImpl() {
        Type type = this.getClass().getGenericSuperclass();  

        if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) this.getClass().getGenericSuperclass();
            this.clazz = (Class<E>) p.getActualTypeArguments()[0];
        } else {
            type = ((Class<?>) type).getGenericSuperclass();
            this.clazz = (Class<E>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
    }

    @Override
    public Optional<E> findByID(I id) {
        return Optional.ofNullable(em.find(clazz, id)); 
    }

    @Override
    public List<E> fetchAll() {
        return em.createQuery("from " + clazz.getSimpleName(), clazz)
                .getResultList();
    }

    @Override
    public void save(E entity) {
        em.persist(entity);
    }

    @Override
    public void delete(E entity) {
        em.remove(entity);
    }
    
    @Override
    public void deleteById(I id) {
        Optional<E> entity = findByID(id);
        if (entity.isPresent()) {
            em.remove(entity.get());
        }
    }

    @Override
    public void update(E entity) {
        em.merge(entity);
    }

}