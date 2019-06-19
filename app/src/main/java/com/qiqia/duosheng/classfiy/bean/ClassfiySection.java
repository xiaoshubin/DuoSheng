package com.qiqia.duosheng.classfiy.bean;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.qiqia.duosheng.main.bean.Classfiy;

public class ClassfiySection extends SectionEntity<Classfiy.DataBean.InfoBean> {
    public ClassfiySection(Classfiy.DataBean.InfoBean infoBean) {
        super(infoBean);
    }

    public ClassfiySection( String header) {
        super(true, header);
    }
}
