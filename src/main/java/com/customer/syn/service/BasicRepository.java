package com.customer.syn.service;

import java.util.List;
import java.util.Optional;

public interface BasicRepository<E, I> {
    
    public Optional<E> findByID(I id);
    
    public List<E> fetchAll();

    public void save(E entity);
    
    public void delete(E entity);
    
    public void deleteById(I id);
    
    public void update(E entity);

}
