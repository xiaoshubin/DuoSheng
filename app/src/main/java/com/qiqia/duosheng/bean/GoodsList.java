package com.qiqia.duosheng.bean;

import com.qiqia.duosheng.search.bean.GoodsInfo;

import java.io.Serializable;
import java.util.List;

public class GoodsList implements Serializable {


    /**
     * data : [{"activityid":"a50eb4060cfa44389ddbde2bc513aa36","couponmoney":"40","discount":"4.9","general_index":"198","product_id":"10405711","itemendprice":"39.0","itemid":"593367141671","itempic":"http://img.alicdn.com/imgextra/i4/3603004100/O1CN019gS7KB1g9qOS0B5OS_!!3603004100.jpg_310x310.jpg","itemprice":"79.0","itemsale":"71818","itemsale2":"3","itemshorttitle":"限购一件ins超火韩版网纱半身裙","itemtitle":"极尔斯黑色不规则半身裙子2019新款女春夏中长款百褶网红纱裙ins","me":"","planlink":"","shopid":"550938464","shopname":"极尔斯旗舰店","is_tmall":1,"todaysale":"12","from":1,"commision":"3.90"}]
     * minid : 2
     * total : 900
     */

    private int minId;
    private String total;
    private int page=1;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private List<GoodsInfo> data;

    public void setMinId(int minId) {
        this.minId = minId;
    }

    public int getMinId() {
        return minId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<GoodsInfo> getData() {
        return data;
    }

    public void setData(List<GoodsInfo> data) {
        this.data = data;
    }


}
