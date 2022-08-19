package com.simona.pastebin3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    EditText inputEmailED, inputPassED;
    TextView toSignUpTV;
    ProgressBar pb;

    private FirebaseAuth myAuth;

    public static final String LOGGED_USERNAME = "lun";
    public static final String LOGGED_USERID = "luid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    void loginExistingUser() {
        String userName = inputEmailED.getText().toString().trim();
        String password = inputPassED.getText().toString().trim();

        if (userName.isEmpty()) {
            Toast.makeText(this, "Email is required!", Toast.LENGTH_SHORT).show();
            inputEmailED.setText(null);
            inputEmailED.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            Toast.makeText(this, "Email is not valid!", Toast.LENGTH_SHORT).show();
            inputEmailED.setText(null);
            inputEmailED.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            inputPassED.setText(null);
            inputPassED.requestFocus();
            Toast.makeText(this, "Password is required!", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 6) {
            inputPassED.setText(null);
            inputPassED.requestFocus();
            Toast.makeText(this, "Minimum pass length is 6!", Toast.LENGTH_SHORT).show();
            return;
        }

        pb.setVisibility(View.VISIBLE);

        myAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "User login succesful!", Toast.LENGTH_SHORT).show();

                    String loggedUID = myAuth.getUid();
                    Intent toAddActivity = new Intent(MainActivity.this, AddActivity.class);
                    toAddActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    toAddActivity.putExtra(LOGGED_USERNAME, userName);
                    toAddActivity.putExtra(LOGGED_USERID, loggedUID);
                    toAddActivity.putExtra("IntentFrom", "MainActivity");
                    startActivity(toAddActivity);
                } else {
                    inputEmailED.requestFocus();
                    Toast.makeText(MainActivity.this, "Error message: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                pb.setVisibility(View.GONE);
            }
        });

    }

    void initViews() {

        pb = findViewById(R.id.loginPB);
        myAuth = FirebaseAuth.getInstance();

        inputEmailED = findViewById(R.id.emailLoginED);
        inputPassED = findViewById(R.id.passLoginED);
//        inputPassED.setText("123456");

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(view -> {
            loginExistingUser();

        });


        toSignUpTV = findViewById(R.id.toSignUpTV);
        toSignUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignUpActivity = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(toSignUpActivity);
            }
        });


    }
}