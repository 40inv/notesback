package com.example.bda.service;

import com.example.bda.model.Note;

public interface NoteService {
    Note updateNote(Long id, Note updatedNote);
    boolean deleteNote(Long noteId);
}
