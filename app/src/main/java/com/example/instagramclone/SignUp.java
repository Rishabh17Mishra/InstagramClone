package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import es.dmoral.toasty.Toasty;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText textEmail, textUsername, textPassword;
    private Button btnSignUpUser, btnLogInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("SIGN UP");

        textEmail = findViewById(R.id.textEmail);
        textUsername = findViewById(R.id.textUsername);
        textPassword = findViewById(R.id.textPassword);
        btnSignUpUser = findViewById(R.id.btnSignUpUser);
        btnLogInUser = findViewById(R.id.btnLogInUser);

        btnSignUpUser.setOnClickListener(this);
        btnLogInUser.setOnClickListener(this);
        textPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) { onClick(btnSignUpUser); }
                return false;
            }
        });

        if (ParseUser.getCurrentUser() != null)
        {
//            ParseUser.getCurrentUser().logOut();
            transitionToSocialMediaActivity();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnSignUpUser:
                if (textEmail.getText().toString().equals("") || textUsername.getText().toString().equals("") || textPassword.getText().toString().equals(""))
                {
                    Toasty.info(SignUp.this, "Email, Username, Password is required", Toasty.LENGTH_SHORT).show();
                } else {
                    final ParseUser signUpUser = new ParseUser();
                    signUpUser.setEmail(textEmail.getText().toString());
                    signUpUser.setUsername(textUsername.getText().toString());
                    signUpUser.setPassword(textPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage(" Signing Up " + textUsername.getText().toString());
                    progressDialog.show();

                    signUpUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toasty.success(SignUp.this, signUpUser.getUsername() + " is Signed Up ", Toasty.LENGTH_SHORT).show();
                                transitionToSocialMediaActivity();
                            } else {
                                Toasty.error(SignUp.this, e.getMessage(), Toasty.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.btnLogInUser:
                Intent intent = new Intent(SignUp.this, LogInActivity.class);
                startActivity(intent);
                break;

        }
    }

    public void rootLayoutTap(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void transitionToSocialMediaActivity() {
        Intent intent = new Intent(SignUp.this, SocialMediaActivity.class);
        startActivity(intent);
    }

}