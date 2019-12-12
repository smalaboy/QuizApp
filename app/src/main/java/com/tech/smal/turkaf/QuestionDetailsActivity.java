package com.tech.smal.turkaf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tech.smal.turkaf.data.GradesReport;
import com.tech.smal.turkaf.data.Question;
import com.tech.smal.turkaf.data.QuestionDetails;
import com.tech.smal.turkaf.data.models.DbPaths;
import com.tech.smal.turkaf.data.models.Question_;

public class QuestionDetailsActivity extends AppCompatActivity {
    public final static String TAG = QuestionDetailsActivity.class.getSimpleName();
    private TextView tvCategory;
    private Spinner spinnerCategory;
    private ImageButton btnEditCategory;
    private EditText textQuestion;
    private EditText textAnsA;
    private EditText textAnsB;
    private EditText textAnsC;
    private EditText textAnsD;
    private Button btnCancel;
    private Button btnValidate;
    private TextView tvPosRating;
    private TextView tvNegRating;

    private Question_ mQuestion;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details);

        tvCategory = (TextView)findViewById(R.id.tv_category);
        btnEditCategory = (ImageButton)findViewById(R.id.btn_edit_category);
        spinnerCategory = (Spinner)findViewById(R.id.spinner_category);
        textQuestion = (EditText)findViewById(R.id.et_question);
        textAnsA = (EditText)findViewById(R.id.et_ans_a);
        textAnsB = (EditText)findViewById(R.id.et_ans_b);
        textAnsC = (EditText)findViewById(R.id.et_ans_c);
        textAnsD = (EditText)findViewById(R.id.et_ans_d);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnValidate = (Button) findViewById(R.id.btn_validate);
        tvPosRating = (TextView)findViewById(R.id.tv_pos_rating);
        tvNegRating = (TextView)findViewById(R.id.tv_neg_rating);


        //enable offline data persistence
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qId = mQuestion.getId();
                String category = mQuestion.getCategory();
                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                Log.d(TAG, "qID " + qId);
                String question = textQuestion.getText().toString().trim();
                String ansA = textAnsA.getText().toString().trim();
                String ansB = textAnsB.getText().toString().trim();
                String ansC = textAnsC.getText().toString().trim();
                String ansD = textAnsD.getText().toString().trim();

                Question_ updatedQst = new Question_(qId, userEmail, qId, qId, question, category, ansA, ansB, ansC, ansD, "pending");
                //new AsyncUpdateTask().execute(updatedQst);
                updateData(qId, updatedQst);
            }
        });





//        btnValidate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String qId = mQuestion.getId();
//                Log.d(TAG, "qID " + qId);
//                int id = mQuestion.getQuestion().getId();
//                String question = textQuestion.getText().toString().trim();
//                String ansA = textAnsA.getText().toString().trim();
//                String ansB = textAnsB.getText().toString().trim();
//                String ansC = textAnsC.getText().toString().trim();
//                String ansD = textAnsD.getText().toString().trim();
//
//                Question updatedQst = new Question(id, question, ansA, ansB, ansC, ansD, ansA);
//                //new AsyncUpdateTask().execute(updatedQst);
//                updateData(qId, updatedQst);
//            }
//        });



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI(mQuestion);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            Question_ qst = (Question_) intent.getSerializableExtra(Intent.EXTRA_TEXT);

            updateUI(qst);
        }
    }

    private void updateUI(Question_ qD) {
        mQuestion = qD;
        tvCategory.setText("Default");
        textQuestion.setText(qD.getQuestion());
        textAnsA.setText(qD.getAnsA());
        textAnsB.setText(qD.getAnsB());
        textAnsC.setText(qD.getAnsC());
        textAnsD.setText(qD.getAnsD());

        //GradesReport report = new GradesReport(qD.getQuestion().getId(), qD.getGradesList());
        //report.makeReport();
        tvPosRating.setText(20+"");
        tvNegRating.setText(4+"");
    }

    void updateData(String key, final Question_ qst) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference(DbPaths.BASE_PATH_QUESTIONS);
        //mRef.child("").
        Log.d(TAG, mRef.orderByChild("id").equalTo(key).getRef().toString());
        mFirebaseDatabase.getReference("questions/"+key).setValue(qst)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "COMPLETE");
                            PropositionsActivity.mAdapter.setItem(getIntent().getIntExtra("POSITION", -1), qst);
                            PropositionsActivity.mAdapter.notifyDataSetChanged();
                            Toast.makeText(QuestionDetailsActivity.this, "Modifiée avec succès!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "dataSetChanged");
                        }
                        else
                            Log.e(TAG, task.getException().getMessage());
                    }
                });
    }



//    void updateData(String key, final Question qst) {
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mRef = mFirebaseDatabase.getReference("questions");
//        //mRef.child("").
//        Query query = mRef.orderByChild("id").equalTo(key);
//        Log.d(TAG, mRef.orderByChild("id").equalTo(key).getRef().toString());
//        mFirebaseDatabase.getReference("questions/"+key + "/question").setValue(qst)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Log.d(TAG, "COMPLETE");
//                            PropositionsActivity.mAdapter.getItem(getIntent().getIntExtra("POSITION", -1)).setQuestion(qst);
//                            PropositionsActivity.mAdapter.notifyDataSetChanged();
//                            Toast.makeText(QuestionDetailsActivity.this, "Modifiée avec succès!", Toast.LENGTH_SHORT).show();
//                            Log.d(TAG, "dataSetChanged");
//                        }
//                        else
//                            Log.e(TAG, task.getException().getMessage());
//                    }
//                });
//    }

//    class AsyncUpdateTask extends AsyncTask<Question, Void, Boolean> {
//
//        Question qst;
//        @Override
//        protected Boolean doInBackground(Question... questions) {
//            DbHelper db = new DbHelper(getApplicationContext());
//            qst = questions[0];
//            boolean isUpdated = db.updateQuestion(questions[0]);
//            return isUpdated;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//            if (aBoolean) {
//                if (PropositionsActivity.mAdapter != null){
//                    PropositionsActivity.mAdapter.getItem(getIntent().getIntExtra("POSITION", -1)).setQuestion(qst);
//                    PropositionsActivity.mAdapter.notifyDataSetChanged();
//                    Log.d(TAG, "dataSetChanged");
//                }
//                Toast.makeText(QuestionDetailsActivity.this, "Mis à jour avec succès!", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Toast.makeText(QuestionDetailsActivity.this, "Mis à jour non effectuée!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
