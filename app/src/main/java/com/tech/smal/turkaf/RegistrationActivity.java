package com.tech.smal.turkaf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.tech.smal.turkaf.data.Grade;
import com.tech.smal.turkaf.data.Question;
import com.tech.smal.turkaf.data.QuestionDetails;
import com.tech.smal.turkaf.data.User;
import com.tech.smal.turkaf.data.models.User_;

import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity {

    public final static String TAG = RegistrationActivity.class.getSimpleName();

    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    EditText textEmail;
    EditText textPassword;
    EditText textDisplayName;
    EditText textRepassword;
    Button btnSignup;
    Button btnGotoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        textEmail = (EditText)findViewById(R.id.text_email);
        textPassword = (EditText)findViewById(R.id.text_password);
        textDisplayName = (EditText)findViewById(R.id.text_display_name);
        textRepassword = (EditText)findViewById(R.id.text_repassword);
        btnSignup = (Button)findViewById(R.id.btn_signup);
        btnGotoLogin = (Button)findViewById(R.id.btn_goto_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        if (mFirebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = textEmail.getText().toString().trim();
                String password = textPassword.getText().toString().trim();
                final String displayName = textDisplayName.getText().toString().trim();
                String repassword = textRepassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Champs manquant", Toast.LENGTH_SHORT).show();
                    return;
                }

                //User creation
                mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener
                        (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mRef = mFirebaseDatabase.getReference("users");
                            //String key = mRef.push().getKey();
                            String key = mFirebaseAuth.getCurrentUser().getUid();
                            User_ user = new User_(key, displayName, mFirebaseAuth.getCurrentUser().getEmail(),
                                    30, 10, 10);
                            mRef.child(key).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegistrationActivity.this, "Ajouté avec succès!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Log.d(TAG, task.getException().getMessage());
                                    }
                                }
                            });

                        }
                        else {
                            Toast.makeText(RegistrationActivity.this, "Une erreur est survenue! :(", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                });
            }
        });

        btnGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

                /*Question qst = new Question("De quelle couleur est l'étoile au centre du drapeau du Burkina Faso?",
                        "Rouge", "Jaune", "Vert", "Blanc", "Jaune");
                User user = new User(1,"Moctar", "smal@tech.boss");
                Rating g1 = new Rating(5, 1, 1);
                Rating g2 = new Rating(6, 1, 4);
                Rating g3 = new Rating(3, 1, 2);
                Rating g4 = new Rating(4, 1, 1);

                ArrayList<Rating> gradeArrayList = new ArrayList<>();
                gradeArrayList.add(g1);
                gradeArrayList.add(g2);
                gradeArrayList.add(g3);
                gradeArrayList.add(g4);

                QuestionDetails qD = new QuestionDetails(qst, user, gradeArrayList);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions");
                String key = ref.push().getKey();
                qD.setId(key);
                ref.child(key).setValue(qD).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(RegistrationActivity.this, "Successfull!", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("RegistrationActivity", task.getException().getMessage());
                            Log.d("RegistrationActivity", task.getException().getStackTrace()+"");
                        }
                    }
                });*/
            }
        });
    }
}
