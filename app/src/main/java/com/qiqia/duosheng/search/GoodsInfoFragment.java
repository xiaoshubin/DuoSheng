package com.qiqia.duosheng.search;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.login.LoginFragment;
import com.qiqia.duosheng.main.AlibcUtils;
import com.qiqia.duosheng.main.adapter.RecommandGoodsAdapter;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.share.CreateShareFragment;
import com.qiqia.duosheng.utils.BannerImgLoader;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.GuideUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.SpannableStringUtils;
import cn.com.smallcake_utils.TimeUtils;
import cn.com.smallcake_utils.ToastUtil;
import io.reactivex.Observable;

public class GoodsInfoFragment extends BaseBarFragment {

    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_commission_price)
    TextView tvCommissionPrice;
    @BindView(R.id.iv_shop_icon)
    ImageView ivShopIcon;
    @BindView(R.id.tv_goods_title)
    TextView tvGoodsTitle;
    @BindView(R.id.tv_org_price)
    TextView tvOrgPrice;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_coupon_time)
    TextView tvCouponTime;
    @BindView(R.id.tv_get_coupon)
    TextView tvGetCoupon;
    @BindView(R.id.tv_shop_title)
    TextView tvShopTitle;
    @BindView(R.id.iv_details_pic)
    ImageView ivDetailsPic;
    @BindView(R.id.tv_share_get)
    TextView tvShareGet;
    @BindView(R.id.tv_ownbuy_recom)
    TextView tvOwnbuyRecom;
    RecommandGoodsAdapter recommandGoodsAdapter;
    @BindView(R.id.recycler_view_recommand)
    RecyclerView recyclerViewRecommand;
    @BindView(R.id.layout_share)
    LinearLayout layoutShare;
    @BindView(R.id.layout_coupon)
    LinearLayout layoutCoupon;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.layout_buy)
    LinearLayout layoutBuy;

    @Override
    public int setLayout() {
        return R.layout.fragment_goods_info;
    }
    String id;//传递过来的商品id
    GoodsInfo goodsInfo;//查询出来的商品或者传递过来的商品
    User user;//收藏需要的参数
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

        /**
         *  id ==null?全网:好单库
         *  id ==null? 直接使用商品对象：通过id查商品详情
         */
         id = getArguments().getString("id");
        if (TextUtils.isEmpty(id)){//（全网商品）id为null,说明传递过来的是商品对象集合，直接使用此对象，
            goodsInfo = (GoodsInfo) getArguments().getSerializable("goodsInfo");
            initData();
            isCollect();//是否收藏
        }else {//（好单库）如果id有值，就根据id查询商品详情，并根据id查询商品对应的猜你喜欢
            getGoodsInfo(id);
            getGuessLike(id);
        }
        onEvent();
        GuideUtils.showGuide(this,layoutCoupon,layoutBuy);

    }

    private void isCollect() {
        dataProvider.user.isCollect(user.getUid(),user.getToken(),goodsInfo.getItemId())
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

    private void initView() {

        recyclerViewRecommand.setHasFixedSize(true);
        recyclerViewRecommand.setNestedScrollingEnabled(false);
        recyclerViewRecommand.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        recommandGoodsAdapter = new RecommandGoodsAdapter();
        recyclerViewRecommand.setAdapter(recommandGoodsAdapter);
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


    private void getGoodsInfo(String id) {
        dataProvider.shop.goodsInfo(user==null?"0":user.getUid(),id, 0)
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

    /**
     * 缺少：
     * 1.预估收益
     * 2.是否可以升级Vip
     * 3.店铺名称，id,宝贝描述,卖家服务,物流服务
     * 4.宝贝详情
     * 5.分享赚，自购返，
     *
     */
    private void initData() {
        if (goodsInfo==null)return;
        //设置顶部商品图片
        String pic = goodsInfo.getItemPic();//商品图片
        String price = goodsInfo.getItemEndPrice();//券后价
        String commission_price = goodsInfo.getCommision();//预估收益

        String org_price = goodsInfo.getItemPrice();//原价
        boolean isTmall = goodsInfo.getIsTmall() == 1;//是否是天猫 0否1是
        String title = goodsInfo.getItemTitle();//商品标题
        String quan_price = goodsInfo.getCouponMoney();//券值面额
        int endTime = goodsInfo.getCouponEndTime();//券到期时间
        int startTime = goodsInfo.getCouponStartTime();//券到期时间
        String couponLink = goodsInfo.getCouponLink();//券链接
        String shopName = goodsInfo.getShopName();//店铺名称
        List<String> itemPicList = goodsInfo.getItemPicList();//banner
        String itemDetailPic = goodsInfo.getItemDetailPic();//宝贝详情


        tvPrice.setText(price);
        tvCommissionPrice.setText("预估收益 ￥" + commission_price);
        tvOrgPrice.setText("￥" + org_price);
        tvOrgPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        ivShopIcon.setImageResource(isTmall ? R.mipmap.icon_tmall : R.mipmap.icon_tb);

        tvGoodsTitle.setText(title);
        tvCoupon.setText(quan_price + "元优惠券");
        if (startTime==0||endTime==0){
            tvCouponTime.setVisibility(View.GONE);
        }else {
            tvCouponTime.setVisibility(View.VISIBLE);
            tvCouponTime.setText("使用期限：" + TimeUtils.tsToYMD(startTime) + "到" + TimeUtils.tsToYMD(endTime));
        }
        tvShopTitle.setText(shopName);
        initBanner(itemPicList, pic);
        Glide.with(_mActivity).load(itemDetailPic).into(ivDetailsPic);

        //分享赚，自购返
        SpannableStringBuilder _commission_price = SpannableStringUtils.getBuilder("￥").setProportion(0.5f)
                .append(commission_price).create();
        tvShareGet.setText(_commission_price);
        tvOwnbuyRecom.setText(_commission_price);
        //是否已经收藏
        boolean isCollect = goodsInfo.getIsCollect()==1;
        drawCollectedImg(isCollect);

    }

    /**
     * 打开领取优惠券链接
     *  跳转到淘宝app领券
     */
    private void openCouponLink() {
        if (goodsInfo==null)return;
        AlibcUtils.openUrlNative(_mActivity,goodsInfo.getCouponLink());
    }

    /**
     * 跳转到淘宝购买
    */
    private void toTbBuy() {
        if (goodsInfo==null)return;
        AlibcUtils.openDetailNative(_mActivity,goodsInfo.getItemId());
    }

    private void initBanner(List<String> small_images, String pic) {
        List<String> images;
        if (small_images == null || small_images.size() == 0) {
            images = new ArrayList<>();
            images.add(pic);
        } else {
            images = small_images;
        }
        banner.setImageLoader(new BannerImgLoader());
        banner.setDelayTime(5000);
        banner.setImages(images);
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
            }
        });

    }



    @OnClick({R.id.tv_get_coupon, R.id.tv_collection, R.id.layout_share, R.id.layout_buy})
    public void doClicks(View view) {
        if (user==null){
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
     *  id ==null?全网:好单库
     *  id ==null? 直接使用商品对象：通过id查商品详情
     *
     *  type int否类型 默认0  0：好单库 1：淘宝全网
     *  couponMoney string否type为1时传  优惠券面额
     *  commision string否type为1时传  预估收入
     */
    private void collect() {

        String s = tvCollection.getText().toString();
        Observable<BaseResponse> collect = null;
        if (s.equals("收藏")){
            int type = TextUtils.isEmpty(id)?1:0;
            String couponMoney = type==1?goodsInfo.getCouponMoney():"";
            String commision = type==1?goodsInfo.getCommision():"";
            collect = dataProvider.user.collect(user.getUid(), user.getToken(), goodsInfo.getItemId(), type, couponMoney, commision,goodsInfo.getCouponLink());//请求收藏
        }else {
            collect=dataProvider.user.delCol(user.getUid(),user.getToken(),goodsInfo.getItemId());//取消收藏
        }
        collect.subscribe(new OnSuccessAndFailListener<BaseResponse>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse stringBaseResponse) {
                        ToastUtil.showLong(stringBaseResponse.getMsg());
                        drawCollectedImg(s.equals("收藏"));
                    }
                });
    }


    private void drawCollectedImg(boolean isCollect) {
        tvCollection.setText(isCollect?"已收藏":"收藏");
        Drawable img = this.getResources().getDrawable(isCollect?R.mipmap.icon_collected:R.mipmap.icon_collect);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        tvCollection.setCompoundDrawables(null, img, null, null);
    }
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
    }

}
