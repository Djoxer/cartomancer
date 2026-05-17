package dev.djoxer.cartomancer.fragments.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import dev.djoxer.cartomancer.R;

public class SortingDialogFragment extends BottomSheetDialogFragment {

    private BottomSheetListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_sorting, container, false);
        Button buttonStart = v.findViewById(R.id.button_sorting_start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStartButtonClicked();
                dismiss();
            }
        });
        return v;
    }

    public interface BottomSheetListener {
        void onStartButtonClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw   new ClassCastException(context.toString()
                    + "must implement BottomSheetListener");
        }
    }
}
