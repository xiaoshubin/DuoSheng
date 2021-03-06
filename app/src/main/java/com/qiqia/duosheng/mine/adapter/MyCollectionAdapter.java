package com.qiqia.duosheng.mine.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.databinding.ItemMyCollectionBinding;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.DataBindBaseViewHolder;

import java.util.ArrayList;
import java.util.Collections;

import cn.com.smallcake_utils.L;

public class MyCollectionAdapter extends BaseQuickAdapter<GoodsInfo, DataBindBaseViewHolder> {
    private ArrayList<Integer> selectArrays = new ArrayList<>();//选中数据项在列表中的位置集合
    private boolean isShowSelectBtn;//是否显示选中按钮,默认不显示
    private boolean isSelectAll;//外部【全选】按钮是否为选中状态
    private onSelectAllListener listener;//选中和取消选中监听器

    public void setListener(onSelectAllListener listener) {
        this.listener = listener;
    }

    public interface onSelectAllListener {
        void cancleSelectAll();

        void selectAll();
    }

    public MyCollectionAdapter() {
        super(R.layout.item_my_collection);
    }

    /**
     * 选中/取消
     *
     * @param selectPosition
     */
    public void setSelectPosition(Integer selectPosition) {
        boolean contains = selectArrays.contains(selectPosition);
        if (contains) {
            selectArrays.remove(selectPosition);
            //取消一项后，如果全选按钮的状态为：选中，应该取消全选按钮选中状态,但不触发取消所有列表已经选中项的事件
            if (isSelectAll) {
                isSelectAll = false;
                if (listener != null) listener.cancleSelectAll();
            }
        } else {
            //如果添加一项后，（外部[全选按钮]为：未选中&&列表数据全部选中），那么设置全选按钮为选中状态，不触发选中事件
            selectArrays.add(selectPosition);
            if (!isSelectAll && isSelectAllData()) {
                isSelectAll = true;
                if (listener != null) listener.selectAll();
            }
        }
        Collections.sort(selectArrays);
        notifyItemChanged(selectPosition);
    }

    /**
     * 全选
     */
    public void selectAll() {
        for (int i = 0; i < mData.size(); i++) {
            boolean contains = selectArrays.contains(i);
            if (!contains) selectArrays.add(i);
        }
        Collections.sort(selectArrays);//进行排序，避免因为选择前后顺序不一致，导致更新位置越界
        L.e("selectArrays==" + selectArrays.toString());
        notifyDataSetChanged();
        isSelectAll = true;
    }

    /**
     * 取消全选
     */
    public void cancleAll() {
        selectArrays.clear();
        notifyDataSetChanged();
        isSelectAll = false;
    }

    /**
     * 是否选中了所有的数据
     * 用来设定RadioButton状态
     */
    public boolean isSelectAllData() {
        return mData.size() == selectArrays.size();
    }

    /**
     * 删除选中项
     * position 为选中数据项在列表中的位置，如0,1,2，3
     * i 为当前集合遍历位置，所以remove()移除时要使用position-i
     * 例如选中 position  1和3，那么遍历两次i为 0,1
     * 由于第一次移除位置1的时候，列表数据少了一项，所以原来位置为3的变成了位置2，所以3-1=2
     * 也就是position-i才会拿到正确的列表数据位置
     */
    public void delSelect() {
        for (int i = 0; i < selectArrays.size(); i++) {
            Integer position = selectArrays.get(i);
            remove(position - i);
        }
        selectArrays.clear();
    }

    /**
     * 获得所有选中的收藏,并组装为id集合，以“，”分割的字符串
     * 方便接口删除收藏项
     *
     * @return
     */
    public String getAllSelectIds() {
        if (selectArrays.size() == 0) return "";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < selectArrays.size(); i++) {
            GoodsInfo dataBean = mData.get(selectArrays.get(i));
            String goodsId = dataBean.getItemId();
            buffer.append(goodsId);
            if (i < selectArrays.size() - 1) buffer.append(",");
        }
        return buffer.toString();
    }

    /**
     * 显示和隐藏操作按钮
     *
     * @param showSelectBtn
     */
    public void setShowSelectBtn(boolean showSelectBtn) {
        isShowSelectBtn = showSelectBtn;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(DataBindBaseViewHolder helper, GoodsInfo item) {
        ItemMyCollectionBinding binding = (ItemMyCollectionBinding) helper.getBinding();
        binding.setItem(item);
        binding.couponPrice.setCouponText(item.getCouponMoney() + "元");
        helper.addOnClickListener(R.id.rb_select);
        //是否显示操作按钮
        if (isShowSelectBtn) {
            binding.rbSelect.setVisibility(View.VISIBLE);
            boolean contains = selectArrays.contains(helper.getLayoutPosition());//是否选中
            binding.rbSelect.setChecked(contains);
        } else {
            binding.rbSelect.setVisibility(View.GONE);
        }
    }

}
