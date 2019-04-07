package com.example.Notes.repository;

import com.example.Notes.model.Note;
import org.springframework.data.repository.CrudRepository;

public interface NotesRepository extends CrudRepository<Note, Long> {

}
