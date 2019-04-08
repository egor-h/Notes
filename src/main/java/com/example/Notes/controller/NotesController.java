package com.example.Notes.controller;

import com.example.Notes.exception.NoteException;
import com.example.Notes.model.Note;
import com.example.Notes.service.NotesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class NotesController {
    private static final Logger logger = LoggerFactory.getLogger(NotesController.class);

    private NotesService notes;

    @Autowired
    public NotesController(NotesService notes) {
        this.notes = notes;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String allNotes(Model model) {
        model.addAttribute("notes", notes.all());
        return "view/notes";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String addNote(@Valid Note note, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "view/formError";
        }
        notes.add(note);
        return "redirect:/";
    }

    @RequestMapping(value = "/remove/{id}")
    public String removeNote(@PathVariable("id") long id) {
        notes.remove(id);
        return "redirect:/";
    }

    @RequestMapping(value = "/find")
    public String findNotes(@RequestParam("query") String query, Model model) {
        model.addAttribute("searchQuery", query);
        model.addAttribute("notes", notes.find(query));
        return "view/notes";
    }

    @ExceptionHandler(NoteException.class)
    public String handleException(Exception e, Model model) {
        logger.error("Error: {} ", e);
        model.addAttribute("msg", e.getMessage());
        return "view/exc";
    }

}
