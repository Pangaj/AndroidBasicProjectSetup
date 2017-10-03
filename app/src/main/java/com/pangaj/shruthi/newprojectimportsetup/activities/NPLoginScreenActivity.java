package com.pangaj.shruthi.newprojectimportsetup.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pangaj.shruthi.newprojectimportsetup.NPApplication;
import com.pangaj.shruthi.newprojectimportsetup.NPPreferences;
import com.pangaj.shruthi.newprojectimportsetup.R;
import com.pangaj.shruthi.newprojectimportsetup.utils.NPUtilities;

/**
 * Created by pangaj on 23/09/17.
 */
public class NPLoginScreenActivity extends NPBaseActivity implements View.OnClickListener {
    private TextInputLayout emailLayout, passwordLayout;
    private EditText etEmail, etPassword;
    private NPPreferences mPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.np_activity_login);

        mPref = NPApplication.getInstance().getPrefs();
        emailLayout = (TextInputLayout) findViewById(R.id.email_layout);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    doLoginValidation();
                }
                return false;
            }
        });
        btnLogin.setOnClickListener(this);
    }

    /**
     * Method to do validation and perform Login API
     */
    private void doLoginValidation() {
        clearErrors();
        dismissKeyboard(this);
        String emailText = etEmail.getText().toString();
        String passwordText = etPassword.getText().toString();
        if (!NPUtilities.isValidEmailAddress(emailText)) {
            emailLayout.setError(getString(R.string.email_not_valid));
        } else if (!NPUtilities.validatePassword(passwordText)) {
            passwordLayout.setError(getString(R.string.password_not_valid));
        } else {
            clearErrors();
            callLoginAPI(emailText, passwordText);
        }
    }

    private void clearErrors() {
        emailLayout.setError(null);
        passwordLayout.setError(null);
    }

    private void callLoginAPI(final String emailText, final String passwordText) {
        showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgress();
                mPref.setLoginEmail(emailText);
                mPref.setLoginPassword(passwordText);
                mPref.setIsLoggedIn(true);
                goToActivity(NPLoginScreenActivity.this, NPNavigationActivity.class);
            }
        }, 3000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                doLoginValidation();
        }
    }
}