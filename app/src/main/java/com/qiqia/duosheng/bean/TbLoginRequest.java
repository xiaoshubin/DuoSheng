package com.qiqia.duosheng.bean;

import com.qiqia.duosheng.base.Contants;

import java.io.Serializable;

//            String url = "https://oauth.taobao.com/token?client_id="+  Contants.ALI_BAICHUAN_APP_KEY +"&client_secret="+Contants.ALI_BAICHUAN_APP_SECRET+"&grant_type=authorization_code&code="+code+"&redirect_uri="+redirectUrl+"&state=1212&view=wap";
public class TbLoginRequest  implements Serializable {

    private String client_id = Contants.ALI_BAICHUAN_APP_KEY;
    private String client_secret = Contants.ALI_BAICHUAN_APP_SECRET;
    private String grant_type = "authorization_code";
    private String code;
    private String redirect_uri;
    private String state = "1212";
    private String view=  "wap";

    private TbLoginRequest(Builder builder) {
        client_id = builder.client_id;
        client_secret = builder.client_secret;
        grant_type = builder.grant_type;
        code = builder.code;
        redirect_uri = builder.redirect_uri;
        state = builder.state;
        view = builder.view;
    }

    public static final class Builder {
        private String client_id = Contants.ALI_BAICHUAN_APP_KEY;
        private String client_secret = Contants.ALI_BAICHUAN_APP_SECRET;
        private String grant_type = "authorization_code";
        private String code;
        private String redirect_uri;
        private String state = "1212";
        private String view=  "wap";

        public Builder() {
        }

        public Builder client_id(String val) {
            client_id = val;
            return this;
        }

        public Builder client_secret(String val) {
            client_secret = val;
            return this;
        }

        public Builder grant_type(String val) {
            grant_type = val;
            return this;
        }

        public Builder code(String val) {
            code = val;
            return this;
        }

        public Builder redirect_uri(String val) {
            redirect_uri = val;
            return this;
        }

        public Builder state(String val) {
            state = val;
            return this;
        }

        public Builder view(String val) {
            view = val;
            return this;
        }

        public TbLoginRequest build() {
            return new TbLoginRequest(this);
        }
    }
}
