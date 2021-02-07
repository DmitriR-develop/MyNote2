package com.dmitri.mynote2;

public interface NoteSourceInterface {

    Note getNote(int position);

    int size();

    void deleteNote(int position);

    void changeNote(int position, Note note);

    void addNote(Note note);

    void clearNotes();
}