package com.cinema.user.repository;

import com.cinema.user.models.MovieShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieShareRepository extends JpaRepository<MovieShare, Long> {
	@Query("""
			SELECT share FROM MovieShare share
			WHERE share.recipientUserId = :recipientUserId
				OR LOWER(share.friendEmail) = LOWER(:recipientEmail)
			ORDER BY share.createdAt DESC
			""")
	List<MovieShare> findReceivedShares(
			@Param("recipientUserId") Long recipientUserId,
			@Param("recipientEmail") String recipientEmail);
}