package com.softserve.academy.event.service.db;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BasicService<T extends Serializable, I extends Serializable> {

    Optional<T> findFirstById(I id);

    List<T> findAll();

    T save(T entity);

    T update(T object);

    void delete(T entity);

}
