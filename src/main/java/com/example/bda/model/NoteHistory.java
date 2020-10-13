package com.example.bda.model;

import org.hibernate.envers.RevisionType;

public class NoteHistory {

    private final Note note;
    private final RevisionType revisionType;

    public NoteHistory(Note note, RevisionType revisionType) {
        this.note = note;
        this.revisionType = revisionType;
    }

    public NoteHistory(Object[] obj) {
        this.note = (Note)obj[0];
        this.revisionType = (RevisionType)obj[2];
    }

    public Note getNote() {
        return note;
    }


    public RevisionType getRevisionType() {
        return revisionType;
    }

}
