package com.example.bda;

import com.example.bda.controller.NoteController;
import com.example.bda.dao.NoteRepository;
import com.example.bda.model.Note;
import com.example.bda.service.NoteHistoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BdaApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class MainTest {

    private MockMvc mockMvc;

    @Autowired
    private NoteRepository repository;
    @Autowired
    private NoteController noteController;
    @Autowired
    private NoteHistoryService noteHistoryService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.noteController).build();
    }

    @Test
    public void whenPostNewNote_thenThatNoteIsInsertedIntoDatabase() throws Exception {
        String content = "{\n" +
                "     \"title\":\"Hello\",\n" +
                "     \"content\":\"world\"\n" +
                "}";

        //POST
        MockHttpServletRequestBuilder postNote = post("/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MvcResult result = mockMvc.perform(postNote)
                .andExpect(status().isOk())
                .andReturn();

        Long noteId = new Long(Integer.parseInt(result.getResponse().getContentAsString().replaceAll("[^0-9]+", "")));

        Optional<Note> actualNote = repository.findById(noteId);
        assertThat(actualNote.isPresent()).isTrue();
        assertThat(actualNote.get().getTitle()).isEqualTo("Hello");
        assertThat(actualNote.get().getContent()).isEqualTo("world");

        content = "{\n" +
                "     \"title\":\"Zdrastvui\",\n" +
                "     \"content\":\"mir\"\n" +
                "}";
        //PUT
        MockHttpServletRequestBuilder putCompany = put(String.format("/notes/%s", noteId)).contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(putCompany)
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Note with id %s updated.", noteId)));


        actualNote = repository.findById(noteId);
        assertThat(actualNote.isPresent()).isTrue();
        assertThat(actualNote.get().getTitle()).isEqualTo("Zdrastvui");

        //GET
        MockHttpServletRequestBuilder getNote = get(String.format("/notes/%s", noteId));
        mockMvc.perform(getNote)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo("Zdrastvui")))
                .andExpect(jsonPath("$.version", equalTo(2)));

        //DELETE
        MockHttpServletRequestBuilder deleteNote = delete(String.format("/notes/%s", noteId));
        mockMvc.perform(deleteNote)
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Note with id %s deleted.", noteId)));

        actualNote = repository.findById(noteId);
        assertThat(actualNote.isPresent()).isFalse();
    }

    @Test
    public void whenNotExistingNote_thenNotExistMessageReturned() throws Exception {
        String content = "{\n" +
                "     \"title\":\"Hello\",\n" +
                "     \"content\":\"world\"\n" +
                "}";
        //PUT
        MockHttpServletRequestBuilder putCompany = put("/notes/12").contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(putCompany)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Note with id 12 does not exist."));


        Optional<Note> actualNote = repository.findById(12L);
        assertThat(actualNote.isPresent()).isFalse();

        //GET
        MockHttpServletRequestBuilder getNote = get("/notes/12");
        mockMvc.perform(getNote)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", equalTo(null)))
                .andExpect(jsonPath("$.version", equalTo(null)));

        //DELETE
        MockHttpServletRequestBuilder deleteNote = delete("/notes/12");
        mockMvc.perform(deleteNote)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Note with id 12 does not exist."));

        actualNote = repository.findById(12L);
        assertThat(actualNote.isPresent()).isFalse();
    }


    @Test
    public void whenPostNewNote_thenThatNoteIsInsertedIntoHistoryDatabase() throws Exception {
        String content = "{\n" +
                "     \"title\":\"Hello\",\n" +
                "     \"content\":\"world\"\n" +
                "}";

        //POST
        MockHttpServletRequestBuilder postNote = post("/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MvcResult result = mockMvc.perform(postNote)
                .andExpect(status().isOk())
                .andReturn();

        Long noteId = new Long(Integer.parseInt(result.getResponse()
                .getContentAsString()
                .replaceAll("[^0-9]+", "")));

        content = "{\n" +
                "     \"title\":\"Zdrastvui\",\n" +
                "     \"content\":\"mir\"\n" +
                "}";
        //PUT
        MockHttpServletRequestBuilder putCompany = put(String.format("/notes/%s", noteId)).contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(putCompany)
                .andExpect(status().isOk());


        //DELETE
        MockHttpServletRequestBuilder deleteNote = delete(String.format("/notes/%s", noteId));
        mockMvc.perform(deleteNote)
                .andExpect(status().isOk());

        List resultList = noteHistoryService.getFullHistoryById(noteId);
        assertThat(resultList.size()).isEqualTo(3);
    }

}
