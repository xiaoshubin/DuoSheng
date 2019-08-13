package com.qiqia.duosheng.custom;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.impl.PartShadowPopupView;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.activities.WhiteBarActivity;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.main.MainViewPagerFragment;
import com.qiqia.duosheng.main.adapter.AllGoodsTypePopAdapter;
import com.qiqia.duosheng.main.bean.Classfiy;
import com.qiqia.duosheng.ranking.RankingListFragment;
import com.qiqia.duosheng.search.SearchFragment;

import java.util.List;

public class GoodsTypePopWindow extends PartShadowPopupView {


    public GoodsTypePopWindow(@NonNull Context context, List<Classfiy> classfiys) {
        super(context);
        mAdaper = new AllGoodsTypePopAdapter();
        mAdaper.setNewData(classfiys);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_goods_type;
    }
    AllGoodsTypePopAdapter mAdaper;
    //精选跳24小时，全部跳转到主页的第二项分类
    @Override
    protected void onCreate() {
        super.onCreate();
        RecyclerView recyclerViewGoodsType = findViewById(R.id.recycler_view_goods_type);
        recyclerViewGoodsType.setLayoutManager(new GridLayoutManager(this.getContext(),4));
        recyclerViewGoodsType.setAdapter(mAdaper);
         mAdaper.setOnItemClickListener((adapter, view, position) -> {
             GoodsTypePopWindow.this.dismiss();
             if (position==0){
                 MainViewPagerFragment.rgBottomTab.check(R.id.rb3);
                 try {
                     RankingListFragment.viewPager.setCurrentItem(1);
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 return;
             }else if (position==mAdaper.getData().size()-1){
                 MainViewPagerFragment.rgBottomTab.check(R.id.rb2);
                 return;
             }

             Classfiy item = (Classfiy) adapter.getItem(position);
             String mainName = item.getMainName();
             Intent intent = new Intent(GoodsTypePopWindow.this.getContext(), WhiteBarActivity.class);
             intent.putExtra(Contants.LOAD_FRAGMENT, SearchFragment.class.getSimpleName());
             intent.putExtra("keyWord",mainName);
             GoodsTypePopWindow.this.getContext().startActivity(intent);

         });
        findViewById(R.id.tv_title).setOnClickListener(v -> dismiss());
    }

}
