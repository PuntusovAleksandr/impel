package it.feio.android.omninotes.async;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;
import it.feio.android.omninotes.MainActivity;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.db.DbHelper;
import it.feio.android.omninotes.models.Note;
import it.feio.android.omninotes.utils.Constants;
import it.feio.android.omninotes.utils.TextHelper;

import java.util.ArrayList;
import java.util.List;


public class ONDashClockExtension extends DashClockExtension {


    private DashClockUpdateReceiver mDashClockReceiver;


    @Override
    protected void onInitialize(boolean isReconnect) {
        super.onInitialize(isReconnect);

        LocalBroadcastManager broadcastMgr = LocalBroadcastManager.getInstance(this);
        if (mDashClockReceiver != null) {
            try {
                broadcastMgr.unregisterReceiver(mDashClockReceiver);
            } catch (Exception ignore) {
            }
        }
        mDashClockReceiver = new DashClockUpdateReceiver();
        broadcastMgr.registerReceiver(mDashClockReceiver, new IntentFilter(Constants.INTENT_UPDATE_DASHCLOCK));
    }


    @SuppressLint("DefaultLocale")
    @Override
    protected void onUpdateData(int reason) {

        DbHelper db = DbHelper.getInstance(this);
        int notes = db.getNotesActive().size();
        int reminders = db.getNotesWithReminder(true).size();
        List<Note> todayReminders = new ArrayList<Note>();
        if (reminders > 0) {
            todayReminders = db.getTodayReminders();
        }

        StringBuilder expandedTitle = new StringBuilder();
        expandedTitle.append(notes).append(" ").append(getString(R.string.notes).toLowerCase());
        if (reminders > 0) {
            expandedTitle.append(" ").append(reminders).append(" ").append(getString(R.string.reminders));
        }

        StringBuilder expandedBody = new StringBuilder();
        if (reminders > 0) {
            expandedBody.append(todayReminders.size()).append(" ").append(getString(R.string.today)).append(":");
            for (Note todayReminder : todayReminders) {
                expandedBody.append(System.getProperty("line.separator")).append(("☆ ")).append(TextHelper
                        .parseTitleAndContent
                                (this,
                                        todayReminder)[0]);
            }
        }

        // Publish the extension data update.
        publishUpdate(new ExtensionData()
                .visible(true)
                .icon(R.drawable.ic_stat_notification_icon)
                .status(String.valueOf(notes))
                .expandedTitle(expandedTitle.toString())
                .expandedBody(expandedBody.toString())
                .clickIntent(new Intent(this, MainActivity.class)));
    }


    public class DashClockUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            onUpdateData(UPDATE_REASON_MANUAL);
        }

    }
}
