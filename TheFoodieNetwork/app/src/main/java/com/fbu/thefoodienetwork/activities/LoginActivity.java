package com.fbu.thefoodienetwork.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        logoAnimation();

        //Wobble on click
        binding.logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoAnimation();
            }
        });

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        onClickLogin();
        onClickRegister();
    }

    private void logoAnimation() {
        YoYo.with(Techniques.Wobble)
                .duration(600)
                .repeat(1)
                .playOn(binding.logo);
    }

    private void onClickLogin() {
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Onclick login button");
                username = binding.usernameEditText.getText().toString();
                password = binding.passwordEditText.getText().toString();
                loginUser(username.trim(), password);
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user: " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {

                    AlertDialog.Builder errorAlert = new AlertDialog.Builder(LoginActivity.this, R.style.ErrorAlertDialog);

                    errorAlert.setMessage("Incorrect username or password.");
                    errorAlert.setTitle("Error Message...");
                    errorAlert.setPositiveButton("OK", null);
                    errorAlert.setCancelable(true);
                    errorAlert.create().show();

                    errorAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    return;
                }

                logoAnimation();

                ParseInstallation.getCurrentInstallation().put("user", user);
                ParseInstallation.getCurrentInstallation().saveInBackground();

                //fetching user data
                CurrentUserUtilities.getInstance();

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