package com.example.bda.service;

import com.example.bda.dao.NoteRepository;
import com.example.bda.model.Note;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;

@Service
public class NoteServiceImpl implements NoteService {
    private final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

    private NoteRepository repository;

    @Autowired
    private EntityManagerFactory emf;

    NoteServiceImpl() { /*Needed only for initializing spy in unit tests*/}

    @Autowired
    NoteServiceImpl(NoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Note updateNote(Long id, Note updatedNote) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Note result = em.find(Note.class, id);;
        if (result != null) {
            result.setTitle(updatedNote.getTitle());
            result.setContent(updatedNote.getContent());
            tx.commit();
        }
        em.close();
        return result;
    }


    @Override
    public boolean deleteNote(Long noteId) {
        boolean result = false;
        if (repository.existsById(noteId)) {
            repository.deleteById(noteId);
            logger.info("Note with id {} deleted.", noteId);
            result = true;
        }
        return result;
    }
}
