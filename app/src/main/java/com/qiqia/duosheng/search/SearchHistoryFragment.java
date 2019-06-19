package com.qiqia.duosheng.search;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.HotWords;
import com.qiqia.duosheng.bean.SearchKey;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.ToastUtil;
import cn.com.smallcake_utils.custom.AutoNewLineLayout;

/**
 * 搜索页
 */
public class SearchHistoryFragment extends BaseBarFragment {


    @BindView(R.id.ele_bar)
    View eleBar;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_key)
    EditText etKey;
    @BindView(R.id.iv_clean)
    ImageView ivClean;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_clean_history)
    TextView tvCleanHistory;
    @BindView(R.id.auto_layout_history)
    AutoNewLineLayout autoLayoutHistory;
    @BindView(R.id.auto_layout_hot)
    AutoNewLineLayout autoLayoutHot;
    @BindView(R.id.layout_search_key)
    LinearLayout layoutSearchKey;
    @BindView(R.id.layout_list)
    NestedScrollView layoutList;

    public static SearchHistoryFragment newInstance() {
        Bundle args = new Bundle();
        SearchHistoryFragment fragment = new SearchHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_search_history;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        initSearchHistory();
        initSearchHot();
        etKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    goToSearch();
                    return true;
                }
                return false;
            }
        });
    }


    private void initSearchHistory() {
        //查询出来历史纪录
        autoLayoutHistory.removeAllViews();
        List<SearchKey> all = LitePal.order("time desc").find(SearchKey.class);//通过时间倒序，进行搜索历史展示
        L.e("历史纪录：" + all.toString());
        for (SearchKey key : all) autoLayoutHistory.addView(createKeyView(key.getKey()));
    }

    private void initSearchHot() {
        autoLayoutHot.removeAllViews();
        dataProvider.shop.hotWords().subscribe(new OnSuccessAndFailListener<BaseResponse<List<HotWords>>>() {
            @Override
            protected void onSuccess(BaseResponse<List<HotWords>> listBaseResponse) {
                List<HotWords> data = listBaseResponse.getData();
                for (HotWords hotWords:data){
                    if (autoLayoutHot!=null)
                    autoLayoutHot.addView(createKeyView(hotWords.getHwName()));
                }
            }

        });
    }

    private TextView createKeyView(String key) {
        TextView tvKey = new TextView(_mActivity);
        int dp8 = DpPxUtils.dp2px( 8);
        tvKey.setPadding(dp8 * 2, dp8, dp8 * 2, dp8);
        tvKey.setTextColor(ContextCompat.getColor(_mActivity, R.color.text_black));
        tvKey.setBackgroundResource(R.drawable.round_gray_bg);
        tvKey.setText(key);
        tvKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etKey.setText(key);
                start(SearchFragment.newInstance(key));
            }
        });
        return tvKey;
    }

    @OnClick({R.id.iv_search, R.id.iv_clean, R.id.tv_right, R.id.tv_clean_history, R.id.iv_right})
    void doClicks(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                goToSearch();
                break;
            case R.id.iv_clean:
                cleanKey();
                break;
            case R.id.tv_right:
                if (getPreFragment()==null){
                    _mActivity.finish();
                }
                pop();
                break;
            case R.id.tv_clean_history:
                new XPopup.Builder(getContext()).asConfirm(getString(R.string.app_name), "是否清除历史纪录？",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                int listNumber = LitePal.deleteAll(SearchKey.class);
                                ToastUtil.showLong("你删除了" + listNumber + "条历史纪录！");
                                autoLayoutHistory.removeAllViews();
                            }
                        })
                        .show();
                break;
            case R.id.iv_right:
                break;
        }
    }

    private void goToSearch() {
        String KeyWord = etKey.getText().toString();
        if (TextUtils.isEmpty(KeyWord)) {
            ToastUtil.showLong("请输入搜索关键字！");
            return;
        }
        start(SearchFragment.newInstance(KeyWord));
    }


    private void cleanKey() {
        etKey.setText("");
    }



}
