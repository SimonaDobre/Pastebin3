package com.simona.pastebin3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    EditText emailSignUpED, pasSignUpED;
    Button signUpBtn;
    TextView toLoginTV;
    private FirebaseAuth myAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
    }


    void registerNewUser() {
        String userName = emailSignUpED.getText().toString().trim();
        String password = pasSignUpED.getText().toString().trim();

        if (userName.isEmpty()) {
            Toast.makeText(this, "Email is required!", Toast.LENGTH_SHORT).show();
            emailSignUpED.setText(null);
            emailSignUpED.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            Toast.makeText(this, "Email is not valid!", Toast.LENGTH_SHORT).show();
            emailSignUpED.setText(null);
            emailSignUpED.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            pasSignUpED.setText(null);
            pasSignUpED.requestFocus();
            Toast.makeText(this, "Password is required!", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 6) {
            pasSignUpED.setText(null);
            pasSignUpED.requestFocus();
            Toast.makeText(this, "Minimum pass length is 6!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        myAuth.createUserWithEmailAndPassword(userName, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "User register succesful!", Toast.LENGTH_SHORT).show();
                        } else {
                            emailSignUpED.requestFocus();
                            Toast.makeText(SignUpActivity.this, "mesaj: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    void hideKb() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(emailSignUpED.getWindowToken(),0);
    }

    void initViews() {

        progressBar = findViewById(R.id.signUpPB);
        myAuth = FirebaseAuth.getInstance();
        emailSignUpED = findViewById(R.id.emailSignUpED);
        pasSignUpED = findViewById(R.id.passSignUpED);
        signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
                hideKb();
            }
        });

        toLoginTV = findViewById(R.id.toLoginTV);

        toLoginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLoginActivity = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(toLoginActivity);
            }
        });

    }

}