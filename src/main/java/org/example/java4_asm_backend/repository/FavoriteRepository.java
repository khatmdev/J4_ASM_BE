package org.example.java4_asm_backend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.java4_asm_backend.model.Favorite;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.java4_asm_backend.model.User;
import org.example.java4_asm_backend.model.Video;

import java.util.List;

public class FavoriteRepository extends AbstractRepository<Favorite, Long> {
	@Override
	protected Long getEntityId(Favorite entity) {
		return entity.getId();
	}

	public FavoriteRepository() {
		super(Favorite.class);
	}

	private Favorite findFavoriteByVideoAndUser(String videoId, String userId) {
		String jpql = "SELECT f FROM Favorite f WHERE f.video.id = :videoId AND f.user.id = :userId";
		TypedQuery<Favorite> query = getEntityManager().createQuery(jpql, Favorite.class);
		query.setParameter("videoId", videoId);
		query.setParameter("userId", userId);
		return query.getSingleResult();
	}

	public Favorite findByVideoAndUser(String videoId, String userId) {
		try {
			return findFavoriteByVideoAndUser(videoId, userId);
		} catch (NoResultException e) {
			System.out.println("repository: favorite not found.");
			return null; // Hoặc xử lý ngoại lệ tùy ý
		}
	}


	public boolean removeFavoriteUnlike(String videoId, String userId) {
		EntityManager em = getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();

			// Sử dụng phương thức tái sử dụng
			Favorite favorite = findFavoriteByVideoAndUser(videoId, userId);

			if (favorite != null) {
				// Xóa Favorite
				em.remove(favorite);

				// Làm mới User để đồng bộ lại danh sách Favorites
				User user = favorite.getUser();
				em.refresh(user);

				transaction.commit();
				return true;
			} else {
				transaction.rollback();
				return false;
			}
		} catch (NoResultException e) {
			transaction.rollback();
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			return false;
		}
	}

	public List<Favorite> findByVideo(Video video) {
		return getEntityManager().createQuery("SELECT f FROM Favorite f WHERE f.video = :video", Favorite.class)
				.setParameter("video", video)
				.getResultList();
	}

	public List<Favorite> findByVideoId(String videoId) {
		return getEntityManager().createQuery("SELECT f FROM Favorite f WHERE f.video.id = :videoId", Favorite.class)
				.setParameter("videoId", videoId)
				.getResultList();
	}
}
