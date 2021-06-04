package com.example.gamescollection;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamescollection.sudoku.Board;

public class Sudoku extends AppCompatActivity implements View.OnClickListener {
    private Board mSudoKu;
    private String mSudoConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku_main);
        mSudoKu = findViewById(R.id.board);
        mSudoConfig = "005406000000000201007380000062700090050023804704109060823590010490867020576031948";
        mSudoKu.setGameOverCallBack(() -> {
            new AlertDialog.Builder(this)
                    .setTitle("awesome!")
                    .setMessage("Congratulations，you solve the Sudoku!")
                    .setNegativeButton("exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        });
        mSudoKu.loadMap(mSudoConfig);
    }

    @Override
    public void onClick(View v) {
        TextView number = (TextView) v;

        mSudoKu.inputText(number.getText().toString());

    }
}
