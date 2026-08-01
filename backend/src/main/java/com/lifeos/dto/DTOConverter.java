package com.lifeos.dto;

import com.lifeos.model.Note;
import com.lifeos.model.Task;

public class DTOConverter {

    public static TaskDTO toDTO(Task task) {
        if (task == null) return null;
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }

    public static NoteDTO toDTO(Note note) {
        if (note == null) return null;
        NoteDTO dto = new NoteDTO();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setUpdatedAt(note.getUpdatedAt());
        return dto;
    }
}
