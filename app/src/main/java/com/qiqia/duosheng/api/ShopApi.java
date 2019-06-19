package com.qiqia.duosheng.api;

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

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ShopApi {
    /**
     *KeyWord	   是	str	关键词
     * Order	   否	int	0.综合（最新），1.券后价(低到高)，2.券后价（高到低），3.券面额（高到低），4.月销量（高到低），5.佣金比例（高到低），6.券面额（低到高），7.月销量（低到高），8.佣金比例（低到高），9.全天销量（高到低），10全天销量（低到高），11.近2小时销量（高到低），12.近2小时销量（低到高），13.优惠券领取量（高到低），14.好单库指数（高到低）
     * Pn	      否	int	分页，用于实现类似分页抓取效果，来源于上次获取后的数据的min_id值，默认开始请求值为1
     * StartPrice 否	int	券后价筛选，筛选大于等于所设置的券后价的商品
     * EndPrice	  否	    int	券后价筛选，筛选小于等于所设置的券后价的商品
     * Cid	      否	int	0全部，1女装，2男装，3内衣，4美妆，5配饰，6鞋品，7箱包，8儿童，9母婴，10居家，11美食，12数码，13家电，14其他，15车品，16文体
     * Type	      否	int	默认0 搜索类型 0天猫 1京东 2拼多多
     */
    @GET("v4.php?ctr=App_Shop.Search")
    Observable<BaseResponse<GoodsListAll>> search(@Query("KeyWord") String keyWord, @Query("Order") Integer Order, @Query("Pn") Integer Pn,@Query("AllPn") Integer AllPn,
                                                  @Query("StartPrice") Integer startPrice, @Query("EndPrice") Integer endPrice, @Query("Cid") Integer cid,
                                                  @Query("Type")Integer type);

    /**
     *
     *  Url	是	str	链接
     * Type	是	int	默认0 搜索类型 0天猫 1京东 2拼多多
     * @return
     */
    @GET("v4.php?ctr=App_Shop.AnalysisUrl")
    Observable<BaseResponse<GoodsList>> analysisUrl(@Query("Url") String url,@Query("Type")Integer type);
    /**
     *
     *  Url	是	str	链接
     * Type	是	int	默认0 搜索类型 0天猫 1京东 2拼多多
     * @return
     */
    @GET("v4.php?ctr=App_Shop.HotWords")
    Observable<BaseResponse<List<HotWords>>> hotWords();
    /**
     *
     *  id	是	int	商品id
     * Type	是	int	默认0 搜索类型 0天猫 1京东 2拼多多
     */
    @GET("v4.php?ctr=App_Shop.GoodsInfo")
    Observable<BaseResponse<GoodsInfo>> goodsInfo(@Query("uid") String uid,@Query("id") String id, @Query("Type")Integer type);

    /**
     *   首页
     * type	是	int	默认0 0实时榜 1 24小时榜
     * page	是	int	默认1
     */
    @GET("v4.php?ctr=App_Shop.Index")
    Observable<BaseResponse<IndexResponse>> index();
    /**
     *   榜单
     * type	是	int	默认1 1是实时销量榜（近2小时销量），2是今日爆单榜，3是昨日爆单榜，4是出单指数版
     * page	是	int	默认1 作为请求地址中获取下一页的参数值
     * cid	是	int	默认0 0全部，1女装，2男装，3内衣，4美妆，5配饰，6鞋品，7箱包，8儿童，9母婴，10居家，11美食，12数码，13家电，14其他，15车品，16文体，17宠物
     */
    @GET("v4.php?ctr=App_Shop.Leaderboard")
    Observable<BaseResponse<GoodsList>> leaderboard(@Query("type") Integer type, @Query("page")Integer page,@Query("cid")Integer cid);

    /**
     * 猜你喜欢
     id	是	int	商品id
     Type	是	int	默认0 搜索类型 0天猫 1京东 2拼多多
     * @return
     */
    @GET("v4.php?ctr=App_Shop.GuessLike")
    Observable<BaseResponse<List<GoodsInfo>>> guessLike( @Query("id")String id,@Query("type") Integer type);

    /**
     * 朋友圈分享数据
     * page	否	int	分页，用于实现类似分页抓取效果，来源于上次获取后的数据的min_id值，默认开始请求值为1
     * @param page
     * @return
     */
    @GET("v4.php?ctr=App_Shop.GetShareList")
    Observable<BaseResponse<ShareList>> getShareList( @Query("page")Integer page);

    /**
     * 所有分类信息
     */
    @GET("v4.php?ctr=App_Shop.Classfiy")
    Observable<BaseResponse<List<Classfiy>>> classfiy();

    /**
     * 首页顶部分类
     * @return
     */
    @GET("v4.php?ctr=App_Shop.IndexClassfiy")
    Observable<BaseResponse<List<IndexClassfiy>>> indexClassfiy();

    /**
     * 实用排行榜
     * type	否	int	默认1，1是今日上新（当天新券商品），2是9.9包邮，3是30元封顶，4是聚划算，5是淘抢购，6是0点过夜单，7是预告单，8是品牌单，9是天猫商品，10是视频单
     * cid	否	int	类型 默认0，0全部，1女装，2男装，3内衣，4美妆，5配饰，6鞋品，7箱包，8儿童，9母婴，10居家，11美食，12数码，13家电，14其他，15车品，16文体，17宠物
     * page	否	int	分页，用于实现类似分页抓取效果，来源于上次获取后的数据的min_id值，默认开始请求值为1
     * sort	否	int	默认0，0.综合（最新），1.券后价(低到高)，2.券后价（高到低），3.券面额（高到低），4.月销量（高到低），5.佣金比例（高到低），6.券面额（低到高），7.月销量（低到高），8.佣金比例（低到高），9.全天销量（高到低），10全天销量（低到高），11.近2小时销量（高到低），12.近2小时销量（低到高），13.优惠券领取量（高到低），14.好单库指数（高到低）
     * @return
     */
    @GET("v4.php?ctr=App_Shop.PracticalList")
    Observable<BaseResponse<GoodsList>> practicalList(@Query("type") Integer type, @Query("cid")Integer cid,@Query("page")Integer page,@Query("sort")Integer sort,@Query("startPrice")Integer startPrice,@Query("endPrice")Integer endPrice);

    /**
     * 位置 默认1，
     * 1 APP首页顶部轮banner,2 APP首页中部轮banner,11 女装顶部轮banner，12 男装顶部轮banner,13 内衣顶部轮banner,14 美妆顶部轮banner,
     * 15 配饰顶部轮banner,16 鞋品顶部轮banner,17 箱包顶部轮banner,18 儿童顶部轮banner,19 母婴顶部轮banner,110 居家顶部轮banner,
     * 111 美食顶部轮banner,112 数码顶部轮banner,113 家电顶部轮banner,3 APP用户底部轮banner,
     * @param position
     * @return
     */
    @GET("v4.php?ctr=App_Shop.Ad")
    Observable<BaseResponse<List<BannerResponse>>> banner(@Query("position") Integer position);
    @GET("v4.php?ctr=App_Shop.IndexAd")
    Observable<BaseResponse<IndexAd>> indexAd();
    @GET("v4.php?ctr=App_Shop.IndexTextAd")
    Observable<BaseResponse<IndexTextAd>> indexTextAd();

    @GET("v4.php?ctr=App_Shop.GetDayFieryList")
    Observable<BaseResponse<List<GoodsInfo>>> getDayFieryList();
}
