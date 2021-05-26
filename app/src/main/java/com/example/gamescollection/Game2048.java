package com.example.gamescollection;

import android.app.Activity;
import android.os.Bundle;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Game2048 extends Activity {
    public Game2048(){
        game2048 = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_2048);

        tvScore = (TextView) findViewById(R.id.tvScore);
    }

    public void clearScore(){
        score = 0;
        showScore();
    }

    public void showScore(){
        tvScore.setText(score+"");
    }

    public void addScore(int s){
        score+=s;
        showScore();
    }

    private int score = 0;

    private TextView tvScore;

    private static Game2048 game2048 = null;

    public static Game2048 getGame2048() {
        return game2048;
    }
}