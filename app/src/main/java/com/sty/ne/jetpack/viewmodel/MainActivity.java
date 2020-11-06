package com.sty.ne.jetpack.viewmodel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btn1;
    private Button btn3;
    private Button btnMain2;
    private TextView tvResult;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListeners();
    }

    private void initView() {
        btn1 = findViewById(R.id.btn1);
        btn3 = findViewById(R.id.btn3);
        btnMain2 = findViewById(R.id.btn_main2);
        tvResult = findViewById(R.id.tv_result);

        //不能这样初始化ViewModel，这样的话，就没有任何ViewModel的功能，就和普通的类没有什么区别了
        //mainViewModel = new MainViewModel();
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(MainViewModel.class);

        //横竖屏切换时会重新执行生命周期函数，这里可以保证切换时数据不丢失
        tvResult.setText(String.valueOf(mainViewModel.number));

    }

    private void initListeners() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.number++;
                tvResult.setText(String.valueOf(mainViewModel.number));
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.number += 3;
                tvResult.setText(String.valueOf(mainViewModel.number));
            }
        });

        btnMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
    }
}