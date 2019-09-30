package com.smallcake.qsh.callback;

public interface OnConfigListener {
    void ready();

    void error(String msg);
}
