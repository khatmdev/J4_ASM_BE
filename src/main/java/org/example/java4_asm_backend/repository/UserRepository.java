package org.example.java4_asm_backend.repository;

import jakarta.persistence.TypedQuery;
import org.example.java4_asm_backend.model.User;

import java.util.List;

public class UserRepository extends AbstractRepository<User, String> {
    @Override
    protected String getEntityId(User entity) {
        return entity.getId();
    }

    public UserRepository() {
        super(User.class);
    }

    public User findByEmail(String email) {
        String jpql = "SELECT u FROM User u WHERE u.email = :email";
        TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    public List<User> findUsersWithPage(int pageNumber, int pageSize) {
        int startPosition = (pageNumber - 1) * pageSize;
        return getEntityManager().createQuery("FROM User", User.class)
                .setFirstResult(startPosition)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public long countUsers() {
        return getEntityManager().createQuery("SELECT COUNT(1) FROM User", Long.class)
                .getSingleResult();
    }

    public boolean existsByEmail(String email) {
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
        Long count = getEntityManager().createQuery(jpql, Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }

}
