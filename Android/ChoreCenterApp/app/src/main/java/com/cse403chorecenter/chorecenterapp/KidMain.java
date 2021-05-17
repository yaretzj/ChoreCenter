package com.cse403chorecenter.chorecenterapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;

public class KidMain extends AppCompatActivity implements View.OnClickListener {
    public CardView submit_card;
    public CardView redeem_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_main);
        submit_card = findViewById(R.id.c1);
        submit_card.setOnClickListener(this);
        redeem_card = findViewById(R.id.c2);
        redeem_card.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch(v.getId()) {
            case R.id.c1 :
                i = new Intent(this, SubmitChore.class);
                startActivity(i);
                break;
            case R.id.c2 :
                i = new Intent(this, KidNavigation.class);
                startActivity(i);
                break;
        }
    }
}