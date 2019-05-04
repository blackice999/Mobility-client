package com.project.mobility.view.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.login.provider.FacebookAuthProvider;
import com.project.mobility.model.login.provider.GoogleAuthProvider;
import com.project.mobility.storage.Preferences;
import com.project.mobility.view.activities.navigation.main.MainNavigationActivity;
import com.project.mobility.viewmodel.login.LoginViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    public static final String FACEBOOK_EMAIL_PERMISSIONS = "email";
    public static final String FACEBOOK_PUBLIC_PROFILE_PERMISSIONS = "public_profile";
    private static final int RC_SIGN_IN_GOOGLE = 1000;

    @BindView(R.id.facebook_sign_in) LoginButton facebookLoginButton;
    @BindView(R.id.logout_button) Button logoutButton;

    @Inject Preferences preferences;
    @Inject GoogleAuthProvider googleAuthProvider;
    @Inject FacebookAuthProvider facebookAuthProvider;

    private CallbackManager mCallbackManager;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Injection.inject(this);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
//        editTextEmail.addTextChangedListener(new GenericTextWatcher(editTextEmail, (TextInputLayout) editTextEmail.getParent().getParent()));
        setupViewModel();
        initFacebookLogin();
    }

    private void initFacebookLogin() {
        mCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setReadPermissions(FACEBOOK_EMAIL_PERMISSIONS, FACEBOOK_PUBLIC_PROFILE_PERMISSIONS);
        facebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Timber.d("facebook:onSuccess:%s", loginResult);
                facebookAuthProvider.setToken(loginResult.getAccessToken().getToken());
                loginViewModel.setAuthProvider(facebookAuthProvider);
                loginViewModel.authenticate();
            }

            @Override
            public void onCancel() {
                Timber.d("facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Timber.d(error, "facebook:onError");
            }
        });
    }

    private void setupViewModel() {
        loginViewModel.getUserAuthenticate().observe(this, authenticated -> {
            if (authenticated) {
                Toast.makeText(this, "Authenticated successfully", Toast.LENGTH_SHORT).show();
                launchSuccessScreen();
                preferences.setBoolean(Preferences.KEY_AUTH_IS_SPLASHSCREEN_LAUNCHED, true);
            } else {
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        loginViewModel.getUserLogout().observe(this, loggedOut -> {
            if (loggedOut) {
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                logoutButton.setVisibility(View.GONE);
                preferences.clearPreferences(Preferences.PREFERENCE_TYPE_AUTH);
            } else {
                Toast.makeText(this, "Logout failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.getBoolean(Preferences.KEY_AUTH_IS_SPLASHSCREEN_LAUNCHED)) {
            launchSuccessScreen();
        }
    }

    @OnClick(R.id.google_sign_in)
    public void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    @OnClick(R.id.logout_button)
    public void logout() {
        if (preferences.getString(Preferences.KEY_AUTH_PROVIDER_NAME) != null) {
            String authProviderName = preferences.getString(Preferences.KEY_AUTH_PROVIDER_NAME);

            switch (authProviderName) {
                case GoogleAuthProvider.AUTH_PROVIDER_NAME:
                    loginViewModel.setAuthProvider(googleAuthProvider);
                    break;

                case FacebookAuthProvider.AUTH_PROVIDER_NAME:
                    loginViewModel.setAuthProvider(facebookAuthProvider);
                    break;
            }

            loginViewModel.logout();
        }
    }

    private void launchSuccessScreen() {
        Intent intent = new Intent(this, MainNavigationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                googleAuthProvider.setToken(task.getResult(ApiException.class).getIdToken());
                loginViewModel.setAuthProvider(googleAuthProvider);
                loginViewModel.authenticate();
            } catch (ApiException e) {
                Timber.w(e, "Google sign in failed");
            }
        } else {
            //Request came from facebook
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
