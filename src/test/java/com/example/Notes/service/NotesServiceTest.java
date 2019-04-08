package com.example.Notes.service;

import com.example.Notes.exception.NoteException;
import com.example.Notes.model.Note;
import com.example.Notes.repository.NotesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotesServiceTest {

    List<Note> getTestData() {
        List<Note> testData = Arrays.asList(
                new Note(0, "Important", "Feed the cat"),
                new Note(1, "Asd asd", "asd"));

        return testData;
    }

    @Mock
    private NotesRepository notesRepository;

    @InjectMocks
    private NotesService notesService;

    @BeforeEach
    void mocksInit() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void all_twoEntriesList_allEntriesReturned() {
        List<Note> testData = getTestData();
        int expectedSize = testData.size();
        when(notesRepository.findAll()).thenReturn(testData);

        List<Note> ret = notesService.all();

        verify(notesRepository).findAll();
        verify(notesRepository, times(1)).findAll();
        assertNotNull(ret);
        assertEquals(ret.size(), expectedSize);
    }

    @Test
    void remove_nonExistentId_exceptionThrown() {
        long testId = 100;
        doThrow(new DataAccessException("id " + testId + " doesn't exist" ) { })
                .when(notesRepository).deleteById(testId);

        assertThrows(NoteException.class, () -> notesService.remove(testId));

    }

    @Test
    void remove_correctId_itemRemoved() {
        long testId = 1;

        notesService.remove(testId);

        verify(notesRepository, times(1)).deleteById(testId);
    }

    @Test
    void add_correctNote_noteSaved() {
        Note testNote = new Note(0, "Title", "Text");
        when(notesRepository.save(testNote)).thenReturn(testNote);

        Note savedNote = notesService.add(testNote);

        verify(notesRepository, times(1)).save(testNote);
        assertEquals(testNote, savedNote);
    }

    @Test
    void find_nonEmptyString_noteFound() {
        String testQuery = "cat";

        notesService.find(testQuery);

        verify(notesRepository, times(1)).findByText(testQuery);
    }
}