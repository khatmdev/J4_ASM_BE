package org.example.java4_asm_backend.repository;

import org.example.java4_asm_backend.model.Share;
import org.example.java4_asm_backend.model.Video;

import java.util.List;

public class ShareRepository extends AbstractRepository<Share, Long> {
	public ShareRepository() {
		super(Share.class);
	}

	@Override
	protected Long getEntityId(Share entity) {
		return entity.getId();
	}

	public List<Share> findByVideo(Video video) {
		return getEntityManager().createQuery("SELECT s FROM Share s WHERE s.video = :video", Share.class)
				.setParameter("video", video)
				.getResultList();
	}

	public List<Share> findByVideoId(String videoId) {
		return getEntityManager().createQuery("SELECT s FROM Share s WHERE s.video.id = :videoId", Share.class)
				.setParameter("videoId", videoId)
				.getResultList();
	}
}
