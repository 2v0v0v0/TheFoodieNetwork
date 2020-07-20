package com.fbu.thefoodienetwork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private String username;
    private String password;
    public CurrentUserUtilities currentUserUtilities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        onClickLogin();
        onTouchRegister();

    }

    private void onClickLogin() {
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Onclick login button");
                username = binding.usernameEditText.getText().toString();
                password = binding.passwordEditText.getText().toString();
                loginUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user: " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    //TODO: Fail login message
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
                goMainActivity();
            }
        });

    }

    private void onTouchRegister() {
        binding.SignupTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i(TAG, "onTouch sign up");
                goSignUpActivity();
                return false;
            }
        });
    }

    private void goMainActivity() {
        currentUserUtilities = new CurrentUserUtilities();
        Log.i(TAG, ParseUser.getCurrentUser().getUsername());
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void goSignUpActivity() {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
        finish();
    }
}