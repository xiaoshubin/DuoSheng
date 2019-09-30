package com.qiqia.duosheng.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.qiqia.duosheng.R;

import java.util.List;

import cn.com.smallcake_utils.L;

public class QiImageActivity extends AppCompatActivity {
    private static final String TAG = "QiImageActivity";
    ImageView ivImg;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qi_image);

        ivImg = findViewById(R.id.iv_img);
        videoView = findViewById(R.id.video_view);

        Intent intent = getIntent();
        String action = intent.getAction();//action
        String type = intent.getType();//类型
        L.e(TAG,"type=="+type);
        //type为image/*或者video/*
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
            L.e(TAG,"uri=="+uri);
            if (uri==null)return;
            switch (type){
                case "image/*":
                    Glide.with(this).load(getRealPathFromURI(uri)).into(ivImg);
                    break;
                case "video/*":
                    initVideoPath(uri);//初始化
                    break;
            }
        }
        /**
         * url: xl://goods:8888/goodsDetail?goodsId=10011002
         * scheme: xl
         * host: goods
         * port: 8888
         * path: /goodsDetail
         * pathSegments: [goodsDetail]
         * query: goodsId=10011002
         * goodsId: 10011002
         */
        Uri uri = intent.getData();
        if (uri != null) {
            // 完整的url信息
            String url = uri.toString();
            L.e(TAG, "url: " + url);
            // scheme部分
            String scheme = uri.getScheme();
            L.e(TAG, "scheme: " + scheme);
            // host部分
            String host = uri.getHost();
            L.e(TAG, "host: " + host);
            //port部分
            int port = uri.getPort();
            L.e(TAG, "port: " + port);
            //访问路劲
            String path = uri.getPath();
            L.e(TAG, "path: " + path);
            List<String> pathSegments = uri.getPathSegments();
            L.e(TAG, "pathSegments: " + pathSegments);
            // Query部分
            String query = uri.getQuery();
            L.e(TAG, "query: " + query);
            //获取指定参数值
            String goodsId = uri.getQueryParameter("goodsId");
            L.e(TAG, "goodsId: " + goodsId);
        }
    }
    //加载视频资源到VideoView
    private void initVideoPath(Uri uri) {
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);//这样就有滑动条了
        videoView.setVideoURI(uri);//播放视频
        setFirstImg(ivImg, getRealPathFromURI(uri));
    }
    //设置第一针视频显示到图片控件上
    private void setFirstImg(ImageView image, String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(this, Uri.parse(path));
        Bitmap bitmap = media.getFrameAtTime();
        image.setImageBitmap(bitmap);
    }
    //获取图片的真实地址
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        }
        cursor.close();
        return null;
    }

}
