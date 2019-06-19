package com.qiqia.duosheng.base;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiqia.duosheng.R;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.SoftInputUtils;

/**
 * 必须引入此头部
 * <include layout="@layout/action_common_bar"/>或者
 *   <include layout="@layout/action_black_common_bar"/>
 */
public class BaseBarActivity extends BaseActivity {
    @BindView(R.id.ele_bar)
    protected View eleBar;
    @BindView(R.id.iv_back)
    protected ImageView ivBack;
    @BindView(R.id.tv_left)
    protected TextView tvLeft;
    @BindView(R.id.tv_title)
    protected TextView tvTitle;
    @BindView(R.id.tv_right)
    protected TextView tvRight;
    @BindView(R.id.iv_right)
    protected ImageView ivRight;


    @OnClick({R.id.iv_back})
    public void doClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                SoftInputUtils.closeSoftInput(this);
                finish();
                break;
        }
    }
}
