package com.qiqia.duosheng.mine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.custom.LoopTransformer;
import com.qiqia.duosheng.utils.Code2Utils;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.BitmapUtils;
import cn.com.smallcake_utils.ClipboardUtils;
import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.IdentifierUtils;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.ScreenUtils;
import cn.com.smallcake_utils.ShareUtils;
import cn.com.smallcake_utils.ToastUtil;

public class InviteFriendFragment extends BaseBarFragment {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tv_copy_code)
    TextView tvCopyCode;
    @BindView(R.id.tv_share_link)
    TextView tvShareLink;
    @BindView(R.id.tv_share_pic)
    TextView tvSharePic;
    @BindView(R.id.tv_invite_code)
    TextView tvInviteCode;

    @Override
    public int setLayout() {
        return R.layout.fragment_invite_friend;
    }
    User user;
    Bitmap headBitmap;

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("邀请好友");
        user = DataLocalUtils.getUser();
        tvInviteCode.setText(user.getSuperCode());
        initViewPager();

        Glide.with(_mActivity).load(user.getHeadimgurl())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        headBitmap = BitmapUtils.drawable2Bitmap(resource);
                    }
                });

    }
    int selectPosition=2;//当前选择的项，默认中间项
    private void initViewPager() {
        int screenWidth = ScreenUtils.getScreenWidth();
        int guideWidth = screenWidth- DpPxUtils.dp2px(88);
        //图片大小575 x 928 ,guideWith/575 = height/928
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int) (guideWidth / 575f * 928f));
        viewPager.setLayoutParams(layoutParams);
        //设置边距
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return InviteFriendItemFragment.newInstance(i);
            }
            @Override
            public int getCount() {
                return 5;
            }
        });
        viewPager.setOffscreenPageLimit(4);
        //定位到中间的一张海报
        viewPager.setCurrentItem(2);
        //页面转换动画设置
        viewPager.setPageTransformer(false,new LoopTransformer());
        //页面变换事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                selectPosition = i;
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }



    @OnClick({R.id.tv_copy_code, R.id.tv_share_link, R.id.tv_share_pic})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.tv_copy_code:
                ClipboardUtils.copy(user.getSuperCode());
                break;
            case R.id.tv_share_link:
                shareLink();
                break;
            case R.id.tv_share_pic:
                takePermission();
                break;
        }
    }

    private void takePermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        L.e("以获得权限" + data.toString());
                        //1.从相册获取照片
                        ToastUtil.showLong("即将分享一张图片！");
                        sharePic();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        L.e("未获得权限" + data.toString());
                        ToastUtil.showLong("没有权限，无法生成分享图片！");
                    }
                }).start();

    }

    private void sharePic() {
        String link = "http://192.168.1.158:9000/share.html?superCode=" + selectPosition;
        Bitmap qrCode;
        if (headBitmap!=null){
            qrCode = Code2Utils.createQRCode(link, 200,headBitmap);
        }else {
             qrCode = Code2Utils.createQRCode(link, 200,1);
        }
        int mipmapResourceID = IdentifierUtils.getMipmapResourceID("share_poster_bg" + selectPosition);
        Bitmap bitmapBg = BitmapUtils.getBitmapRes(mipmapResourceID);
        Bitmap bitmap = combineBitmap(bitmapBg, BitmapUtils.setRoundedCorner(qrCode,10));
        ShareUtils.shareImg(_mActivity,getString(R.string.app_name),"分享","图片",bitmap);

    }

    /**
     * 合并两张bitmap为一张
     *
     * @param background
     * @param foreground
     * @return Bitmap
     */
    public static Bitmap combineBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int foregroundWidth = bgWidth/5*2;
        foreground =zoomImg(foreground,foregroundWidth,foregroundWidth);
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        Bitmap newmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2, bgHeight-fgHeight-fgHeight/3, null);
        canvas.save();
        canvas.restore();
        return newmap;
    }

    /**
     * 缩放bitmap
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    private void shareLink() {
        String link = "http://192.168.1.158:9000/share.html?superCode="+user.getSuperCode();
        ShareUtils.shareText(_mActivity,getString(R.string.app_name),"分享",link);
    }
}
