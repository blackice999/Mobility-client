package com.project.mobility.view.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputEditText;
import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.login.provider.FacebookAuthProvider;
import com.project.mobility.model.login.provider.GoogleAuthProvider;
import com.project.mobility.storage.AuthProviderPreferences;
import com.project.mobility.view.activities.ProductsActivity;
import com.project.mobility.viewmodel.login.LoginViewModel;

import java.util.regex.Pattern;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
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

    @Inject AuthProviderPreferences authProviderPreferences;
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

        // Initialize Facebook Login button
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

    @Override
    protected void onResume() {
        super.onResume();
        if (authProviderPreferences.getSplashScreenLaunched()) {
            launchProductsActivity();
        } else {
            loginViewModel.authenticate().observe(this, couldLogIn -> {
                if (couldLogIn) {
                    launchProductsActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "Could not log in", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    public boolean validPhoneNumber(String number)
    {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }


    @OnClick(R.id.google_sign_in)
    public void signInWithGoogle() {
        TextInputEditText email = (TextInputEditText)findViewById(R.id.input_delivery_address);
        TextInputEditText phone = (TextInputEditText)findViewById(R.id.input_phone_number);
        if(!isValidEmailId(email.getText().toString().trim())){
            Toast.makeText(getApplicationContext(), "Invalid Email Address.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!validPhoneNumber(phone.getText().toString().trim())){
            Toast.makeText(getApplicationContext(), "Invalid Phone Number.", Toast.LENGTH_SHORT).show();
            return;
        }
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
        if (authProviderPreferences.getProviderName() != null) {
            String authProviderName = authProviderPreferences.getProviderName();

            switch (authProviderName) {
                case GoogleAuthProvider.AUTH_PROVIDER_NAME:
                    loginViewModel.setAuthProvider(googleAuthProvider);
                    break;

                case FacebookAuthProvider.AUTH_PROVIDER_NAME:
                    loginViewModel.setAuthProvider(facebookAuthProvider);
                    break;
            }

            loginViewModel.logout().observe(this, loggedOut -> {
                if (loggedOut) {
                    logoutButton.setVisibility(View.GONE);
                    authProviderPreferences.clearPreferences();
                }
            });
        }
    }

    private void launchProductsActivity() {
        Intent intent = new Intent(this, ProductsActivity.class);
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
                // Google Sign In failed, update UI appropriately
                Timber.w(e, "Google sign in failed");
            }
        } else {
            //Request came from facebook
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

//    private void addUser(String firebaseId, String email, String displayName) {
//        String url = "http://192.168.0.103/hiking-trails-server/user/add";
//
//        // Request a string response from the provided URL.
//        JSONObject jsonRequest = new JSONObject();
//
//        try {
//            jsonRequest.put("firebaseId", firebaseId);
//            jsonRequest.put("email", email);
//            jsonRequest.put("displayName", displayName);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Log.d("fishLogin", jsonRequest.toString());
//
//        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        boolean success = response.has("data");
//                        String userId;
//                        try {
//                            if (success) {
//                                JSONObject data = response.getJSONObject("data");
//                                userId = data.getString("userId");
//
//                                launchProductsActivity(mAuth.getCurrentUser(), false);
//                                Toast.makeText(LoginActivity.this, userId, Toast.LENGTH_SHORT).show();
//                            }
//
//                            if ("100".equals(response.getString("errorCode"))) {
//
//                                //Go to main activity if the user is already in the database
//                                launchProductsActivity(mAuth.getCurrentUser(), false);
////                                Toast.makeText(LoginActivity.this, "Error: " + response.getString("errorCode") + " " + response.getString("errorMessage"), Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(LoginActivity.this, "No connection to server", Toast.LENGTH_LONG).show();
//                } else if (error instanceof AuthFailureError) {
//                    //TODO
//                } else if (error instanceof ServerError) {
//                    //TODO
//                } else if (error instanceof NetworkError) {
//                    //TODO
//                } else if (error instanceof ParseError) {
//                    //TODO
//                }
////                mTextView.setText("That didn't work!");
//            }
//        });
//
//// Add the request to the RequestQueue.
//        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
//    }

}