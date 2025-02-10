package org.example.java4_asm_backend.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("polyoe");
    private static final ThreadLocal<EntityManager> emThreadLocal = new ThreadLocal<>();

    public static void initialize() {
        System.out.println("Initializing EntityManagerFactory...");
        emf.isOpen(); // Kích hoạt khởi tạo
    }

    public static EntityManager getEntityManager() {
        EntityManager em = emThreadLocal.get();
        if (em == null) {
            em = emf.createEntityManager();
            emThreadLocal.set(em);
        }
        return em;
    }

    public static void closeEntityManager() {
        EntityManager em = emThreadLocal.get();
        if (em != null) {
            em.close();
            emThreadLocal.remove();
        }
    }

    public static void closeEntityManagerFactory() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
