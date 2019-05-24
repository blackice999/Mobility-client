package com.project.mobility.view.activities.navigation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.storage.Preferences;
import com.project.mobility.view.activities.navigation.NavigationFragment;
import com.project.mobility.viewmodel.main.navigation.MainNavigationViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainNavigationActivity extends AppCompatActivity {
    public static final String FRAGMENT_TAG = "tag_navigation";

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Inject MainNavigationFragmentFactory mainNavigationFragmentFactory;
    @Inject Preferences preferences;

    private MainNavigationViewModel mainNavigationViewModel;
    private AppCompatTextView tvUnreadChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        ButterKnife.bind(this);
        Injection.inject(this);
        setSupportActionBar(toolbar);

        mainNavigationViewModel = ViewModelProviders.of(this).get(MainNavigationViewModel.class);
        setupViewModel();

        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View chatBadge = LayoutInflater.from(this).inflate(R.layout.cart_content_count_layout, bottomNavigationMenuView, false);
        BottomNavigationItemView itemView = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(1);

        tvUnreadChats = chatBadge.findViewById(R.id.tvUnreadChats);
        itemView.addView(chatBadge);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationItemListener());
    }

    private void setupViewModel() {
        mainNavigationViewModel.getCartContentCountData().observe(this, count -> {
            if (count == 0) {
                tvUnreadChats.setVisibility(View.GONE);
            } else {
                tvUnreadChats.setVisibility(View.VISIBLE);
                tvUnreadChats.setText(String.valueOf(count));
            }
        });
    }

    @Override
    protected void onDestroy() {
        Injection.closeScope(this);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getStringExtra(FRAGMENT_TAG) != null) {
            goToFragment(intent.getStringExtra(FRAGMENT_TAG));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String tag;
        String lastShownFragmentTag = preferences.getString(Preferences.KEY_LAST_SHOWN_FRAGMENT);
        tag = lastShownFragmentTag != null ? lastShownFragmentTag : mainNavigationFragmentFactory.getList().get(0).getTag();
        goToFragment(tag);
        updateNavigationIcon(tag);
    }

    private void updateNavigationIcon(String lastShownFragmentTag) {
        List<NavigationFragment> list = mainNavigationFragmentFactory.getList();
        for (NavigationFragment navigationFragment : list) {
            if (navigationFragment.getTag().equals(lastShownFragmentTag)) {
                bottomNavigationView.setSelectedItemId(navigationFragment.getId());
            }
        }
    }

    private void goToFragment(String fragmentTag) {
        Timber.d("placeProperFragment %s", fragmentTag);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        if (fragment == null) {
            fragment = fragmentManager.getFragmentFactory().instantiate(getClassLoader(), fragmentTag, new Bundle());
            transaction.add(R.id.main_container, fragment, fragmentTag);
        } else {
            transaction.show(fragment);
        }

        transaction.setPrimaryNavigationFragment(fragment);
        transaction.setReorderingAllowed(true);
        transaction.commit();
        preferences.setString(Preferences.KEY_LAST_SHOWN_FRAGMENT, fragmentTag);
    }

    private class BottomNavigationItemListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent i = new Intent(MainNavigationActivity.this, MainNavigationActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            i.putExtra(FRAGMENT_TAG, mainNavigationFragmentFactory.getTagById(menuItem.getItemId()));
            startActivity(i);
            return true;
        }
    }
}
