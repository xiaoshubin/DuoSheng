package com.qiqia.duosheng.main.guide;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.qiqia.duosheng.MainActivity;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.SPStr;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.smallcake_utils.AppUtils;
import cn.com.smallcake_utils.SPUtils;

public class GuideFragment2 extends BaseFragment {
    @BindView(R.id.video_view)
    VideoView videoView;
    @BindView(R.id.btn_play_again)
    Button btnPlayAgain;
    @BindView(R.id.btn_start_use)
    Button btnStartUse;
    @BindView(R.id.layout_bottom)
    LinearLayout layoutBottom;
    Unbinder unbinder;
    @BindView(R.id.iv_first_bit)
    ImageView ivFirstBit;

    @Override
    public int setLayout() {
        return R.layout.guide_fragment2;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        SPUtils.put(SPStr.GUIDE_VIEWPAGER_FRAGMENT, 1);

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initVideoPath();//初始化
        onEvent();//事件监听

    }

    private void onEvent() {
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i("tag", "准备完毕,可以播放了");
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i("tag", "播放完毕");
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i("tag", "播放失败");
                return false;
            }
        });
    }


    String uri;

    private void initVideoPath() {
//        MediaController mediaController = new MediaController(_mActivity);
//        videoView.setMediaController(mediaController);//这样就有滑动条了
        uri = "android.resource://" + AppUtils.getAppPackageName() + "/" + R.raw.guidevideo;
        videoView.setVideoURI(Uri.parse(uri));//播放网络视频
    }

    @OnClick({R.id.btn_play_again, R.id.btn_start_use,R.id.iv_first_bit})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.iv_first_bit:
                ivFirstBit.setVisibility(View.GONE);
                videoView.start();
                break;
            case R.id.btn_play_again:
                palyAgain();
                break;
            case R.id.btn_start_use:

                startActivity(new Intent(_mActivity, MainActivity.class));
                _mActivity.finish();
                break;
        }
    }

    private void palyAgain() {
        if (!videoView.isPlaying()) {
//                    videoView.resume(); // 重新播放,无效
            videoView.pause();//暂停
            videoView.stopPlayback();//停止播放,释放资源
            videoView.setVideoURI(Uri.parse(uri));//重新设置资源
            videoView.start();//开始播放,可以不在准备监听中播放
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (videoView != null) {
            videoView.suspend();
        }
    }
}
