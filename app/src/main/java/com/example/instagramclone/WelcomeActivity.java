package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import es.dmoral.toasty.Toasty;

public class WelcomeActivity extends AppCompatActivity {

    private TextView textWelcome;
    private Button btnLogOutUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnLogOutUser = findViewById(R.id.btnLogOutUser);
        textWelcome = findViewById(R.id.textWelcome);

        textWelcome.setText("Welcome " + ParseUser.getCurrentUser().getUsername() + " To your LogIn Page");

        btnLogOutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                finish();
                Toasty.success(WelcomeActivity.this,  " Logged Out Successfully ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
