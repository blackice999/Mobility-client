package com.project.mobility.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.animation.ArgbEvaluatorCompat;
import com.google.android.material.tabs.TabLayout;
import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.storage.Preferences;
import com.project.mobility.view.activities.login.LoginActivity;
import com.project.mobility.view.activities.navigation.main.MainNavigationActivity;
import com.project.mobility.view.fragments.onboarding.CategoryPageFragment;
import com.project.mobility.view.fragments.onboarding.FragmentFinishedListener;
import com.project.mobility.view.fragments.onboarding.PageFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnboardingActivity extends AppCompatActivity {

    @BindView(R.id.container) ViewPager viewPager;
    @BindView(R.id.tab_navigation) TabLayout tabNavigation;
    @BindView(R.id.onboarding_complete_button) AppCompatButton completeOnboardingButton;
    @BindView(R.id.onboarding_login_button) AppCompatButton loginButton;
    @BindView(R.id.onboarding_forward_button) AppCompatButton nextOnboardingPageButton;
    @BindView(R.id.onboarding_back_button) AppCompatButton backOnboardingPageButton;

    @Inject Preferences preferences;

    private int currentPage;
    private OnboardingSectionAdapter adapter;
    private int[] colorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);
        Injection.inject(this);

        if (preferences.getBoolean(Preferences.KEY_ONBOARDING_COMPLETE)) {
            Intent introIntent = new Intent(this, MainNavigationActivity.class);
            startActivity(introIntent);
            finish();
        }

        colorList = new int[]{
                getResources().getColor(R.color.com_facebook_blue),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.orange)
        };

        setupViewPager();
        tabNavigation.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int nextPosition = onLastPage(position) ? position : position + 1;
                int colorUpdate = ArgbEvaluatorCompat.getInstance().evaluate(positionOffset, colorList[position], colorList[nextPosition]);
                viewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {
                setPageBackground(position);
                currentPage = position;
                handleNavigationButtons(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void handleNavigationButtons(int position) {
        nextOnboardingPageButton.setVisibility(onLastPage(position) ? View.GONE : View.VISIBLE);
        backOnboardingPageButton.setVisibility(!(onFirstPage(position) || onLastPage(position)) ? View.VISIBLE : View.GONE);
        completeOnboardingButton.setVisibility(onLastPage(position) ? View.VISIBLE : View.GONE);
        loginButton.setVisibility(onLastPage(position) ? View.VISIBLE : View.GONE);
    }

    private void setPageBackground(int position) {
        viewPager.setBackgroundColor(colorList[position]);
    }

    private void setupViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(PageFragment.newInstance(getString(R.string.onboarding_description_text), R.drawable.splash_image));
        fragments.add(CategoryPageFragment.newInstance());
        fragments.add(PageFragment.newInstance(getString(R.string.onboarding_have_fun_text), R.drawable.splash_image));
        adapter = new OnboardingSectionAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    @OnClick(R.id.onboarding_forward_button)
    public void navigateForward() {
        viewPager.setCurrentItem(currentPage + 1, true);
    }

    @OnClick(R.id.onboarding_back_button)
    public void navigateBackward() {
        goToPreviousPage();
    }


    @OnClick(R.id.onboarding_complete_button)
    public void completeOnboarding() {
        finishFragments();
        startActivity(new Intent(this, MainNavigationActivity.class));
        preferences.setBoolean(Preferences.KEY_ONBOARDING_COMPLETE, true);
        finish();
    }

    @OnClick(R.id.onboarding_login_button)
    public void login() {
        startActivity(new Intent(this, LoginActivity.class));
        preferences.setBoolean(Preferences.KEY_ONBOARDING_COMPLETE, true);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (onFirstPage(currentPage)) {
            super.onBackPressed();
        } else {
            goToPreviousPage();
        }
    }

    private void goToPreviousPage() {
        viewPager.setCurrentItem(currentPage - 1, true);
    }

    private void finishFragments() {
        List<Fragment> fragmentList = adapter.getFragmentList();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof FragmentFinishedListener) {
                ((FragmentFinishedListener) fragment).doFinish();
            }
        }
    }

    private boolean onFirstPage(int position) {
        return position == 0;
    }

    private boolean onLastPage(int position) {
        return position == viewPager.getAdapter().getCount() - 1;
    }
}
