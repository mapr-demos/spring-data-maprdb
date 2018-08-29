package com.mapr.springframework.data.maprdb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

@NoRepositoryBean
public interface MapRRepository<T, ID> extends CrudRepository<T, ID>, QueryByExampleExecutor<T> {

    @Override
    <S extends T> List<S> saveAll(Iterable<S> entites);

    @Override
    List<T> findAll();

    @Override
    List<T> findAllById(Iterable<ID> iterable);

    <S extends T> S insert(S entity);

    <S extends T> List<S> insert(Iterable<S> entities);

}
