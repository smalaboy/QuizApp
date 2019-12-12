package com.tech.smal.turkaf;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tech.smal.turkaf.data.DbHelper;
import com.tech.smal.turkaf.data.Question;
import com.tech.smal.turkaf.data.QuestionDetails;
import com.tech.smal.turkaf.data.User;
import com.tech.smal.turkaf.data.models.DbPaths;
import com.tech.smal.turkaf.data.models.Question_;
import com.tech.smal.turkaf.data.models.Rating_;
import com.tech.smal.turkaf.data.models.Vote_;

public class NewQuestionActivity extends AppCompatActivity {

    private static final String TAG = NewQuestionActivity.class.getSimpleName();
    private Spinner spinnerCategory;
    private EditText textQuestion;
    private EditText textAnsA;
    private EditText textAnsB;
    private EditText textAnsC;
    private EditText textAnsD;
    private Button btnClear;
    private Button btnValidate;

    DbHelper dbHelper;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        spinnerCategory = (Spinner)findViewById(R.id.spinner_category);
        textQuestion = (EditText)findViewById(R.id.et_question);
        textAnsA = (EditText)findViewById(R.id.et_ans_a);
        textAnsB = (EditText)findViewById(R.id.et_ans_b);
        textAnsC = (EditText)findViewById(R.id.et_ans_c);
        textAnsD = (EditText)findViewById(R.id.et_ans_d);
        btnClear = (Button) findViewById(R.id.btn_clear);
        btnValidate = (Button) findViewById(R.id.btn_validate);

        dbHelper = new DbHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFields();
            }
        });

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addQuestion();
                addData();
            }
        });
    }


    private void addQuestion() {
        String qst = textQuestion.getText().toString().trim();
        String ansA = textAnsA.getText().toString().trim();
        String ansB = textAnsB.getText().toString().trim();
        String ansC = textAnsC.getText().toString().trim();
        String ansD = textAnsD.getText().toString().trim();

        if (qst.isEmpty() || ansA.isEmpty() || ansB.isEmpty() || ansC.isEmpty() || ansD.isEmpty()) {
            Toast.makeText(this, "Champs manquants!", Toast.LENGTH_SHORT).show();
        }
        else {
            //ansA is the right answer
            Question newQuestion = new Question(qst, ansA, ansB, ansC, ansD, ansA);
            boolean isAdded = dbHelper.addQuestion(newQuestion);

            if (isAdded) {
                clearFields();
                Toast.makeText(this, "Ajouté avec succès!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void addData() {
        String qst = textQuestion.getText().toString().trim();
        String ansA = textAnsA.getText().toString().trim();
        String ansB = textAnsB.getText().toString().trim();
        String ansC = textAnsC.getText().toString().trim();
        String ansD = textAnsD.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("questions");
        DatabaseReference ratingsRef = mFirebaseDatabase.getReference(DbPaths.BASE_PATH_RATINGS);
        DatabaseReference votesRef = mFirebaseDatabase.getReference(DbPaths.BASE_PATH_VOTES);

        if (qst.isEmpty() || ansA.isEmpty() || ansB.isEmpty() || ansC.isEmpty() || ansD.isEmpty()) {
            Toast.makeText(this, "Champs manquants!", Toast.LENGTH_SHORT).show();
        }
        else {
            //ansA is the right answer
            Question_ newQuestion = new Question_();
            newQuestion.setUserId(mAuth.getCurrentUser().getEmail());
            ///QuestionDetails qDet = new QuestionDetails();
            ///User user = new User(1, mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail());
            String key = mRef.push().getKey(); //questionId
            String userId = mAuth.getCurrentUser().getEmail();
            newQuestion.setId(key);
            newQuestion.setQuestion(qst);
            newQuestion.setAnsA(ansA);
            newQuestion.setAnsB(ansB);
            newQuestion.setAnsC(ansC);
            newQuestion.setAnsD(ansD);
            newQuestion.setCategory(category);
            newQuestion.setUserId(userId);

            Rating_ rating = new Rating_(userId, key, -1); //default rating , not counted
            Vote_ vote = new Vote_(userId, key, 0); //default vote

            //the question, it ratings list, its votes list have the same key
            String newVoteKey = votesRef.child(key).push().getKey();
            String newRatingKey = ratingsRef.child(key).push().getKey();

            votesRef.child(key).child(newVoteKey).setValue(vote); //creates the list for the ratings
            ratingsRef.child(key).child(newRatingKey).setValue(rating); //creates the list for the votes

            //now create the question
            newQuestion.setRatingsId(key);
            newQuestion.setVotesId(key);

            Log.d(TAG, "dispName = " + mAuth.getCurrentUser().getDisplayName() + "  email = "
                    + mAuth.getCurrentUser().getEmail());

            mRef.child(key).setValue(newQuestion).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        clearFields();
                        Toast.makeText(NewQuestionActivity.this, "Ajouté avec succès!"
                                , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.e(TAG, task.getException().getMessage());
                        Toast.makeText(NewQuestionActivity.this, "Une erreur est survenue! :("
                                , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }




//    private void addData() {
//        String qst = textQuestion.getText().toString().trim();
//        String ansA = textAnsA.getText().toString().trim();
//        String ansB = textAnsB.getText().toString().trim();
//        String ansC = textAnsC.getText().toString().trim();
//        String ansD = textAnsD.getText().toString().trim();
//
//        mAuth = FirebaseAuth.getInstance();
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mRef = mFirebaseDatabase.getReference("questions");
//
//        if (qst.isEmpty() || ansA.isEmpty() || ansB.isEmpty() || ansC.isEmpty() || ansD.isEmpty()) {
//            Toast.makeText(this, "Champs manquants!", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            //ansA is the right answer
//            Question newQuestion = new Question(qst, ansA, ansB, ansC, ansD, ansA);
//            newQuestion.setUserId(1); //TODO temporary filling. will be removed and replaced by email adress
//            QuestionDetails qDet = new QuestionDetails();
//            User user = new User(1, mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail());
//            String key = mRef.push().getKey();
//            qDet.setId(key);
//            qDet.setQuestion(newQuestion);
//            qDet.setUser(user);
//            Log.d(TAG, "dispName = " + mAuth.getCurrentUser().getDisplayName() + "  email = "
//                    + mAuth.getCurrentUser().getEmail());
//
//            mRef.child(key).setValue(qDet).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        clearFields();
//                        Toast.makeText(NewQuestionActivity.this, "Ajouté avec succès!"
//                                , Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Log.e(TAG, task.getException().getMessage());
//                        Toast.makeText(NewQuestionActivity.this, "Une erreur est survenue! :("
//                                , Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }


    private void clearFields () {
        textQuestion.setText("");
        textAnsA.setText("");
        textAnsB.setText("");
        textAnsC.setText("");
        textAnsD.setText("");
    }
}
