package com.project.mobility.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.mobility.R;

import javax.inject.Inject;

import androidx.core.content.ContextCompat;

public class GenericTextWatcher implements TextWatcher {

    private final TextInputEditText textInputEditText;
    private final TextInputLayout textInputLayout;

    @Inject Context context;

    public GenericTextWatcher(TextInputEditText view, TextInputLayout textInputLayout) {
        this.textInputEditText = view;
        this.textInputLayout = textInputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable email) {
        String textToVerify = email.toString();

        switch (textInputEditText.getId()) {
//            case R.id.et_email:
//                validateField(StringUtils.isEmailValid(textToVerify));
//                break;
        }
    }

    private void validateField(boolean condition) {
        if (condition) {
            changeFieldColor(textInputLayout, android.R.color.white);
            textInputEditText.setError(null);
        } else {
            changeFieldColor(textInputLayout, R.color.progress_failure);
            textInputEditText.setError(textInputEditText.getHint() + " is invalid");
        }
    }

    private void changeFieldColor(TextInputLayout textInputLayout, int colorResource) {
        textInputLayout.setBoxStrokeColor(ContextCompat.getColor(context, colorResource));
        textInputLayout.setDefaultHintTextColor(ContextCompat.getColorStateList(context, colorResource));
    }
}