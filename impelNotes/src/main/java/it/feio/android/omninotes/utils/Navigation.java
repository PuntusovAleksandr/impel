package it.feio.android.omninotes.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

import it.feio.android.omninotes.ImpelNotes;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.models.Category;


public class Navigation {

    public static final int NOTES = 0;
    public static final int ARCHIVE = 1;
    public static final int REMINDERS = 2;
    public static final int TRASH = 3;
    public static final int UNCATEGORIZED = 4;
    public static final int CATEGORY = 5;



    /**
     * Returns actual navigation status
     */
    public static int getNavigation() {
        Context mContext = ImpelNotes.getAppContext();
        String[] navigationListCodes = mContext.getResources().getStringArray(R.array.navigation_list_codes);
        @SuppressWarnings("static-access")
        String navigation = mContext.getSharedPreferences(Constants.PREFS_NAME, 
                mContext.MODE_MULTI_PROCESS).getString(Constants.PREF_NAVIGATION, navigationListCodes[0]);

        if (navigationListCodes[NOTES].equals(navigation)) {
            return NOTES;
        } else if (navigationListCodes[ARCHIVE].equals(navigation)) {
            return ARCHIVE;
        } else if (navigationListCodes[REMINDERS].equals(navigation)) {
            return REMINDERS;
        } else if (navigationListCodes[TRASH].equals(navigation)) {
            return TRASH;
        } else if (navigationListCodes[CATEGORY].equals(navigation)) {
            return CATEGORY;
        } else {
            return UNCATEGORIZED;
        }
    }


    /**
     * Retrieves category currently shown
     *
     * @return id of category or 0 if current navigation is not a category
     */
    public static String getCategory() {
        if (getNavigation() == CATEGORY) {
            Context mContext = ImpelNotes.getAppContext();
            return mContext.getSharedPreferences(Constants.PREFS_NAME, mContext.MODE_MULTI_PROCESS).getString
                    (Constants.PREF_NAVIGATION, "");
        } else {
            return null;
        }
    }


    /**
     * Checks if passed parameters is the actual navigation status
     */
    public static boolean checkNavigation(int navigationToCheck) {
        return checkNavigation(new Integer[]{navigationToCheck});
    }


    public static boolean checkNavigation(Integer[] navigationsToCheck) {
        boolean res = false;
        int navigation = getNavigation();
        for (int navigationToCheck : new ArrayList<Integer>(Arrays.asList(navigationsToCheck))) {
            if (navigation == navigationToCheck) {
                res = true;
                break;
            }
        }
        return res;
    }


    /**
     * Checks if passed parameters is the category user is actually navigating in
     */
    public static boolean checkNavigationCategory(Category categoryToCheck) {
        Context mContext = ImpelNotes.getAppContext();
        String[] navigationListCodes = mContext.getResources().getStringArray(R.array.navigation_list_codes);
        String navigation = mContext.getSharedPreferences(Constants.PREFS_NAME, mContext.MODE_MULTI_PROCESS).getString(Constants.PREF_NAVIGATION, navigationListCodes[0]);
        return (categoryToCheck != null && navigation.equals(String.valueOf(categoryToCheck.getId())));
    }
}
