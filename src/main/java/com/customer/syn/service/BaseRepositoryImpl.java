package com.customer.syn.service;

import static java.lang.String.format;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customer.syn.model.BaseEntity;


public abstract class BaseRepositoryImpl<E extends BaseEntity<I>, I extends Number> implements BasicRepository<E, I> {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    @PersistenceContext
    protected EntityManager em;
    
    private Class<E> clazz;
    
    private static final String GET_COUNT = "select count(e) from %s e where e.id = :id";
    private static final String DATE_RANGE = "select e from  %s e where e.createdAt between :from and :to";
    private static final String LAST_NAME = "select e from %s e where e.lastName like :lastName";
    private static final String FULL_NAME = "select e from %s e where e.firstName like :firstName and e.lastName like :lastName";
   
    
    // ----------------------------------------------------------------- constructors
    
    @SuppressWarnings("unchecked")
    protected BaseRepositoryImpl() {
        Type type = this.getClass().getGenericSuperclass();  
        if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) this.getClass()
                    .getGenericSuperclass();
            this.clazz = (Class<E>) p.getActualTypeArguments()[0];
        } 
        else {
            type = ((Class<?>) type).getGenericSuperclass();
            this.clazz = (Class<E>) ((ParameterizedType) type)
                    .getActualTypeArguments()[0];
        }
    }

    
    // ---------------------------------------------------------------- common operations
    
    public E findByID(I id) {
        return em.find(getClazz(), id); 
    }
    
    
    public E findByID(Class<E> type, I id) {
        return em.find(type, id);
    }
    
    
    public E findReferenceByID(I id) {
        return em.getReference(getClazz(), id);
    }

    
    public List<E> fetchAll() {
        return em.createQuery("from " + getEntityName(), getClazz())
                .getResultList();
    }

    
    public void save(E entity) {
        if (entity.getId() == null && !exists(entity)) {
            em.persist(entity);
            if (log.isDebugEnabled())
                log.debug("entity with id: {} persisted.", entity.getId());
        }
    }

    
    public void delete(E entity) {
        if (entity != null && entity.getId() != null)
            deleteById(entity.getId());
    }
    
    
    public void deleteById(I id) {
        getEntityManager().remove(findByID(id));
    }

    
    public E update(E entity) {
        if (entity.getId() == null && !exists(entity))
            throw new IllegalArgumentException("entity does not exist.");
        return em.merge(entity);
    }
    
    
    public boolean exists(E entity) {
        return em.createQuery(format(GET_COUNT, getEntityName()), Long.class)
                .setParameter("id", entity.getId()).getSingleResult() == 1;
    }
    
    
    public List<E> findByFullName(String firstName, String lastName) {
        return em.createQuery(format(FULL_NAME, getEntityName()), getClazz())
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getResultList();
    }
    
    
    public List<E> findByLastName(String lastName) {
        return em.createQuery(format(LAST_NAME, getEntityName()), getClazz())
                .setParameter("lastName", lastName)
                .getResultList();
    }
    
    
    public List<E> findByCreatedDateRange(LocalDate from, LocalDate to) {
        return em.createQuery(format(DATE_RANGE, getEntityName()), getClazz())
                .setParameter("from", from.atStartOfDay().toInstant(ZoneOffset.UTC))
                .setParameter("to", to.atStartOfDay().toInstant(ZoneOffset.UTC))
                .getResultList();
    }
    
    
    // ---------------------------------------------------------------- setters and getters
    
    protected EntityManager getEntityManager() {
        return em;
    }

    protected Class<E> getClazz() {
        return clazz;
    }
    
    protected String getEntityName() {
        return getClazz().getSimpleName();
    }
    
    

}



