package com.example.bda.controller;

import com.example.bda.dao.NoteRepository;
import com.example.bda.dto.NoteDto;
import com.example.bda.model.Note;
import com.example.bda.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping(path = "/notes")
public class NoteController {

    private final Logger logger = LoggerFactory.getLogger(NoteController.class);

    private NoteRepository repository;
    private NoteService noteService;

    @Autowired
    public NoteController(NoteRepository repository, NoteService noteService) {
        this.repository = repository;
        this.noteService = noteService;
    }

    private void logHeaders(@RequestHeader HttpHeaders headers) {
        logger.info("Controller request headers {}",
                headers.entrySet()
                        .stream()
                        .map(entry -> String.format("%s->[%s]", entry.getKey(), String.join(",", entry.getValue())))
                        .collect(joining(","))
        );
    }

    @PostMapping(path = "")
    public ResponseEntity<String> createNote(@RequestHeader HttpHeaders headers,@Valid @RequestBody Note note, Errors errors) {
        logHeaders(headers);
        if (errors.hasErrors()) {
            return new ResponseEntity(errors.getAllErrors().stream()
                    .map(element -> element.getDefaultMessage())
                    .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
        Long id = repository.saveAndFlush(note).getId();
        return ResponseEntity.ok(String.format("Note with id %s has been successfully saved",id));
    }

    @DeleteMapping(path = "/{noteId}")
    public ResponseEntity<String> deleteNote(@RequestHeader HttpHeaders headers, @PathVariable Long noteId) {
        logHeaders(headers);
        boolean deleted = noteService.deleteNote(noteId);
        if (!deleted) {
            return new ResponseEntity<>(String.format("Note with id %s does not exist.", noteId), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(String.format("Note with id %s deleted.", noteId));
    }

    @PutMapping(path = "/{noteId}")
    public ResponseEntity<String> updateNote(@RequestHeader HttpHeaders headers,
                                                 @PathVariable Long noteId,
                                                 @RequestBody Note updatedNote) {
        logHeaders(headers);
        Note result = noteService.updateNote(noteId, updatedNote);
        if (result == null) {
            return new ResponseEntity<>(String.format("Note with id %s does not exist.", noteId), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(String.format("Note with id %s updated.", noteId));
    }

    @GetMapping(path = "")
    public ResponseEntity<Collection<NoteDto>> getAllNotes(@RequestHeader HttpHeaders headers) {
        logHeaders(headers);
        return ResponseEntity.ok(repository.findAll()
                .stream().map(element -> new NoteDto(element))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteDto> getNote(@RequestHeader HttpHeaders headers, @PathVariable Long noteId) {
        logHeaders(headers);
        Optional<Note> result = repository.findById(noteId);
        if(result.isPresent())
            return ResponseEntity.ok(new NoteDto(result.get()));
        return new ResponseEntity<>(new NoteDto(), HttpStatus.NOT_FOUND);
    }

}
