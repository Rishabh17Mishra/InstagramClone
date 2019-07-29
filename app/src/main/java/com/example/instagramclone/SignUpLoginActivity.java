package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import es.dmoral.toasty.Toasty;

public class SignUpLoginActivity extends AppCompatActivity {

    private EditText editUserName, editPassword, editUserNameLogin, editPasswordLogin;
    private Button btnSignUpUser, btnLoginUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_login_activity);

        editUserName = findViewById(R.id.editUserName);
        editPassword = findViewById(R.id.editPassword);
        btnSignUpUser = findViewById(R.id.btnSignUpUser);

        btnLoginUser = findViewById(R.id.btnLoginUser);
        editUserNameLogin = findViewById(R.id.editUserNameLogin);
        editPasswordLogin = findViewById(R.id.editPasswordLogin);

        btnSignUpUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseUser appUser = new ParseUser();
                appUser.setUsername(editUserName.getText().toString());
                appUser.setPassword(editPassword.getText().toString());

                appUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toasty.success(SignUpLoginActivity.this, appUser.get("username") + " is Signed Up Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignUpLoginActivity.this, WelcomeActivity.class);
                            startActivity(intent);

                        } else
                            Toasty.error(SignUpLoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(editUserNameLogin.getText().toString(),
                        editPasswordLogin.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user != null && e == null) {
                                    Toasty.success(SignUpLoginActivity.this, user.get("username") + " is Logged In Successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(SignUpLoginActivity.this, WelcomeActivity.class);
                                    startActivity(intent);

                                } else
                                    Toasty.error(SignUpLoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }
}
