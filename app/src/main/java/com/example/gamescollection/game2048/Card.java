package com.example.gamescollection.game2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Card extends FrameLayout {
    public Card(Context context) {
        super(context);

        label = new TextView(getContext());
        label.setTextSize(32);
        label.setGravity(Gravity.CENTER);
//        label.setBackgroundColor(0xffEEE4DA);

        LayoutParams lp = new LayoutParams(-1,-1);
        lp.setMargins(10,10,0,0);
        addView(label,lp);

        setNum(0);
    }

    public int getColor(int num){
        int color;
        switch (num){
            case 0:
                color = 0xffCCC0B3;
                break;
            case 2:
                color = 0xffEEE4DA;
                break;
            case 4:
                color = 0xffEDE0C8;
                break;
            case 8:
                color = 0xffF2B179;// 0xffF2B179
                break;
            case 16:
                color = 0xffF49563;
                break;
            case 32:
                color = 0xffF5794D;
                break;
            case 64:
                color = 0xffF55D37;
                break;
            case 128:
                color = 0xffEEE863;
                break;
            case 256:
                color = 0xffEDB04D;
                break;
            case 512:
                color = 0xffECB04D;
                break;
            case 1024:
                color = 0xffEB9437;
                break;
            case 2048:
                color = 0xffEA7821;
                break;
            default:
                color = 0xffEA7821;
                break;
        }
        return color;
    }

    private int num = 0;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if(num<=0){
            label.setText("");
        }else {
            label.setText(num+"");
        }
        label.setBackgroundColor(getColor(num));
    }

    public boolean equals(Card o){
        return getNum()==o.getNum();
    }

    private TextView label;
}
