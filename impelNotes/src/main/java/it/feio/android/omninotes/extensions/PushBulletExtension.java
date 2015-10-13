package it.feio.android.omninotes.extensions;

import com.pushbullet.android.extension.MessagingExtension;

import it.feio.android.omninotes.MainActivity;
import roboguice.util.Ln;


public class PushBulletExtension extends MessagingExtension {

    private static final String TAG = "PushBulletExtension";


    @Override
    protected void onMessageReceived(final String conversationIden, final String message) {
        Ln.i("Pushbullet MessagingExtension: onMessageReceived(" + conversationIden + ", " + message + ")");
        MainActivity runningMainActivity = MainActivity.getInstance();
        if (runningMainActivity != null && !runningMainActivity.isFinishing()) {
            runningMainActivity.onPushBulletReply(message);
        }
    }


    @Override
    protected void onConversationDismissed(final String conversationIden) {
        Ln.i("Pushbullet MessagingExtension: onConversationDismissed(" + conversationIden + ")");
    }
}
