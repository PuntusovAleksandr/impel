package it.feio.android.omninotes.async;

import android.content.Context;
import android.os.AsyncTask;

import it.feio.android.omninotes.BaseActivity;
import it.feio.android.omninotes.db.DbHelper;
import it.feio.android.omninotes.models.Attachment;
import it.feio.android.omninotes.models.Note;
import it.feio.android.omninotes.utils.StorageManager;


public class DeleteNoteTask extends AsyncTask<Note, Void, Integer> {

    private final Context mContext;


    public DeleteNoteTask(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    protected Integer doInBackground(Note... params) {
        Integer result = null;
        Note note = params[0];

        // Deleting note using DbHelper
        DbHelper db = DbHelper.getInstance(mContext);
        boolean deleted = db.deleteNote(note);

        if (deleted) {
            // Attachment deletion from storage
            boolean attachmentsDeleted = false;
            for (Attachment mAttachment : note.getAttachmentsList()) {
                StorageManager.deleteExternalStoragePrivateFile(mContext, mAttachment.getUri().getLastPathSegment());
            }
            result = deleted && attachmentsDeleted ? note.get_id() : null;
        }
        return result;
    }


    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        BaseActivity.notifyAppWidgets(mContext);
    }
}
