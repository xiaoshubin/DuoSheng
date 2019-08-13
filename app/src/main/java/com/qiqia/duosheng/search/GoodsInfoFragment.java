package com.qiqia.duosheng.search;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.GridLayoutManager;

import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBindFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.custom.BigPicPopImageLoader;
import com.qiqia.duosheng.custom.BigPicsPopupView;
import com.qiqia.duosheng.databinding.FragmentGoodsInfoBinding;
import com.qiqia.duosheng.login.LoginFragment;
import com.qiqia.duosheng.search.adapter.GoodsVAdapter;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.share.CreateShareFragment;
import com.qiqia.duosheng.utils.AlibcUtils;
import com.qiqia.duosheng.utils.BannerImgLoader;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.GuideUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.OnClick;
import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.SPUtils;
import cn.com.smallcake_utils.ScreenUtils;
import cn.com.smallcake_utils.SpannableStringUtils;
import cn.com.smallcake_utils.TimeUtils;
import cn.com.smallcake_utils.ToastUtil;
import cn.com.smallcake_utils.popup.GoodView;
import io.reactivex.Observable;

public class GoodsInfoFragment extends BaseBindFragment<FragmentGoodsInfoBinding>{

    GoodsVAdapter recommandGoodsAdapter;
    String id;//传递过来的商品id
    GoodsInfo goodsInfo;//查询出来的商品或者传递过来的商品
    User user;//收藏需要的参数

    @Override
    public int setLayout() {
        return R.layout.fragment_goods_info;
    }


    public static GoodsInfoFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        GoodsInfoFragment fragment = new GoodsInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static GoodsInfoFragment newInstance(GoodsInfo info) {
        Bundle args = new Bundle();
        args.putSerializable("goodsInfo", info);
        GoodsInfoFragment fragment = new GoodsInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        user = DataLocalUtils.getUser();
        initView();
//
//        /*
//         *  id ==null?全网:好单库
//         *  id ==null? 直接使用商品对象：通过id查商品详情
//         */
        assert getArguments() != null;
        id = getArguments().getString("id");
        if (TextUtils.isEmpty(id)) {//（全网商品）id为null,说明传递过来的是商品对象集合，直接使用此对象，
            goodsInfo = (GoodsInfo) getArguments().getSerializable("goodsInfo");
            initData(goodsInfo);
            isCollect();//直接获取的对象，就需要查询此商品是否已收藏
            //因为没有猜你喜欢内容，故隐藏猜你喜欢部分
            showShareTakeNotieByTime();
            if (!TextUtils.isEmpty(goodsInfo.getItemId()))getGuessLike(goodsInfo.getItemId());//查询此商品对应的猜你喜欢
        } else {//（好单库）如果id有值，就根据id查询商品详情，并根据id查询商品对应的猜你喜欢
            getGoodsInfo(id);//查询商品详情,结果出来后再查询商品id对应的猜你喜欢
        }
        onEvent();

        //是全面屏，设置顶部电量栏高度
        if (ScreenUtils.isAllScreenDevice(this.getContext())){
            int statusHeight = ScreenUtils.getStatusHeight();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusHeight);
            mBinding.viewStatusBar.setLayoutParams(layoutParams);
        }



    }
    //大于一天，从新提示
    private void showShareTakeNotieByTime() {
        DateTime nowTime = DateTime.now();
        DateTime lastTime =  SPUtils.readObject(SPStr.SHOW_SHARE_TAKE_TIME);
        if (lastTime==null||Days.daysBetween(lastTime, nowTime).getDays()>0){
            showShareTakeNotie();
            SPUtils.saveObject(SPStr.SHOW_SHARE_TAKE_TIME,DateTime.now());
        }
    }
    private void showShareTakeNotie(){
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.pop_share_take, null);
        PopupWindow popBank = new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popBank.setAnimationStyle(R.style.AnimFade);
        popBank.setBackgroundDrawable(new ColorDrawable(0));
        popBank.setOutsideTouchable(true);
        popBank.setFocusable(false);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight  = view.getMeasuredHeight();
        popBank.showAsDropDown(mBinding.tvCollection,0,-DpPxUtils.dp2px(40)-popupHeight);
    }

    private void initView() {
        mBinding.recyclerViewRecommand.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        recommandGoodsAdapter = new GoodsVAdapter();
        mBinding.recyclerViewRecommand.setAdapter(recommandGoodsAdapter);
    }

    /**
     * 缺少：
     * 1.预估收益
     * 2.是否可以升级Vip
     * 3.店铺名称，id,宝贝描述,卖家服务,物流服务
     * 4.宝贝详情
     * 5.分享赚，自购返，
     */
    public void initData(GoodsInfo goodsInfo) {
        if (goodsInfo == null) return;
        mBinding.setItem(goodsInfo);
        //设置顶部商品图片
        String commission_price = goodsInfo.getCommision();//预估收益
        String couponMoney = goodsInfo.getCouponMoney();//券值面额
        int endTime = goodsInfo.getCouponEndTime();//券到期时间
        int startTime = goodsInfo.getCouponStartTime();//券到期时间
        String itemDetailPic = goodsInfo.getItemDetailPic();//宝贝详情图片

        //优惠券和使用期限
//        mBinding.layoutCoupon.setVisibility(Float.parseFloat(couponMoney) <= 0 ? View.GONE : View.VISIBLE);
        mBinding.tvCoupon.setText(Float.parseFloat(couponMoney) <= 0 ?"暂无优惠券":String.format("%s元优惠券",couponMoney ));
        if (startTime == 0 || endTime == 0) {
            mBinding.tvCouponTime.setVisibility(View.GONE);
        } else {
            mBinding.tvCouponTime.setVisibility(View.VISIBLE);
            mBinding.tvCouponTime.setText(String.format("使用期限：%s到%s", TimeUtils.tsToYMD(startTime) , TimeUtils.tsToYMD(endTime)));
        }
        //宝贝详情图片
        mBinding.tvDetailsPic.setVisibility(TextUtils.isEmpty(itemDetailPic)?View.GONE:View.VISIBLE);
        //分享赚，自购返
        mBinding.tvShareGet.setText(SpannableStringUtils.getBuilder(getString(R.string.share_take)).append(getString(R.string.rmb_symbol)).setProportion(0.6f).append(commission_price).create());
        mBinding.tvOwnbuyRecom.setText(SpannableStringUtils.getBuilder(getString(R.string.buy_return)).append(getString(R.string.rmb_symbol)).setProportion(0.6f).append(commission_price).create());

        initBanner(goodsInfo);
        GuideUtils.showGuide(this, mBinding.layoutCouponIn, mBinding.layoutBuy);

    }

    private void getGoodsInfo(String id) {
        dataProvider.shop.goodsInfo(user == null ? "0" : user.getUid(), id, 0)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<GoodsInfo>>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse<GoodsInfo> listBaseResponse) {
                        goodsInfo = listBaseResponse.getData();
                        if (goodsInfo == null) {
                            ToastUtil.showLong("暂无商品详情信息！");
                            _mActivity.finish();
                            return;
                        }
                        initData(goodsInfo);
                        getGuessLike(id);//查询此商品对应的猜你喜欢
                        showShareTakeNotieByTime();
                    }
                });
    }

    private void getGuessLike(String id) {
        dataProvider.shop.guessLike(id, 0)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<List<GoodsInfo>>>() {
                    @Override
                    protected void onSuccess(BaseResponse<List<GoodsInfo>> listBaseResponse) {
                        mBinding.tvGuesslike.setVisibility(View.VISIBLE);
                        List<GoodsInfo> datas = listBaseResponse.getData();
                        recommandGoodsAdapter.setNewData(datas);
                    }
                });

    }

    private void initBanner(GoodsInfo goodsInfo) {
        List<String> images = goodsInfo.getItemPicList();
        if (images == null || images.size() == 0) images = Collections.singletonList(goodsInfo.getItemPic());
        List<String> finalImages = images;
        if (images.size()>1){
            mBinding.banner.setOnBannerListener(position -> showBigPics(position, finalImages));
        }else {
            mBinding.banner.setOnClickListener(v -> showBigPics(0, finalImages));
        }
        mBinding.banner.setImageLoader(new BannerImgLoader())
                .setDelayTime(5000)
                .setImages(images)
                .start();
    }

    private void showBigPics(int position, List<String> images) {
        BigPicsPopupView bigPicsPopupView = new BigPicsPopupView(Objects.requireNonNull(getContext()));
        bigPicsPopupView.setImageUrls(images);
        bigPicsPopupView.setSrcView(mBinding.banner, position);
        bigPicsPopupView.setXPopupImageLoader(new BigPicPopImageLoader());
        bigPicsPopupView.setSrcViewUpdateListener((popupView, index) ->
                bigPicsPopupView.updateSrcView(mBinding.banner, images.get(index))
        );
        new XPopup.Builder(getContext())
                .asCustom(bigPicsPopupView)
                .show();
    }

    private void isCollect() {
        if (user == null) {
            ToastUtil.showShort("请先登录！");
            goWhiteBarActivity(LoginFragment.class.getSimpleName());
            return;
        }
        dataProvider.user.isCollect(user.getUid(), user.getToken(), goodsInfo.getItemId())
                .subscribe(new OnSuccessAndFailListener<BaseResponse>() {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        goodsInfo.setIsCollect(1);
                    }

                    @Override
                    protected void onErr(String msg) {
                    }

                });

    }


    private void onEvent() {
        //精选商品
        recommandGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
            GoodsInfo item = (GoodsInfo) adapter.getItem(position);
            goGoodsInfoFragment(item);
        });
        //不需要登录也可以返回，单独列出
        mBinding.ivBack.setOnClickListener(v -> popActivity());
        mBinding.ivBackArraw.setOnClickListener(v -> popActivity());
        mBinding.tvTitle.setOnClickListener(v -> popActivity());
    }

    @OnClick({R.id.layout_share, R.id.tv_get_coupon, R.id.tv_collection, R.id.layout_buy})
    public void doClicks(View view) {
        if (user == null) {
            ToastUtil.showLong("请先登录！");
            goWhiteBarActivity(LoginFragment.class.getSimpleName());
            return;
        }
        switch (view.getId()) {
            case R.id.tv_get_coupon://立即领取优惠券
                openCouponLink();
                break;
            case R.id.tv_collection://收藏
                collect();
                break;
            case R.id.layout_share://分享赚
                start(CreateShareFragment.newInstance(goodsInfo));
                break;
            case R.id.layout_buy://自购返
                toTbBuy();
                break;

        }
    }

    /**
     * id ==null?全网:好单库
     * id ==null? 直接使用商品对象：通过id查商品详情
     * <p>
     * type int否类型 默认0  0：好单库 1：淘宝全网
     * couponMoney string否type为1时传  优惠券面额
     * commision string否type为1时传  预估收入
     *
     *  int isCollect;//0未收藏 1已收藏
     */
    private void collect() {
        Observable<BaseResponse> collectPost;
        if (0 == goodsInfo.getIsCollect()) {//未收藏
            //如果id不为null,说明是通过id查询出来的商品,收藏就需要传递优惠券和收益--》type int否类型 默认0  0：好单库 1：淘宝全网
            int type = TextUtils.isEmpty(id) ? 1 : 0;
            collectPost = dataProvider.user.collect(user.getUid(), user.getToken(),
                    goodsInfo.getItemId(), type, goodsInfo.getCouponMoney(), goodsInfo.getCommision(), goodsInfo.getCouponLink());//请求收藏
        } else {
            collectPost = dataProvider.user.delCol(user.getUid(), user.getToken(), goodsInfo.getItemId());//取消收藏
        }
        collectPost.subscribe(new OnSuccessAndFailListener<BaseResponse>(dialog) {
            @Override
            protected void onSuccess(BaseResponse stringBaseResponse) {
                ToastUtil.showShort(stringBaseResponse.getMsg());
                animCollectColor(0 == goodsInfo.getIsCollect());
                goodsInfo.setIsCollect(goodsInfo.getIsCollect()==1?0:1);
            }
        });
    }

    /**
     * 跳转到淘宝购买
     */
    private void toTbBuy() {
        if (goodsInfo == null) return;
        AlibcUtils.openDetailNative(_mActivity, goodsInfo.getItemId());
    }

    /**
     * 打开领取优惠券链接
     * 跳转到淘宝app领券
     */
    private void openCouponLink() {
        if (goodsInfo == null) return;
        String couponMoney = goodsInfo.getCouponMoney();
        if (Float.parseFloat(couponMoney) <= 0){
            ToastUtil.showShort("暂无优惠券可领");
            return;
        }
        AlibcUtils.openUrlNative(_mActivity, goodsInfo.getCouponLink());
    }

    private void animCollectColor(boolean isCollect){
        if (isCollect){
            GoodView img = new GoodView(_mActivity);
            img.setImage(R.mipmap.icon_collected);
            img.show(mBinding.tvCollection);
        }
        ObjectAnimator animator = ObjectAnimator.ofInt(mBinding.tvCollection,"textColor", isCollect?0xff707070:0xffFF5836, isCollect?0xffFF5836:0xff707070);
        animator.setDuration(300);
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
    }

    /**
     *  ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();||ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
     *  隐藏了电量栏(包括电量栏数字)和导航栏 err:全面屏顶部圆弧区域为黑色，无法达到沉浸效果，普通屏幕效果良好
     *   ImmersionBar.with(this).init();
     *   全面屏和普通屏幕都实现了沉浸效果，但电量栏数字仍然显示，会挡住商品图片顶部区域
     *
     */
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).init();
    }
}


