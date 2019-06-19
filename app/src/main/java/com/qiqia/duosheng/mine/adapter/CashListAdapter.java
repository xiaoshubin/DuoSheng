package com.qiqia.duosheng.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.mine.bean.CashList;

public class CashListAdapter extends BaseQuickAdapter<CashList, BaseViewHolder> {
    public CashListAdapter() {
        super(R.layout.item_cash_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, CashList item) {

    }
}
