package com.qiqia.duosheng.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.mine.adapter.MyCollectionAdapter;
import com.qiqia.duosheng.mine.bean.MyCollection;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.ToastUtil;

public class MyCollectionFragment extends BaseBarFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.rb_select_all)
    RadioButton rbSelectAll;
    @BindView(R.id.btn_del)
    Button btnDel;
    @BindView(R.id.layout_edit)
    RelativeLayout layoutEdit;
    MyCollectionAdapter mAdapter = new MyCollectionAdapter();
    User user;
    int page = 1;
    int totalPages = 1;

    @Override
    public int setLayout() {
        return R.layout.fragment_my_collection;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        user = DataLocalUtils.getUser();
        tvTitle.setText("收藏");
        tvRight.setText("管理");
        tvRight.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(mAdapter);
        getMyCollection();
        onEvent();
    }

    private void onEvent() {
        //加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page < totalPages) {
                    page++;
                    getMyCollection();
                } else {
                    mAdapter.loadMoreEnd();
                }
            }
        }, recyclerView);
        //进入商品详情
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsInfo item = (GoodsInfo) adapter.getItem(position);
                goGoodsInfoFragment(item);
            }
        });

        //选中和取消选中
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.setSelectPosition(position);
            }
        });

        //全选后，取消一项，解除全选按钮选中状态,选中一项后，如果已经全选，改变全选按钮状态为选中
        mAdapter.setListener(new MyCollectionAdapter.onSelectAllListener() {
            @Override
            public void cancleSelectAll() {
                rbSelectAll.setChecked(false);
            }

            @Override
            public void selectAll() {
                rbSelectAll.setChecked(true);
            }
        });
    }

    private void getMyCollection() {
        dataProvider.user.collectList(user.getUid(), user.getToken(), page)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<MyCollection>>() {
                    @Override
                    protected void onSuccess(BaseResponse<MyCollection> baseResponse) {
                        MyCollection obj = baseResponse.getData();
                        List<GoodsInfo> datas = obj.getData();
                        mAdapter.addData(datas);
                        mAdapter.loadMoreComplete();
                        totalPages = obj.getPages();
                    }
                });
    }

    @OnClick({R.id.tv_right, R.id.rb_select_all, R.id.btn_del})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.tv_right://操作的显示和隐藏
                edit();
                break;
            case R.id.rb_select_all://全选/取消全选
                clickSelectAll();
                break;
            case R.id.btn_del://删除
                del();
                break;
        }
    }

    private void clickSelectAll() {
        if (mAdapter.isSelectAllData()) {
            rbSelectAll.setChecked(false);
            mAdapter.cancleAll();
        } else {
            rbSelectAll.setChecked(true);
            mAdapter.selectAll();
        }
    }


    private void del() {
        String allSelectIds = mAdapter.getAllSelectIds();
        if (TextUtils.isEmpty(allSelectIds)) {
            ToastUtil.showLong("未选中任何项！");
            return;
        }
        //刷新适配器
        dataProvider.user.delCol(user.getUid(), user.getToken(), allSelectIds)
                .subscribe(new OnSuccessAndFailListener<BaseResponse>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        ToastUtil.showLong(baseResponse.getMsg());
                        //刷新适配器
                        mAdapter.delSelect();
                    }
                });
    }

    private void edit() {
        String s = tvRight.getText().toString();
        if (s.equals("管理")) {
            tvRight.setText("取消");
            mAdapter.setShowSelectBtn(true);
            layoutEdit.setVisibility(View.VISIBLE);
        } else {
            tvRight.setText("管理");
            mAdapter.setShowSelectBtn(false);
            layoutEdit.setVisibility(View.GONE);
        }
    }
}

