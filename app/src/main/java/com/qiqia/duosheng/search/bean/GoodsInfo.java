package com.qiqia.duosheng.search.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import java.io.Serializable;
import java.util.List;

public class GoodsInfo extends BaseObservable implements Serializable {

    /**
     * activityId : 26b3c977689f46b9873869c64ef29eee
     * couponMoney : 10
     * couponStartTime : 1557331200
     * couponEndTime : 1557590399
     * discount : 7.40
     * generalIndex : 302
     * productId : 10503435
     * itemendPrice : 29.00
     * itemId : 585407433439
     * itemPic : https://gd4.alicdn.com/imgextra/i3/916319073/O1CN01Wd1dPu2GtTzniTax9_!!916319073.jpg_310x310.jpg
     * itemPicList : []
     * itemDetailPic : http://img.haodanku.com/1_585407433439_1557364215.jpg-600
     * itemVideo :
     * itemPrice : 39.00
     * itemSale : 46201
     * itemSale2 : 0
     * itemShortTitle : 短袖睡衣女夏季纯棉韩版学生薄款春秋女士可爱夏天家居服两件套装
     * itemTitle : 短袖睡衣女夏季纯棉韩版学生薄款春秋女士可爱夏天家居服两件套装
     * me :
     * planLink :
     * shopId : 136276356
     * shopName : 菲斯梦家居服
     * isTmall : 0
     * todaySale : 0
     * commision : 2.90
     * commisionTop : 4.06
     * guideArticle :
     */

    private String activityId;
    private String couponMoney;
    private int couponStartTime;
    private int couponEndTime;
    private String discount;
    private String generalIndex;
    private String productId;
    private String itemEndPrice;
    private String itemId;
    private String itemPic;
    private String itemDetailPic;//宝贝详情图片
    private String itemVideo;
    private String itemPrice;
    private String itemSale;
    private String itemSale2;
    private String itemShortTitle;
    private String itemTitle;
    private String me;
    private String planLink;
    private String couponLink;//优惠券链接
    private String shopId;
    private String shopName;
    private int isTmall;
    private String todaySale;
    private String commision;
    private String commisionTop;
    private String guideArticle;
    private List<String> itemPicList;//小图片集合


    private String comment; //每日爆款单独字段，评论
    private String itemDesc;
    private String couponInfo; //全网查包含的字段，如果没有此字段，就判定为好单库，可以通过id来查询商品详情
    @Bindable
    public int isCollect;//0未收藏 1已收藏
    //收藏列表，判定全网或好单库
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
        notifyPropertyChanged(BR.isCollect);
    }

    public String getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(String couponInfo) {
        this.couponInfo = couponInfo;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(String couponMoney) {
        this.couponMoney = couponMoney;
    }

    public int getCouponStartTime() {
        return couponStartTime;
    }

    public void setCouponStartTime(int couponStartTime) {
        this.couponStartTime = couponStartTime;
    }

    public int getCouponEndTime() {
        return couponEndTime;
    }

    public void setCouponEndTime(int couponEndTime) {
        this.couponEndTime = couponEndTime;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getGeneralIndex() {
        return generalIndex;
    }

    public void setGeneralIndex(String generalIndex) {
        this.generalIndex = generalIndex;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getItemEndPrice() {
        return itemEndPrice;
    }

    public void setItemEndPrice(String itemendPrice) {
        this.itemEndPrice = itemendPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemPic() {
        return itemPic;
    }

    public void setItemPic(String itemPic) {
        this.itemPic = itemPic;
    }

    public String getItemDetailPic() {
        return itemDetailPic;
    }

    public void setItemDetailPic(String itemDetailPic) {
        this.itemDetailPic = itemDetailPic;
    }

    public String getItemVideo() {
        return itemVideo;
    }

    public void setItemVideo(String itemVideo) {
        this.itemVideo = itemVideo;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemSale() {
        return itemSale;
    }

    public void setItemSale(String itemSale) {
        this.itemSale = itemSale;
    }

    public String getItemSale2() {
        return itemSale2;
    }

    public void setItemSale2(String itemSale2) {
        this.itemSale2 = itemSale2;
    }

    public String getItemShortTitle() {
        return itemShortTitle;
    }

    public void setItemShortTitle(String itemShortTitle) {
        this.itemShortTitle = itemShortTitle;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getMe() {
        return me;
    }

    public void setMe(String me) {
        this.me = me;
    }

    public String getPlanLink() {
        return planLink;
    }

    public void setPlanLink(String planLink) {
        this.planLink = planLink;
    }

    public String getCouponLink() {
        return couponLink;
    }

    public void setCouponLink(String couponLink) {
        this.couponLink = couponLink;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getIsTmall() {
        return isTmall;
    }

    public void setIsTmall(int isTmall) {
        this.isTmall = isTmall;
    }

    public String getTodaySale() {
        return todaySale;
    }

    public void setTodaySale(String todaySale) {
        this.todaySale = todaySale;
    }

    public String getCommision() {
        return commision;
    }

    public void setCommision(String commision) {
        this.commision = commision;
    }

    public String getCommisionTop() {
        return commisionTop;
    }

    public void setCommisionTop(String commisionTop) {
        this.commisionTop = commisionTop;
    }

    public String getGuideArticle() {
        return guideArticle;
    }

    public void setGuideArticle(String guideArticle) {
        this.guideArticle = guideArticle;
    }

    public List<String> getItemPicList() {
        return itemPicList;
    }

    public void setItemPicList(List<String> itemPicList) {
        this.itemPicList = itemPicList;
    }

}
