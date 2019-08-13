package com.qiqia.duosheng.main.guide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.qiqia.duosheng.MainActivity;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.SPStr;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.AppUtils;
import cn.com.smallcake_utils.SPUtils;

public class GuideFragment2 extends BaseFragment {

    private static final String TAG = "GuideFragment2";
    @BindView(R.id.video_view)
    VideoView videoView;
    @BindView(R.id.iv_first_bit)
    ImageView ivFirstBit;
    @BindView(R.id.btn_play_again)
    Button btnPlayAgain;
    @BindView(R.id.btn_start_use)
    Button btnStartUse;
    @BindView(R.id.iv_play)
    ImageView ivPlay;

    @Override
    public int setLayout() {
        return R.layout.guide_fragment2;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        SPUtils.put(SPStr.GUIDE_VIEWPAGER_FRAGMENT, 1);
        initVideoPath();//初始化
        onEvent();//事件监听
    }

    private void onEvent() {
        videoView.setOnPreparedListener(mp -> Log.i("tag", "准备完毕,可以播放了"));
        videoView.setOnCompletionListener(mp -> Log.i("tag", "播放完毕"));
        videoView.setOnErrorListener((mp, what, extra) -> {
            Log.i("tag", "播放失败");
            return false;
        });
    }


    String uri;

    private void initVideoPath() {
//        MediaController mediaController = new MediaController(_mActivity);
//        videoView.setMediaController(mediaController);//这样就有滑动条了
        uri = "android.resource://" + AppUtils.getAppPackageName() + "/" + R.raw.guidevideo;
        videoView.setVideoURI(Uri.parse(uri));//播放网络视频
        setFirstImg(ivFirstBit, uri);
    }

    @OnClick({R.id.btn_play_again, R.id.btn_start_use, R.id.iv_first_bit, R.id.iv_play})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.iv_play://图片点击开始播放
            case R.id.iv_first_bit://图片点击开始播放
                ivFirstBit.setVisibility(View.GONE);
                ivPlay.setVisibility(View.GONE);
                videoView.start();
                break;
            case R.id.btn_play_again://重新播放
                palyAgain();
                break;
            case R.id.btn_start_use:
                startActivity(new Intent(_mActivity, MainActivity.class));
                _mActivity.finish();
                break;
        }
    }

    private void palyAgain() {
        //如果没有点击图片开始播放
        if (ivFirstBit.getVisibility() == View.VISIBLE) {
            ivFirstBit.setVisibility(View.GONE);
            ivPlay.setVisibility(View.GONE);
            videoView.start();
            return;
        }
        if (!videoView.isPlaying()) {
            videoView.start();//开始播放,可以不在准备监听中播放
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null)videoView.pause();//暂停
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoView!=null&&!videoView.isPlaying()) {
            videoView.start();//开始播放,可以不在准备监听中播放
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (videoView != null) {
            videoView.pause();//暂停
            videoView.stopPlayback();//停止播放,释放资源
            videoView.suspend();
        }
    }
    private void setFirstImg(ImageView image, String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(_mActivity, Uri.parse(path));
        Bitmap bitmap = media.getFrameAtTime();
        image.setImageBitmap(bitmap);
    }

}
