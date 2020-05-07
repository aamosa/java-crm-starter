package com.customer.syn.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseRepositoryImpl<E, I> implements BasicRepository<E, I> {
    
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
    
    
    public Optional<E> findByID(I id) {
        return Optional.ofNullable(em.find(clazz, id)); 
    }

    public List<E> fetchAll() {
        return em.createQuery("from " + clazz.getSimpleName(), clazz)
                .getResultList();
    }

    public void save(E entity) {
        em.persist(entity);
    }

    public void delete(E entity) {
        em.remove(entity);
    }
    
    public void deleteById(I id) {
        Optional<E> entity = findByID(id);
        if (entity.isPresent()) {
            em.remove(entity.get());
        }
    }

    public void update(E entity) {
        em.merge(entity);
    }

}
