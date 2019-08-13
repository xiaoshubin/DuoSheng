package com.qiqia.duosheng.mine;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.text.TextUtils;
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
        String link = "http://192.168.1.158:9000/share.html?superCode=" + position;
        position = getArguments().getInt("position");
        //首先通过链接，直接生成二维码图片
        Bitmap qrCode = Code2Utils.createQRCode(link, 200);
        if (ivCode2 != null) ivCode2.setImageBitmap(qrCode);
        //如果用户有头像，并把头像合成到二维码当中
        if (!TextUtils.isEmpty(headimgurl))
            Glide.with(_mActivity).asBitmap().load(headimgurl)
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Bitmap roundHeadBitmap = BitmapUtils.setRoundedCorner(resource, 10);
                    Bitmap qrCode = Code2Utils.createQRCode(link, 200, roundHeadBitmap);
                    if (ivCode2 != null) ivCode2.setImageBitmap(qrCode);
                }
            });
        //设置海报背景图片
        int mipmapResourceID = IdentifierUtils.getMipmapResourceID("share_poster_bg" + position);
        layoutBg.setBackgroundResource(mipmapResourceID);
    }
}
