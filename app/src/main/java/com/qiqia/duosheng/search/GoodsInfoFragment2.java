package com.qiqia.duosheng.search;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBindFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.custom.BigPicPopImageLoader;
import com.qiqia.duosheng.custom.BigPicsPopupView;
import com.qiqia.duosheng.custom.OffsetGridLayoutManager;
import com.qiqia.duosheng.databinding.FragmentGoodsInfo2Binding;
import com.qiqia.duosheng.databinding.HeaderGoodsinfoTopBinding;
import com.qiqia.duosheng.login.LoginFragment;
import com.qiqia.duosheng.search.adapter.GoodsVAdapter;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.share.CreateShareFragment;
import com.qiqia.duosheng.utils.AlibcUtils;
import com.qiqia.duosheng.utils.BannerImgLoader;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.GuideUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;
import com.qiqia.duosheng.utils.TabCreateUtils;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.lang.ref.WeakReference;
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
import io.reactivex.Observable;

/**
 * 此页面包含了TabLayout和RecyclerView的滚动联动
 * 商品，详情，喜欢联动滚动位置
 */
public class GoodsInfoFragment2 extends BaseBindFragment<FragmentGoodsInfo2Binding> {

    GoodsVAdapter mAdapter;
    String id;//传递过来的商品id
    GoodsInfo goodsInfo;//查询出来的商品或者传递过来的商品
    User user;//收藏需要的参数
    HeaderGoodsinfoTopBinding bindTop;//顶部商品banner,详情，宝贝详情图片的binding
    int topBannerAndInfoHeight;//顶部商品banner和详情高度
    int topGoodsPicHeight;//顶部商品详情图片高度
    MyHandler myHandler;//滚动停止时，通知标签栏更新位置

    @Override
    public int setLayout() {
        return R.layout.fragment_goods_info2;
    }


    public static GoodsInfoFragment2 newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        GoodsInfoFragment2 fragment = new GoodsInfoFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    public static GoodsInfoFragment2 newInstance(GoodsInfo info) {
        Bundle args = new Bundle();
        args.putSerializable("goodsInfo", info);
        GoodsInfoFragment2 fragment = new GoodsInfoFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        user = DataLocalUtils.getUser();
        initView();
        //通过id判定商品来源：好单库还是全网
        assert getArguments() != null;
        id = getArguments().getString("id");
        if (TextUtils.isEmpty(id)) {//（全网商品）id为null,说明传递过来的是商品对象集合，直接使用此对象，
            goodsInfo = (GoodsInfo) getArguments().getSerializable("goodsInfo");
            initData(goodsInfo);
            isCollect();//直接获取的对象，就需要查询此商品是否已收藏
            showShareTakeNotieByTime();//显示分享赚的提示pop
            if (!TextUtils.isEmpty(goodsInfo.getItemId())) getGuessLike(goodsInfo.getItemId());//查询此商品对应的猜你喜欢
        } else {//（好单库）如果id有值，就根据id查询商品详情，并根据id查询商品对应的猜你喜欢
            getGoodsInfo(id);//查询商品详情,结果出来后再查询商品id对应的猜你喜欢，最后判定是否需要新手引导
        }
        //是全面屏，设置顶部电量栏高度
        if (ScreenUtils.isAllScreenDevice(this.getContext())) {
            mBinding.viewStatusBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getStatusHeight()));
        }
        //点击和滚动事件
        onEvent();


    }

    //大于一天，从新提示
    private void showShareTakeNotieByTime() {
        DateTime nowTime = DateTime.now();
        DateTime lastTime = SPUtils.readObject(SPStr.SHOW_SHARE_TAKE_TIME);
        if (lastTime == null || Days.daysBetween(lastTime, nowTime).getDays() > 0) {
            showShareTakeNotie();
            SPUtils.saveObject(SPStr.SHOW_SHARE_TAKE_TIME, DateTime.now());
        }
    }

    //显示分享赚的提示pop
    private void showShareTakeNotie() {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.pop_share_take, null);
        PopupWindow popBank = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popBank.setAnimationStyle(R.style.AnimFade);
        popBank.setBackgroundDrawable(new ColorDrawable(0));
        popBank.setOutsideTouchable(true);
        popBank.setFocusable(false);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = view.getMeasuredHeight();
        popBank.showAsDropDown(mBinding.tvCollection, 0, -DpPxUtils.dp2px(40) - popupHeight);
    }

    OffsetGridLayoutManager gridLayoutManager;//获取RecyclerView滚动距离，来显示和隐藏排序Tab

    private void initView() {

        gridLayoutManager = new OffsetGridLayoutManager(_mActivity, 2);
        gridLayoutManager.setSmoothScrollbarEnabled(true);//慢慢滚动
        mBinding.recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new GoodsVAdapter();
        mBinding.recyclerView.setAdapter(mAdapter);
        //头部banner和商品信息，券
        View headerTop = LayoutInflater.from(_mActivity).inflate(R.layout.header_goodsinfo_top, null);
        bindTop = DataBindingUtil.bind(headerTop);
        mAdapter.addHeaderView(headerTop);
        //顶部商品banner和详情高度测量，方便滚动到宝贝详情位置
        bindTop.layoutCoupon.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (topBannerAndInfoHeight < top) topBannerAndInfoHeight = top;
        });
        bindTop.ivDetailsPic.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (topGoodsPicHeight < bottom) topGoodsPicHeight = bottom;
        });
        myHandler = new MyHandler(_mActivity);

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
        bindTop.setItem(goodsInfo);
        //设置顶部商品图片
        String commission_price = goodsInfo.getCommision();//预估收益
        String couponMoney = goodsInfo.getCouponMoney();//券值面额
        int endTime = goodsInfo.getCouponEndTime();//券到期时间
        int startTime = goodsInfo.getCouponStartTime();//券到期时间
        String itemDetailPic = goodsInfo.getItemDetailPic();//宝贝详情图片

        //优惠券和使用期限
//        mBinding.layoutCoupon.setVisibility(Float.parseFloat(couponMoney) <= 0 ? View.GONE : View.VISIBLE);
        bindTop.tvCoupon.setText(Float.parseFloat(couponMoney) <= 0 ? "暂无优惠券" : String.format("%s元优惠券", couponMoney));
        if (startTime == 0 || endTime == 0) {
            bindTop.tvCouponTime.setVisibility(View.GONE);
        } else {
            bindTop.tvCouponTime.setVisibility(View.VISIBLE);
            bindTop.tvCouponTime.setText(String.format("使用期限：%s到%s", TimeUtils.tsToYMD(startTime), TimeUtils.tsToYMD(endTime)));
        }
        //宝贝详情图片
        bindTop.tvDetailsPic.setVisibility(TextUtils.isEmpty(itemDetailPic) ? View.GONE : View.VISIBLE);
        //分享赚，自购返
        mBinding.tvShareGet.setText(SpannableStringUtils.getBuilder(getString(R.string.share_take)).append(getString(R.string.rmb_symbol)).setProportion(0.6f).append(commission_price).create());
        mBinding.tvOwnbuyRecom.setText(SpannableStringUtils.getBuilder(getString(R.string.buy_return)).append(getString(R.string.rmb_symbol)).setProportion(0.6f).append(commission_price).create());

        initBanner(goodsInfo);
        GuideUtils.showGuide(this, bindTop.layoutCouponIn, mBinding.layoutBuy);//新手引导

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
                        bindTop.tvGuesslike.setVisibility(View.VISIBLE);
                        List<GoodsInfo> datas = listBaseResponse.getData();
                        mAdapter.setNewData(datas);
                    }
                });

    }

    private void initBanner(GoodsInfo goodsInfo) {
        List<String> images = goodsInfo.getItemPicList();
        if (images == null || images.size() == 0)
            images = Collections.singletonList(goodsInfo.getItemPic());
        List<String> finalImages = images;
        if (images.size() > 1) {
            bindTop.banner.setOnBannerListener(position -> showBigPics(position, finalImages));
        } else {
            bindTop.banner.setOnClickListener(v -> showBigPics(0, finalImages));
        }
        bindTop.banner.setImageLoader(new BannerImgLoader())
                .setDelayTime(5000)
                .setImages(images)
                .start();
    }

    private void showBigPics(int position, List<String> images) {
        BigPicsPopupView bigPicsPopupView = new BigPicsPopupView(Objects.requireNonNull(getContext()));
        bigPicsPopupView.setImageUrls(images);
        bigPicsPopupView.setSrcView(bindTop.banner, position);
        bigPicsPopupView.setXPopupImageLoader(new BigPicPopImageLoader());
        bigPicsPopupView.setSrcViewUpdateListener((popupView, index) ->
                bigPicsPopupView.updateSrcView(bindTop.banner, images.get(index))
        );
        new XPopup.Builder(getContext())
                .asCustom(bigPicsPopupView)
                .show();
    }

    private void isCollect() {
        if (user == null) return;
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

    int scrollHeight;//RecyclerView滚动的高度
    boolean isClickTab;//是否是点击的tab
    private void onEvent() {
        //猜你喜欢
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            GoodsInfo item = (GoodsInfo) adapter.getItem(position);
            goGoodsInfoFragment(item);
        });
        //不需要登录也可以返回，单独列出
        bindTop.ivBack.setOnClickListener(v -> popActivity());
        bindTop.tvGetCoupon.setOnClickListener(v -> {
            if (user == null) {
                ToastUtil.showLong("请先登录！");
                goWhiteBarActivity(LoginFragment.class.getSimpleName());
                return;
            }
            openCouponLink();
        });
        mBinding.ivBackArraw.setOnClickListener(v -> popActivity());
        //滚动显示顶部菜单栏，onScrollStateChanged执行优先于onScrolled方法
        mBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://滚动停止
                        if (isClickTab)myHandler.sendEmptyMessage(1);
                        else myHandler.sendEmptyMessage(0);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://手指 拖动
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://惯性滚动
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollHeight += dy; //滑动的距离
                //滚动距离和导航栏高度算出透明度，实现上滑隐藏，下滑渐现
                float alp = (float) scrollHeight / (float) DpPxUtils.dp2px(80);
                mBinding.layoutMenu.setAlpha(alp);
            }
        });

        //tabLayout和RecyclerView联动事件
        String[] names = new String[]{"商品", "详情", "喜欢"};
        TabCreateUtils.setWhiteTab(_mActivity, mBinding.magicIndicator, names, index -> {
            if (scrollHeight == 0) return;
            isClickTab=true;
            switch (index) {
                case 0://商品
                    mBinding.recyclerView.smoothScrollToPosition(0);
                    break;
                case 1://详情
                    int y = topBannerAndInfoHeight - scrollHeight;
                    mBinding.recyclerView.smoothScrollBy(0, y);
                    break;
                case 2://推荐
                    mBinding.recyclerView.smoothScrollToPosition(3);
                    break;
            }
        });
    }

    class MyHandler extends Handler {
        private WeakReference<Activity> reference;
        public MyHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            if (reference.get() != null) {
                switch (msg.what) {
                    case 0:
                        //停止位置为【商品】
                        if (scrollHeight <= topBannerAndInfoHeight) {
                            mBinding.magicIndicator.onPageSelected(0);
                        //停止位置为【详情】
                        } else if (scrollHeight > topBannerAndInfoHeight && scrollHeight <= topGoodsPicHeight) {
                            mBinding.magicIndicator.onPageSelected(1);
                        //停止位置为【喜欢】
                        } else {
                            mBinding.magicIndicator.onPageSelected(2);
                        }
                        break;
                    case 1://如果是点击的tab,不重新选择选项卡
                        isClickTab=false;
                        break;
                }
            }
        }
    }

    @OnClick({R.id.layout_share, R.id.tv_collection, R.id.layout_buy})
    public void doClicks(View view) {
        if (user == null) {
            ToastUtil.showLong("请先登录！");
            goWhiteBarActivity(LoginFragment.class.getSimpleName());
            return;
        }
        switch (view.getId()) {
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
     * <p>
     * int isCollect;//0未收藏 1已收藏
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
                goodsInfo.setIsCollect(goodsInfo.getIsCollect() == 1 ? 0 : 1);
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        AlibcTradeSDK.destory();
    }

    /**
     * 打开领取优惠券链接
     * 跳转到淘宝app领券
     */
    private void openCouponLink() {
        if (goodsInfo == null) return;
        String couponMoney = goodsInfo.getCouponMoney();
        if (Float.parseFloat(couponMoney) <= 0) {
            ToastUtil.showShort("暂无优惠券可领");
            return;
        }
        AlibcUtils.openUrlNative(_mActivity, goodsInfo.getCouponLink());
    }

    /**
     * ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();||ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
     * 隐藏了电量栏(包括电量栏数字)和导航栏 err:全面屏顶部圆弧区域为黑色，无法达到沉浸效果，普通屏幕效果良好
     * ImmersionBar.with(this).init();
     * 全面屏和普通屏幕都实现了沉浸效果，但电量栏数字仍然显示，会挡住商品图片顶部区域
     */
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).init();
    }
}


