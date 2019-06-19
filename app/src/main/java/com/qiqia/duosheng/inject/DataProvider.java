package com.qiqia.duosheng.inject;

import com.qiqia.duosheng.impl.LoginImpl;
import com.qiqia.duosheng.impl.ShopImpl;
import com.qiqia.duosheng.impl.TbLoginImpl;
import com.qiqia.duosheng.impl.UserImpl;

import javax.inject.Inject;

/**
 * 数据提供者
 */
public class DataProvider {
   @Inject
   public DataProvider(){}
   @Inject
   public LoginImpl login;
   @Inject
   public ShopImpl shop;
   @Inject
   public TbLoginImpl tbLogin;
   @Inject
   public UserImpl user;
}
