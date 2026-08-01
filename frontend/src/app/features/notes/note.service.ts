import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface Note {
  id: number;
  title: string;
  content: string;
  createdAt?: Date;
  updatedAt?: Date;
}

@Injectable({
  providedIn: 'root'
})
export class NoteService {

  private API_URL = `${environment.apiUrl}/api/notes`;

  constructor(private http: HttpClient) { }

  getNotes(): Observable<Note[]> {
    return this.http.get<any>(this.API_URL).pipe(
      map(res => {
        if (Array.isArray(res)) return res;
        if (res && res.content && Array.isArray(res.content)) return res.content;
        return [];
      })
    );
  }

  getNoteById(id: number): Observable<Note> {
    return this.http.get<Note>(`${this.API_URL}/${id}`);
  }

  createNote(title: string, content: string): Observable<Note> {
    return this.http.post<Note>(this.API_URL, { title, content });
  }

  updateNote(id: number, title: string, content: string): Observable<Note> {
    return this.http.put<Note>(`${this.API_URL}/${id}`, { title, content });
  }

  deleteNote(id: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/${id}`);
  }

  searchNotes(q: string): Observable<Note[]> {
    return this.http.get<any>(
      `${this.API_URL}/search?q=${encodeURIComponent(q)}`
    ).pipe(
      map(res => {
        if (Array.isArray(res)) return res;
        if (res && res.content && Array.isArray(res.content)) return res.content;
        return [];
      })
    );
  }
}
