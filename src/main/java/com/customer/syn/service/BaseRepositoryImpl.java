package com.customer.syn.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.customer.syn.resource.model.BaseEntity;

public abstract class BaseRepositoryImpl<E extends BaseEntity<I>, I extends Number> implements BasicRepository<E, I> {
    
    private final static Logger log = Logger.getLogger(BaseRepositoryImpl.class.getName());
    
    @PersistenceContext
    protected EntityManager em;
    
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
    

    
    // ---------------------------------------------------------------- basic operations
    
    public E findByID(I id) {
        return em.find(clazz, id); 
    }
    
    
    public E findByID(Class<E> type, I id) {
        return em.find(type, id);
    }
    
    
    public E findReferenceByID(I id) {
        return em.getReference(clazz, id);
    }

    
    public List<E> fetchAll() {
        return em.createQuery("from " + clazz.getSimpleName(), clazz)
                .getResultList();
    }

    
    public void save(E entity) {
        if (entity.getId() == null && !exists(entity)) {
            em.persist(entity);
            log.log(Level.INFO, () -> String.format("New entity with id %d persisted.", entity.getId()));
        }
    }

    
    public void delete(E entity) {
        em.remove(entity);
    }
    
    
    public void deleteById(I id) {
        try {
            em.remove(findByID(id));
        } catch (Exception e) {
            log.log(Level.WARNING, e.getMessage());
        }
    }

    
    public E update(E entity) {
        log.log(Level.INFO, () -> "update invoked.");
        
        if (entity.getId() == null && !exists(entity)) {
            throw new IllegalArgumentException("Entity does not exist, yet.");
        }
        return em.merge(entity);
    }
    
    
    protected boolean exists(E entity) {
        return em.createQuery("select count(e) from " + clazz.getSimpleName() + " e where e.id = :id", Long.class)
                .setParameter("id", entity.getId())
                .getSingleResult() == 1;
    }
    
    
    public List<E> findByFullName(String firstName, String lastName) {
        return em.createQuery("select e from " + clazz.getSimpleName()
                        + " e where e.firstName like :firstName and e.lastName like :lastName", clazz)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getResultList();
    }
    
    
    public List<E> findByLastName(String lastName) {
        return em.createQuery("select e from " + clazz.getSimpleName() + " e where e.lastName like :lastName", clazz)
                .setParameter("lastName", lastName)
                .getResultList();
    }
    
    
    public List<E> findByDateRange(LocalDate from, LocalDate to) {
        return em.createQuery("select e from " + clazz.getSimpleName() + " e where e.createdAt between :from and :to", clazz)
                .setParameter("from", from.atStartOfDay().toInstant(ZoneOffset.UTC))
                .setParameter("to", to.atStartOfDay().toInstant(ZoneOffset.UTC))
                .getResultList();
    }
    
    
    
    // ---------------------------------------------------------------- getters
    
    protected EntityManager getEntityManager() {
        return em;
    }

    public Class<E> getClazz() {
        return clazz;
    }

}



