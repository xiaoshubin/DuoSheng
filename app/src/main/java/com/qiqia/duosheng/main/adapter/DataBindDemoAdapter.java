package com.qiqia.duosheng.main.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.databinding.ItemBindingBinding;
import com.qiqia.duosheng.main.bean.MenuItem;
import com.qiqia.duosheng.utils.DataBindBaseViewHolder;

public class DataBindDemoAdapter extends BaseQuickAdapter<MenuItem,DataBindBaseViewHolder> {
    public DataBindDemoAdapter() {
        super(R.layout.item_binding);
    }
    @Override
    protected void convert(DataBindBaseViewHolder helper, MenuItem item) {
        //这里的ItemBindingBinding就是item_binding去下划线后的骆驼命名+Binding
        ItemBindingBinding binding = (ItemBindingBinding) helper.getBinding();
        binding.setItem(item);
    }
}
