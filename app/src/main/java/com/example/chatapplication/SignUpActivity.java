package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    EditText emailText, passwordText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.sign_up);
        setContentView(R.layout.activity_sign_up);

        emailText = findViewById(R.id.user_email_edit_text);
        passwordText = findViewById(R.id.user_password_edit_text);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);
        }
    }

    public void signUp(View view) {

        if (!emailText.getText().toString().equals("") && !passwordText.getText().toString().equals("")){
            mAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

//                            FirebaseUser user = mAuth.getCurrentUser();
//                            String userEmail = null;
//                            if (user != null) {
//                                userEmail = user.getEmail();
//                            }

                                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                startActivity(intent);

                            }else{
                                Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.please_enter_your_email_and_password), Toast.LENGTH_LONG).show();
        }
    }

    public void SignIn(View view) {

        if (!emailText.getText().toString().equals("") && !passwordText.getText().toString().equals("")){
            mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.please_enter_your_email_and_password), Toast.LENGTH_LONG).show();
        }
    }
}
