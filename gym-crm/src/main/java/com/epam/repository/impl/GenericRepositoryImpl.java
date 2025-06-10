package com.epam.repository.impl;


import com.epam.exception.EntityDoesNotExistException;
import com.epam.repository.GenericRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;


public abstract class GenericRepositoryImpl<T, ID> implements GenericRepository<T, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public GenericRepositoryImpl() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public List<T> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
    }


    public Optional<T> findById(ID id) {
        T trainee = entityManager.find(entityClass, id);
        return Optional.ofNullable(trainee);
    }

    @Transactional
    @Override
    public T save(T entity) {
        Assert.notNull(entity, "Entity must not be null");

        ID id = getEntityId(entity);

        if (id != null && findById(id).isPresent()) {
            return entityManager.merge(entity);
        }

        entityManager.persist(entity);
        return entity;
    }


    @Transactional
    @Override
    public List<T> saveAll(List<T> entities) {
        Assert.notNull(entities, "Entities must not be null");
        return entities.stream().map(this::save).toList();
    }

    @Transactional
    @Override
    public void deleteById(ID id) {
        Optional<T> entityOptional = findById(id);
        T entity = entityOptional.orElseThrow(() -> new EntityDoesNotExistException(id, "ID", "Trainee"));
        entityManager.remove(entity);
    }

    @Transactional
    @Override
    public void delete(T entity) {
        Assert.notNull(entity, "Entity must not be null");
        entityManager.remove(entity);
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @SuppressWarnings("unchecked")
    private ID getEntityId(T entity) {
        try {
            return (ID) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve entity ID", e);
        }
    }
}
