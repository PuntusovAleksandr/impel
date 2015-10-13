package it.feio.android.omninotes.async;

import it.feio.android.omninotes.ImpelNotes;
import it.feio.android.omninotes.db.DbHelper;
import it.feio.android.omninotes.models.Note;

import java.util.List;


public class NoteProcessorArchive extends NoteProcessor {

    boolean archive;


    public NoteProcessorArchive(List<Note> notes, boolean archive) {
        super(notes);
        this.archive = archive;
    }


    @Override
    protected void processNote(Note note) {
        DbHelper.getInstance(ImpelNotes.getAppContext()).archiveNote(note, archive);
    }
}
