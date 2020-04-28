package com.customer.syn.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BasicRepository<T extends Serializable, I> {
    
    public Optional<T> findByID(I id);
    
    public List<T> fetchAll();

    public void save(T entity);
    
    public void delete(T entity);
    
    public void update(T entity);

}
