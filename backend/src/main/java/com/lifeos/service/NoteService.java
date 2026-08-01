package com.lifeos.service;

import com.lifeos.exception.ResourceNotFoundException;
import com.lifeos.exception.UnauthorizedException;
import com.lifeos.model.Note;
import com.lifeos.model.User;
import com.lifeos.repository.NoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Page<Note> searchNotes(String q, User user, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return getNotes(user, pageable);
        }
        return noteRepository.searchByUser(user, q, pageable);
    }

    public Note createNote(String title, String content, User user) {
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setUser(user);
        return noteRepository.save(note);
    }

    public Page<Note> getNotes(User user, Pageable pageable) {
        return noteRepository.findByUser(user, pageable);
    }

    public Note getNoteById(Long id, User user) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        if (!note.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Unauthorized to access this note");
        }
        return note;
    }

    public Note updateNote(Long id, String title, String content, User user) {
        Note note = getNoteById(id, user);
        note.setTitle(title);
        note.setContent(content);
        return noteRepository.save(note);
    }

    public void deleteNote(Long id, User user) {
        Note note = getNoteById(id, user);
        noteRepository.delete(note);
    }
}
