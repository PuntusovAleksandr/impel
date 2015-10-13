package it.feio.android.omninotes.utils;

import android.content.Context;
import android.content.Intent;
import it.feio.android.omninotes.MainActivity;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.models.Note;


public class ShortcutHelper {


    /**
     * Adding shortcut on Home screen
     */
    public static void addShortcut(Context context, Note note) {
        Intent shortcutIntent = new Intent(context, MainActivity.class);
        shortcutIntent.putExtra(Constants.INTENT_KEY, note.get_id());
        shortcutIntent.setAction(Constants.ACTION_SHORTCUT);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        String shortcutTitle = note.getTitle().length() > 0 ? note.getTitle() : note.getCreationShort(context);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutTitle);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_shortcut));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        context.sendBroadcast(addIntent);
    }

    /**
     * Removes note shortcut from home launcher
     */
    public static void removeshortCut(Context context, Note note) {
        Intent shortcutIntent = new Intent(context, MainActivity.class);
        shortcutIntent.putExtra(Constants.INTENT_KEY, note.get_id());
        shortcutIntent.setAction(Constants.ACTION_SHORTCUT);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        String shortcutTitle = note.getTitle().length() > 0 ? note.getTitle() : note.getCreationShort(context);

        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutTitle);

        addIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
    }
}
