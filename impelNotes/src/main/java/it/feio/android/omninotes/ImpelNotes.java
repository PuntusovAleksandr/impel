package it.feio.android.omninotes;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import org.acra.ACRA;

import java.util.Locale;

import it.feio.android.omninotes.utils.BitmapCache;
import it.feio.android.omninotes.utils.Constants;


public class ImpelNotes extends Application {

    private static Context mContext;

    private final static String PREF_LANG = "settings_language";
    static SharedPreferences prefs;
    private static Tracker mTracker;
    private static GoogleAnalytics mGa;
    private static BitmapCache mBitmapCache;


    @Override
    public void onCreate() {
        mContext = getApplicationContext();
        prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_MULTI_PROCESS);
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        // Instantiate bitmap cache
        mBitmapCache = new BitmapCache(getApplicationContext(), 0, 0, getExternalCacheDir());
        // Checks selected locale or default one
        updateLanguage(this, null);
        // Google Analytics initialization
        initializeGa();
        super.onCreate();
    }


    @Override
    // Used to restore user selected locale when configuration changes
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String language = prefs.getString(PREF_LANG, "");
        super.onConfigurationChanged(newConfig);
        updateLanguage(this, language);
    }


    public static Context getAppContext() {
        return ImpelNotes.mContext;
    }


    /**
     * Updates default language with forced one
     */
    public static void updateLanguage(Context ctx, String lang) {
        Configuration cfg = new Configuration();
        String language = prefs.getString(PREF_LANG, "");

        if (TextUtils.isEmpty(language) && lang == null) {
            cfg.locale = Locale.getDefault();

            String tmp = "";
            tmp = Locale.getDefault().toString().substring(0, 2);

            prefs.edit().putString(PREF_LANG, tmp).commit();

        } else if (lang != null) {
            // Checks country
            if (lang.contains("_")) {
                cfg.locale = new Locale(lang.split("_")[0], lang.split("_")[1]);
            } else {
                cfg.locale = new Locale(lang);
            }
            prefs.edit().putString(PREF_LANG, lang).commit();

        } else if (!TextUtils.isEmpty(language)) {
            // Checks country
            if (language.contains("_")) {
                cfg.locale = new Locale(language.split("_")[0], language.split("_")[1]);
            } else {
                cfg.locale = new Locale(language);
            }
        }

        ctx.getResources().updateConfiguration(cfg, null);
    }


    /*
     * Method to handle basic Google Analytics initialization. This call will not block as all Google Analytics work
     * occurs off the main thread.
     */
    private void initializeGa() {
        mGa = GoogleAnalytics.getInstance(this);
        mTracker = mGa.getTracker("UA-45502770-1");
    }


    /*
     * Returns the Google Analytics tracker.
     */
    public static Tracker getGaTracker() {
        return mTracker;
    }


    /*
     * Returns the Google Analytics instance.
     */
    public static GoogleAnalytics getGaInstance() {
        return mGa;
    }


    /*
     * Returns the Google Analytics instance.
     */
    public static BitmapCache getBitmapCache() {
        return mBitmapCache;
    }


    /**
     * Performs a full app restart
     */
    public static void restartApp(final Context mContext) {

        if (MainActivity.getInstance() != null) {
            MainActivity.getInstance().finish();
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(mContext, mPendingIntentId, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
        }
    }

}
