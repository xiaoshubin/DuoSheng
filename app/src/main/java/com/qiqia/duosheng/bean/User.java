package com.qiqia.duosheng.bean;

import java.io.Serializable;

public class User implements Serializable {


    @Override
    public String toString() {
        return "User{" +
                "newUser=" + newUser +
                ", nickname='" + nickname + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", phone='" + phone + '\'' +
                ", wxid='" + wxid + '\'' +
                ", uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", superCode='" + superCode + '\'' +
                ", pdid='" + pdid + '\'' +
                ", level=" + level +
                '}';
    }

    /**
     * newUser : 0
     * nickname : 18324138218
     * headimgurl : http://images.liqucn.com/img/h1/h966/img201709181327380_info300X300.jpg
     * phone : 18324138218
     * wxid : 0
     * uid : 9
     * token : 1e98f2d7e9a38b69ba9b6762a56879eb
     * superCode : A1B2C3
     * pdid : 普通会员
     */

    private int newUser;
    private String nickname;
    private String headimgurl;
    private String phone;
    private int wxid;
    private String uid;
    private String token;
    private String superCode;
    private String pdid;
    private int level;
    private String sex;
    private String wx;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNewUser() {
        return newUser;
    }

    public void setNewUser(int newUser) {
        this.newUser = newUser;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getWxid() {
        return wxid;
    }

    public void setWxid(int wxid) {
        this.wxid = wxid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSuperCode() {
        return superCode;
    }

    public void setSuperCode(String superCode) {
        this.superCode = superCode;
    }

    public String getPdid() {
        return pdid;
    }

    public void setPdid(String pdid) {
        this.pdid = pdid;
    }
}
