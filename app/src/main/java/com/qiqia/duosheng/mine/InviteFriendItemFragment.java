package com.qiqia.duosheng.mine;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.utils.Code2Utils;
import com.qiqia.duosheng.utils.DataLocalUtils;

import butterknife.BindView;
import cn.com.smallcake_utils.BitmapUtils;
import cn.com.smallcake_utils.IdentifierUtils;

public class InviteFriendItemFragment extends BaseFragment {
    @BindView(R.id.iv_code2)
    ImageView ivCode2;
    int position;
    @BindView(R.id.layout_bg)
    ConstraintLayout layoutBg;
    @BindView(R.id.layout_invite_root)
    ConstraintLayout layoutInviteRoot;


    public static InviteFriendItemFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        InviteFriendItemFragment fragment = new InviteFriendItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_invite_friend_item;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
//        if (true)return;
        //下载头像合成二维码图片
        User user = DataLocalUtils.getUser();
        String headimgurl = user.getHeadimgurl();
        position = getArguments().getInt("position");
        Glide.with(_mActivity).load(headimgurl)
                .into(new SimpleTarget<Drawable>(100, 100) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Bitmap headBitmap = BitmapUtils.drawable2Bitmap(resource);
                        String link = "http://192.168.1.158:9000/share.html?superCode=" + position;
                        Bitmap roundHeadBitmap = BitmapUtils.setRoundedCorner(headBitmap, 10);
                        Bitmap qrCode = Code2Utils.createQRCode(link, 200, roundHeadBitmap);
//                Bitmap qrCodeBitmap = BitmapUtils.setRoundedCorner(qrCode, 10);
                        ivCode2.setImageBitmap(qrCode);
                    }
                });
        //设置海报背景图片
        int mipmapResourceID = IdentifierUtils.getMipmapResourceID("share_poster_bg" + position);
        layoutBg.setBackgroundResource(mipmapResourceID);

    }



    public ConstraintLayout getLayoutInviteRoot() {
        return layoutInviteRoot;
    }
}
