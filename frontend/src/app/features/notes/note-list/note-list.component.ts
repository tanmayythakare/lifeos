import { Component, OnInit, ViewEncapsulation, AfterViewChecked } from '@angular/core';
import { NoteService, Note } from '../note.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-note-list',
  templateUrl: './note-list.component.html',
  styleUrls: ['./note-list.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class NoteListComponent implements OnInit, AfterViewChecked {

  ngAfterViewChecked(): void {
    if ((window as any).lucide) {
      (window as any).lucide.createIcons();
    }
  }

  notes: Note[] = [];

  searchQuery = '';
  noteTitle = '';
  noteContent = '';
  editingNote: Note | null = null;

  deleteConfirmNoteId: number | null = null;

  isLoading = false;
  errorMessage = '';
  successMessage: string | null = null;

  get filteredNotes(): Note[] {
    const validNotes = (this.notes || []).filter(note =>
      (note?.title && note.title.trim() !== '') || (note?.content && note.content.trim() !== '')
    );

    if (!this.searchQuery.trim()) {
      return validNotes;
    }

    const query = this.searchQuery.toLowerCase();
    return validNotes.filter(note =>
      note.title.toLowerCase().includes(query) ||
      note.content.toLowerCase().includes(query)
    );
  }

  constructor(
    private noteService: NoteService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.noteService.getNotes().subscribe(notes => {
        this.notes = notes.filter(n => n.id === Number(id));
      });
    } else {
      this.loadNotes();
    }
  }

  loadNotes() {
    this.isLoading = true;
    this.errorMessage = '';
    this.noteService.getNotes().subscribe({
      next: (notes) => {
        this.notes = notes;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load notes. Please try again.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  editNote(note: Note) {
    this.router.navigate(['/notes', note.id]);
  }

  showSuccess(message: string) {
    this.successMessage = message;
    setTimeout(() => {
      this.successMessage = null;
    }, 3000);
  }

  createNewNote() {
    this.isLoading = true;
    this.noteService.createNote('New Note', 'Start writing...').subscribe({
      next: (note: any) => {
        this.isLoading = false;
        if (note && note.id) {
          this.router.navigate(['/notes', note.id]);
        } else if (note && note.content && note.content[0] && note.content[0].id) {
          this.router.navigate(['/notes', note.content[0].id]);
        } else {
          this.errorMessage = 'Failed to create note: invalid response from server.';
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Failed to create note. Please try again.';
        console.error(err);
      }
    });
  }

  toggleDeleteConfirm(note: Note) {
    if (this.deleteConfirmNoteId === note.id) {
      this.deleteConfirmNoteId = null;
    } else {
      this.deleteConfirmNoteId = note.id;
    }
  }

  confirmDelete() {
    if (!this.deleteConfirmNoteId) return;
    this.isLoading = true;
    this.noteService.deleteNote(this.deleteConfirmNoteId)
      .subscribe({
        next: () => {
          this.showSuccess('Note deleted successfully');
          this.deleteConfirmNoteId = null;
          this.loadNotes();
        },
        error: (err) => {
          this.errorMessage = 'Failed to delete note.';
          this.isLoading = false;
          this.deleteConfirmNoteId = null;
          console.error(err);
        }
      });
  }
}
