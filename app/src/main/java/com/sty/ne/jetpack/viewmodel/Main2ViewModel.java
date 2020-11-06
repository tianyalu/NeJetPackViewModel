package com.sty.ne.jetpack.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 管理数据
 * @Author: tian
 * @UpdateDate: 2020/11/6 9:52 PM
 */
public class Main2ViewModel extends ViewModel {
    private MutableLiveData<Integer> liveDataNumber;

    //对外暴露的时候就判断初始化
    public MutableLiveData<Integer> getLiveDataNumber() {
        if(liveDataNumber == null) {
            liveDataNumber = new MutableLiveData<>();
            liveDataNumber.setValue(0);
        }
        return liveDataNumber;
    }

    //对外暴露的
    public void addNumber(int number) {
        liveDataNumber.setValue(liveDataNumber.getValue() + number);
    }
}
