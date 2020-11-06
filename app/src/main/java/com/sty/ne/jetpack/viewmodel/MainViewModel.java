package com.sty.ne.jetpack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 对数据进行管理
 * 放到这里的数据在进行横竖屏切换时数据不会丢失
 * @Author: tian
 * @UpdateDate: 2020/11/6 9:12 PM
 */
public class MainViewModel extends ViewModel {

    public int number = 0;

    //正规的写法
    private MutableLiveData<Integer> number2;

    //不建议的写法
    //规则：不建议把LiveData暴露出去给V层用，建议把子类MutableLiveData给暴露出去
    private LiveData<Integer> number3;

    //自定义的LiveData可以使用
    private CustomLiveData<Integer> number4;
}
