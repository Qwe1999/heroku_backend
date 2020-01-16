package com.softserve.academy.event.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


public interface BasicRepository<T extends Serializable, I extends Serializable> {

    Optional<T> findFirstById(I id);

    List<T> findAll();

    T save(T entity);

    T update(T object);

    void delete(T entity);

}
