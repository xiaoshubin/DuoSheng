package com.qiqia.duosheng.custom;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

public class RollTextView extends androidx.appcompat.widget.AppCompatTextView {
    public RollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean isFocused() {
        return true;
    }
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(true, direction, previouslyFocusedRect);
    }
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(true);
    }
}
