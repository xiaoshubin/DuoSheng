package com.qiqia.duosheng.classfiy.adapter;

import com.qiqia.duosheng.main.bean.Classfiy;

import java.util.List;

import q.rorbin.verticaltablayout.adapter.SimpleTabAdapter;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

public class ClassfiyMenuTabAdapter extends SimpleTabAdapter {
    List<Classfiy> menus;
    public ClassfiyMenuTabAdapter(List<Classfiy> menus) {
        this.menus = menus;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public TabView.TabBadge getBadge(int position) {
        return null;
    }

    @Override
    public TabView.TabIcon getIcon(int position) {
        return  null;
    }

    @Override
    public TabView.TabTitle getTitle(int position) {
        Classfiy classfiy = menus.get(position);
        return new QTabView.TabTitle.Builder()
                .setTextColor(0xFFffffff,0xFF2A2323)
                .setTextSize(14)
                .setContent(classfiy.getMainName())
                .build();

    }

    @Override
    public int getBackground(int position) {
        return -1;
    }
}
