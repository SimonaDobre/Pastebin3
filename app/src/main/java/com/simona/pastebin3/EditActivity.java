package com.simona.pastebin3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditActivity extends AppCompatActivity {

    TextView userNameTV, numberOfViewsTV, startDateTV, expirDateTV;
    TextInputEditText titleTIET, contentTIET;
    Button updateBtn;

    public static final String USER_NAME = "un";
    public static final String USER_ID = "uid";

    DatabaseReference dbReferenceToUserPastes;
    DatabaseReference dbReferenceToAllPastes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initViews();

    }


    void initViews() {

        Intent receivedIntent = getIntent();
        String userID = receivedIntent.getStringExtra(AddActivity.USER_ID);
        String userName = receivedIntent.getStringExtra(AddActivity.USER_NAME);

        String postId = receivedIntent.getStringExtra(AddActivity.POST_ID);
        String postTitle = receivedIntent.getStringExtra(AddActivity.TITLE);
        String postContent = receivedIntent.getStringExtra(AddActivity.CONTENT);
        String postStartDate = receivedIntent.getStringExtra(AddActivity.START_DATE);
        String postExpDate = receivedIntent.getStringExtra(AddActivity.EXP_DATE);
        int postNumberOfViews = receivedIntent.getIntExtra(AddActivity.VIEWS, -1);

        userNameTV = findViewById(R.id.userNameTV);
        numberOfViewsTV = findViewById(R.id.numberOfViewsTV);
        startDateTV = findViewById(R.id.startDateTV);

        expirDateTV = findViewById(R.id.expirDateTV);
        titleTIET = findViewById(R.id.titleTIET);
        contentTIET = findViewById(R.id.contectTIET);

        userNameTV.setText("UserName: " + userName);
        numberOfViewsTV.setText("Number of views: " + postNumberOfViews);
        startDateTV.setText(postStartDate);

        expirDateTV.setText(postExpDate);
        titleTIET.setText(postTitle);
        contentTIET.setText(postContent);

        dbReferenceToUserPastes = FirebaseDatabase.getInstance().getReference("UsersPastes").child(userID);
        dbReferenceToAllPastes = FirebaseDatabase.getInstance().getReference("AllPastes").child(postId);

        updateBtn = findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = titleTIET.getText().toString().trim();
                String newContent = contentTIET.getText().toString().trim();

                Paste updatedPost = new Paste(postId, newTitle, newContent, postStartDate,
                        postExpDate, postNumberOfViews, userName, userID);
                dbReferenceToUserPastes.child(postId).setValue(updatedPost);
                dbReferenceToAllPastes.setValue(updatedPost);
                Intent toAddActivity = new Intent(EditActivity.this, AddActivity.class);
                toAddActivity.putExtra("IntentFrom", "EditActiv");
                toAddActivity.putExtra(USER_NAME, userName);
                toAddActivity.putExtra(USER_ID, userID);
                startActivity(toAddActivity);
                finish();
            }
        });

    }

}