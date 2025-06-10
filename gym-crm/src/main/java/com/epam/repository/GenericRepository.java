package com.epam.repository;

import java.util.List;
import java.util.Optional;


public interface GenericRepository<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);

    T save(T entity);

    List<T> saveAll(List<T> entities);

    void deleteById(ID id);

    void delete(T entity);

    boolean existsById(ID id);

}
