package com.qiqia.duosheng.inject;

import com.qiqia.duosheng.base.BaseActivity;
import com.qiqia.duosheng.base.BaseFragment;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = NetWorkMoudle.class)
public interface CommonComponent {
    void inject(BaseFragment baseFragment);
    void inject(BaseActivity baseActivity);
}
