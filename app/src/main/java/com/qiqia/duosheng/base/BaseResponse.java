package com.qiqia.duosheng.base;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {
     public int status;
     public String msg;
     public T data;

     public int getStatus() {
          return status;
     }

     public void setStatus(int status) {
          this.status = status;
     }

     public String getMsg() {
          return msg;
     }

     public void setMsg(String msg) {
          this.msg = msg;
     }

     public T getData() {
          return data;
     }

     public void setData(T data) {
          this.data = data;
     }
}
