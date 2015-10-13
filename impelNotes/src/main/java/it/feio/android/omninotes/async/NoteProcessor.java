package it.feio.android.omninotes.async;

import android.os.AsyncTask;
import it.feio.android.omninotes.models.Note;

import java.util.ArrayList;
import java.util.List;


public abstract class NoteProcessor {

    List<Note> notes;


    protected NoteProcessor(List<Note> notes) {
        this.notes = new ArrayList<Note>(notes);
    }


    public void process() {
        NotesProcessorTask task = new NotesProcessorTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, notes);
    }


    protected abstract void processNote(Note note);


    class NotesProcessorTask extends AsyncTask<List<Note>, Void, Void> {


        @Override
        protected Void doInBackground(List<Note>... params) {
            List<Note> notes = params[0];
            for (Note note : notes) {
                processNote(note);
            }
            return null;
        }
    }
}
