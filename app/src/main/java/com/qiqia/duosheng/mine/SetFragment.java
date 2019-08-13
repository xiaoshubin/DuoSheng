package com.qiqia.duosheng.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.lxj.xpopup.XPopup;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBindBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.databinding.FragmentSetBinding;
import com.qiqia.duosheng.dialog.DownloadCircleDialog;
import com.qiqia.duosheng.login.LoginFragment;
import com.qiqia.duosheng.main.MainViewPagerFragment;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.DownloadUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;
import com.smallcake.okhttp.FileRequestBody;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.OnClick;
import cn.com.smallcake_utils.AppUtils;
import cn.com.smallcake_utils.DataCleanUtil;
import cn.com.smallcake_utils.FileUtils;
import cn.com.smallcake_utils.FormatUtils;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.SPUtils;
import cn.com.smallcake_utils.SdUtils;
import cn.com.smallcake_utils.ToastUtil;
import cn.com.smallcake_utils.dialog.BottomMenuDialog;
import cn.com.smallcake_utils.dialog.CakeResolveDialog;
import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class SetFragment extends BaseBindBarFragment<FragmentSetBinding> {

    AlibcLogin alibcLogin;
    User user;
    DownloadCircleDialog dialogProgress;
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
//        ImmersionBar.with(this).statusBarColor(R.color.transparent).autoStatusBarDarkModeEnable(true, 0.2f).init();
        initUser();
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
        mBinding.layoutBar.setBackgroundColor(Color.TRANSPARENT);
        //获取缓存大小，清理缓存
        setCacheSize();
        //版本号设置
        String versionName = AppUtils.getVersionName();
        mBinding.tvVersionName.setText("版本V" + versionName);
        //淘宝授权信息设置
        alibcLogin = AlibcLogin.getInstance();
        if (alibcLogin.isLogin()) {
            Session session = AlibcLogin.getInstance().getSession();
            mBinding.tvTbAuthLogin.setText(session.nick);
        }
        //下载更新
        dialogProgress = new DownloadCircleDialog(_mActivity);
    }


    private void setCacheSize() {
        try {
            String totalCacheSize = DataCleanUtil.getTotalCacheSize(_mActivity);
            mBinding.tvCleanCache.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUser() {
        user = DataLocalUtils.getUser();
        if (user == null) return;
        mBinding.setItem(user);
    }

    @OnClick({R.id.layout_nick, R.id.tv_nick, R.id.layout_update_phone, R.id.tv_update_phone, R.id.layout_update_wxkf, R.id.tv_update_wxkf,
            R.id.tv_notice, R.id.layout_tb_auth_login, R.id.tv_tb_auth_login,
            R.id.btn_login_out, R.id.layout_clean_cache, R.id.tv_clean_cache, R.id.tv_about_us, R.id.layout_sex, R.id.tv_sex, R.id.iv_head
            , R.id.layout_version_name, R.id.tv_version_name})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.iv_head:
//                selectPic();
                break;
            case R.id.layout_nick:
            case R.id.tv_nick:
                start(new UpdateNickNameFragment());
                break;
            case R.id.layout_update_phone:
            case R.id.tv_update_phone:
                start(new UpdatePhoneFragment());
                break;
            case R.id.layout_update_wxkf:
            case R.id.tv_update_wxkf:
                start(new UpdateWxkfFragment());
                break;
            case R.id.layout_sex:
            case R.id.tv_sex:
                showUpdateSexDialog();
                break;
            case R.id.tv_notice:
                start(new NoticeSetFragment());
                break;
            case R.id.layout_tb_auth_login:
            case R.id.tv_tb_auth_login:
                tbAuthLogin();
                break;
            case R.id.btn_login_out:
                loginOut();
                break;
            case R.id.tv_about_us:
                start(new AboutUsFragment());
                break;
            case R.id.layout_clean_cache:
            case R.id.tv_clean_cache:
                new XPopup.Builder(getContext())
                        .asConfirm(getString(R.string.app_name), "确认要清除缓存吗？",
                                () -> {
                                    DataCleanUtil.clearAllCache(getContext());
                                    ToastUtil.showShort("清理成功！");
                                    mBinding.tvCleanCache.setText("");
                                })
                        .show();
                break;
            case R.id.layout_version_name:
            case R.id.tv_version_name:
                showNewVersion();
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
                        Glide.with(_mActivity).load(headPicUrl).apply(options).into(mBinding.ivHead);
                    }

                });
    }


    /**
     * 淘宝授权登录成功后可以获取：
     * nick:昵称ava 头像 ,openId,openSid
     */
    private void tbAuthLogin() {
        if (alibcLogin.isLogin()) {
            ToastUtil.showLong("你已经授权成功！");
            return;
        }
        alibcLogin.showLogin(_mActivity, new AlibcLoginCallback() {
            @Override
            public void onSuccess() {
                Session session = alibcLogin.getSession();
                mBinding.tvTbAuthLogin.setText(session.nick);
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
     * 1.清空user用户数据
     * 2.主页选中第一项
     * 3.跳转到登录页面，关闭本Fragment和其基于的Activity
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
        new BottomMenuDialog.BottomMenuBuilder()
                .addItem("男", v -> updateSex("1"))
                .addItem("女", v -> updateSex("0"))
                .addItem("取消", null)
                .build().show(this.getFragmentManager());
    }

    private void updateSex(String sexStr) {
        dataProvider.user.info(user.getUid(), 1, sexStr, user.getToken())
                .subscribe(new OnSuccessAndFailListener<BaseResponse>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        mBinding.tvSex.setText(sexStr.equals("0") ? "女" : "男");
                        user.setSex(sexStr);
                        DataLocalUtils.saveUser(user);
                        ToastUtil.showLong("修改成功！");
                    }
                });
    }


    //1.权限申请，通过后开始下载
    private void showNewVersion() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(data -> {
                    L.e("以获得权限" + data.toString());
                    new AlertDialog.Builder(_mActivity).setTitle("软件更新").setMessage("发现新版本")
                            .setPositiveButton("确定", (dialog, which) -> {
                                String down_url = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk";
                                downloadApk(_mActivity, down_url);
                            })
                            .setNegativeButton("取消",null).show();

                })
                .onDenied(data -> {
                    L.e("未获得权限" + data.toString());
                    new AlertDialog.Builder(_mActivity).setTitle("权限申请").setMessage("更新应用需要存储权限")
                            .setPositiveButton("去开启", (dialog, which) -> {
                                AppUtils.goIntentSetting();
                            })
                            .setNegativeButton("取消",null).show();

                }).start();
    }
    //2.开始下载apk
    public void downloadApk(final Activity context, String down_url) {
        dialogProgress.show();
        DownloadUtils.getInstance().download(down_url, SdUtils.getDownloadPath(), "QQ.apk", new DownloadUtils.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                dialogProgress.dismiss();
                L.i("恭喜你下载成功，开始安装！==" + SdUtils.getDownloadPath() + "QQ.apk");
                ToastUtil.showShort("恭喜你下载成功，开始安装！");
                String successDownloadApkPath = SdUtils.getDownloadPath() + "QQ.apk";
                installApkO(_mActivity, successDownloadApkPath);
            }
            @Override
            public void onDownloading(ProgressInfo progressInfo) {
                dialogProgress.setProgress(progressInfo.getPercent());
                boolean finish = progressInfo.isFinish();
                if (!finish) {
                    long speed = progressInfo.getSpeed();
                    dialogProgress.setMsg("(" + (speed > 0 ? FormatUtils.formatSize(context, speed) : speed) + "/s)正在下载...");
                } else {
                    dialogProgress.setMsg("下载完成！");
                }
            }
            @Override
            public void onDownloadFailed() {
                dialogProgress.dismiss();
                ToastUtil.showShort("下载失败！");
            }
        });

    }
    // 3.下载成功，开始安装,兼容8.0安装位置来源的权限
    private void installApkO(Context context, String downloadApkPath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //是否有安装位置来源的权限
            boolean haveInstallPermission = _mActivity.getPackageManager().canRequestPackageInstalls();
            if (haveInstallPermission) {
                L.i("8.0手机已经拥有安装未知来源应用的权限，直接安装！");
                AppUtils.installApk(context, downloadApkPath);
            } else {
                new CakeResolveDialog(context, "安装应用需要打开安装未知来源应用权限，请去设置中开启权限", new CakeResolveDialog.OnOkListener() {
                    @Override
                    public void onOkClick() {
                        Uri packageUri = Uri.parse("package:"+ AppUtils.getAppPackageName());
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageUri);
                        startActivityForResult(intent,10086);
                    }
                }).show();
            }
        } else {
            AppUtils.installApk(context, downloadApkPath);
        }
    }
    //4.开启了安装未知来源应用权限后，再次进行步骤3的安装。
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086) {
            L.i("设置了安装未知应用后的回调。。。");
            String successDownloadApkPath = SdUtils.getDownloadPath() + "QQ.apk";
            installApkO(_mActivity, successDownloadApkPath);
        }else if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                compressPic(uri);
            }
        }
    }
}
