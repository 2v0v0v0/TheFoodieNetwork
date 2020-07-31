package com.fbu.thefoodienetwork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    public CurrentUserUtilities currentUserUtilities;
    private ActivityLoginBinding binding;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setLogoAnimation();

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        onClickLogin();
        onClickRegister();
    }

    private void setLogoAnimation(){
        YoYo.with(Techniques.Wobble)
                .duration(600)
                .repeat(1)
                .playOn(binding.logo);
        //Wobble on click
        binding.logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Wobble)
                        .duration(600)
                        .repeat(1)
                        .playOn(binding.logo);
            }
        });
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
                ParseInstallation.getCurrentInstallation().put("user", user);
                ParseInstallation.getCurrentInstallation().saveInBackground();
                goMainActivity();
            }
        });
    }

    private void onClickRegister() {
        binding.SignupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSignUpActivity();
            }
        });
    }

    private void goMainActivity() {
        currentUserUtilities = new CurrentUserUtilities();

        Log.i(TAG, ParseUser.getCurrentUser().getUsername());
        //TODO: change back to main
        //For testing purpose only
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void goSignUpActivity() {
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpIntent);
    }
}