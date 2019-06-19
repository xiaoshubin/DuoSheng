package com.qiqia.duosheng.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.User;
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
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.smallcake_utils.EditTextUtils;
import cn.com.smallcake_utils.FileUtils;
import cn.com.smallcake_utils.FormatUtils;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.SdUtils;
import cn.com.smallcake_utils.ToastUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class FeedBackFragment extends BaseBarFragment {

    @BindViews({R.id.iv_img1, R.id.iv_img2, R.id.iv_img3})
    List<ImageView> imageViews;
    String[] uploadImgUrls;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_input_desc)
    TextView tvInputDesc;
    @BindView(R.id.iv_img1)
    ImageView ivImg1;
    @BindView(R.id.iv_img2)
    ImageView ivImg2;
    @BindView(R.id.iv_img3)
    ImageView ivImg3;
    Unbinder unbinder;

    @Override
    public int setLayout() {
        return R.layout.fragment_feed_back;
    }

    User user;

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("意见反馈");
        user = DataLocalUtils.getUser();
        uploadImgUrls = new String[imageViews.size()];
        onEvent();

    }

    private void onEvent() {
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                tvInputDesc.setText("您还能输入"+(300-length)+"个字");
            }
        });
    }

    @OnClick({R.id.iv_img1, R.id.iv_img2, R.id.iv_img3,R.id.btn_submit})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.iv_img1:
                selectPic(0);
                break;
            case R.id.iv_img2:
                selectPic(1);
                break;
            case R.id.iv_img3:
                selectPic(2);
                break;
            case R.id.btn_submit:
               submit();
                break;
        }
    }

    private void submit() {
        if (EditTextUtils.isEmpty(etContent))return;
        String content = etContent.getText().toString();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < uploadImgUrls.length; i++) {
            String uploadImgUrl = uploadImgUrls[i];
            if (!TextUtils.isEmpty(uploadImgUrl)){
                buffer.append(uploadImgUrl);
                if (i<uploadImgUrls.length)buffer.append(",");
            }
        }
        String imgs  =buffer.toString();
        dataProvider.user.feedBack(user.getUid(),user.getToken(),content,imgs)
                .subscribe(new OnSuccessAndFailListener<BaseResponse>() {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        ToastUtil.showLong(baseResponse.getMsg());
                        popActivity();
                    }
                });
    }


    private void selectPic(int imgPosition) {
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
                        startActivityForResult(intent, imgPosition);
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
        // 从相册返回的数据
        if (data != null) {
            // 得到图片的全路径
            Uri uri = data.getData();
            compressPic(uri, requestCode);
        }
    }

    //压缩图片
    private void compressPic(Uri uri, int requestCode) {
        String realPathFromURI = FileUtils.getRealPathFromURI(uri);
        File file = new File(realPathFromURI);
        Luban.with(_mActivity)
                .load(file)
                .ignoreBy(1000)
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
                        uploadPic(file, requestCode);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        L.e("压缩失败==" + e.getMessage());
                    }
                }).launch();
    }

    //上传图片到阿里云，获得上传的地址
    private void uploadPic(File file, int requestCode) {
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
        dataProvider.user.uploadImg(body).subscribe(new OnSuccessAndFailListener<BaseResponse>() {
            @Override
            protected void onSuccess(BaseResponse baseResponse) {
                String headPicUrl = (String) baseResponse.getData();
                ImageView imageView = imageViews.get(requestCode);
                Glide.with(_mActivity).load(file).apply(new RequestOptions().centerCrop()).into(imageView);//显示图片
                uploadImgUrls[requestCode] = headPicUrl;//添加上传图片成功后的地址到要提交的反馈信息中
                if (requestCode < imageViews.size() - 1)
                    imageViews.get(requestCode + 1).setVisibility(View.VISIBLE);//如果不是最后一张图片，显示下一张要上传的图片

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
