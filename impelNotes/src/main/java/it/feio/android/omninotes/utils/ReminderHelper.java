package it.feio.android.omninotes.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import it.feio.android.omninotes.models.Note;
import it.feio.android.omninotes.receiver.AlarmReceiver;

import java.util.Calendar;


public class ReminderHelper {

    public static void addReminder(Context context, Note note) {
        if (hasFutureReminder(note)) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(Constants.INTENT_NOTE, (android.os.Parcelable) note);
            PendingIntent sender = PendingIntent.getBroadcast(context, note.getCreation().intValue(), intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, Long.parseLong(note.getAlarm()), sender);
        }
    }


    private static boolean hasFutureReminder(Note note) {
        boolean hasFutureReminder = false;
        if (!TextUtils.isEmpty(note.getAlarm()) && Long.parseLong(note.getAlarm()) > Calendar.getInstance()
                .getTimeInMillis()) {
            hasFutureReminder = true;
        }
        return hasFutureReminder;
    }


    public static void removeReminder(Context context, Note note) {
        if (!TextUtils.isEmpty(note.getAlarm())) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent p = PendingIntent.getBroadcast(context, note.getCreation().intValue(), intent, 0);
            am.cancel(p);
            p.cancel();
        }
    }
}
