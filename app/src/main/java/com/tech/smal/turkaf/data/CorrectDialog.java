package com.tech.smal.turkaf.data;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.tech.smal.turkaf.R;

/**
 * Created by smoct on 17/03/2019.
 */

public class CorrectDialog extends Dialog {
    private Button btnContinue;

//    public CorrectDialog(@NonNull Context context) {
//        super(context);
//    }

    public CorrectDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.setContentView(themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);

        btnContinue = (Button)findViewById(R.id.btn_continue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
