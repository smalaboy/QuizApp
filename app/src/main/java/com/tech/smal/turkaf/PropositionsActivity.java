package com.tech.smal.turkaf;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tech.smal.turkaf.adapters.PropListAdapter;
import com.tech.smal.turkaf.data.DbHelper;
import com.tech.smal.turkaf.data.Grade;
import com.tech.smal.turkaf.data.Question;
import com.tech.smal.turkaf.data.QuestionDetails;
import com.tech.smal.turkaf.data.User;
import com.tech.smal.turkaf.data.models.DbPaths;
import com.tech.smal.turkaf.data.models.Question_;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PropositionsActivity extends AppCompatActivity {

    public final static String TAG = PropositionsActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public static PropListAdapter mAdapter;
    private ProgressBar progressBarLoader;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private ArrayList<Question_> mDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propositions);

        mockData();

        progressBarLoader = (ProgressBar)findViewById(R.id.pb_loader);

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_prop_list);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new PropListAdapter(this, new ArrayList<Question_>());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setListener(new PropListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getApplication(), QuestionDetailsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, mAdapter.getItem(position));
                intent.putExtra("POSITION", position);
                //b.putSerializable(Intent.EXTRA_TEXT, mAdapter.getItem(position));
                startActivity(intent /*,b*/);
            }
        });

        //TODO get the user's id first
        //new QuestionsAsynctask().execute("1");

        queryQuestions();

        progressBarLoader.setVisibility(View.VISIBLE);
    }





    void queryQuestions() {
        mDataset = new ArrayList<>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference(DbPaths.BASE_PATH_QUESTIONS );
        mRef.keepSynced(true);

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        //To be corrected
        //Should query randomly
        Query query = mRef.orderByChild("userId").equalTo(userEmail);
        //Query query = mRef.orderByChild("question").equalTo(1, "question/user/id");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    ///DataSnapshot qstSnapshot = next.child("question");
                    Question_ newQst = next.getValue(Question_.class);
                    //newQst = qstSnapshot.getValue(Question.class);
                    mDataset.add(newQst);
                    Log.d(TAG + " val", next.getValue().toString());
                }
                Log.d(TAG, mDataset.size() + " size");

                progressBarLoader.setVisibility(View.INVISIBLE);
                mAdapter.setData(mDataset);
                Log.d(TAG, "AfterBACKGROUNDAdapter " + mAdapter.getItemCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Cancelled " + databaseError.getDetails());
            }
        });
    }





//    void queryQuestions() {
//        mDataset = new ArrayList<>();
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mRef = mFirebaseDatabase.getReference("questions");
//        mRef.keepSynced(true);
//        //To be corrected
//        //Should query randomly
//        Query query = mRef.orderByChild("user/id").equalTo(1);
//        //Query query = mRef.orderByChild("question").equalTo(1, "question/user/id");
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
//                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
//                while (iterator.hasNext()) {
//                    DataSnapshot next = iterator.next();
//                    DataSnapshot qstSnapshot = next.child("question");
//                    QuestionDetails newQD = next.getValue(QuestionDetails.class);
//                    //newQst = qstSnapshot.getValue(Question.class);
//                    mDataset.add(newQD);
//                    Log.d(TAG + " val", next.getValue().toString());
//                }
//                Log.d(TAG, mDataset.size() + " size");
//
//                progressBarLoader.setVisibility(View.INVISIBLE);
//                mAdapter.setData(mDataset);
//                Log.d(TAG, "AfterBACKGROUNDAdapter " + mAdapter.getItemCount());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d(TAG, "Cancelled " + databaseError.getDetails());
//            }
//        });
//    }



    private ArrayList<QuestionDetails> mockData() {
        Question q = new Question("A, B, C, ou D?", "A", "B", "C", "D", "C");
        User u = new User(1,"DEFAULT", "smal.tech");
        Grade g1 = new Grade(2, 1, 4);
        Grade g2 = new Grade(3, 1, 5);
        Grade g3 = new Grade(4, 1, 3);
        ArrayList<Grade> gs = new ArrayList<>();
        gs.add(g1);
        gs.add(g2);
        gs.add(g3);
        QuestionDetails qd = new QuestionDetails(q,u, gs);


        q = new Question("A, B, C, ou D2?", "A", "B", "C", "D", "C");
        u = new User(1,"DEFAULT", "smal.tech");
        g1 = new Grade(5, 1, 4);
        g2 = new Grade(6, 1, 5);
        g3 = new Grade(7, 1, 3);
        ArrayList<Grade> gs1 = new ArrayList<>();
        gs1.add(g1);
        gs1.add(g2);
        gs1.add(g3);
        QuestionDetails qd1 = new QuestionDetails(q,u, gs);



        q = new Question("A, B, C, ou D3?", "A", "B", "C", "D", "C");
        u = new User(1,"DEFAULT", "smal.tech");
        g1 = new Grade(8, 1, 4);
        g2 = new Grade(9, 1, 5);
        g3 = new Grade(10, 1, 3);
        ArrayList<Grade> gs2 = new ArrayList<>();
        gs1.add(g1);
        gs1.add(g2);
        gs1.add(g3);
        QuestionDetails qd2 = new QuestionDetails(q,u, gs);


        ArrayList<QuestionDetails> list = new ArrayList<>();
        list.add(qd);
        list.add(qd1);
        list.add(qd2);

        return list;
    }

//    class QuestionsAsynctask extends AsyncTask<String, Void, List> {
//        DbHelper db = new DbHelper(getApplication());
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressBarLoader.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected List doInBackground(String... params) {
//            Log.d(TAG, "InBckground " + params[0]);
//            ArrayList<QuestionDetails> list = (ArrayList<QuestionDetails>) db.getUserQuestions(Integer.parseInt(params[0]));
//            return list;
//        }
//
//        @Override
//        protected void onPostExecute(List list) {
//            super.onPostExecute(list);
//            Log.d(TAG, "AfterBACKGROUND " + list.size());
//            progressBarLoader.setVisibility(View.INVISIBLE);
//            mAdapter.setData((ArrayList<QuestionDetails>) list);
//            Log.d(TAG, "AfterBACKGROUNDAdapter " + mAdapter.getItemCount());
//        }
//    }
}
