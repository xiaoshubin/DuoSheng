package com.qiqia.duosheng.mine;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.utils.Code2Utils;
import com.qiqia.duosheng.utils.DataLocalUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.smallcake_utils.BitmapUtils;
import cn.com.smallcake_utils.IdentifierUtils;

public class InviteFriendItemFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.iv_code2)
    ImageView ivCode2;
    int position;
    @BindView(R.id.layout_bg)
    RelativeLayout layoutBg;



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
        User user = DataLocalUtils.getUser();
        String headimgurl = user.getHeadimgurl();
        position = getArguments().getInt("position");
        Glide.with(_mActivity).load(headimgurl)
                .into(new SimpleTarget<Drawable>(100,100) {
            @Override
            public void onResourceReady(@NonNull Drawable  resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap headBitmap = BitmapUtils.drawable2Bitmap(resource);
                String link = "http://192.168.1.158:9000/share.html?superCode=" + position;
                Bitmap qrCode = Code2Utils.createQRCode(link, 200,headBitmap);
                ivCode2.setImageBitmap(qrCode);
            }
        });


        int mipmapResourceID = IdentifierUtils.getMipmapResourceID("share_poster_bg" + position);
        layoutBg.setBackgroundResource(mipmapResourceID);



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
    }
}
