package com.customer.syn.service;

import java.util.List;

import com.customer.syn.model.BaseEntity;

public interface BasicRepository<E extends BaseEntity<I>, I extends Number> {
    
    E findByID(I id);
    
    E findReferenceByID(I id);
    
    E update(E entity);
    
    List<E> fetchAll();

    void save(E entity);
    
    void delete(E entity);
    
    void deleteById(I id);

}
