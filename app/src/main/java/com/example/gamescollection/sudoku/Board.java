package com.example.gamescollection.sudoku;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gamescollection.R;
import com.example.gamescollection.sudoku.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class Board extends RelativeLayout implements View.OnClickListener {
    private final String TAG = Board.class.getSimpleName();

    private List<List<Grid>> mGridArray = new ArrayList<>(); //3*3 的Grid
    private List<List<TextView>> mCellArray; //9*9的Cell
    private TextView mCurrentCell; //当前选中的Cell
    private String mErrorTextColor = "#ff0000"; // 错误时候的文字颜色
    private String mLightTextColor = "#ffffff"; // 选中时候的文字颜色
    private String mDefaultTextColor = "#000000";   // 默认文字颜色
    private String mLightBgColor = "#4fe8fc";   // 选中的背景颜色
    private String mDefaultBgColor = "#ffffff"; // 默认背景颜色
    private String mDisableTextColor = "#e2e2e2";// 不可编辑的文字颜色

    private GameOverCallBack mGameOverCallBack;

    public Board(Context context) {
        this(context, null);
    }

    public Board(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Board(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    // 初始化Grid
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        int padding = DensityUtils.dp2px(context, 1);
        setPadding(padding, padding, padding, padding);
        setBackgroundColor(Color.BLACK);
        for (int i = 0; i < 3; i++) {
            List<Grid> gridList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Grid grid = new Grid(context, attrs, defStyleAttr);
                grid.setId(View.generateViewId());
                addView(grid);
                LayoutParams params = (LayoutParams) grid.getLayoutParams();
                if (j == 0) {
                    if (i == 0) {
                        params.addRule(RelativeLayout.ALIGN_PARENT_START);
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    } else if (i == 1) {
                        params.addRule(RelativeLayout.BELOW, mGridArray.get(0).get(0).getId());
                        params.topMargin = DensityUtils.dp2px(context, 1);
                    } else {
                        params.addRule(RelativeLayout.BELOW, mGridArray.get(1).get(0).getId());
                        params.topMargin = DensityUtils.dp2px(context, 1);
                    }
                } else if (j == 1) {
                    params.addRule(RelativeLayout.RIGHT_OF, gridList.get(j - 1).getId());
                    params.addRule(ALIGN_TOP, gridList.get(j - 1).getId());
                    params.leftMargin = DensityUtils.dp2px(context, 1);
                } else {
                    params.addRule(RelativeLayout.RIGHT_OF, gridList.get(j - 1).getId());
                    params.addRule(ALIGN_TOP, gridList.get(j - 1).getId());
                    params.leftMargin = DensityUtils.dp2px(context, 1);
                }
                gridList.add(grid);
            }
            mGridArray.add(gridList);
        }

        mCellArray = new ArrayList<>();
        for (int i = 0; i < 9; i++) {// 初始化Cell Array
            List<TextView> cellArray = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                int x = i < 3 ? 0 : i < 6 ? 1 : 2; // 3*3 的格子
                int y = j < 3 ? 0 : j < 6 ? 1 : 2;
                Grid grid = mGridArray.get(x).get(y);
                List<List<TextView>> gridTextArrays = grid.getTextArrays();
                TextView cell = gridTextArrays.get(i - x * 3).get(j - y * 3);
                cell.setTag(R.id.row, i);
                cell.setTag(R.id.column, j);
                cell.setTag(R.id.isLoad, false);
                cell.setTextColor(Color.parseColor(mDefaultTextColor));
                cell.setBackgroundColor(Color.parseColor(mDefaultBgColor));
                cell.setOnClickListener(this);
                cellArray.add(j, cell);
            }
            mCellArray.add(i, cellArray);
        }
    }

    public void setGameOverCallBack(GameOverCallBack mGameOverCallBack) {
        this.mGameOverCallBack = mGameOverCallBack;
    }

    @Override
    public void onClick(View v) {
        mCurrentCell = (TextView) v;
        lightUpCellByRowAndColumn((int) v.getTag(R.id.row), (int) v.getTag(R.id.column));
    }

    // 输入1-9
    public void inputText(String number) {
        if (mCurrentCell == null) return;
        if (!(Boolean) mCurrentCell.getTag(R.id.isLoad)) {
            mCurrentCell.setText(number);
            boolean gameOver = checkFinish();
            if (gameOver) {
                if (mGameOverCallBack != null) mGameOverCallBack.gameOver();
            }
        }
    }

    // 加载地图
    public void loadMap(String map) {
        if (TextUtils.isEmpty(map)) return;
        for (int i = 0; i < mCellArray.size(); i++) {
            List<TextView> array = mCellArray.get(i);
            for (int j = 0; j < array.size(); j++) {
                TextView cell = array.get(j);
                String s = map.substring(9 * i + j, 9 * i + j + 1);
                if (!"0".equals(s)) {
                    cell.setText(s);
                    cell.setTag(R.id.isLoad, true);
                    cell.setTextColor(Color.parseColor(mDisableTextColor));
                }
            }
        }
    }

    // 检查游戏是否完成
    private boolean checkFinish() {
        boolean finish = false;
        int row = (int) mCurrentCell.getTag(R.id.row);
        int column = (int) mCurrentCell.getTag(R.id.column);
        boolean error = checkGameError(row, column);
        if (error) {
            lightSameNumber(row, column, true);
        } else {
            finish = true;
            lightSameNumber(row, column, false);
            for (int i = 0; i < mCellArray.size(); i++) {
                List<TextView> array = mCellArray.get(i);
                for (int j = 0; j < array.size(); j++) {
                    TextView textView = array.get(j);
                    if (TextUtils.isEmpty(textView.getText().toString())) {
                        finish = false;
                        break;
                    }
                }
            }
        }
        return finish;
    }

    // 检查游戏错误
    private boolean checkGameError(int row, int column) {
        boolean result = false;
        result = checkSection(row, column);
        if (result) return result;
        //check row
        for (int i = 0; i < 9; i++) {
            String value = mCellArray.get(i).get(column).getText().toString();
            if (TextUtils.isEmpty(value)) continue;
            for (int j = i; j < 9; j++) {
                if (i == j) continue;
                if (value.equals(mCellArray.get(j).get(column).getText().toString())) {
                    Log.d(TAG, String.format("row error,value:%1$s in row:%2$d and column:%3$d", value, row, column));
                    result = true;
                    break;
                }
            }
        }

        if (result) return result;

        //check column
        for (int i = 0; i < 9; i++) {
            String value = mCellArray.get(row).get(i).getText().toString();
            if (TextUtils.isEmpty(value)) continue;
            for (int j = i; j < 9; j++) {
                if (i == j) continue;
                if (value.equals(mCellArray.get(row).get(j).getText().toString())) {
                    Log.d(TAG, String.format("column error,value:%1$s in row:%2$d and column:%3$d", value, row, column));
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    // 检查3*3格子中是否重复
    private boolean checkSection(int row, int column) {
        boolean result = false;
        String value = mCellArray.get(row).get(column).getText().toString();
        if (TextUtils.isEmpty(value)) {
            return result;
        }
        int start_i = row < 3 ? 0 : (row < 6 ? 3 : 6);//3x3 格子的边界
        int start_j = column < 3 ? 0 : (column < 6 ? 3 : 6);
        int end_i = start_i + 3;
        int end_j = start_j + 3;

        for (int i = start_i; i < end_i; i++) {
            for (int j = start_j; j < end_j; j++) {
                if (i == row && j == column) continue;
                if (value.equals(mCellArray.get(i).get(j).getText().toString())) {//如果3x3格子的内容有重复的数字则返回错误
                    Log.d(TAG, String.format("section error,value:%1$s in row:%2$d and column:%3$d", value, row, column));
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    // 按行和列突出网格
    private void lightUpCellByRowAndColumn(int row, int column) {
        boolean lightText = !TextUtils.isEmpty(mCellArray.get(row).get(column).getText().toString());
        if (lightText) {
            lightSameNumber(row, column, false);
        } else {
            lightRowAndColumn(row, column);
        }
    }

    // 高亮相同数字
    private void lightSameNumber(int row, int column, boolean isError) {
        String value = mCellArray.get(row).get(column).getText().toString();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (value.equals(mCellArray.get(i).get(j).getText().toString())) {
                    //change number color
                    if (i == row && column == j) {
                        //If it is wrong, change the text color without changing the background.
                        if (isError || mCellArray.get(i).get(j).getCurrentTextColor() == Color.parseColor(mErrorTextColor)) {
                            mCellArray.get(i).get(j).setBackgroundColor(Color.parseColor(mErrorTextColor));
                            mCellArray.get(i).get(j).setTextColor(Color.parseColor(mLightTextColor));
                        } else {
                            mCellArray.get(i).get(j).setBackgroundColor(Color.parseColor(mLightBgColor));
                            mCellArray.get(i).get(j).setTextColor(Color.parseColor(mLightTextColor));
                        }
                    } else {
                        if (mCellArray.get(i).get(j).getCurrentTextColor() == Color.parseColor(mErrorTextColor)) {
                            mCellArray.get(i).get(j).setTextColor(Color.parseColor(mErrorTextColor));
                        } else {
                            mCellArray.get(i).get(j).setTextColor(Color.parseColor(mLightTextColor));
                        }
                        mCellArray.get(i).get(j).setBackgroundColor(Color.parseColor(mLightBgColor));
                    }
                } else {
                    if ((Boolean) mCellArray.get(i).get(j).getTag(R.id.isLoad)) {
                        mCellArray.get(i).get(j).setTextColor(Color.parseColor(mDisableTextColor));
                    } else {
                        mCellArray.get(i).get(j).setTextColor(Color.parseColor(mDefaultTextColor));
                    }
                    mCellArray.get(i).get(j).setBackgroundColor(Color.parseColor(mDefaultBgColor));
                }
            }
        }
    }

    // 高亮行和列
    private void lightRowAndColumn(int row, int column) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (i == row || j == column) {
                    if (mCellArray.get(i).get(j).getCurrentTextColor() == Color.parseColor(mErrorTextColor)) {
                        mCellArray.get(i).get(j).setTextColor(Color.parseColor(mErrorTextColor));
                    }
                    mCellArray.get(i).get(j).setBackgroundColor(Color.parseColor(mLightBgColor));
                } else {
                    if ((Boolean) mCellArray.get(i).get(j).getTag(R.id.isLoad)) {
                        mCellArray.get(i).get(j).setTextColor(Color.parseColor(mDisableTextColor));
                    } else {
                        if (mCellArray.get(i).get(j).getCurrentTextColor() == Color.parseColor(mErrorTextColor)) {
                            mCellArray.get(i).get(j).setTextColor(Color.parseColor(mErrorTextColor));
                        } else {
                            mCellArray.get(i).get(j).setTextColor(Color.parseColor(mDefaultTextColor));
                        }
                    }
                    mCellArray.get(i).get(j).setBackgroundColor(Color.parseColor(mLightTextColor));
                }
            }
        }
        mCellArray.get(row).get(column).setBackgroundColor(Color.parseColor(mLightBgColor));
    }

    public interface GameOverCallBack {
        void gameOver();
    }




}
