package org.example.java4_asm_backend.service;

import org.example.java4_asm_backend.repository.AbstractRepository;

import java.util.List;

public abstract class AbstractService<T, IdType, R extends AbstractRepository<T, IdType>> implements IService<T, IdType>{
    protected final R repository;

    protected AbstractService(R repository) {
        this.repository = repository;
    }


    @Override
    public List<T> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            System.out.println("Error fetching all entities: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public T findById(IdType id) {
        try {
            if (id == null) {
                System.out.println("Error: ID is null.");
                return null;
            }
            return repository.findById(id);
        } catch (Exception e) {
            System.out.println("Error fetching entity by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean save(T entity) {
        try {
            if (entity == null) {
                System.out.println("Error: Entity is null.");
                return false;
            }
            return repository.save(entity);
        } catch (Exception e) {
            System.out.println("Error saving entity: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteById(IdType id) {
        try {
            if (id == null) {
                System.out.println("Error: ID is null.");
                return false;
            }
            return repository.deleteById(id);
        } catch (Exception e) {
            System.out.println("Error deleting entity by ID: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void refresh(T entity) {
        try{
            if (entity == null) {
                System.out.println("Error: Entity is null.");
            }
            repository.refresh(entity);
        } catch (Exception e) {
            System.out.println("Error refreshing entity: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
