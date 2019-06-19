package com.qiqia.duosheng.main.bean;

import java.io.Serializable;
import java.util.List;

public class IndexClassfiy implements Serializable {

    public IndexClassfiy(String mainName) {
        this.mainName = mainName;
    }

    /**
     * cid : 1
     * mainName : 女装
     * data : [{"cid":"1","name":"吊带裙","url":"http://img.haodanku.com/0716abc13652355b130dc3c83d39a7dc-600"},{"cid":"1","name":"露肩上衣","url":"http://img.haodanku.com/a39d41be029747367e3889ea195043b1-600"},{"cid":"1","name":"连衣裙","url":"http://img.haodanku.com/89937f347f81f5c5539f9da9b35b7a62-600"},{"cid":"1","name":"雪纺裙","url":"http://img.haodanku.com/3deb054da8cb2f4b1b5a07ab530e7e41-600"},{"cid":"1","name":"半身裙","url":"http://img.haodanku.com/b68bc66ab1a81db336110b7c1196b5a9-600"},{"cid":"1","name":"印花裙","url":"http://img.haodanku.com/3ce7249ba847286308c82bed97f7817d-600"},{"cid":"1","name":"纯色裙","url":"http://img.haodanku.com/de464503dab5d20a5d6505573f1624bd-600"},{"cid":"1","name":"婚纱","url":"http://img.haodanku.com/d40c79df78c0a0cfbbb05605891950db-600"},{"cid":"1","name":"小香风","url":"http://img.haodanku.com/2907a1a4faf78674c4ff422ce9ca16eb-600"}]
     */

    private int cid;
    private String mainName;
    private List<DataBean> data;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * cid : 1
         * name : 吊带裙
         * url : http://img.haodanku.com/0716abc13652355b130dc3c83d39a7dc-600
         */

        private String cid;
        private String name;
        private String url;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
