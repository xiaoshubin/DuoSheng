package com.qiqia.duosheng.custom;

import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.RadioGroup;

import com.lxj.xpopup.impl.PartShadowPopupView;
import com.qiqia.duosheng.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 综合排序弹出窗口
 */
public class ComprehensivePopWindow extends PartShadowPopupView {


    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    private OnComListener listener;

    interface OnComListener {
        void onItemChecked(int position);
    }

    public void setListener(OnComListener listener) {
        this.listener = listener;
    }

    public ComprehensivePopWindow(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_comprehensive;
    }

    @Override
    protected void onCreate() {
        ButterKnife.bind(this);
        super.onCreate();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (listener!=null)
                switch (checkedId){
                    case R.id.rb1:
                        listener.onItemChecked(0);
                        break;
                    case R.id.rb2:
                        listener.onItemChecked(1);
                        break;
                    case R.id.rb3:
                        listener.onItemChecked(2);
                        break;
                }
                ComprehensivePopWindow.this.dismiss();

            }
        });

    }


}
