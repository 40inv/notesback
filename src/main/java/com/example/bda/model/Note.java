package com.example.bda.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "Notes")
@Table(name = "notes")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Audited
public class Note implements Serializable {
    public static Note EMPTY = new Note();

    @Id
    @Column(name="note_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version = 1;

    @Column(name = "title", nullable = false, columnDefinition="varchar(75)")
    @NotNull(message = "Title cannot be null")
    @Size(min = 1, max = 75, message = "Title length must be >= 1 and <= 75")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition="varchar(1000)")
    @NotNull(message = "Content cannot be null")
    @Size(min = 1, max = 1000, message = "Content length must be >= 1 and <= 1000")
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at", nullable = false)
    @UpdateTimestamp
    @NotAudited
    private LocalDateTime lastModifiedAt;

    protected Note() {}

    public Note(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() { return this.version; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public  LocalDateTime getLastModifiedAt() { return  lastModifiedAt; }
}