package com.example.experiment5;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button registerButton, loginButton;
    ImageView usernameTick, passwordTick;
    com.example.experiment5.DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etusername);
        etPassword = findViewById(R.id.etpassword);
        registerButton = findViewById(R.id.etregisterButton);
        loginButton = findViewById(R.id.etloginButton);
        usernameTick = findViewById(R.id.usernameTick);
        passwordTick = findViewById(R.id.passwordTick);
        dbHelper = new com.example.experiment5.DBHelper(this);

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateUsername(s.toString())) {
                    usernameTick.setVisibility(View.VISIBLE);
                } else {
                    usernameTick.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validatePassword(s.toString())) {
                    passwordTick.setVisibility(View.VISIBLE);
                } else {
                    passwordTick.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (validateInput(username, password)) {
                    boolean isInserted = dbHelper.insertUser(username, password);
                    if (isInserted) {
                        Toast.makeText(MainActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (validateInput(username, password)) {
                    boolean isAuthenticated = dbHelper.checkUser(username, password);
                    if (isAuthenticated) {
                        Toast.makeText(MainActivity.this, "Welcome to REC, logged in successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong credentials,Failed to login", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateInput(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!validateUsername(username)) {
            etUsername.setError("Username must contain at least one uppercase letter and one special character.");
            return false;
        }

        if (!validatePassword(password)) {
            etPassword.setError("Password must contain at least one uppercase letter, one number, and one special character.");
            return false;
        }

        return true;
    }

    private boolean validateUsername(String username) {
        return username.matches(".*[A-Z].*") && username.matches(".*[!@#$%^&*+=?-].*");
    }

    private boolean validatePassword(String password) {
        return password.matches(".*[A-Z].*") && password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*+=?-].*");
    }
}
