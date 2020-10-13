package com.example.bda.controller;


import com.example.bda.dao.NoteRepository;
import com.example.bda.dto.NoteDto;
import com.example.bda.model.Note;
import com.example.bda.service.NoteHistoryService;
import com.example.bda.service.NoteService;
import net.bytebuddy.description.type.TypeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/history")
public class NoteHistoryController {

    private NoteHistoryService noteHistoryService;

    @Autowired
    public NoteHistoryController(NoteHistoryService noteHistoryService) {
        this.noteHistoryService = noteHistoryService;
    }

    @GetMapping(path = "/{noteId}")
    public ResponseEntity<List> getNoteHistory(@RequestHeader HttpHeaders headers,
                                              @PathVariable Long noteId,
                                              @RequestBody Note updatedCompany) {
        List result = noteHistoryService.getFullHistoryById(noteId);
        if(result.size() > 0)
            return ResponseEntity.ok(result);
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

}
