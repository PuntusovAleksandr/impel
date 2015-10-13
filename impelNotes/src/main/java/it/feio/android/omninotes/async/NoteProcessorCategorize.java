package it.feio.android.omninotes.async;

import it.feio.android.omninotes.ImpelNotes;
import it.feio.android.omninotes.db.DbHelper;
import it.feio.android.omninotes.models.Category;
import it.feio.android.omninotes.models.Note;

import java.util.List;


public class NoteProcessorCategorize extends NoteProcessor {

    Category category;


    public NoteProcessorCategorize(List<Note> notes, Category category) {
        super(notes);
        this.category = category;
    }


    @Override
    protected void processNote(Note note) {
        note.setCategory(category);
        DbHelper.getInstance(ImpelNotes.getAppContext()).updateNote(note, false);
    }
}
