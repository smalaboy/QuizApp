package com.tech.smal.turkaf.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by smoct on 25/03/2019.
 */

public class DataProvider {

    public static final String TAG = DataProvider.class.getSimpleName();

    private ArrayList<QuestionDetails> questionsSet;
    private FirebaseDatabase mDb;
    private DatabaseReference mQstRef;
    Context mContext;

    public DataProvider(Context context) {
        if (this.mDb == null)
            this.mDb = FirebaseDatabase.getInstance();
        mContext = context;

    }

    private ArrayList<QuestionDetails> queryQuestion(int maxCount) {
        questionsSet = new ArrayList<>();

        mQstRef = mDb.getReference("questions");

        //To be corrected
        //Should query randomly
        Query query = mQstRef.orderByChild("question").limitToFirst(maxCount);
        Log.d(TAG, query.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    DataSnapshot qstSnapshot = next.child("question");
                    QuestionDetails newQD = new QuestionDetails();
                    Question newQst = new Question();
                    //newQst = qstSnapshot.getValue(Question.class);
                    questionsSet.add(next.getValue(QuestionDetails.class));
                    i++;
                    Log.d(TAG, next.getValue().toString());
                }
                Log.d(TAG, i+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Cancelled " + databaseError.getDetails());
            }
        });

        return questionsSet;
    }

    public ArrayList<QuestionDetails> getQuestionsSet(int maxCount) {

        return queryQuestion(maxCount);
    }


//    public ArrayList<Question> getQuestionsOnly(int maxCount) {
//        queryQuestion(maxCount);
//        return null;
//    }

    public void pushQuestion(QuestionDetails qst) {
        mQstRef = mDb.getReference("questions");
        String key = mQstRef.push().getKey();
        qst.setId(key);
        mQstRef.child(key).setValue(qst).addOnCompleteListener(new CompletionListener(mContext));
    }

    //TODO remove this after tests
    public void pushMockData() {

        DbHelper dbHelper = new DbHelper(mContext);
        ArrayList<QuestionDetails> questionDetailsList = (ArrayList<QuestionDetails>)
                dbHelper.getUserQuestions(1);
        mQstRef = mDb.getReference("questions");

        for (QuestionDetails qD : questionDetailsList) {
            String key = mQstRef.push().getKey();
            mQstRef.child(key).setValue("");
            qD.setId(key);
            mQstRef.child(key).setValue(qD).addOnCompleteListener(new CompletionListener(mContext));
        }
    }
}




class CompletionListener implements OnCompleteListener {
    private Context mContext;

    public CompletionListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onComplete(@NonNull Task task) {
        Toast.makeText(mContext, "Ajouté avec succès!", Toast.LENGTH_SHORT).show();
    }
}
