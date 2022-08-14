package com.simona.pastebin3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class AddActivity extends AppCompatActivity implements ManagePastesInterface {

    TextView loggedUserTV;
    RecyclerView rv;
    AdapterPaste myAdapter;
    ArrayList<Paste> postssArray;

    EditText inputContentED, inputTitleED;
    Spinner syntaxSpinner, expirationSpinner, exposureSpinner, folderSpinner;
    Button createPasteBtn, showMyPastesBtn, showPublicPastesBtn;

    DatabaseReference dbReferenceToUserPastes;
    DatabaseReference dbReferenceToAllPastes;
    boolean showAllUsersPastes;

    String loggedUser, loggedUserID;

    public static final String USER_NAME = "un";
    public static final String USER_ID = "userID";

    public static final String POST_ID = "postID";
    public static final String TITLE = "ti";
    public static final String CONTENT = "cnt";
    public static final String VIEWS = "v";
    public static final String START_DATE = "sd";
    public static final String EXP_DATE = "expd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initViews();

        swipeToDelete();

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (showAllUsersPastes) {
            dbReferenceToAllPastes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    postssArray.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Paste currentPost = ds.getValue(Paste.class);
                        postssArray.add(currentPost);
                    }
                    refreshAdapter();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            dbReferenceToUserPastes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    postssArray.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Paste currentPost = ds.getValue(Paste.class);
                        postssArray.add(currentPost);
                    }
                    refreshAdapter();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    void refreshAdapter() {
        Collections.reverse(postssArray);
        myAdapter = new AdapterPaste(this, postssArray, this);
        rv.setAdapter(myAdapter);
    }

    void swipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolderForSwipe,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                        @NonNull RecyclerView.ViewHolder viewHolder) {
                int swipeFlag = 0;
                Paste postToBeDeleted = postssArray.get(viewHolder.getAdapterPosition());
                if (postToBeDeleted.getUserName().equals(loggedUser)) {
                    swipeFlag = ItemTouchHelper.LEFT;
                }
                return makeMovementFlags(0, swipeFlag);
//                return super.getMovementFlags(recyclerView, viewHolderForFlag);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolderForSwipe,
                                 int direction) {
                Paste postToBeDeleted = postssArray.get(viewHolderForSwipe.getAdapterPosition());
                String idToBeDeleted = postToBeDeleted.getPostID();
                String titleToBeDeleted = postToBeDeleted.getPostTitle();
                dbReferenceToUserPastes.child(idToBeDeleted).removeValue();
                dbReferenceToAllPastes.child(idToBeDeleted).removeValue();
                Snackbar.make(rv, "Am sters " + titleToBeDeleted, BaseTransientBottomBar.LENGTH_LONG)
                        .setAction("UNDO ?", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dbReferenceToUserPastes.child(idToBeDeleted).setValue(postToBeDeleted);
                                dbReferenceToAllPastes.child(idToBeDeleted).setValue(postToBeDeleted);
                            }
                        }).show();
                refreshAdapter();
            }
        }).attachToRecyclerView(rv);


    }

    void initViews() {
        Intent receivedIntent = getIntent();
        String previousActivity = receivedIntent.getStringExtra("IntentFrom");
        if (previousActivity.equals("MainActiv")) {
            loggedUser = receivedIntent.getStringExtra(MainActivity.LOGGED_USERNAME);
            loggedUserID = receivedIntent.getStringExtra(MainActivity.LOGGED_USERID);
        } else if (previousActivity.equals("EditActiv")) {
            loggedUser = receivedIntent.getStringExtra(EditActivity.USER_NAME);
            loggedUserID = receivedIntent.getStringExtra(EditActivity.USER_ID);
        }

        loggedUserTV = findViewById(R.id.loggedUserTV);
        loggedUserTV.setText("User logged " + loggedUser);

        rv = findViewById(R.id.postsRV);
        postssArray = new ArrayList<>();
        // to display LIFO
        Collections.reverse(postssArray);
        myAdapter = new AdapterPaste(this, postssArray, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration did = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(did);
        rv.setAdapter(myAdapter);

        dbReferenceToUserPastes = FirebaseDatabase.getInstance().getReference("UsersPastes").child(loggedUserID);
        dbReferenceToAllPastes = FirebaseDatabase.getInstance().getReference("AllPastes");

        inputContentED = findViewById(R.id.inputContentED);
        inputTitleED = findViewById(R.id.inputTitleED);

        syntaxSpinner = findViewById(R.id.syntaxSpinner);
        expirationSpinner = findViewById(R.id.expirationSpinner);
        exposureSpinner = findViewById(R.id.exposureSpinner);
        folderSpinner = findViewById(R.id.folderSpinner);

        createPasteBtn = findViewById(R.id.createBtn);
        createPasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKb();
                String postID = dbReferenceToUserPastes.push().getKey();
                String postTitle = inputTitleED.getText().toString().trim();
                if (postTitle.equals("")) {
                    Toast.makeText(AddActivity.this, "Adauga un titlu / un nume!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String postContent = inputContentED.getText().toString().trim();
                if (postContent.equals("")) {
                    Toast.makeText(AddActivity.this, "Adauga un continut!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String expirDate = setExpirationDate();
                if (expirDate.equals("None")) {
                    Toast.makeText(AddActivity.this, "Selecteaza data expirarii!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Paste newPost = new Paste(postID, postTitle, postContent, setPostDate(), expirDate,
                        0, loggedUser, loggedUserID);
                dbReferenceToUserPastes.child(postID).setValue(newPost);
                dbReferenceToAllPastes.child(postID).setValue(newPost);
                setToDefaultValues();
            }
        });

        showMyPastesBtn = findViewById(R.id.myPasesBtn);
        showPublicPastesBtn = findViewById(R.id.publicPastesBtn);

        showMyPastesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllUsersPastes = false;
                postssArray.clear();
                dbReferenceToUserPastes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Paste currentPaste = ds.getValue(Paste.class);
                            postssArray.add(currentPaste);
                        }
                        refreshAdapter();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        showPublicPastesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllUsersPastes = true;
                postssArray.clear();
                dbReferenceToAllPastes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            postssArray.add(ds.getValue(Paste.class));
                        }
                        refreshAdapter();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    String setPostDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String postDate = sdf.format(cal.getTime());
        return postDate;
    }

    String setExpirationDate() {
        int numberOfDays = 0;
        int minutes = 0;
        String expSelectedItem = expirationSpinner.getSelectedItem().toString();
        switch (expSelectedItem) {
            case "1 minut":
                minutes = 1;
                break;
            case "10 minute":
                minutes = 10;
                break;
            case "1 ora":
                minutes = 60;
                break;
            case "1 zi":
                numberOfDays = 1;
                break;
            case "1 sapt":
                numberOfDays = 7;
                break;
            case "2 sapt":
                numberOfDays = 14;
                break;
            case "1 luna":
                numberOfDays = 30;
                break;
            case "6 luni":
                numberOfDays = 183;
                break;
            case "1 an":
                numberOfDays = 365;
                break;
        }
        String expirDate = "";
        if (!expSelectedItem.equals("None")){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, numberOfDays);
            cal.add(Calendar.MINUTE, minutes);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            expirDate = sdf.format(cal.getTime());
        } else {
            expirDate = "None";
        }
        return expirDate;
    }


    void hideKb() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputContentED.getWindowToken(), 0);
    }

    void setToDefaultValues() {
        inputTitleED.setText(null);
        inputContentED.setText(null);
        inputContentED.requestFocus();
        folderSpinner.setSelection(0);
        exposureSpinner.setSelection(0);
        expirationSpinner.setSelection(0);
        syntaxSpinner.setSelection(0);
    }

    @Override
    public void clickToViewPaste(int pos) {
        Paste clickedPost = postssArray.get(pos);
        Intent toDetailsActivity = new Intent(AddActivity.this, ContentDetailsActivity.class);
        toDetailsActivity.putExtra(USER_NAME, clickedPost.getUserName());
        toDetailsActivity.putExtra(USER_ID, clickedPost.getUserID());
        toDetailsActivity.putExtra(POST_ID, clickedPost.getPostID());
        toDetailsActivity.putExtra(TITLE, clickedPost.getPostTitle());
        toDetailsActivity.putExtra(CONTENT, clickedPost.getPostContent());
        toDetailsActivity.putExtra(VIEWS, clickedPost.getNumberOfViews());
        toDetailsActivity.putExtra(START_DATE, clickedPost.getPostDate());
        toDetailsActivity.putExtra(EXP_DATE, clickedPost.getPostExpirationDate());
        startActivity(toDetailsActivity);
    }

    @Override
    public void longClickToUpdatePaste(int pos) {
        Paste clickedPost = postssArray.get(pos);
        if (clickedPost.getUserName().equals(loggedUser)) {
            Intent toEditctivity = new Intent(AddActivity.this, EditActivity.class);
            toEditctivity.putExtra(USER_NAME, loggedUser);
            toEditctivity.putExtra(USER_ID, loggedUserID);
            toEditctivity.putExtra(POST_ID, clickedPost.getPostID());
            toEditctivity.putExtra(TITLE, clickedPost.getPostTitle());
            toEditctivity.putExtra(CONTENT, clickedPost.getPostContent());
            toEditctivity.putExtra(VIEWS, clickedPost.getNumberOfViews());
            toEditctivity.putExtra(START_DATE, clickedPost.getPostDate());
            toEditctivity.putExtra(EXP_DATE, clickedPost.getPostExpirationDate());
            startActivity(toEditctivity);
        }
    }


}