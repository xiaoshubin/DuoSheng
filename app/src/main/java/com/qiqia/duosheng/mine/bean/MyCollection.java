package com.qiqia.duosheng.mine.bean;

import com.qiqia.duosheng.search.bean.GoodsInfo;

import java.io.Serializable;
import java.util.List;

public class MyCollection implements Serializable {


    /**
     * data : [{"VcId":"3","VcType":"1","VcVpId":"13","VcItemId":"572874377966","VcItemPic":"https://img.alicdn.com/bao/uploaded/i1/3984855886/TB2otpgtTXYBeNkHFrdXXciuVXa_!!3984855886-0-item_pic.jpg_310x310.jpg","VcItemPrice":"49","VcItemEndPrice":"49","VcItemTitle":"性感睡衣情调衣人小胸透明情趣内衣激情套装骚午夜魅力透视装挑逗","VcItemSale":"1232","VcShopName":"随行成人用品专营店","VcIsTmall":"1","VcCommision":"0","VcCouponMoney":"0","VcActivityId":"","VcTime":"1559805104"}]
     * page : 1
     * pages : 1
     * total : 23
     */

    private int page;
    private int pages;
    private String total;
    private List<GoodsInfo> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
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
