package com.example.bda.dao;

import com.example.bda.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface NoteRepository extends JpaRepository<Note, Long> {

}
