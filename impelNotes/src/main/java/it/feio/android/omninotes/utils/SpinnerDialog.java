package it.feio.android.omninotes.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


public class SpinnerDialog extends DialogFragment {

    public SpinnerDialog() {
        // use empty constructors. If something is needed use onCreate's
    }


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);

        return dialog;
    }
}