import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { JwtInterceptor } from './core/interceptors/jwt.interceptor';
import { TaskListComponent } from './features/tasks/task-list/task-list.component';
import { NoteListComponent } from './features/notes/note-list/note-list.component';
import { NoteDetailComponent } from './features/notes/note-detail/note-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    TaskListComponent,
    NoteListComponent,
    NoteDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
