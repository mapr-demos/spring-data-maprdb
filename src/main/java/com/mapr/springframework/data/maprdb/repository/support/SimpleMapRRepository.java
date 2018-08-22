package com.mapr.springframework.data.maprdb.repository.support;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import com.mapr.springframework.data.maprdb.repository.MapRRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Optional;

public class SimpleMapRRepository<T, ID> implements MapRRepository {
    private final MapROperations maprOperations;
    private final Class<T> domainClass;

    public SimpleMapRRepository(final MapROperations maprOperations, final Class<T> domainClass) {
        super();
        this.maprOperations = maprOperations;
        this.domainClass = domainClass;

    }

    @Override
    public Iterable findAll(Sort sort) {
        return null;
    }

    @Override
    public Page findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Object save(Object entity) {
        return null;
    }

    @Override
    public Iterable saveAll(Iterable entities) {
        return null;
    }

    @Override
    public Optional findById(Object o) {
        return maprOperations.find(o, domainClass);
    }

    @Override
    public boolean existsById(Object o) {
        return false;
    }

    @Override
    public Iterable findAll() {
        return maprOperations.findAll(domainClass);
    }

    @Override
    public Iterable findAllById(Iterable iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Object o) {

    }

    @Override
    public void delete(Object entity) {

    }

    @Override
    public void deleteAll(Iterable entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Optional findOne(Example example) {
        return Optional.empty();
    }

    @Override
    public Iterable findAll(Example example) {
        return null;
    }

    @Override
    public Iterable findAll(Example example, Sort sort) {
        return null;
    }

    @Override
    public Page findAll(Example example, Pageable pageable) {
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
