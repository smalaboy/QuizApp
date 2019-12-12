package com.tech.smal.turkaf;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by smoct on 27/03/2019.
 */

public class FirebaseHandler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //enable offline data persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
