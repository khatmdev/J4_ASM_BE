package org.example.java4_asm_backend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.java4_asm_backend.utils.EntityManagerFactoryUtil;

import java.util.List;

public abstract class AbstractRepository<T, IdType> implements IRepository<T, IdType> {
    private final Class<T> entityClass;

    public AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // Lấy EntityManager từ EntityManagerFactoryUtil
    protected EntityManager getEntityManager() {
        return EntityManagerFactoryUtil.getEntityManager();
    }

    @Override
    public List<T> findAll() {
        EntityManager em = getEntityManager();
        TypedQuery<T> query = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
        return query.getResultList();
    }

    @Override
    public T findById(IdType id) {
        return getEntityManager().find(entityClass, id);
    }

    @Override
    public boolean save(T entity) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            T existingEntity = findById(getEntityId(entity));
            if (existingEntity != null) {
                // Cập nhật dữ liệu từ entity mới sang entity cũ (giữ lại các trường cũ không được cập nhật)
                copyNonNullProperties(entity, existingEntity);
                em.merge(existingEntity); // Cập nhật
            } else {
                em.persist(entity); // Tạo mới
            }

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteById(IdType id) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            T entity = em.find(entityClass, id);
            if (entity != null) {
                transaction.begin();
                em.remove(entity);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void refresh(T entity) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            em.refresh(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Phương thức trừu tượng để lấy ID của thực thể (do từng thực thể sẽ có cách lấy ID khác nhau)
    protected abstract IdType getEntityId(T entity);

    // Phương thức hỗ trợ save (update)
    private void copyNonNullProperties(T source, T target) {
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
