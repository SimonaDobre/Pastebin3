package com.simona.pastebin3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContentDetailsActivity extends AppCompatActivity {

    TextView nameTV, titleTV, contentTV, postDateTV, expirDateTV, numberOFViewsTV;
    DatabaseReference dbReferenceToUserPastes;
    DatabaseReference dbReferenceToAllPastes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_details);

        initViews();

    }

    void initViews() {

        nameTV = findViewById(R.id.userNameDetailsTV);
        titleTV = findViewById(R.id.titleDetailsTV);
        contentTV = findViewById(R.id.messageTV);
        postDateTV = findViewById(R.id.dataPostariiTV);
        expirDateTV = findViewById(R.id.dataExpirariiTV);
        numberOFViewsTV = findViewById(R.id.viewsNumberTV);

        Intent receivedIntent = getIntent();
        String userID = receivedIntent.getStringExtra(AddActivity.USER_ID);
        String userEmail = receivedIntent.getStringExtra(AddActivity.USER_NAME);

        String postId = receivedIntent.getStringExtra(AddActivity.POST_ID);
        String postTitle = receivedIntent.getStringExtra(AddActivity.TITLE);
        String postContent = receivedIntent.getStringExtra(AddActivity.CONTENT);
        String postStartDate = receivedIntent.getStringExtra(AddActivity.START_DATE);
        String postExpDate = receivedIntent.getStringExtra(AddActivity.EXP_DATE);
        int postNumberOfViews = receivedIntent.getIntExtra(AddActivity.VIEWS, -1);
        postNumberOfViews++;

        nameTV.setText("UserName: " + userEmail);
        titleTV.setText("Title: " + postTitle);
        contentTV.setText("Content: " + postContent);
        postDateTV.setText("PostDate: " + postStartDate);
        expirDateTV.setText("Exp Date: " + postExpDate);
        numberOFViewsTV.setText("Number of views: " + postNumberOfViews);

        Paste modifPost = new Paste(postId, postTitle, postContent,
                postStartDate, postExpDate, postNumberOfViews, userEmail, userID);

        dbReferenceToUserPastes = FirebaseDatabase.getInstance().getReference("UsersPastes").child(userID);
        dbReferenceToUserPastes.child(postId).setValue(modifPost);

        dbReferenceToAllPastes = FirebaseDatabase.getInstance().getReference("AllPastes").child(postId);
        dbReferenceToAllPastes.setValue(modifPost);

    }


}