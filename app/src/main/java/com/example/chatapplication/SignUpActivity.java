package com.example.chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    EditText emailText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailText = findViewById(R.id.user_email_edit_text);
        passwordText = findViewById(R.id.user_password_edit_text);
    }

    public void signUp(View view) {

    }

    public void SignIn(View view) {

    }
}
