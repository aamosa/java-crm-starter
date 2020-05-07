package com.customer.syn.service;

import java.util.List;
import java.util.Optional;

public interface BasicRepository<E, I> {
    
    Optional<E> findByID(I id);
    
    List<E> fetchAll();

    void save(E entity);
    
    void delete(E entity);
    
    void deleteById(I id);
    
    void update(E entity);

}
