package com.qiqia.duosheng.impl;

import com.qiqia.duosheng.api.ShopApi;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.BannerResponse;
import com.qiqia.duosheng.bean.GoodsList;
import com.qiqia.duosheng.bean.GoodsListAll;
import com.qiqia.duosheng.bean.HotWords;
import com.qiqia.duosheng.bean.IndexAd;
import com.qiqia.duosheng.bean.IndexTextAd;
import com.qiqia.duosheng.main.bean.Classfiy;
import com.qiqia.duosheng.main.bean.IndexClassfiy;
import com.qiqia.duosheng.main.bean.IndexResponse;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.share.bean.ShareList;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import static com.smallcake.okhttp.retrofit2.RetrofitComposeUtils.bindIoUI;

public class ShopImpl implements ShopApi {
    @Inject
    ShopApi shopApi;
    @Inject
    public ShopImpl() {
    }

    @Override
    public Observable<BaseResponse<GoodsListAll>> search(String keyWord, Integer Order, Integer Pn,Integer AllPn, Integer startPrice, Integer endPrice, Integer cid, Integer type) {
        return bindIoUI(shopApi.search(  keyWord,  Order,  Pn,AllPn,  startPrice,  endPrice,  cid,  type));
    }

    @Override
    public Observable<BaseResponse<GoodsList>> analysisUrl(String url, Integer type) {
        return bindIoUI(shopApi.analysisUrl(url,type));
    }

    @Override
    public Observable<BaseResponse<List<HotWords>>> hotWords() {
        return bindIoUI(shopApi.hotWords());
    }

    @Override
    public Observable<BaseResponse<GoodsInfo>> goodsInfo(String uid,String id, Integer type) {
        return bindIoUI(shopApi.goodsInfo( uid,id,  type));
    }

    @Override
    public Observable<BaseResponse<IndexResponse>> index() {
        return bindIoUI(shopApi.index());
    }

    @Override
    public Observable<BaseResponse<GoodsList>> leaderboard(Integer type, Integer page,Integer cid) {
        return bindIoUI(shopApi.leaderboard( type,  page,cid));
    }

    @Override
    public Observable<BaseResponse<List<GoodsInfo>>> guessLike(String id, Integer type) {
        return bindIoUI(shopApi.guessLike(  id,  type));
    }

    @Override
    public Observable<BaseResponse<ShareList>> getShareList(Integer page) {
        return bindIoUI(shopApi.getShareList(page));
    }

    @Override
    public Observable<BaseResponse<List<Classfiy>>> classfiy() {
        return bindIoUI(shopApi.classfiy());
    }

    @Override
    public Observable<BaseResponse<List<IndexClassfiy>>> indexClassfiy() {
        return bindIoUI(shopApi.indexClassfiy());
    }

    @Override
    public Observable<BaseResponse<GoodsList>> practicalList(Integer type, Integer cid, Integer page, Integer sort, Integer startPrice, Integer endPrice) {
        return bindIoUI(shopApi.practicalList( type,  cid,  page,  sort,startPrice,endPrice));
    }

    @Override
   public Observable<BaseResponse<List<BannerResponse>>> banner(Integer position) {
        return bindIoUI(shopApi.banner(position));
    }

    @Override
    public Observable<BaseResponse<IndexAd>> indexAd() {
        return bindIoUI(shopApi.indexAd());
    }

    @Override
    public Observable<BaseResponse<IndexTextAd>> indexTextAd() {
            return bindIoUI(shopApi.indexTextAd());
    }

    @Override
    public Observable<BaseResponse<List<GoodsInfo>>> getDayFieryList() {
        return bindIoUI(shopApi.getDayFieryList());
    }
}
