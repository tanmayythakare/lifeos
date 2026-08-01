package com.lifeos.repository;

import com.lifeos.model.Note;
import com.lifeos.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findByUser(User user, Pageable pageable);

    @Query("SELECT n FROM Note n WHERE n.user = :user AND (LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(n.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Note> searchByUser(@Param("user") User user, @Param("query") String query, Pageable pageable);
}
