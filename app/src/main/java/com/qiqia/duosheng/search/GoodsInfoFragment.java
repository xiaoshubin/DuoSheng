package com.qiqia.duosheng.search;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBindFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.User;
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
            isCollect();//是否收藏
            //因为没有猜你喜欢内容，故隐藏猜你喜欢部分
            mBinding.tvGuesslike.setVisibility(View.GONE);
            GuideUtils.showGuide(this, mBinding.layoutCoupon, mBinding.layoutBuy);
        } else {//（好单库）如果id有值，就根据id查询商品详情，并根据id查询商品对应的猜你喜欢
            getGoodsInfo(id);//查询商品详情
            getGuessLike(id);//查询此商品对应的猜你喜欢
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
//    分享赚，自购返
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
                    }
                });
    }

    private void getGuessLike(String id) {
        dataProvider.shop.guessLike(id, 0)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<List<GoodsInfo>>>() {
                    @Override
                    protected void onSuccess(BaseResponse<List<GoodsInfo>> listBaseResponse) {
                        List<GoodsInfo> datas = listBaseResponse.getData();
                        recommandGoodsAdapter.setNewData(datas);
                    }


                });

    }

    private void initBanner() {
        List<String> images = goodsInfo.getItemPicList();
        if (images == null || images.size() == 0) {
            images.add(goodsInfo.getItemPic());
        }
        mBinding.banner.setImageLoader(new BannerImgLoader())
                .setDelayTime(5000)
                .setImages(images)
                .start();
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
                        drawCollectedImg(true);
                    }

                    @Override
                    protected void onErr(String msg) {
                    }

                });

    }

    private void drawCollectedImg(boolean isCollect) {
        mBinding.tvCollection.setText(isCollect ? "已收藏" : "收藏");
        Drawable img = _mActivity.getResources().getDrawable(isCollect ? R.mipmap.icon_collected : R.mipmap.icon_collect);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        mBinding.tvCollection.setCompoundDrawables(null, img, null, null);
    }


    private void onEvent() {
        //精选商品
        recommandGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsInfo item = (GoodsInfo) adapter.getItem(position);
                jumpToFragment(item);
            }
        });
        //不需要登录，单独列出
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popActivity();
            }
        });
        mBinding.ivBackArraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popActivity();
            }
        });
        mBinding.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popActivity();
            }
        });
    }

    @OnClick({R.id.layout_share, R.id.tv_get_coupon, R.id.tv_collection, R.id.tv_share_get, R.id.layout_buy})
    public void doClicks(View view) {
        if (user == null) {
            ToastUtil.showLong("请先登录！");
            goWhiteBarActivity(LoginFragment.class.getSimpleName());
            return;
        }
        switch (view.getId()) {
            case R.id.tv_get_coupon://获取优惠券
                openCouponLink();
                break;
            case R.id.tv_collection://收藏
                collect();
                break;
            case R.id.layout_share://创建分享
                start(CreateShareFragment.newInstance(goodsInfo));
                break;
            case R.id.layout_buy://跳淘宝购买
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
     */
    private void collect() {
        String s = mBinding.tvCollection.getText().toString();
        Observable<BaseResponse> collect = null;
        if (s.equals("收藏")) {
            int type = TextUtils.isEmpty(id) ? 1 : 0;
            String couponMoney = type == 1 ? goodsInfo.getCouponMoney() + "" : "";
            String commision = type == 1 ? goodsInfo.getCommision() : "";
            collect = dataProvider.user.collect(user.getUid(), user.getToken(), goodsInfo.getItemId(), type, couponMoney, commision, goodsInfo.getCouponLink());//请求收藏
        } else {
            collect = dataProvider.user.delCol(user.getUid(), user.getToken(), goodsInfo.getItemId());//取消收藏
        }
        collect.subscribe(new OnSuccessAndFailListener<BaseResponse>(dialog) {
            @Override
            protected void onSuccess(BaseResponse stringBaseResponse) {
                ToastUtil.showLong(stringBaseResponse.getMsg());
                drawCollectedImg(s.equals("收藏"));
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
