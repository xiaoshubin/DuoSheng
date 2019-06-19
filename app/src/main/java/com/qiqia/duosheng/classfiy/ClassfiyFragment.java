package com.qiqia.duosheng.classfiy;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.classfiy.adapter.ClassfiyAdapter;
import com.qiqia.duosheng.classfiy.bean.ClassfiySection;
import com.qiqia.duosheng.main.bean.Classfiy;
import com.qiqia.duosheng.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ClassfiyFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static ClassfiyFragment newInstance(Classfiy classfiy) {
        Bundle args = new Bundle();
        args.putSerializable("classfiy", classfiy);
        ClassfiyFragment fragment = new ClassfiyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_classfiy;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        Classfiy classfiy = (Classfiy) getArguments().getSerializable("classfiy");
        recyclerView.setLayoutManager(new GridLayoutManager(_mActivity,3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        List<ClassfiySection> datas = new ArrayList<>();
        List<Classfiy.DataBean> data = classfiy.getData();
        for (int i = 0; i < data.size(); i++) {
            Classfiy.DataBean dataBean = data.get(i);
            List<Classfiy.DataBean.InfoBean> infos = dataBean.getInfo();
            ClassfiySection headItem = new ClassfiySection(dataBean.getNextName());
            datas.add(headItem);
            for (int j = 0; j <infos.size() ; j++) {
                Classfiy.DataBean.InfoBean infoBean = infos.get(j);
                ClassfiySection contentItem = new ClassfiySection(infoBean);
                datas.add(contentItem);
            }
        }
        ClassfiyAdapter classfiyAdapter = new ClassfiyAdapter(datas);
        recyclerView.setAdapter(classfiyAdapter);
        classfiyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ClassfiySection item = (ClassfiySection) adapter.getItem(position);
                if (!item.isHeader){
                    Classfiy.DataBean.InfoBean t = item.t;
                    goWhiteBarActivity(SearchFragment.class.getSimpleName(),"keyWord",t.getSonName());
                }

            }
        });

    }
}
