package com.smallcake.qsh.http;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {
    public static String getStringFromNet(String urlPath) {
        String result = "";
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                byte[] data = new byte[1024];
                int len = is.read(data);
                result = new String(data, 0, len);
                is.close();
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
