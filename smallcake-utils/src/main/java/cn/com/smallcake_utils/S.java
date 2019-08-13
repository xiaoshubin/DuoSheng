package cn.com.smallcake_utils;

import com.google.android.material.snackbar.Snackbar;
import android.view.View;

/**
 * MyApplication --  cq.cake.util
 * Created by Small Cake on  2016/9/30 14:05.
 */

public class S {

    public static void showShort(View view, CharSequence message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
    public static void showLong(View view, CharSequence message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
    public static void showIndefinite(View view, CharSequence message) {
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).show();
    }

}
