package com.example.Notes.controller;

import com.example.Notes.exception.NoteDoesntExistException;
import com.example.Notes.model.Note;
import com.example.Notes.service.NotesService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(NotesController.class)
class NotesControllerTest {

    List<Note> getTestData() {
        List<Note> testData = Arrays.asList(
                new Note(0, "Important", "Feed the cat"),
                new Note(1, "Asd asd", "asd"));

        return testData;
    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    private NotesService notesService;

    @Test
    void allNotes_getNotes_allNotesReturned() throws Exception {
        List<Note> testNotes = getTestData();
        Note firstNote = testNotes.get(0);
        Note secondNote = testNotes.get(1);
        doReturn(testNotes).when(notesService).all();

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(firstNote.getTitle())))
                .andExpect(content().string(containsString(firstNote.getText())))
                .andExpect(content().string(containsString(secondNote.getTitle())))
                .andExpect(content().string(containsString(secondNote.getText())));
    }

    @Test
    void addNote_addEmptyNote_fieldErrors() throws  Exception {
        String emptyTitle = "";
        String emptyText = "";

        mvc.perform(post("/").param("title", emptyTitle).param("text", emptyText))
                .andExpect(content().string(containsString("must not be blank")));

    }

    @Test
    void addNote_addCorrectNote_newNoteAdded() throws Exception {
        Note testNote = new Note(0, "do", "something");

        mvc.perform(post("/").param("title", testNote.getTitle()).param("text", testNote.getText()))
                .andExpect(redirectedUrl("/"));
        verify(notesService, times(1)).add(testNote);
    }

    @Test
    void removeNote_removeNonExistentId_expectErrorPage() throws Exception {
        int nonExistentId = 100;
        String excMsg = "Error";
        doThrow(new NoteDoesntExistException(excMsg)).when(notesService).remove(nonExistentId);

        mvc.perform(get("/remove/" + nonExistentId))
                .andExpect(content().string(containsString(excMsg)));
        verify(notesService, times(1)).remove(nonExistentId);
    }

    @Test
    void removeNote_removeCorrectNote_successfulNoteRemoval() throws Exception {
        int noteId = 1;

        mvc.perform(get("/remove/" + noteId))
                .andExpect(redirectedUrl("/"));

        verify(notesService, times(1)).remove(noteId);
    }

    @Test
    void findNotest_findNote_returnFoundNote() throws Exception {
        String query = "something";

        mvc.perform(get("/find").param("query", query))
                .andExpect(model().attribute("searchQuery", query));

        verify(notesService, times(1)).find(query);
    }

}