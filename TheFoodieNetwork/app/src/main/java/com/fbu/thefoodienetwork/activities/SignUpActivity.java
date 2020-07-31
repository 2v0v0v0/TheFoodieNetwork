package com.fbu.thefoodienetwork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.ActivitySignUpBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private static final int PASS_UNMATCH = -1;
    private static final int PASS_INVALID_LENGTH = 1;
    private static final int PASS_VALID = 0;
    private ActivitySignUpBinding binding;

    private EditText usernameEditText;
    private EditText emailEditText;

    private static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return true;
        }
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private static int isValidPassword(String password, String confirmPass) {
        if (password.length() < 6) {
            return PASS_INVALID_LENGTH;
        }
        if (!password.equals(confirmPass)) {
            return PASS_UNMATCH;
        }
        return PASS_VALID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);

        binding.sigupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick sign up button");
                String screenName = binding.fullNameEditText.getText().toString();
                String username = binding.usernameEditText.getText().toString();
                String email = binding.emailEditText.getText().toString();
                if (!isValidEmail(email) && !email.isEmpty()) {
                    binding.emailEditText.setError("Invalid email");
                    return;
                }
                String password = binding.passwordEditText.getText().toString();
                String confirmPass = binding.confirmPasswordEditText.getText().toString();
                switch (isValidPassword(password, confirmPass)) {
                    case PASS_INVALID_LENGTH: {
                        binding.passwordEditText.setError("Password must be at least 6 characters");
                        binding.confirmPasswordEditText.setError("Password must be at least 6 characters");
                        return;
                    }
                    case PASS_UNMATCH: {
                        binding.passwordEditText.setError("Password unmatched");
                        binding.confirmPasswordEditText.setError("Password unmatched");
                        return;
                    }
                    default: {
                        signUp(email, screenName, username, password);
                    }
                }
            }
        });
    }

    private void signUp(String email, String screenname, String username, String password) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.sigupButton.setEnabled(false);
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        if (email.isEmpty()) {
            email = null;
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (screenname != null) {
            user.put("screenName", screenname);
        }

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                binding.progressBar.setVisibility(View.GONE);
                binding.sigupButton.setEnabled(true);
                if (e == null) {
                    Log.i(TAG, "Sign up successful");
                    goLoginActivity();
                    Toast.makeText(SignUpActivity.this, "Login to your new account!", Toast.LENGTH_LONG).show();
                } else {

                    switch (e.getCode()) {
                        case ParseException.USERNAME_TAKEN: {
                            usernameEditText.setError("Username already exist");
                            break;
                        }
                        case ParseException.EMAIL_TAKEN: {
                            emailEditText.setError("Email taken");
                            break;
                        }
                        default: {
                            // Something else went wrong
                            Log.e(TAG, "Sign up failed");
                        }
                    }
                }
            }
        });
    }

    private void goLoginActivity() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();
        Log.i(TAG, currentUser == null ? "Log out success" : "Log out fail");
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}