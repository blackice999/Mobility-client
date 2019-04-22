package com.project.mobility.view.activities.navigation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.storage.Preferences;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import toothpick.Toothpick;

public class MainNavigationActivity extends AppCompatActivity {
    public static final String FRAGMENT_TAG = "tag_navigation";

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;

    @Inject MainNavigationFragmentFactory mainNavigationFragmentFactory;
    @Inject Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        ButterKnife.bind(this);
        Injection.inject(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationItemListener());
    }

    @Override
    protected void onDestroy() {
        Toothpick.closeScope(this);
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
        if (preferences.getString(Preferences.KEY_LAST_SHOWN_FRAGMENT) != null) {
            goToFragment(preferences.getString(Preferences.KEY_LAST_SHOWN_FRAGMENT));
        } else {
            goToFragment(mainNavigationFragmentFactory.getList().get(0).getTag());
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
