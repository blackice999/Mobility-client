package com.project.mobility.view.fragments.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.util.image.ImageLoader;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PageFragment extends Fragment implements FragmentFinishedListener {
    @Inject ImageLoader imageLoader;

    @BindView(R.id.description) TextView description;
    @BindView(R.id.image) AppCompatImageView image;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static PageFragment newInstance(String param1, @DrawableRes int image) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injection.inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Injection.closeScope(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, view);
        description.setText(getArguments().getString(ARG_PARAM1));
        imageLoader.load(getContext(), getArguments().getInt(ARG_PARAM2), image);
        return view;
    }

    @Override
    public void doFinish() {
        Timber.d("Fragment is sending finish");
    }
}