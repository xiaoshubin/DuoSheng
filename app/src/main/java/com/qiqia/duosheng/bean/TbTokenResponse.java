package com.qiqia.duosheng.bean;

import java.io.Serializable;

public class TbTokenResponse  implements Serializable {

    /**
     * w2_expires_in : 0
     * taobao_user_id : 263685215
     * taobao_user_nick : %E5%95%86%E5%AE%B6%E6%B5%8B%E8%AF%95%E5%B8%90%E5%8F%B752
     * w1_expires_in : 1800
     * re_expires_in : 0
     * r2_expires_in : 0
     * expires_in : 86400
     * token_type : Bearer
     * refresh_token : 6200e1909ca29b04685c49d67f5ZZ3675347c0c6d5abccd263685215
     * access_token : 6200819d9366af1383023a19907ZZf9048e4c14fd56333b263685215
     * r1_expires_in : 1800
     */

    private int w2_expires_in;
    private String taobao_user_id;
    private String taobao_user_nick;
    private int w1_expires_in;
    private int re_expires_in;
    private int r2_expires_in;
    private int expires_in;
    private String token_type;
    private String refresh_token;
    private String access_token;
    private int r1_expires_in;

    public int getW2_expires_in() {
        return w2_expires_in;
    }

    public void setW2_expires_in(int w2_expires_in) {
        this.w2_expires_in = w2_expires_in;
    }

    public String getTaobao_user_id() {
        return taobao_user_id;
    }

    public void setTaobao_user_id(String taobao_user_id) {
        this.taobao_user_id = taobao_user_id;
    }

    public String getTaobao_user_nick() {
        return taobao_user_nick;
    }

    public void setTaobao_user_nick(String taobao_user_nick) {
        this.taobao_user_nick = taobao_user_nick;
    }

    public int getW1_expires_in() {
        return w1_expires_in;
    }

    public void setW1_expires_in(int w1_expires_in) {
        this.w1_expires_in = w1_expires_in;
    }

    public int getRe_expires_in() {
        return re_expires_in;
    }

    public void setRe_expires_in(int re_expires_in) {
        this.re_expires_in = re_expires_in;
    }

    public int getR2_expires_in() {
        return r2_expires_in;
    }

    public void setR2_expires_in(int r2_expires_in) {
        this.r2_expires_in = r2_expires_in;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getR1_expires_in() {
        return r1_expires_in;
    }

    public void setR1_expires_in(int r1_expires_in) {
        this.r1_expires_in = r1_expires_in;
    }
}
