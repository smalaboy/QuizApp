package com.tech.smal.turkaf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.tech.smal.turkaf.data.models.Score;

public class ResultsActivity extends AppCompatActivity {

    private TextView tvMsg;
    private TextView tvScore;
    private TextView tvCorrect;
    private TextView tvWrong;
    private TextView tvHighestScore;
    private PublisherAdView adView;
    private Button btnReplay;
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        tvMsg = findViewById(R.id.tv_message);
        tvScore = findViewById(R.id.tv_score);
        tvHighestScore = findViewById(R.id.tv_highest_score);
        tvCorrect = findViewById(R.id.tv_right);
        tvWrong = findViewById(R.id.tv_wrong);
        adView = findViewById(R.id.ad_view);
        btnHome = findViewById(R.id.btn_home);
        btnReplay = findViewById(R.id.btn_replay);

        //loading ad
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        adView.loadAd(adRequest);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), HomeActivity.class));
                finish();
            }
        });

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), GameActivity.class));
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Score score = new Score() ;
        int questionsCount = 0;
        if (intent.hasExtra("SCORE")) {
            score = (Score) intent.getSerializableExtra("SCORE");
        }
        if (intent.hasExtra("QUESTIONS_COUNT")) {
            questionsCount = intent.getIntExtra("QUESTIONS_COUNT", 0);
        }
        tvScore.setText(score.getScore() + " pts");
        tvCorrect.setText(score.getScore()/5+"");
        tvWrong.setText((questionsCount-score.getScore()/5)+"");
    }
}
