package dev.djoxer.cartomancer.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import dev.djoxer.cartomancer.R;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        // Versionsnummer dynamisch aus dem Manifest lesen
        TextView textVersion = v.findViewById(R.id.text_version);
        try {
            PackageInfo info = getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0);
            textVersion.setText("Version " + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            textVersion.setText("Version 1.0");
        }

        return v;
    }
}
