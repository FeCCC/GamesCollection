package com.example.gamescollection.sudoku;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import com.example.gamescollection.sudoku.DensityUtils;

public class Grid extends RelativeLayout {
    private int TEXT_SIZE = DensityUtils.dp2px(getContext(), 40);
    // 定义3*3的cell数组
    private List<List<TextView>> mTextArrays;

    public Grid(Context context) {
        this(context, null);
    }

    public Grid(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Grid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // 初始化代码，初始化cell
    private void init(Context context) {
        mTextArrays = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            List<TextView> viewList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                TextView textView = new TextView(context);
                textView.setWidth(TEXT_SIZE);
                textView.setHeight(TEXT_SIZE);
                textView.setBackgroundColor(Color.rgb(206,147,216));
                textView.setId(View.generateViewId());
                textView.setGravity(Gravity.CENTER);
                addView(textView);
                viewList.add(textView);
                LayoutParams params = (LayoutParams) textView.getLayoutParams();
                if (j == 0) {
                    if (i == 0) {
                        params.addRule(RelativeLayout.ALIGN_PARENT_START);
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    } else if (i == 1) {
                        params.addRule(RelativeLayout.BELOW, mTextArrays.get(0).get(0).getId());
                        params.topMargin = DensityUtils.dp2px(context,1);
                    } else {
                        params.addRule(RelativeLayout.BELOW, mTextArrays.get(1).get(0).getId());
                        params.topMargin = DensityUtils.dp2px(context,1);
                    }
                } else if (j == 1) {
                    params.addRule(RelativeLayout.RIGHT_OF, viewList.get(j - 1).getId());
                    params.addRule(ALIGN_TOP, viewList.get(j - 1).getId());
                    params.leftMargin = DensityUtils.dp2px(context,1);
                } else {
                    params.addRule(RelativeLayout.RIGHT_OF, viewList.get(j - 1).getId());
                    params.addRule(ALIGN_TOP, viewList.get(j - 1).getId());
                    params.leftMargin = DensityUtils.dp2px(context,1);
                }
            }
            mTextArrays.add(viewList);
        }
        setBackgroundColor(Color.rgb(134,218,207));
    }

    // 测量控件的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = DensityUtils.dp2px(getContext(), 6 + 3 * 40);
        super.onMeasure(size, size);
    }

    public List<List<TextView>> getTextArrays() {
        return this.mTextArrays;
    }
}
