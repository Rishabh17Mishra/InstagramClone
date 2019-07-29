package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import es.dmoral.toasty.Toasty;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textEmailLogin, textPasswordLogin;
    private Button btnSignUpUserLogin, btnLogInUserLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        setTitle("LOGIN");

        textEmailLogin = findViewById(R.id.textEmailLogin);
        textPasswordLogin = findViewById(R.id.textPasswordLogin);
        btnSignUpUserLogin = findViewById(R.id.btnSignUpUserLogin);
        btnLogInUserLogin = findViewById(R.id.btnLogInUserLogin);

        btnSignUpUserLogin.setOnClickListener(this);
        btnLogInUserLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser().logOut();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnLogInUserLogin:

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(textEmailLogin.getText().toString() + " Log In Complete");
                progressDialog.show();

                ParseUser.logInInBackground(textEmailLogin.getText().toString(), textPasswordLogin.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null) {
                            Toasty.success(LogInActivity.this,user.getUsername() + " is Logged in Successfully ", Toasty.LENGTH_SHORT).show();
                        } else {
                            Toasty.error(LogInActivity.this, e.getMessage(), Toasty.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

                break;

            case R.id.btnSignUpUserLogin:
                break;
        }

    }
}
