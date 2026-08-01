package com.lifeos.controller;

import com.lifeos.dto.DTOConverter;
import com.lifeos.dto.NoteDTO;
import com.lifeos.model.Note;
import com.lifeos.model.User;
import com.lifeos.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/search")
    public Page<NoteDTO> searchNotes(@RequestParam String q, @AuthenticationPrincipal User user, @PageableDefault(size = 20) Pageable pageable) {
        return noteService.searchNotes(q, user, pageable)
                .map(DTOConverter::toDTO);
    }

    @GetMapping("/{id}")
    public NoteDTO getNote(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Note note = noteService.getNoteById(id, user);
        return DTOConverter.toDTO(note);
    }

    @PostMapping
    public NoteDTO createNote(@Valid @RequestBody NoteDTO request, @AuthenticationPrincipal User user) {
        Note note = noteService.createNote(
                request.getTitle(),
                request.getContent(),
                user
        );
        return DTOConverter.toDTO(note);
    }

    @GetMapping
    public Page<NoteDTO> getNotes(@AuthenticationPrincipal User user, @PageableDefault(size = 50) Pageable pageable) {
        return noteService.getNotes(user, pageable)
                .map(DTOConverter::toDTO);
    }

    @PutMapping("/{id}")
    public NoteDTO updateNote(@PathVariable Long id,
                              @Valid @RequestBody NoteDTO request,
                              @AuthenticationPrincipal User user) {
        Note note = noteService.updateNote(
                id,
                request.getTitle(),
                request.getContent(),
                user
        );
        return DTOConverter.toDTO(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id, @AuthenticationPrincipal User user) {
        noteService.deleteNote(id, user);
        return ResponseEntity.noContent().build();
    }
}
