package com.example.bda.dto;

import com.example.bda.model.Note;
import lombok.Data;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;

import java.time.ZoneId;

@Data
public class NoteHistoryDto {
    private final NoteDto noteDto;
    private final RevisionType type;

    public NoteHistoryDto() {
        this.noteDto = new NoteDto();
        this.type = null;
    }

    public NoteHistoryDto(Object[] object) {
        this.noteDto = new NoteDto((Note)object[0]);
        this.noteDto.setLastModifiedAt(((DefaultRevisionEntity)object[1]).getRevisionDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        this.type = (RevisionType) object[2];
    }



}
