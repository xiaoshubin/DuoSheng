package com.qiqia.duosheng.utils;

import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.bean.User;

import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.SPUtils;

public class DataLocalUtils {

    public static User getUser(){
        return SPUtils.readObject(SPStr.USER);
    }
    public static void saveUser(User user){
         SPUtils.saveObject(SPStr.USER,user);
    }
    public static boolean isLogin(){
        long t1 = System.currentTimeMillis();
        User user = SPUtils.readObject(SPStr.USER);
        long t2 = System.currentTimeMillis();
        L.e("查询user耗时=="+(t2-t1));
        return user!=null;
    }
}
