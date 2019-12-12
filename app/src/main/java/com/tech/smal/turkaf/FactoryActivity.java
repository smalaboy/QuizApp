package com.tech.smal.turkaf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.tech.smal.turkaf.data.QuestionDetails;

public class FactoryActivity extends AppCompatActivity {

    private CardView cardViewNew;
    private CardView cardViewList;
    private CardView cardViewRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory);

        cardViewNew = (CardView)findViewById(R.id.cv_new);
        cardViewList = (CardView)findViewById(R.id.cv_list);
        cardViewRate = (CardView)findViewById(R.id.cv_rate);
        cardViewNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), NewQuestionActivity.class);
                startActivity(intent);
            }
        });

        cardViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), PropositionsActivity.class);
                startActivity(intent);
            }
        });

        cardViewRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), QuestionDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}
