package com.tech.smal.turkaf;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tech.smal.turkaf.data.models.User_;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private Button btnEnergy;
    private Button btnDiamonds;
    private Button btnReputation;
    private Button btnNewGame;
    private Button btnFactory;
    private Button btnProfile;
    private RecyclerView rvInvitations;
    private RecyclerView rvScores;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    User_ currentUser;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
    }


    private void showNewGameDialog() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setTitle("Nouveau jeu");
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }

        dialog.setContentView(R.layout.dialog_new_game);
        dialog.setCancelable(true);

        ImageButton btnSimpleGame = dialog.findViewById(R.id.btn_simple_game);
        ImageButton btnRandomOpponent = dialog.findViewById(R.id.btn_random_opponent);
        ImageButton btnFriend = dialog.findViewById(R.id.btn_friend);
        ImageButton btnAClock = dialog.findViewById(R.id.btn_against_the_clock);

        btnSimpleGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, GameActivity.class));
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef.orderByChild("email").equalTo(firebaseUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();

                for (DataSnapshot item : iterable) {
                    String key = dataSnapshot.getKey();
                    Log.d(TAG + " key " + key, item.getValue().toString());
                    currentUser = item.getValue(User_.class);
                    Log.d(TAG, currentUser.toString());
                    btnEnergy.setText(currentUser.getEnergy()+"");
                    btnDiamonds.setText(currentUser.getDiamonds()+"");
                    btnReputation.setText(currentUser.getReputation()+"");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getDetails());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onStop();
    }

    private void initUI() {
        btnEnergy = findViewById(R.id.btn_energy);
        btnDiamonds = findViewById(R.id.btn_diamonds);
        btnReputation = findViewById(R.id.btn_reputation);
        btnNewGame = findViewById(R.id.btn_new_game);
        btnFactory = findViewById(R.id.btn_factory);
        btnProfile = findViewById(R.id.btn_profile);

        rvInvitations = findViewById(R.id.rv_invitations);
        rvScores = findViewById(R.id.rv_scores);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("users");
        mRef.keepSynced(true);


        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser.getEnergy() < 3) {
                    Toast.makeText(HomeActivity.this, "Vous n'avez pas assez d'énergie pour continuer!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                showNewGameDialog();
            }
        });

        btnFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, FactoryActivity.class));
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });
    }
}
