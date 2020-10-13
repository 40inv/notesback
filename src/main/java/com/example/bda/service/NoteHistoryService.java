package com.example.bda.service;

import com.example.bda.model.Note;

import java.util.List;

public interface NoteHistoryService {
    List getFullHistoryById(Long id);
}
