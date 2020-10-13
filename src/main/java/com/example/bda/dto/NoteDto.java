package com.example.bda.dto;

import com.example.bda.model.Note;
import com.example.bda.utils.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class NoteDto {

    private String title;
    private String content;
    @JsonSerialize(using = JsonDateSerializer.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = JsonDateSerializer.class)
    private LocalDateTime lastModifiedAt;
    private Integer version;

    public NoteDto() { }

    public NoteDto(Note note) {
        if(note != null) {
            this.title = note.getTitle();
            this.content = note.getContent();
            this.createdAt = note.getCreatedAt();
            this.lastModifiedAt = note.getLastModifiedAt();
            this.version = note.getVersion();
        }
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}
