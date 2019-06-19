package com.qiqia.duosheng.mine.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.mine.bean.MemberFans;

import java.util.List;

public class MemberFansAdapter extends BaseMultiItemQuickAdapter<MemberFans, BaseViewHolder> {

    public MemberFansAdapter(List<MemberFans> data) {
        super(data);
        addItemType(MemberFans.UNDER,R.layout.item_member_fans);
        addItemType(MemberFans.FISSION,R.layout.item_member_fans_fission);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberFans item) {
        switch (helper.getItemViewType()) {
            case MemberFans.UNDER:
                break;
            case MemberFans.FISSION:
                break;
        }
    }
}
