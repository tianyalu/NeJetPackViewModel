package com.sty.ne.jetpack.viewmodel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class Main2Activity extends AppCompatActivity {
    private Button btn1;
    private Button btn3;
    private TextView tvResult;
    private Main2ViewModel main2ViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();
        initListeners();
    }

    private void initView() {
        btn1 = findViewById(R.id.btn1);
        btn3 = findViewById(R.id.btn3);
        tvResult = findViewById(R.id.tv_result);

        //不能这样初始化ViewModel，这样的话，就没有任何ViewModel的功能，就和普通的类没有什么区别了
        //main2ViewModel = new main2ViewModel();
        main2ViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(Main2ViewModel.class);

        //横竖屏切换时会重新执行生命周期函数，这里可以保证切换时数据不丢失
        //通过LiveData的感应去更新UI
        main2ViewModel.getLiveDataNumber().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvResult.setText(String.valueOf(integer));
            }
        });
    }

    private void initListeners() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main2ViewModel.addNumber(1);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main2ViewModel.addNumber(3);
            }
        });
    }
}