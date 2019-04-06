package com.project.mobility.view.fragments.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.mobility.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import timber.log.Timber;

public class PageFragment extends Fragment implements FragmentFinishedListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static PageFragment newInstance(String param1, String param2) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        TextView textView = view.findViewById(R.id.text_page_number);
        textView.setText(getArguments().getString(ARG_PARAM1));
        return view;
    }

    @Override
    public void doFinish() {
        Timber.d("Fragment is sending finish");
    }
}