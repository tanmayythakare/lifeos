package com.lifeos.service;

import com.lifeos.exception.ResourceNotFoundException;
import com.lifeos.exception.UnauthorizedException;
import com.lifeos.model.Task;
import com.lifeos.model.TaskPriority;
import com.lifeos.model.TaskStatus;
import com.lifeos.model.User;
import com.lifeos.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Page<Task> searchTasks(String q, User user, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return getTasks(user, pageable);
        }
        return taskRepository.searchByUser(user, q, pageable);
    }

    public Task createTask(String title, String priority, String dueDate, User user) {
        Task task = new Task();
        task.setTitle(title);
        task.setPriority(parsePriority(priority));
        task.setDueDate(parseDate(dueDate));
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Page<Task> getTasks(User user, Pageable pageable) {
        return taskRepository.findByUser(user, pageable);
    }

    public Task getTaskById(Long id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        if (!task.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Unauthorized to access this task");
        }
        return task;
    }

    public Task updateTask(Long taskId, String title, boolean completed, String status, String priority, String dueDate, User user) {
        Task task = getTaskById(taskId, user);
        task.setTitle(title);
        task.setStatus(parseStatus(status, completed));
        task.setPriority(parsePriority(priority));
        task.setDueDate(parseDate(dueDate));
        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId, User user) {
        Task task = getTaskById(taskId, user);
        taskRepository.delete(task);
    }

    private TaskStatus parseStatus(String s, boolean completed) {
        if (completed) return TaskStatus.DONE;
        if (s == null) return TaskStatus.TODO;
        try {
            return TaskStatus.valueOf(s.toUpperCase());
        } catch (Exception e) {
            return TaskStatus.TODO;
        }
    }

    private TaskPriority parsePriority(String p) {
        if (p == null) return TaskPriority.MEDIUM;
        try {
            return TaskPriority.valueOf(p.toUpperCase());
        } catch (Exception e) {
            return TaskPriority.MEDIUM;
        }
    }

    private LocalDate parseDate(String d) {
        if (d == null || d.isBlank()) return null;
        try {
            return LocalDate.parse(d);
        } catch (Exception e) {
            return null;
        }
    }
}
