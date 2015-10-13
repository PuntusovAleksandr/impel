package it.feio.android.omninotes.async;

import it.feio.android.omninotes.ImpelNotes;
import it.feio.android.omninotes.db.DbHelper;
import it.feio.android.omninotes.models.Note;
import it.feio.android.omninotes.utils.ReminderHelper;
import it.feio.android.omninotes.utils.ShortcutHelper;

import java.util.List;


public class NoteProcessorTrash extends NoteProcessor {

    boolean trash;


    public NoteProcessorTrash(List<Note> notes, boolean trash) {
        super(notes);
        this.trash = trash;
    }


    @Override
    protected void processNote(Note note) {
        if (trash) {
            ShortcutHelper.removeshortCut(ImpelNotes.getAppContext(), note);
            ReminderHelper.removeReminder(ImpelNotes.getAppContext(), note);
        } else {
            ReminderHelper.addReminder(ImpelNotes.getAppContext(), note);
        }
        DbHelper.getInstance(ImpelNotes.getAppContext()).trashNote(note, trash);
    }
}
