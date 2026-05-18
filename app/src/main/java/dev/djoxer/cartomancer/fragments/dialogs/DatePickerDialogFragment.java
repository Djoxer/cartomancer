package dev.djoxer.cartomancer.fragments.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import static dev.djoxer.cartomancer.MainActivity.calendar;

import dev.djoxer.cartomancer.R;

public class DatePickerDialogFragment extends DialogFragment {
    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context themedContext = new ContextThemeWrapper(
                getActivity(),
                R.style.CartomancerDatePicker
        );

        DatePickerDialog dialog = new DatePickerDialog(
                themedContext,
                (DatePickerDialog.OnDateSetListener) getActivity(),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Buttons nach Show direkt setzen
        dialog.setOnShowListener(d -> {
            int nightMode = AppCompatDelegate.getDefaultNightMode();
            int colorRes = (nightMode == AppCompatDelegate.MODE_NIGHT_YES)
                    ? R.color.text_primary_dark
                    : R.color.text_primary_light;
            int color = getResources().getColor(colorRes, null);
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(color);
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(color);
        });

        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }
}