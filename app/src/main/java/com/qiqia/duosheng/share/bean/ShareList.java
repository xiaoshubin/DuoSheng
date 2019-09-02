package com.qiqia.duosheng.share.bean;

import java.io.Serializable;
import java.util.List;

public class ShareList  implements Serializable {

    /**
     * data : [{"productId":"16586","itemId":"42167076752","itemPic":"https://img.alicdn.com/imgextra/i4/2258324204/O1CN011gvTWDIQgd08WBb_!!2258324204.jpg","itemPicList":["http://img01.taobaocdn.com:80/tfscom/TB1EG6lVgHqK1RjSZFkXXX.WFXa_310x310.jpg","http://img03.taobaocdn.com:80/tfscom/TB18OfeVkvoK1RjSZFNXXcxMVXa_310x310.jpg","https://img.alicdn.com/imgextra/i4/2258324204/O1CN011gvTWJikPuggmNj_!!2258324204.jpg_310x310.jpg"],"itemPrice":"9.90","itemEndPrice":"6.90","itemTitle":"PE厨房食品袋冰箱微波炉保鲜袋320只","copyContent":"新低价！比超市便宜3倍😱<br>居家必备的PE食品袋保鲜袋保鲜膜<br>加厚240只⚡【6.9】秒杀<br>断点易撕，韧性强、耐撕扯<br>大小搭配合理，装肉装水果🍓都可以","copyComment":".<br>fu@zhi👉 $淘口令$|品质好货，性价比高<br>买一件可以用很久了","couponUrl":"https://uland.taobao.com/quan/detail?sellerId=2258324204&activityId=947fcad6e7044eb9b26e5570d67e27de","couponMoney":"3.00","commision":"1.04","addTime":"1557546578","showTime":"1557561900","click":"353"}]
     * minId : 3
     */

    private int minId;
    private List<DataBean> data;

    public int getMinId() {
        return minId;
    }

    public void setMinId(int minId) {
        this.minId = minId;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean  implements Serializable{
        /**
         * productId : 16586
         * itemId : 42167076752
         * itemPic : https://img.alicdn.com/imgextra/i4/2258324204/O1CN011gvTWDIQgd08WBb_!!2258324204.jpg
         * itemPicList : ["http://img01.taobaocdn.com:80/tfscom/TB1EG6lVgHqK1RjSZFkXXX.WFXa_310x310.jpg","http://img03.taobaocdn.com:80/tfscom/TB18OfeVkvoK1RjSZFNXXcxMVXa_310x310.jpg","https://img.alicdn.com/imgextra/i4/2258324204/O1CN011gvTWJikPuggmNj_!!2258324204.jpg_310x310.jpg"]
         * itemPrice : 9.90
         * itemEndPrice : 6.90
         * itemTitle : PE厨房食品袋冰箱微波炉保鲜袋320只
         * copyContent : 新低价！比超市便宜3倍😱<br>居家必备的PE食品袋保鲜袋保鲜膜<br>加厚240只⚡【6.9】秒杀<br>断点易撕，韧性强、耐撕扯<br>大小搭配合理，装肉装水果🍓都可以
         * copyComment : .<br>fu@zhi👉 $淘口令$|品质好货，性价比高<br>买一件可以用很久了
         * couponUrl : https://uland.taobao.com/quan/detail?sellerId=2258324204&activityId=947fcad6e7044eb9b26e5570d67e27de
         * couponMoney : 3.00
         * commision : 1.04
         * addTime : 1557546578
         * showTime : 1557561900
         * click : 353
         */

        private String couponMoney;
        private String productId;
        private String itemEndPrice;
        private String itemId;
        private String itemPic;
        private String itemPrice;
        private String itemTitle;
        private String commision;
        private List<String> itemPicList;



        private String copyContent;
        private String copyComment;
        private String couponUrl;
        private String addTime;
        private int showTime;
        private String click;


        public String getCouponMoney() {
            return couponMoney;
        }

        public void setCouponMoney(String couponMoney) {
            this.couponMoney = couponMoney;
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

        public void setItemEndPrice(String itemEndPrice) {
            this.itemEndPrice = itemEndPrice;
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

        public String getItemPrice() {
            return itemPrice;
        }

        public void setItemPrice(String itemPrice) {
            this.itemPrice = itemPrice;
        }

        public String getItemTitle() {
            return itemTitle;
        }

        public void setItemTitle(String itemTitle) {
            this.itemTitle = itemTitle;
        }

        public String getCommision() {
            return commision;
        }

        public void setCommision(String commision) {
            this.commision = commision;
        }

        public List<String> getItemPicList() {
            return itemPicList;
        }

        public void setItemPicList(List<String> itemPicList) {
            this.itemPicList = itemPicList;
        }

        public String getCopyContent() {
            return copyContent;
        }

        public void setCopyContent(String copyContent) {
            this.copyContent = copyContent;
        }

        public String getCopyComment() {
            return copyComment;
        }

        public void setCopyComment(String copyComment) {
            this.copyComment = copyComment;
        }

        public String getCouponUrl() {
            return couponUrl;
        }

        public void setCouponUrl(String couponUrl) {
            this.couponUrl = couponUrl;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public int getShowTime() {
            return showTime;
        }

        public void setShowTime(int showTime) {
            this.showTime = showTime;
        }

        public String getClick() {
            return click;
        }

        public void setClick(String click) {
            this.click = click;
        }
    }
}
