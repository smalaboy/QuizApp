package com.tech.smal.turkaf;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tech.smal.turkaf.data.DataProvider;
import com.tech.smal.turkaf.data.DbHelper;
import com.tech.smal.turkaf.data.Question;
import com.tech.smal.turkaf.data.QuestionDetails;
import com.tech.smal.turkaf.data.models.DbPaths;
import com.tech.smal.turkaf.data.models.Question_;
import com.tech.smal.turkaf.data.models.Score;
import com.tech.smal.turkaf.data.models.User_;
import com.tech.smal.turkaf.data.models.Vote_;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    public static final String TAG = GameActivity.class.getSimpleName();

    public static final String CORRECT = "Correcte!";

    public static final String INCORRECT = "Incorrecte!";

    public static final int MAX_QUESTIONS = 10;

    public static final long MAX_TIME_IN_MILLIS = 10000;

    public static final int POSITIVE_VOTE = 1;
    public static final int NEGATIVE_VOTE = -1;

    private ProgressBar pbCounter;

    private DatabaseReference mQstRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference votesRef;

    private Toolbar mToolbar;
    TextView tvQuestion;
    Button btnOptA;
    Button btnOptB;
    Button btnOptC;
    Button btnOptD;
    TextView tvTimer;
    TextView tvScore;
    ProgressBar progressBar;
    LinearLayout gameLinearLayout;

    private PublisherAdView adView;

    private List<Question> questionList;
    private List<Question_> mDataSet;
    DbHelper dbHelper;
    DataProvider dataProvider;

    private int qid = 0;
    private int numberOfQuestions = 0;
    private Question_ mCurrentQuestion;
    //private QuestionDetails mqDetails;
    private CountDownTimer mCountDownTimer;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        tvQuestion = (TextView)findViewById(R.id.tv_question);
        btnOptA = (Button)findViewById(R.id.btn_opt_a);
        btnOptB = (Button)findViewById(R.id.btn_opt_b);
        btnOptC = (Button)findViewById(R.id.btn_opt_c);
        btnOptD = (Button)findViewById(R.id.btn_opt_d);
        tvTimer = (TextView)findViewById(R.id.tv_timer);
        tvScore = (TextView)findViewById(R.id.tv_score);

        pbCounter = (ProgressBar)findViewById(R.id.pbar_counter);
        pbCounter.setIndeterminate(false);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        //game_linear_layout contains all the game views except the progressbar shown while data is loading
        gameLinearLayout = (LinearLayout)findViewById(R.id.game_linear_layout);

        progressBar.setVisibility(View.VISIBLE);
        gameLinearLayout.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();


        //load ad
        loadAd();


        ///dbHelper = new DbHelper(getApplicationContext());
        //dataProvider = new DataProvider(getApplication());
        /*if (dbHelper.getAllQuestions().size() == 0) {
            dbHelper.addQuestions();
        }*/
        //questionList = dbHelper.getAllQuestions();


        mDataSet = new ArrayList<>();

        queryQuestions(10);

        btnAOnClick();
        btnBOnClick();
        btnCOnClick();
        btnDOnClick();

    }

    public void loadAd() {
        adView = findViewById(R.id.ad_view);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void updateQuestionView () {
        Log.d(TAG+"UPDATEVIEW", "in unpdateView()");

        Log.d(TAG+"mDataset", mDataSet.size() + " " + mDataSet.toString());
        if (numberOfQuestions >= MAX_QUESTIONS) {
            qid = 0;
//            tvQuestion.setText("Vous avez obtenu " + score + "/" + MAX_QUESTIONS);
//            btnOptA.setText("");
//            btnOptB.setText("");
//            btnOptC.setText("");
//            btnOptD.setText("");
            uploadScores();

            Intent intent = new Intent(getApplication(), ResultsActivity.class);
            intent.putExtra("SCORE", new Score(score*5));
            intent.putExtra("QUESTIONS_COUNT", MAX_QUESTIONS);
            startActivity(intent);
            finish();
            return;
        }
        //if at the end of the questions, shuffle and start again
        if (qid >= mDataSet.size() || qid == 0) {
            Collections.shuffle(mDataSet);
            qid = 0;
        }

        Log.d(TAG, mDataSet.size() + " mDataset");
        mCurrentQuestion = mDataSet.get(qid++);
        ///mCurrentQuestion = mqDetails.getQuestion();
        List<String> options = new ArrayList<>();
        options.add(mCurrentQuestion.getAnsA());
        options.add(mCurrentQuestion.getAnsB());
        options.add(mCurrentQuestion.getAnsC());
        options.add(mCurrentQuestion.getAnsD());
        Collections.shuffle(options);
        tvQuestion.setText(mCurrentQuestion.getQuestion());
        btnOptA.setText(options.get(0));
        btnOptB.setText(options.get(1));
        btnOptC.setText(options.get(2));
        btnOptD.setText(options.get(3));
        enableOptions();
        numberOfQuestions++;
        setCountDownTimer();
    }




//    private void updateQuestionView2 () {
//        Log.d(TAG+"UPDATEVIEW", "in unpdateView()");
//
//        Log.d(TAG+"mDataset", mDataSet.size() + " " + mDataSet.toString());
//        if (numberOfQuestions >= MAX_QUESTIONS) {
//            qid = 0;
//            //TODO show dialog instead
//            tvQuestion.setText("Vous avez obtenu " + score + "/" + MAX_QUESTIONS);
//            btnOptA.setText("");
//            btnOptB.setText("");
//            btnOptC.setText("");
//            btnOptD.setText("");
//            return;
//        }
//        //if at the end of the questions, shuffle and start again
//        if (qid >= mDataSet.size() || qid == 0) {
//            Collections.shuffle(mDataSet);
//            qid = 0;
//        }
//
//        Log.d(TAG, mDataSet.size() + " mDataset");
//        mCurrentQuestion = mDataSet.get(qid++);
//        ///mCurrentQuestion = mqDetails.getQuestion();
//        List<String> options = new ArrayList<>();
//        options.add(mCurrentQuestion.getAnsA());
//        options.add(mCurrentQuestion.getAnsB());
//        options.add(mCurrentQuestion.getAnsC());
//        options.add(mCurrentQuestion.getAnsD());
//        Collections.shuffle(options);
//        tvQuestion.setText(mCurrentQuestion.getQuestion());
//        btnOptA.setText(options.get(0));
//        btnOptB.setText(options.get(1));
//        btnOptC.setText(options.get(2));
//        btnOptD.setText(options.get(3));
//        enableOptions();
//        numberOfQuestions++;
//        setCountDownTimer();
//    }


    void uploadScores () {
        DatabaseReference scoresRef = mFirebaseDatabase.getReference("scores");
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        final String userUid = firebaseAuth.getCurrentUser().getUid();
        Log.d(TAG + " INFO", "uid = " + userUid + " pid = " + firebaseAuth.getCurrentUser().getProviderId());
        String newScoreKey = scoresRef.child(userUid).push().getKey();
        final Score newScore = new Score(newScoreKey, userEmail, "DEFAULT", score*5);
        scoresRef.child(userUid).child(newScoreKey).setValue(newScore).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, task.getException().getMessage());
                } else {
                    final DatabaseReference userRef = mFirebaseDatabase.getReference(DbPaths.BASE_PATH_USERS );
                    userRef.orderByChild("id").equalTo(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        User_ user;
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();

                            for (DataSnapshot item : iterable) {
                                String key = dataSnapshot.getKey();
                                Log.d(TAG + " key " + key, item.getValue().toString());
                                user = item.getValue(User_.class);
                                user.setEnergy(user.getEnergy()+ newScore.getEnergyGained());
                                user.setReputation(user.getReputation()+ newScore.getReputationGained());
                                user.setDiamonds(user.getDiamonds()+ newScore.getDiamondsGained());
                                if (newScore.getScore() > user.getHighestScore())
                                    user.setHighestScore(newScore.getScore());
                                userRef.child(userUid).setValue(user);
                                Log.d(TAG, item.toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //TODO change user id to uid value
                }
            }
        });
    }



    void queryQuestions(int maxCount) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mQstRef = mFirebaseDatabase.getReference(DbPaths.BASE_PATH_QUESTIONS);
        mQstRef.keepSynced(true);

        //To be corrected
        //Should query randomly
        Query query = mQstRef.orderByValue().limitToFirst(maxCount);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Question_ newQst = next.getValue(Question_.class);
                    mDataSet.add(newQst);
                    Log.d(TAG + " val", next.getValue().toString());
                }
                Log.d(TAG, mDataSet.size() + " size");

                progressBar.setVisibility(View.INVISIBLE);
                gameLinearLayout.setVisibility(View.VISIBLE);

                //decrement energy level
                decrementEnergyLevel();

                //start the game
                updateQuestionView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Cancelled " + databaseError.getDetails());
            }
        });
    }


    private void decrementEnergyLevel () {
        final String userUid = firebaseAuth.getCurrentUser().getUid();
        final DatabaseReference userRef = mFirebaseDatabase.getReference(DbPaths.BASE_PATH_USERS );
        userRef.orderByChild("id").equalTo(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
        User_ user;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();

                for (DataSnapshot item : iterable) {
                    String key = dataSnapshot.getKey();
                    Log.d(TAG + " key " + key, item.getValue().toString());
                    user = item.getValue(User_.class);
                    user.setEnergy(user.getEnergy()-3);
                    userRef.child(userUid).setValue(user);
                    Log.d(TAG, item.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getDetails());
            }
        });
    }



    private void btnAOnClick () {
        btnOptA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableOptions();
                boolean isCorrect = false;
                if (btnOptA.getText().equals(mCurrentQuestion.getAnsA())) {
                    Toast.makeText(GameActivity.this, "Bonne Réponse! :)", Toast.LENGTH_SHORT).show();
                    isCorrect = true;
                    incrementScore();
                    btnOptA.setBackgroundColor(Color.GREEN);
                    btnOptA.setTextColor(Color.WHITE);
                } else {
                    Toast.makeText(GameActivity.this, "Mauvaise Réponse! :(", Toast.LENGTH_SHORT).show();
                    isCorrect = false;
                    btnOptA.setBackgroundColor(Color.RED);
                    btnOptA.setTextColor(Color.WHITE);
                }
                //cancel the countdown  and start over again
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                }
                //updateQuestionView();
                //set2SecondsTimer();
                showCorrectDialog(isCorrect);
            }
        });
    }

    private void btnBOnClick () {
        btnOptB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableOptions();
                boolean isCorrect = false;
                if (btnOptB.getText().equals(mCurrentQuestion.getAnsA())) {
                    Toast.makeText(GameActivity.this, "Bonne Réponse! :)", Toast.LENGTH_SHORT).show();
                    isCorrect = true;
                    incrementScore();
                    btnOptB.setBackgroundColor(Color.GREEN);
                    btnOptB.setTextColor(Color.WHITE);
                } else {
                    Toast.makeText(GameActivity.this, "Mauvaise Réponse! :(", Toast.LENGTH_SHORT).show();
                    isCorrect = false;
                    btnOptB.setBackgroundColor(Color.RED);
                    btnOptB.setTextColor(Color.WHITE);
                }
                //cancel the countdown  and start over again
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                }
                //updateQuestionView();
                //set2SecondsTimer();
                showCorrectDialog(isCorrect);
            }
        });
    }

    private void btnCOnClick () {
        btnOptC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableOptions();
                boolean isCorrect = false;
                if (btnOptC.getText().equals(mCurrentQuestion.getAnsA())) {
                    Toast.makeText(GameActivity.this, "Bonne Réponse! :)", Toast.LENGTH_SHORT).show();
                    isCorrect = true;
                    incrementScore();
                    btnOptC.setBackgroundColor(Color.GREEN);
                    btnOptC.setTextColor(Color.WHITE);
                } else {
                    Toast.makeText(GameActivity.this, "Mauvaise Réponse! :(", Toast.LENGTH_SHORT).show();
                    isCorrect = false;
                    btnOptC.setBackgroundColor(Color.RED);
                    btnOptC.setTextColor(Color.WHITE);
                }
                //cancel the countdown  and start over again
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                }
                //updateQuestionView();
                //set2SecondsTimer();
                showCorrectDialog(isCorrect);
            }
        });
    }

    private void btnDOnClick () {
        btnOptD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableOptions();
                boolean isCorrect = false;
                if (btnOptD.getText().equals(mCurrentQuestion.getAnsA())) {
                    Toast.makeText(GameActivity.this, "Bonne Réponse! :)", Toast.LENGTH_SHORT).show();
                    isCorrect = true;
                    incrementScore();
                    btnOptD.setBackgroundColor(Color.GREEN);
                    btnOptD.setTextColor(Color.WHITE);
                } else {
                    Toast.makeText(GameActivity.this, "Mauvaise Réponse! :(", Toast.LENGTH_SHORT).show();
                    isCorrect = false;
                    btnOptD.setBackgroundColor(Color.RED);
                    btnOptD.setTextColor(Color.WHITE);
                }
                //cancel the countdown  and start over again
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                }
                //updateQuestionView();
                //set2SecondsTimer();
                showCorrectDialog(isCorrect);
            }
        });
    }

    private void setCountDownTimer () {
        mCountDownTimer = new CountDownTimer(MAX_TIME_IN_MILLIS, 50) {
            @Override
            public void onTick(long l) {
                //if (l%1000 == 0)
                    tvTimer.setText("");//there is extra 2s to show the state of the choices
//                if (l == 15000) {
//                    disableOptions();
//                }
                long percent = l * 100 / MAX_TIME_IN_MILLIS;
                pbCounter.setProgress((int)percent);

            }

            @Override
            public void onFinish() {
                Toast.makeText(GameActivity.this, "Temps écoulé!", Toast.LENGTH_SHORT).show();
                disableOptions();
                updateQuestionView();
            }
        }.start();
    }

    private void incrementScore() {
        tvScore.setText(++score + "/" + MAX_QUESTIONS);
    }

    private void disableOptions() {
        btnOptA.setEnabled(false);
        btnOptB.setEnabled(false);
        btnOptC.setEnabled(false);
        btnOptD.setEnabled(false);
    }

    private void enableOptions() {
        btnOptA.setEnabled(true);
        btnOptB.setEnabled(true);
        btnOptC.setEnabled(true);
        btnOptD.setEnabled(true);

//        btnOptA.setBackgroundColor(Color.TRANSPARENT);
//        btnOptB.setBackgroundColor(Color.TRANSPARENT);
//        btnOptC.setBackgroundColor(Color.TRANSPARENT);
//        btnOptD.setBackgroundColor(Color.TRANSPARENT);
        btnOptA.setBackgroundResource(R.drawable.btn_default);
        btnOptB.setBackgroundResource(R.drawable.btn_default);
        btnOptC.setBackgroundResource(R.drawable.btn_default);
        btnOptD.setBackgroundResource(R.drawable.btn_default);
    }


    public void showCorrectDialog(boolean isCorrect) {
        final Dialog dialog = new Dialog(GameActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5c5c5c")));
        }
        dialog.setContentView(R.layout.dialog_correct);


        dialog.setCancelable(false);

        votesRef = mFirebaseDatabase.getReference(DbPaths.BASE_PATH_VOTES);
        final String questionId = mCurrentQuestion.getId();

        Button btnContinue = (Button)dialog.findViewById(R.id.btn_continue);
        TextView tvCorrect = dialog.findViewById(R.id.tv_correct);
        final ImageButton btnLike = dialog.findViewById(R.id.btn_like);
        final ImageButton btnDislike = dialog.findViewById(R.id.btn_dislike);

        PublisherAdView dialogAdView = dialog.findViewById(R.id.ad_view);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        dialogAdView.loadAd(adRequest);

        if (isCorrect) {
            tvCorrect.setText(CORRECT);
            tvCorrect.setTextColor(Color.parseColor("#1de9b6"));
        }
        else {
            tvCorrect.setText(INCORRECT);
            tvCorrect.setTextColor(Color.parseColor("#f59d1a"));
        }

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuestionView();
                dialog.dismiss();
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLike.setEnabled(false);
                btnDislike.setEnabled(false);
                String newKey = votesRef.child(questionId).push().getKey();
                Vote_ newVote = new Vote_(firebaseAuth.getCurrentUser().getEmail(), questionId, POSITIVE_VOTE);
                votesRef.child(questionId).child(newKey).setValue(newVote).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(GameActivity.this, "Merci pour votre vote! :)", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d(TAG, task.getException().getMessage());
                            btnLike.setEnabled(true);
                            btnDislike.setEnabled(true);
                        }
                    }
                });
            }
        });


        btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLike.setEnabled(false);
                btnDislike.setEnabled(false);
                String newKey = votesRef.child(questionId).push().getKey();
                Vote_ newVote = new Vote_(firebaseAuth.getCurrentUser().getEmail(), questionId, NEGATIVE_VOTE);
                votesRef.child(questionId).child(newKey).setValue(newVote).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(GameActivity.this, "Merci pour votre vote! :)", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d(TAG, task.getException().getMessage());
                            btnLike.setEnabled(true);
                            btnDislike.setEnabled(true);
                        }
                    }
                });
            }
        });

        dialog.show();

        onPause();
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
    }

}
