package com.example.Notes.service;

import com.example.Notes.exception.NoteDoesntExistException;
import com.example.Notes.exception.NoteException;
import com.example.Notes.model.Note;
import com.example.Notes.repository.NotesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {
    private static final Logger logger = LoggerFactory.getLogger(NotesService.class);

    private NotesRepository notes;

    @Autowired
    public NotesService(NotesRepository notes) {
        this.notes = notes;
    }

    public List<Note> all() {
        return notes.findAll();
    }

    public Note add(Note note) {
        Note n = notes.save(note);
        logger.trace("Save note " + note.getId());
        return n;
    }

    public void remove(long id) throws NoteException {
        logger.trace("Remove note id " + id);
        try {
            notes.deleteById(id);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new NoteDoesntExistException(e.getMessage());
        }
    }

    public Iterable<Note> find(String query) {
        return notes.findByText(query);
    }
}
