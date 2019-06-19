package com.qiqia.duosheng.main.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.main.bean.MenuItem;

public class MainMenuAdapter extends BaseQuickAdapter<MenuItem, BaseViewHolder> {
    public MainMenuAdapter() {
        super(R.layout.item_main_menu);
    }

    @Override
    protected void convert(BaseViewHolder helper, MenuItem item) {
        helper.setText(R.id.tv_name,item.getTitle()).setImageResource(R.id.iv_icon,item.getImgRes());
    }
}
