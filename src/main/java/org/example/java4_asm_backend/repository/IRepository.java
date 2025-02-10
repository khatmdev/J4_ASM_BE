package org.example.java4_asm_backend.repository;

import java.util.List;

public interface IRepository<T, IdType> {
    // Lấy tất cả các thực thể
    List<T> findAll();

    // Lấy thực thể theo ID
    T findById(IdType id);

    // Thêm thực thể mới
    boolean save(T entity);

    // Xóa thực thể theo ID
    boolean deleteById(IdType id);

    //Refresh thực thể
    void refresh(T entity);
}
