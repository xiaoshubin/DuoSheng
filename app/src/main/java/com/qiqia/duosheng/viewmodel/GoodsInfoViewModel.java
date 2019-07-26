package com.qiqia.duosheng.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.qiqia.duosheng.inject.DataProvider;

import javax.inject.Inject;

public class GoodsInfoViewModel extends AndroidViewModel {
    @Inject
    DataProvider dataProvider;

    public GoodsInfoViewModel(@NonNull Application application) {
        super(application);

    }

}
