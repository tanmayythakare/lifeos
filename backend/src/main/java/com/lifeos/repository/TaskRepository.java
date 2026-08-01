package com.lifeos.repository;

import com.lifeos.model.Task;
import com.lifeos.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUser(User user, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.user = :user AND LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Task> searchByUser(@Param("user") User user, @Param("query") String query, Pageable pageable);
}
