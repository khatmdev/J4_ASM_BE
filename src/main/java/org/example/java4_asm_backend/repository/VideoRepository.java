package org.example.java4_asm_backend.repository;

import jakarta.persistence.NoResultException;
import org.example.java4_asm_backend.model.Video;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class VideoRepository extends AbstractRepository<Video, String> {
	@Override
	protected String getEntityId(Video entity) {
		return entity.getId();
	}

	public VideoRepository() {
		super(Video.class);
	}

	public List<Video> findByKeyword(String keyword) {
		try(EntityManager em = getEntityManager()) {
			String jpql = "select v from Video o v where v.title like '%" + keyword + "%'";
			TypedQuery<Video> query = em.createQuery(jpql, Video.class);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Lấy video theo trang
	public List<Video> getVideosByPage(int start, int limit) {
		try {
			String jpql = "SELECT v FROM Video v ORDER BY v.views DESC";
			TypedQuery<Video> query = getEntityManager().createQuery(jpql, Video.class);
			query.setFirstResult(start);
			query.setMaxResults(limit);
			return query.getResultList();
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Đếm tổng số video
	public int getTotalVideoCount() {
		String jpql = "SELECT COUNT(v) FROM Video v";
		TypedQuery<Long> query = getEntityManager().createQuery(jpql, Long.class);
		return query.getSingleResult().intValue();
	}

	public Video findByTitle(String title) {
		List<Video> videos = getEntityManager().createQuery("SELECT v FROM Video v WHERE v.title = :title", Video.class)
				.setParameter("title", title)
				.getResultList();

		if (videos.isEmpty()) {
			return null;
		}
		return videos.get(0); // Giả sử chỉ trả về video đầu tiên nếu có
	}

	public List<String> getTitles(){
		String jpql = "SELECT v.title FROM Video v";
		TypedQuery<String> query = getEntityManager().createQuery(jpql, String.class);
		return query.getResultList();
	}
}
