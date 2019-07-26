package com.qiqia.duosheng.search;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBindFragment;
import com.qiqia.duosheng.base.BaseResponse;
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

import java.util.Arrays;
import java.util.List;

import butterknife.OnClick;
import cn.com.smallcake_utils.SpannableStringUtils;
import cn.com.smallcake_utils.TimeUtils;
import cn.com.smallcake_utils.ToastUtil;
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
//        int navBarHeight = BarUtils.getStatusBarHeight();
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, navBarHeight);
//        mBinding.viewStatusBar.setLayoutParams(layoutParams);
        initView();

        /**
         *  id ==null?全网:好单库
         *  id ==null? 直接使用商品对象：通过id查商品详情
         */
        id = getArguments().getString("id");
        if (TextUtils.isEmpty(id)) {//（全网商品）id为null,说明传递过来的是商品对象集合，直接使用此对象，
            goodsInfo = (GoodsInfo) getArguments().getSerializable("goodsInfo");
            initData();
            isCollect();//直接获取的对象，就需要查询此商品是否已收藏
            //因为没有猜你喜欢内容，故隐藏猜你喜欢部分
            GuideUtils.showGuide(this, mBinding.layoutCoupon, mBinding.layoutBuy);
        } else {//（好单库）如果id有值，就根据id查询商品详情，并根据id查询商品对应的猜你喜欢
            getGoodsInfo(id);//查询商品详情,结果出来后再查询商品id对应的猜你喜欢
        }
        onEvent();


    }

    private void initView() {
        mBinding.recyclerViewRecommand.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        recommandGoodsAdapter = new GoodsVAdapter();
        mBinding.recyclerViewRecommand.setAdapter(recommandGoodsAdapter);
        //增加距离

    }

    /**
     * 缺少：
     * 1.预估收益
     * 2.是否可以升级Vip
     * 3.店铺名称，id,宝贝描述,卖家服务,物流服务
     * 4.宝贝详情
     * 5.分享赚，自购返，
     */
    private void initData() {
        if (goodsInfo == null) return;
        mBinding.setItem(goodsInfo);
        //设置顶部商品图片
        String commission_price = goodsInfo.getCommision();//预估收益
        String couponMoney = goodsInfo.getCouponMoney();//券值面额
        int endTime = goodsInfo.getCouponEndTime();//券到期时间
        int startTime = goodsInfo.getCouponStartTime();//券到期时间
        String itemDetailPic = goodsInfo.getItemDetailPic();//宝贝详情图片

        //优惠券和使用期限
        mBinding.layoutCoupon.setVisibility(Float.parseFloat(couponMoney) <= 0 ? View.GONE : View.VISIBLE);
        mBinding.tvCoupon.setText(String.format("%s元优惠券",couponMoney ));
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

        initBanner();
        GuideUtils.showGuide(this, mBinding.layoutCoupon, mBinding.layoutBuy);

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
                        initData();
                        getGuessLike(id);//查询此商品对应的猜你喜欢
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

    private void initBanner() {
        List<String> images = goodsInfo.getItemPicList();
        if (images == null || images.size() == 0) images = Arrays.asList(goodsInfo.getItemPic());
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
        BigPicsPopupView bigPicsPopupView = new BigPicsPopupView(getContext());
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
            jumpToFragment(item);
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
        AlibcUtils.openUrlNative(_mActivity, goodsInfo.getCouponLink());
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
