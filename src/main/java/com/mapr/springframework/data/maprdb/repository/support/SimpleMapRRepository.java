package com.mapr.springframework.data.maprdb.repository.support;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import com.mapr.springframework.data.maprdb.repository.MapRRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class SimpleMapRRepository<T, ID> implements MapRRepository<T, ID> {

    private final MapROperations maprOperations;
    private final Class<T> domainClass;

    public SimpleMapRRepository(final MapROperations maprOperations, final Class<T> domainClass) {
        super();
        this.maprOperations = maprOperations;
        this.domainClass = domainClass;
    }

    @Override
    public Iterable<T> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends T> S save(S entity) {
        return maprOperations.save(entity);
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return maprOperations.save(entities);
    }

    @Override
    public Optional<T> findById(final ID id) {
        return maprOperations.findById(id, domainClass);
    }

    @Override
    public boolean existsById(final ID id) {
        return findById(id).isPresent();
    }

    @Override
    public List<T> findAll() {
        return maprOperations.findAll(domainClass);
    }

    @Override
    public <S extends T> S insert(S entity) {
        return maprOperations.insert(entity);
    }

    @Override
    public <S extends T> List<S> insert(Iterable<S> entities) {
        return maprOperations.insert(entities);
    }

    @Override
    public Iterable<T> findAllById(Iterable iterable) {
        return null;
    }

    @Override
    public long count() {
        return maprOperations.count(domainClass);
    }

    @Override
    public void deleteById(final ID id) {
        maprOperations.removeById(id, domainClass);
    }

    @Override
    public void delete(Object entity) {
        maprOperations.remove(entity);
    }

    @Override
    public void deleteAll(Iterable entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        deleteAll(findAll());
    }

    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends T> Iterable<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends T> Iterable<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public long count(Example example) {
        return 0;
    }

    @Override
    public boolean exists(Example example) {
        return false;
    }

}
