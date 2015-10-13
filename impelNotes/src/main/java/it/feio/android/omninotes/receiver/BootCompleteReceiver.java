package it.feio.android.omninotes.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import it.feio.android.omninotes.async.AlarmRestoreOnRebootService;
import roboguice.util.Ln;


public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Ln.i("System rebooted: refreshing reminders");

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent service = new Intent(ctx, AlarmRestoreOnRebootService.class);
            ctx.startService(service);
        }

    }


}
