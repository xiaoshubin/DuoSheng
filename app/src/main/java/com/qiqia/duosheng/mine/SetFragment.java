package com.qiqia.duosheng.mine;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.lsxiao.apollo.core.annotations.Receive;
import com.lxj.xpopup.XPopup;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.EventStr;
import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.login.LoginFragment;
import com.qiqia.duosheng.main.MainViewPagerFragment;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;
import com.smallcake.okhttp.FileRequestBody;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.smallcake_utils.AppUtils;
import cn.com.smallcake_utils.DataCleanUtil;
import cn.com.smallcake_utils.FileUtils;
import cn.com.smallcake_utils.FormatUtils;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.SPUtils;
import cn.com.smallcake_utils.SdUtils;
import cn.com.smallcake_utils.ToastUtil;
import cn.com.smallcake_utils.dialog.BottomMenuDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class SetFragment extends BaseBarFragment {
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_clean_cache)
    TextView tvCleanCache;
    @BindView(R.id.tv_about_us)
    TextView tvAboutUs;
    @BindView(R.id.btn_login_out)
    Button btnLoginOut;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_tb_auth_login)
    TextView tvTbAuthLogin;
    @BindView(R.id.tv_update_phone)
    TextView tvUpdatePhone;
    @BindView(R.id.tv_update_wxkf)
    TextView tvUpdateWxkf;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    AlibcLogin alibcLogin;
    User user;
    @BindView(R.id.layout_bar)
    LinearLayout layoutBar;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    Unbinder unbinder;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
//        ImmersionBar.with(this).statusBarColor(R.color.transparent).autoStatusBarDarkModeEnable(true, 0.2f).init();
    }

    public static SetFragment newInstance() {
        Bundle args = new Bundle();
        SetFragment fragment = new SetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_set;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("设置");
        tvTitle.setTextColor(Color.WHITE);
        ivBack.setImageResource(R.mipmap.icon_backwhite);
        layoutBar.setBackgroundColor(Color.TRANSPARENT);


        //初始化个人信息
        initUser();
        //获取缓存大小，清理缓存
        setCacheSize();
        //版本号设置
        String versionName = AppUtils.getVersionName();
        tvVersionName.setText("版本V" + versionName);
        //淘宝授权信息设置
        alibcLogin = AlibcLogin.getInstance();

        if (alibcLogin.isLogin()) {
            Session session = AlibcLogin.getInstance().getSession();
            tvTbAuthLogin.setText(session.nick);
        }
    }


    private void setCacheSize() {
        try {
            String totalCacheSize = DataCleanUtil.getTotalCacheSize(_mActivity);
            tvCleanCache.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUser() {
        user = DataLocalUtils.getUser();
        if (user == null) return;
        //1.头像
        String headimgurl = user.getHeadimgurl();
        RequestOptions options = new RequestOptions();
        options.transform(new CircleCrop()).placeholder(R.mipmap.logo).error(R.mipmap.logo);
        Glide.with(this).load(headimgurl).apply(options).into(ivHead);
        //2.昵称
        tvNick.setText(user.getNickname());
        //3.性别
        tvSex.setText(user.getSex().equals("0") ? "女" : "男");
        //4.手机号
        tvUpdatePhone.setText(user.getPhone());
        //5.微信客服
        tvUpdateWxkf.setText(user.getWx());

    }

    @Receive(EventStr.UPDATE_NICKNAME)
    public void updateNick(String nick) {
        tvNick.setText(nick);
    }

    @Receive(EventStr.UPDATE_PHONE)
    public void updatePhoneString(String phone) {
        tvUpdatePhone.setText(phone);
    }

    @Receive(EventStr.UPDATE_WXKF)
    public void updateWxkf(String wxkf) {
        tvUpdateWxkf.setText(wxkf);
    }


    @OnClick({R.id.tv_nick, R.id.tv_update_phone, R.id.tv_update_wxkf, R.id.tv_notice, R.id.tv_tb_auth_login, R.id.btn_login_out, R.id.tv_clean_cache,
            R.id.tv_about_us, R.id.tv_sex, R.id.iv_head})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.iv_head:
//                selectPic();
                break;
            case R.id.tv_nick:
                start(new UpdateNickNameFragment());
                break;
            case R.id.tv_update_phone:
                start(new UpdatePhoneFragment());
                break;
            case R.id.tv_update_wxkf:
                start(new UpdateWxkfFragment());
                break;
            case R.id.tv_sex:
                showUpdateSexDialog();
                break;
            case R.id.tv_notice:
                start(new NoticeSetFragment());
                break;
            case R.id.tv_tb_auth_login:
                tbAuthLogin();
                break;
            case R.id.btn_login_out:
                loginOut();
                break;
            case R.id.tv_about_us:
                start(new AboutUsFragment());
                break;
            case R.id.tv_clean_cache:
                new XPopup.Builder(getContext()).asConfirm(getString(R.string.app_name), "确认要清除缓存吗？",
                        () -> {
                            DataCleanUtil.clearAllCache(getContext());
                            ToastUtil.showShort("清理成功！");
                            tvCleanCache.setText("");
                        })
                        .show();
                break;
            default:
                ToastUtil.showLong("开发中...");
                break;
        }
    }

    private void selectPic() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        L.e("以获得权限" + data.toString());
                        //1.从相册获取照片
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 2);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        L.e("未获得权限" + data.toString());
                    }
                }).start();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                compressPic(uri);
            }
        }
    }

    //压缩图片
    private void compressPic(Uri uri) {
        String realPathFromURI = FileUtils.getRealPathFromURI(uri);
        File file = new File(realPathFromURI);
        uploadPic(file);
        if (true) return;
        Luban.with(_mActivity)
                .load(file)
                .ignoreBy(2000)
                .setTargetDir(SdUtils.getCameraPath())
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        L.e("压缩onStart==");
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        L.e("压缩成功==" + file.getAbsolutePath());
                        uploadPic(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        L.e("压缩失败==" + e.getMessage());
                    }
                }).launch();
    }

    //上传图片到阿里云，获得上传的地址
    private void uploadPic(File file) {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", user.getUid());
        map.put("token", user.getToken());
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        for (String key : map.keySet()) builder.addFormDataPart(key, map.get(key));
        builder.addFormDataPart("file", "headPic.jpg", RequestBody.create(MediaType.parse("image/jpg"), file));
        FileRequestBody body = new FileRequestBody(builder.build(), new FileRequestBody.LoadingListener() {
            @Override
            public void onProgress(long currentLength, long contentLength) {
                L.e("上传进度==" + FormatUtils.getProgress(currentLength, contentLength) + "%");
            }
        });
        dataProvider.user.updateAvatar(body).subscribe(new OnSuccessAndFailListener<BaseResponse>() {
            @Override
            protected void onSuccess(BaseResponse baseResponse) {
                String headPicUrl = (String) baseResponse.getData();
                updateHead(headPicUrl);
            }
        });

    }

    /**
     * 使用图片上传地址，提交修改头像
     *
     * @param headPicUrl
     */
    private void updateHead(String headPicUrl) {
        dataProvider.user.info(user.getUid(), 2, headPicUrl, user.getToken())
                .subscribe(new OnSuccessAndFailListener<BaseResponse>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        user.setHeadimgurl(headPicUrl);
                        DataLocalUtils.saveUser(user);
                        ToastUtil.showLong("修改成功！");
                        RequestOptions options = new RequestOptions();
                        options.transform(new CircleCrop()).placeholder(R.mipmap.logo).error(R.mipmap.logo);
                        Glide.with(_mActivity).load(headPicUrl).apply(options).into(ivHead);
                    }

                });
    }


    /**
     * 淘宝授权
     * 登录成功后可以获取：
     * nick:昵称
     * ava 头像 ,
     * openId,
     * openSid
     */
    private void tbAuthLogin() {
        if (alibcLogin.isLogin()) {
            ToastUtil.showLong("你已经授权成功！");
            return;
        }
        alibcLogin.showLogin(_mActivity, new AlibcLoginCallback() {
            @Override
            public void onSuccess() {
//                ToastUtil.showLong("授权成功！");
                Session session = alibcLogin.getSession();
                tvTbAuthLogin.setText(session.nick);
                //获取淘宝用户信息
                L.e("获取淘宝用户信息: " + session);
            }

            @Override
            public void onFailure(int code, String msg) {
                L.e(code + "淘宝登录失败: " + msg);
            }
        });
    }

    /**
     * 退出登录
     */
    private void loginOut() {
        new XPopup.Builder(getContext()).asConfirm(getString(R.string.app_name), "确认要退出吗？",
                () -> {
                    SPUtils.remove(SPStr.USER);
                    ((RadioButton) MainViewPagerFragment.rgBottomTab.getChildAt(0)).setChecked(true);
                    goWhiteBarActivity(LoginFragment.class.getSimpleName());
                    pop();
                    _mActivity.finish();

//                    AlibcTradeSDK.destory();
//                    AlibcLogin.getInstance().logout(_mActivity, new LogoutCallback() {
//                        @Override
//                        public void onSuccess() {
//                            L.e("onSuccess");
//                        }
//
//                        @Override
//                        public void onFailure(int i, String s) {
//                            L.e("onFailure=="+s);
//                        }
//                    });
                })
                .show();
    }

    private void showUpdateSexDialog() {
        new BottomMenuDialog.BottomMenuBuilder().addItem("男", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSex("1");
            }
        }).addItem("女", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSex("0");

            }
        }).addItem("取消", null).build().show(this.getFragmentManager());
    }

    private void updateSex(String sexStr) {
        dataProvider.user.info(user.getUid(), 1, sexStr, user.getToken())
                .subscribe(new OnSuccessAndFailListener<BaseResponse>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        tvSex.setText(sexStr.equals("0") ? "女" : "男");
                        user.setSex(sexStr);
                        DataLocalUtils.saveUser(user);
                        ToastUtil.showLong("修改成功！");
                    }


                });
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
