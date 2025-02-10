package org.example.java4_asm_backend.service;

import org.example.java4_asm_backend.repository.IRepository;

import java.util.List;

public interface IService<T, idType> extends IRepository<T, idType> {
    @Override
    List<T> findAll();

    @Override
    T findById(idType id);

    @Override
    boolean save(T entity);

    @Override
    boolean deleteById(idType id);

    @Override
    void refresh(T entity);
}
