package com.example.bda.service;

import com.example.bda.dto.NoteDto;
import com.example.bda.dto.NoteHistoryDto;
import com.example.bda.model.Note;
import com.example.bda.model.NoteHistory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.cfg.Configuration;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;

@Service
public class NoteHistoryImpl implements NoteHistoryService {

    @PersistenceUnit
    private EntityManagerFactory emf;

    protected SessionFactory getSessionFactory() {
        return emf.unwrap(SessionFactory.class);
    }

    public AuditReader getAuditReader() {
        SessionFactory sessionFactory = getSessionFactory();
        Session session = sessionFactory.openSession();
        return  AuditReaderFactory.get(session);
    }

    @Override
    public List getFullHistoryById(Long id) {
        AuditReader auditReader = getAuditReader();
        AuditQuery q = auditReader.createQuery().forRevisionsOfEntity(Note.class, false,true);
        q.add(AuditEntity.id().eq(id));
        List<Object[]> revisions = q.getResultList();
        List<NoteHistoryDto> result = revisions.stream().map(element -> new NoteHistoryDto(element)).collect(Collectors.toList());
        return result;
    }
}
