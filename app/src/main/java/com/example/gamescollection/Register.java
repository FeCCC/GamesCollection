package com.example.gamescollection;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {



    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";//验证密码是否有特殊符号或长度不满6位
    private SQLiteDatabase sdb;
    private Mysqlist mys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mys = new Mysqlist(this, "zhu_c", null, 1);//使用halper创建数据库
        sdb = mys.getWritableDatabase();

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClicked();
            }
        });
    }

    public void onViewClicked() {
        EditText editText3 = findViewById(R.id.editText3);
        EditText editText4 = findViewById(R.id.editText4);
        //获得账号密码
        String name = editText3.getText().toString().trim();
        String pass = editText4.getText().toString().trim();
        if (name == null || "".equals(name) || pass == null || "".equals(pass)) {
            Toast.makeText(this, "账号与密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
            String number = editText3.getText().toString();
            String pa = editText4.getText().toString();
            boolean judge = isPassword(pa);
            if (judge == true) {
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                sdb.execSQL("insert into user_mo(name,pass)values('" + name + "','" + pass + "')");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);//启动跳转
            } else {
                Toast.makeText(this, "密码不能有特殊符号", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }
}

