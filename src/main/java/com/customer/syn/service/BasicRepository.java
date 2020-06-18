package com.customer.syn.service;

import java.util.List;
import java.util.Optional;

public interface BasicRepository<E, I> {
    
    E findByID(I id);
    
    E findReferenceByID(I id);
    
    E update(E entity);
    
    List<E> fetchAll();

    void save(E entity);
    
    void delete(E entity);
    
    void deleteById(I id);

}
