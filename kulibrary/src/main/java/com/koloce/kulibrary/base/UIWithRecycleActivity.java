package com.koloce.kulibrary.base;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2019/3/30
 */
public abstract class UIWithRecycleActivity<T> extends UIActivity implements OnLoadMoreListener, OnRefreshListener {

    protected int page = 1;
    private boolean isEnableLoadMore = true;
    private boolean isEnableRefresh = true;

    /**
     * 网络获取资料
     *
     * @param isRefresh
     */
    protected abstract void getDataFromNet(boolean isRefresh, int page);

    private void initRecycle() {

        if (getRefreshLayout() != null) {
            //设置自动刷新
            if (isAutoRefresh()) {
                getRefreshLayout().autoRefresh();
            }
            //取消内容不满一页时开启上拉加载功能
            getRefreshLayout().setEnableLoadMoreWhenContentNotFull(false);
            //下拉刷新
            getRefreshLayout().setOnRefreshListener(this);
            //上拉加载更多
            getRefreshLayout().setOnLoadMoreListener(this);
            //是否允许加载更多
            getRefreshLayout().setEnableLoadMore(isEnableLoadMore);
            //是否允许刷新
            getRefreshLayout().setEnableRefresh(isEnableRefresh);
        }
        if (getQuickAdapter() != null && getEmptyView() != null) {
            getQuickAdapter().setEmptyView(getEmptyView());
            getQuickAdapter().isUseEmpty(false);
        }
    }

    protected abstract View getEmptyView();

    protected abstract SmartRefreshLayout getRefreshLayout();

    protected abstract RecyclerView getRecyclerView();

    protected abstract BaseQuickAdapter getQuickAdapter();

    /**
     * 获取到数据后加载数据
     *
     * @param isRefresh
     * @param datas
     */
    protected void setNewData(boolean isRefresh, List datas) {
        finishAnim();
        if (getQuickAdapter() == null) {
            return;
        }
        if (datas == null) {
            datas = new ArrayList();
        }
        if (isRefresh) {
            getQuickAdapter().setNewData(datas);
        } else {
            getQuickAdapter().addData(datas);
        }

        if (getQuickAdapter().getData().size() == 0) {
            getQuickAdapter().isUseEmpty(true);
        } else {
            getQuickAdapter().isUseEmpty(false);
        }

        try {
            getQuickAdapter().disableLoadMoreIfNotFullPage(); //屏蔽数据不足满屏
        } catch (Exception e) {
            new Exception("initRecycler() 没初始化！！！");
            e.printStackTrace();
        } finally {
            if (getRefreshLayout() == null) return;
            if (isEnableLoadMore) {
                if (datas == null || datas.size() == 0) {
                    getRefreshLayout().setEnableLoadMore(false);
                } else {
                    getRefreshLayout().setEnableLoadMore(true);
                }
            }

        }
    }

    /**
     * 是否自动刷新
     *
     * @return
     */
    protected abstract boolean isAutoRefresh();

    /**
     * 是否允许刷新
     *
     * @param isEnable
     * @return
     */
    protected void isEnableRefresh(boolean isEnable) {
        isEnableRefresh = isEnable;
        if (getRefreshLayout() == null) {
            return;
        }
        getRefreshLayout().setEnableRefresh(isEnable);
    }

    /**
     * 是否允许加载更多
     *
     * @param isEnable
     * @return
     */
    protected void isEnableLoadMore(boolean isEnable) {
        isEnableLoadMore = isEnable;
        if (getRefreshLayout() == null) {
            return;
        }
        getRefreshLayout().setEnableLoadMore(isEnable);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        getData(false);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        getData(true);
    }

    public void getData(boolean isRefresh) {
        if (isRefresh) {
            page = 0;
        }
        page += 1;
        getDataFromNet(isRefresh, page);
    }

    /**
     * 结束刷新或加载动画
     */
    public void finishAnim() {
        if (getRefreshLayout() == null) return;
        getRefreshLayout().closeHeaderOrFooter();
    }

    /**
     * 设置空白页面
     *
     * @param view
     */
    public void setEmptyView(View view) {
        if (getQuickAdapter() != null) {
            getQuickAdapter().setEmptyView(view);
        }
    }

    @Override
    protected void afterInitView() {
        initRecycle();
    }

    public T getListDataFromPosition(int position) {
        return (T) getQuickAdapter().getData().get(position);
    }

    public List<T> getListData() {
        return getQuickAdapter().getData();
    }
}
