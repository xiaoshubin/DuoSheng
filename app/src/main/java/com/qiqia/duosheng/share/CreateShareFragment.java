package com.qiqia.duosheng.share;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.custom.BigPicPopImageLoader;
import com.qiqia.duosheng.dialog.OpenAccessibilityServicePop;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.share.utils.OpenAccessibilitySettingHelper;
import com.qiqia.duosheng.share.utils.ShareAccessibilityService;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.ShareHelper;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.com.smallcake_utils.ClipboardUtils;
import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.FileUtils;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.ScreenUtils;
import cn.com.smallcake_utils.ShareUtils;
import cn.com.smallcake_utils.ToastUtil;

/**
 * 创建分享
 */
public class CreateShareFragment extends BaseBarFragment {
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.layout_imgs)
    LinearLayout layoutImgs;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.cb_show_income)
    AppCompatCheckBox cbShowIncome;
    @BindView(R.id.cb_show_link)
    AppCompatCheckBox cbShowLink;
    @BindView(R.id.cb_show_invite_code)
    AppCompatCheckBox cbShowInviteCode;
    @BindView(R.id.btn_copy_comment)
    Button btnCopyComment;
    @BindView(R.id.tv_save_img)
    TextView tvSaveImg;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.tv_circle)
    TextView tvCircle;
    @BindView(R.id.tv_qq)
    TextView tvQq;
    @BindView(R.id.tv_qzone)
    TextView tvQzone;
    @BindView(R.id.layout_img)
    LinearLayout layoutImg;

    String commision;//预估收益
    String down_link;//下载链接
    String superCode = "123456";//邀请码
    private ArrayList<String> selectImgUrls = new ArrayList<>();//选中的图片地址，用于判断至少选择一项
    private List<Bitmap> allImgBitmaps = new ArrayList<>();//所有图片bitmap持有集合，用于图片分享时快速保存到相册后进行分享
    IWXAPI api;//微信分享API
    GoodsInfo goodsInfo;

    public static CreateShareFragment newInstance(GoodsInfo goodsInfo) {
        Bundle args = new Bundle();
        args.putSerializable("goodsInfo", goodsInfo);
        CreateShareFragment fragment = new CreateShareFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_create_share;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("创建分享");
        assert getArguments() != null;
        goodsInfo = (GoodsInfo) getArguments().getSerializable("goodsInfo");
        if (goodsInfo == null) return;
        api = WXAPIFactory.createWXAPI(_mActivity, Contants.WX_OPEN_APP_ID, true);
        getPermission();
        //0.初始化视图
        initView();
        //1.设置分享内容
        setContentTxt();
        //2.设置评论淘口令
        initCommentTxt();
        //3.设置分享的图片
        setShareImg();
    }

    private void initView() {
        tvTitle.setFocusable(true);
        tvTitle.setFocusableInTouchMode(true);
        tvComment.setMovementMethod(ScrollingMovementMethod.getInstance());//评论文字可滚动
    }

    private void setContentTxt() {
        String guideArticle = goodsInfo.getGuideArticle();//【推荐理由】
        etContent.setText((TextUtils.isEmpty(guideArticle) ? goodsInfo.getItemTitle() : "【推荐理由】" + guideArticle));
    }

    private void initCommentTxt() {
        commision = goodsInfo.getCommision();//1.佣金，收益
        down_link = "http://a.app.qq.com/o/simple.jsp?pkgname=com.n_add.android";//2.下载链接
        try {
            superCode = DataLocalUtils.getUser().getSuperCode();//3.邀请码
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCommentTxt();
    }

    private void setShareImg() {
        String itemPic = goodsInfo.getItemPic();
        List<String> itemPicList = goodsInfo.getItemPicList();
        if (itemPicList != null && itemPicList.size() > 0) {
            for (int i = 0; i < itemPicList.size(); i++) addImageViews(itemPicList.get(i));
            //如果有多张图片，默认选中第一张图片，且至少选中一张图片
            RelativeLayout relativeLayout = (RelativeLayout) layoutImgs.getChildAt(0);
            AppCompatCheckBox cbBox = (AppCompatCheckBox) relativeLayout.getChildAt(1);
            cbBox.setChecked(true);
            //设置第一张为二维码推广图片
            setFirstCode2ImageView(itemPicList.get(0));
        } else {
            //如果只有一张图片，默认选中此张图片，且无法更改
            addImageViews(itemPic);
            RelativeLayout relativeLayout = (RelativeLayout) layoutImgs.getChildAt(0);
            AppCompatCheckBox cbBox = (AppCompatCheckBox) relativeLayout.getChildAt(1);
            cbBox.setChecked(true);
            cbBox.setEnabled(false);
            //设置第一张为二维码推广图片
            setFirstCode2ImageView(itemPic);
        }

    }

    /**
     * 设置评论区域文字
     */
    private void setCommentTxt() {
        StringBuffer buffer = new StringBuffer(goodsInfo.getItemTitle() + "\n");
        if (cbShowIncome.isChecked()) buffer.append("【预估收益】" + commision + "元\n");
        if (cbShowLink.isChecked()) buffer.append("【下载链接】" + down_link + "\n");
        if (cbShowInviteCode.isChecked()) buffer.append("【邀请码】" + superCode + "\n");
        tvComment.setText(buffer.toString());
    }

    /**
     * 默认设置第一张为推广二维码图片
     * 设置TAG
     * 添加选中的url到选中集合
     */
    private void setFirstCode2ImageView(String url) {
        Glide.with(_mActivity).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                try {
                    RelativeLayout relativeLayout = (RelativeLayout) layoutImgs.getChildAt(0);
                    ImageView ivIcon = (ImageView) relativeLayout.getChildAt(0);
                    ShareHelper.createCode2BitmapToImageView(layoutImg, goodsInfo, resource, ivIcon);
                    ivIcon.setTag("isCode2Img");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 设置仅有的图片为二维码分享图,设置是二维码图Tag，
     * 并还原其他图片为默认的图片
     */
    private void setOnlyCode2ImageView() {
        if (goodsInfo == null) return;
        //循环唯一一个选中项，其他设置为默认
        for (int i = 0; i < layoutImgs.getChildCount(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) layoutImgs.getChildAt(i);
            ImageView ivIcon = (ImageView) relativeLayout.getChildAt(0);
            AppCompatCheckBox cbBox = (AppCompatCheckBox) relativeLayout.getChildAt(1);
            Bitmap bitmap = allImgBitmaps.get(i);
            if (cbBox.isChecked()) {
                ivIcon.setTag("isCode2Img");
                ShareHelper.createCode2BitmapToImageView(layoutImg, goodsInfo, bitmap, ivIcon);
            } else {
                ivIcon.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * 取消了二维码推广图片，还剩多张选中图片，多张选中图片第一张设置为要分享的二维码图片
     */
    private void setStartCode2ImageView() {
        if (goodsInfo == null) return;
        //循环唯一一个选中项，其他设置为默认
        for (int i = 0; i < layoutImgs.getChildCount(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) layoutImgs.getChildAt(i);
            ImageView ivIcon = (ImageView) relativeLayout.getChildAt(0);
            AppCompatCheckBox cbBox = (AppCompatCheckBox) relativeLayout.getChildAt(1);
            Bitmap bitmap = allImgBitmaps.get(i);
            if (cbBox.isChecked()) {
                ivIcon.setTag("isCode2Img");
                ShareHelper.createCode2BitmapToImageView(layoutImg, goodsInfo, bitmap, ivIcon);
                break;
            } else {
                ivIcon.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * 添加图片到滚动控件中
     * 根据url，获取到图片的Bitmap集合
     *
     * @param url 图片地址
     */
    private void addImageViews(String url) {
        //布局
        RelativeLayout relativeLayout = new RelativeLayout(_mActivity);
        int imgHeight = (ScreenUtils.getScreenWidth()-DpPxUtils.dp2px(32))/10*3;
        relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(imgHeight, LinearLayout.LayoutParams.MATCH_PARENT));
        //图片
        ImageView ivIcon = new ImageView(_mActivity);
        ivIcon.setBackgroundResource(R.drawable.rect_gray_rim_bg);
        int dp1 = DpPxUtils.dp2px(1);
        ivIcon.setPadding(dp1,dp1,dp1,dp1);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imgHeight, imgHeight);
        layoutParams.setMargins(DpPxUtils.dp2px(4),0,DpPxUtils.dp2px(4),0);
        ivIcon.setLayoutParams(layoutParams);
        Glide.with(_mActivity).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                allImgBitmaps.add(resource);
                ivIcon.setImageBitmap(resource);
            }
        });
        //选择器
        AppCompatCheckBox cbBox = new AppCompatCheckBox(_mActivity);
        cbBox.setButtonDrawable(R.drawable.check_box_soild_selecter);
        int dp32 = DpPxUtils.dp2px(32);
        int dp28 = DpPxUtils.dp2px(28);
        RelativeLayout.LayoutParams cbLayoutParams = new RelativeLayout.LayoutParams(dp28, dp32);
        cbLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cbBox.setLayoutParams(cbLayoutParams);
        //布局添加
        relativeLayout.addView(ivIcon);
        relativeLayout.addView(cbBox);
        layoutImgs.addView(relativeLayout);


        // 单张图片场景
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bm = ((BitmapDrawable) ivIcon.getDrawable()).getBitmap();
                new XPopup.Builder(getContext())
                        .asImageViewer(ivIcon, bm, new BigPicPopImageLoader())
                        .show();
            }
        });

        //选中图片
        cbBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    selectImgUrls.add(url);
//                    L.e("选中后" + selectImgUrls.toString());
                } else {
                    selectImgUrls.remove(url);
//                    L.e("取消后" + selectImgUrls.toString());
                    //如果取消选中之后，一张图片都没有了，那么再次选中此图片，保证至少包含一张图片
                    if (selectImgUrls.size() == 0) {
                        ToastUtil.showShort("至少选中一张图片！");
                        //oncheckchanged的意思是check changed的情况下才有可能调用，当他本身为true的时候，你再次设置setCheck为true的话当然也不会回调oncheckchanged这个函数了
                        cbBox.setChecked(true);
                        selectImgUrls.add(url);
                        L.e("至少选中一张图片==" + selectImgUrls.toString());
                        //如果取消后，只剩下一张图片，设置这张图片为推广二维码图片，并设置Tag
                    } else if (selectImgUrls.size() == 1) {
                        setOnlyCode2ImageView();
                        //如果取消后，还剩余多张图片，且取消的图片为二维码图片：取消当前图片的tag,并设置多张第一张为推广二维码图片，并打上标记
                    } else {
                        selectImgUrls.size();
                        String tag = (String) ivIcon.getTag();
                        if (!TextUtils.isEmpty(tag) && tag.equals("isCode2Img")) {
                            ivIcon.setTag("");
                            setStartCode2ImageView();
                        }

                    }
                }
            }
        });
//        L.e("选中的图片"+selectImgUrls.toString());
    }

    private void getPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        L.e("以获得权限" + data.toString());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        L.e("未获得权限" + data.toString());
                        ToastUtil.showShort("未获得存储权限，页面功能不用！");
                    }
                }).start();
    }

    /**
     * 1.批量存图
     */
    private void saveImgList() {
        if (selectImgUrls == null || selectImgUrls.size() == 0) return;
        getSelectFilesAndSave();

    }

    /**
     * 2.分享多张图片到微信个人
     */
    private void shareToWechat() {
        //1.存储图片，并得到图片地址集合
        List<File> files = getSelectFilesAndSave();
        //如果只有一张图片，那么使用微信SDK分享法
        ToastUtil.showLong("即将分享...");
        if (files.size() == 1) {
            shareToWechatOrCircle(SendMessageToWX.Req.WXSceneSession);
            return;
        }
        ArrayList<Uri> uriList = ShareUtils.getImageContentUris(files);
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        Intent shareIntent = new Intent();
        shareIntent.setComponent(comp);
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享多张图片"));
    }

    /**
     * 如果是选中状态，抽离出选中的Bitmap，保存后返回保存的File集合
     *
     * @return
     */
    @NotNull
    private ArrayList<File> getSelectFilesAndSave() {
        ArrayList<File> files = new ArrayList<>();
        for (int i = 0; i < layoutImgs.getChildCount(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) layoutImgs.getChildAt(i);
            AppCompatCheckBox cbBox = (AppCompatCheckBox) relativeLayout.getChildAt(1);
            if (cbBox.isChecked()) {
                ImageView image = (ImageView) relativeLayout.getChildAt(0);
                Bitmap bm = ((BitmapDrawable) image.getDrawable()).getBitmap();
                files.add(FileUtils.saveImage(bm));
            }
        }
        ToastUtil.showShort("已保存了" + files.size() + "张图片到相册！");
        return files;
    }

    /**
     * 3.分享到朋友圈
     */
    private void shareToWechatCircle() {
        //多图分享，需要开启辅助权限
        if (selectImgUrls.size() > 1) {
            shareToTimeLineUI();
            return;
        }
        //1.存储图片，并得到图片地址集合
        List<File> files = getSelectFilesAndSave();
        //如果只有一张图片，那么使用微信SDK分享法
        if (files.size() == 1) {
            shareToWechatOrCircle(SendMessageToWX.Req.WXSceneTimeline);
            return;
        }
//        ToastUtil.showLong("暂不支持多图分享到朋友圈，请批量存图后手动分享！");


    }

    /**
     * 跳转到微信
     */
    private void shareToTimeLineUI() {
        //1.验证是否开启了辅助权限
        checkServices(_mActivity);
    }
    BasePopupView basePopupView;
    private void checkServices(Context context) {
        if (!OpenAccessibilitySettingHelper.isAccessibilitySettingsOn(context, ShareAccessibilityService.class.getName())) {// 判断服务是否开启
            if (basePopupView == null) {
                OpenAccessibilityServicePop openPop = new OpenAccessibilityServicePop(getContext());
                openPop.setListener(() -> {
                    OpenAccessibilitySettingHelper.jumpToSettingPage(context);// 跳转到开启页面
                });
                basePopupView = new XPopup.Builder(getContext())
                        .asCustom(openPop);
                basePopupView.show();
            } else {
                basePopupView.show();
            }

        } else {
            L.e("ShareAccessibilityService服务已开启");
            String content = etContent.getText().toString();
            ShareAccessibilityService.SELECT_PIC_NUM = selectImgUrls.size();
            ShareAccessibilityService.IS_SHARE = true;
            ShareAccessibilityService.SHARE_CONTENT = TextUtils.isEmpty(content) ? goodsInfo.getItemTitle() : content;
            getSelectFilesAndSave();
            getWechatApi();

        }
    }

    /**
     * 跳转到微信
     */
    private void getWechatApi() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // TODO: handle exception
            ToastUtil.showLong("检查到您手机没有安装微信，请安装后使用该功能");
        }
    }


    /**
     * 4.分享【单张】图片给朋友,或朋友圈
     *
     * @param WXSceneSession
     */
    private void shareToWechatOrCircle(int WXSceneSession) {
        Bitmap bmp = null;
        for (int i = 0; i < layoutImgs.getChildCount(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) layoutImgs.getChildAt(i);
            AppCompatCheckBox cbBox = (AppCompatCheckBox) relativeLayout.getChildAt(1);
            if (cbBox.isChecked()) {
                ImageView image = (ImageView) relativeLayout.getChildAt(0);
                bmp = ((BitmapDrawable) image.getDrawable()).getBitmap();
                break;
            }
        }
        IWXAPI api = WXAPIFactory.createWXAPI(_mActivity, Contants.WX_OPEN_APP_ID, true);
        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        //设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 80, 80, true);
        int bytes = thumbBmp.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(bytes);
        thumbBmp.copyPixelsToBuffer(buf);
        byte[] byteArray = buf.array();
        msg.thumbData = byteArray;
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "" + WXSceneSession;
        req.message = msg;
        req.scene = WXSceneSession;
        //调用api接口，发送数据到微信
        api.sendReq(req);
    }

    /**
     * 分享图片到QQ
     */
    private void shareToQQ() {
        List<File> files = getSelectFilesAndSave();
        ArrayList<Uri> uriList = ShareUtils.getImageContentUris(files);
        ComponentName comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
        Intent shareIntent = new Intent();
        shareIntent.setComponent(comp);
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享多张图片"));
    }

    /**
     * 分享图片到QQ空间
     * qq空间由于qq空间页面activity不对外开放，所以qq空间分享只支持sdk分享，
     * 不支持原生qq空间分享，qq空间分享早期支持纯图片分享，但是google从android4.0开始，
     * 关闭对apn权限的自动获取，如果再使用qq分享sdk往qq空间分享纯图片就会抛出no permission to write apn setting或者分享 失败。
     * 1.12 分享到QQ空间（无需QQ登录）
     * 注意:：
     * 分享类型参数Tencent.SHARE_TO_QQ_KEY_TYPE，目前只支持图文分享（SHARE_TO_QZONE_TYPE_IMAGE_TEXT ）。
     * QZone接口暂不支持发送多张图片的能力，若传入多张图片，则会自动选入第一张图片作为预览图。多图的能力将会在以后支持。：
     * <p>
     * 官方说明文档：
     * http://wiki.open.qq.com/index.php?title=Android_API调用说明
     */
    private void shareToQzone() {
        if (selectImgUrls == null || selectImgUrls.size() == 0) {
            return;
        }
        ArrayList<String> imgUrls = new ArrayList<>();
        ArrayList<File> files = getSelectFilesAndSave();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            imgUrls.add(file.getAbsolutePath());
        }
        L.e("分享到QQ空间的图片==" + imgUrls.get(0));
        ToastUtil.showLong("QQ空间只支持单图分享，默认分享第一张！");
        String shareLink = "http://192.168.1.158:9000/share.html?id=" + goodsInfo.getItemId() + "&superCode=" + superCode;
        String content = etContent.getText().toString();
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, goodsInfo.getItemTitle());// 标题
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, TextUtils.isEmpty(content) ? goodsInfo.getItemTitle() : content);// 摘要
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareLink);// 内容地址
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrls);// 图片地址
        Tencent mTencent = Tencent.createInstance(Contants.QQ_APP_ID, _mActivity);
        mTencent.shareToQzone(_mActivity, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                ToastUtil.showLong("分享成功");
            }

            @Override
            public void onError(UiError uiError) {
                L.e(uiError.errorCode + " ==" + uiError.errorMessage + "  ==" + uiError.errorDetail);
                ToastUtil.showLong("分享异常：" + uiError.errorMessage);
            }

            @Override
            public void onCancel() {
                ToastUtil.showLong("分享取消");
            }
        });
    }



    @OnCheckedChanged({R.id.cb_show_income, R.id.cb_show_link, R.id.cb_show_invite_code})
    public void onCheckedChanged(CompoundButton checkBox, boolean checked) {
        //至少需要有一项选中才执行
        if (cbShowIncome.isChecked() || cbShowLink.isChecked() || cbShowInviteCode.isChecked()) {
            setCommentTxt();
        }
    }

    @OnClick({R.id.btn_copy_comment, R.id.tv_save_img, R.id.tv_wechat, R.id.tv_circle, R.id.tv_qq, R.id.tv_qzone})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.btn_copy_comment://复制评淘口令
                ClipboardUtils.copy(tvComment.getText().toString());
                break;
            case R.id.tv_save_img://批量存图
                saveImgList();
                break;
            case R.id.tv_wechat://分享到微信个人
                shareToWechat();
                break;
            case R.id.tv_circle://分享朋友圈
                shareToWechatCircle();
                break;
            case R.id.tv_qq://分享到QQ
                shareToQQ();
                break;
            case R.id.tv_qzone://分享到QQ空间
                shareToQzone();
                break;
        }
    }

}
