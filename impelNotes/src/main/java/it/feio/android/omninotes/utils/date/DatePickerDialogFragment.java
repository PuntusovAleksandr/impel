package it.feio.android.omninotes.utils.date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;


/**
 * <p>This class provides a usable {@link android.app.DatePickerDialog} wrapped as a {@link android.support.v4.app
 * * .DialogFragment},
 * using the compatibility package v4. Its main advantage is handling Issue 34833
 * automatically for you.</p>
 * <p/>
 * <p>Current implementation (because I wanted that way =) ):</p>
 * <p/>
 * <ul>
 * <li>Only two buttons, a {@code BUTTON_POSITIVE} and a {@code BUTTON_NEGATIVE}.
 * <li>Buttons labeled from {@code android.R.string.ok} and {@code android.R.string.cancel}.
 * </ul>
 * <p/>
 * <p><strong>Usage sample:</strong></p>
 * <p/>
 * <pre>class YourActivity extends Activity implements OnDateSetListener
 * <p/>
 * // ...
 * <p/>
 * Bundle b = new Bundle();
 * b.putInt(DatePickerDialogFragment.YEAR, 2012);
 * b.putInt(DatePickerDialogFragment.MONTH, 6);
 * b.putInt(DatePickerDialogFragment.DATE, 17);
 * DialogFragment picker = new DatePickerDialogFragment();
 * picker.setArguments(b);
 * picker.show(getActivity().getSupportFragmentManager(), "fragment_date_picker");</pre>
 *
 * @author davidcesarino@gmail.com
 * @version 2012.0828
 * @see <a href="http://code.google.com/p/android/issues/detail?id=34833">Android Issue 34833</a>
 * @see <a href="http://stackoverflow.com/q/11444238/489607"
 * >Jelly Bean DatePickerDialog — is there a way to cancel?</a>
 */
public class DatePickerDialogFragment extends DialogFragment {

    public static final String DEFAULT_DATE = "default_date";

    private OnDateSetListener mListener;
    private Long defaultDate;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getArguments().containsKey(DEFAULT_DATE)) {
            this.defaultDate = getArguments().getLong(DEFAULT_DATE);
        }

        try {
            mListener = (OnDateSetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDateSetListener");
        }
    }


    @Override
    public void onDetach() {
        this.mListener = null;
        super.onDetach();
    }


    @TargetApi(11)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Calendar cal = DateHelper.getCalendar(defaultDate);
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog picker = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_LIGHT,
                getConstructorListener(), y, m, d);
        picker.setTitle("");

        if (hasJellyBeanAndAbove()) {
            picker.setButton(DialogInterface.BUTTON_POSITIVE,
                    getActivity().getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatePicker dp = picker.getDatePicker();
                            mListener.onDateSet(dp,
                                    dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
                        }
                    });
            picker.setButton(DialogInterface.BUTTON_NEGATIVE,
                    getActivity().getString(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
        }

        return picker;
    }


    private static boolean hasJellyBeanAndAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }


    private OnDateSetListener getConstructorListener() {
        return hasJellyBeanAndAbove() ? null : mListener;
    }
}
