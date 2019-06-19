package com.qiqia.duosheng.utils;

import android.app.Dialog;
import android.support.v4.widget.SwipeRefreshLayout;

import com.qiqia.duosheng.base.BaseResponse;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.ToastUtil;
import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class OnSuccessAndFailListener<T> extends DisposableObserver<T> {

    Dialog loadDialog;
    SwipeRefreshLayout refresh;
    private void showLoading(){
        if (loadDialog!=null) loadDialog.show();
        if (refresh!=null) refresh.setRefreshing(true);
    }
    private void dismissLoading(){
        if (loadDialog!=null) loadDialog.dismiss();
        if (refresh!=null) refresh.setRefreshing(false);
    }
    public OnSuccessAndFailListener() {

    }
    public OnSuccessAndFailListener(Dialog loadDialog) {
        this.loadDialog=loadDialog;
    }
    public OnSuccessAndFailListener(SwipeRefreshLayout refresh) {
        this.refresh=refresh;
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoading();
    }

    @Override
    public void onNext(T t) {
        dismissLoading();
        try {
            BaseResponse baseResponse = (BaseResponse) t;
            if (baseResponse.getStatus()==1){
                onSuccess(t);
            }else {
                onErr(baseResponse.getMsg());
            }
        } catch (Exception e) {
            onError(e);
            onErr(e.getMessage());
        }

    }


    @Override
    public void onError(Throwable e) {
        dismissLoading();
        e.printStackTrace();
        String message;
        if (e instanceof SocketTimeoutException){
            message="SocketTimeoutException:网络连接超时！";
        }else if (e instanceof ConnectException){
            message="ConnectException:网络无法连接！";
        } else if (e instanceof HttpException) {
            message="HttpException:网络中断，请检查您的网络状态！";
        } else if (e instanceof UnknownHostException) {
            message="UnknownHostException:网络错误，请检查您的网络状态！";
        }else {
            message = e.getMessage();
        }
        onErr(message);
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuccess(T t);
    //很多异常不需要反馈给用户，所以不必抽象，如果需要异常信息，重写即可
    protected  void onErr(String msg){
        ToastUtil.showShort(msg);
        L.e("网络数据异常："+msg);
    }
}
