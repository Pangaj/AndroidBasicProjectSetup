package com.pangaj.shruthi.newprojectimportsetup.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pangaj.shruthi.newprojectimportsetup.R;

/**
 * Created by pangaj on 23/09/17.
 */
public class RALoginScreenActivity extends NPBaseActivity implements View.OnClickListener {
    private EditText etEmail, etPassword;
    private Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.np_activity_login);

        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
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
        dismissKeyboard(this);
        String emailText = etEmail.getText().toString();
        String passwordText = etPassword.getText().toString();
        if(TextUtils.isEmpty(emailText)) {

        } else if(TextUtils.isEmpty(passwordText)) {

        } else {
            goToActivity(this, RANavigationActivity.class);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                doLoginValidation();
        }
    }
}