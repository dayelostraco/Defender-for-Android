package com.sparcedge.android.defender.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.sparcedge.android.defender.R;
import com.sparcedge.android.defender.model.Defender;

/**
 * User: Dayel Ostraco
 * Date: 12/17/12
 * Time: 4:03 PM
 */
public class ConnectDefenderActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect);

        Intent intent = getIntent();
        Defender defender = (Defender) intent.getSerializableExtra("defender");
        System.out.println("defender = " + defender);

    }
}