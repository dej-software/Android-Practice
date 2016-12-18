package com.jikexueyuan.duanzi;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by dej on 2016/10/30.
 */
public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = "PullToRefreshListView";

    // 上拉刷新状态 初始 下拉 释放后刷新 刷新中
    private static final int REFRESH_BEGIN_STATE = 1;
    private static final int REFRESH_PULL_STATE = 2;
    private static final int REFRESH_RELEASE_STATE = 3;
    private static final int REFRESHING_STATE = 4;

    // 下拉加载状态 初始 加载中
    private static final int LOAD_BEGIN_STATE = 5;
    private static final int LOADING_STATE = 6;

    // ListView中的资源对应
    private LinearLayout headView, footView;
    private TextView headMsg, footMsg;
    private ImageView headArrowImg;
    private ProgressBar headProgressBar, footProgressBar;

    // 下拉与恢复的动画 上下箭头
    private RotateAnimation downPullAnimation, upPullAnimation;

    // 刷新状态 加载状态
    private int refreshState;
    private int loadState;

    // 最初的头视图的TopPadding
    private int headViewTopPadding;
    // 刷新的高度 点击位置
    private int refreshViewHeight;
    private int lastMotionY;
    // ListView滑动状态 在onScrollStateChanged中更新
    private int currentScrollState;

    public PullToRefreshListView(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        // 动画初始化
        downPullAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upPullAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        downPullAnimation.setDuration(250); // 持续时间
        downPullAnimation.setFillAfter(true); // 结束保持状态
        upPullAnimation.setDuration(250); // 持续时间
        upPullAnimation.setFillAfter(true); // 结束保持状态

        // 实例对象
        headView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.listview_header, null);
        footView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.listview_footer, null);

        headArrowImg = (ImageView) headView.findViewById(R.id.headArrowImg);
        headMsg = (TextView) headView.findViewById(R.id.headMsg);
        headProgressBar = (ProgressBar) headView.findViewById(R.id.headProgressBar);

        footMsg = (TextView) footView.findViewById(R.id.footMsg);
        footProgressBar = (ProgressBar) footView.findViewById(R.id.footProgressBar);

        // 增加 头部 尾部
        addHeaderView(headView, null, false);
        // 控制FooterView的显示状态 用FrameLayout装起来 不然不显示时会留下空白的区域
        FrameLayout footLayout = new FrameLayout(context);
        footLayout.addView(footView);
        addFooterView(footLayout, null, false);

        // 初始状态
        refreshState = REFRESH_BEGIN_STATE;
        loadState = LOAD_BEGIN_STATE;

        // ListView滑动监听
        super.setOnScrollListener(this);

        // 一些headView初始值
        measureView(headView);
        refreshViewHeight = headView.getMeasuredHeight();
        headViewTopPadding = headView.getPaddingTop();

        // 使footView可测量
        measureView(footView);

        //头尾部的视图为初始化状态
        resetHeader();
        resetFooter();
    }

    /**
     * 复位HeaderPadding
     * -1 * refreshViewHeight 表示隐藏住头部视图
     */
    private void resetHeaderPadding() {
        headView.setPadding(headView.getPaddingLeft(),
                -1 * refreshViewHeight,
                headView.getPaddingRight(),
                headView.getPaddingBottom());
    }

    /**
     * 头部视图初始化状态
     */
    private void resetHeader() {
        if (refreshState != REFRESH_BEGIN_STATE) {
            refreshState = REFRESH_BEGIN_STATE;
        }

        resetHeaderPadding();

        headMsg.setText("下拉刷新");
        headArrowImg.setImageResource(R.drawable.down_arrow);
        headArrowImg.clearAnimation();
        headArrowImg.setVisibility(GONE);
        headProgressBar.setVisibility(GONE);
    }

    /**
     * 为下拉刷新做准备
     */
    private void prepareForRefresh() {
        headView.setPadding(headView.getPaddingLeft(),
                headViewTopPadding,
                headView.getPaddingRight(),
                headView.getPaddingBottom());
        headArrowImg.setVisibility(GONE);
        headArrowImg.setImageDrawable(null);
        headProgressBar.setVisibility(VISIBLE);
        headMsg.setText("正在刷新");
        refreshState = REFRESHING_STATE;
    }

    /**
     * 使用回调方法下拉刷新
     */
    private void onRefresh() {
        if (onPullRefreshListener != null) {
            onPullRefreshListener.onRefresh();
        }
    }

    /**
     * 刷新完成
     */
    public void onRefreshComplete() {
        Log.d(TAG, "onRefreshComplete");
        resetHeader();
    }

    /**
     * 尾部视图初始化状态
     */
    private void resetFooter() {
        if (loadState != LOAD_BEGIN_STATE) {
            loadState = LOAD_BEGIN_STATE;
        }

        footProgressBar.setVisibility(INVISIBLE);
        footMsg.setVisibility(INVISIBLE);
        footView.setVisibility(GONE);
    }

    /**
     * 为加载分页做准备
     */
    private void prepareForLoad() {
        footProgressBar.setVisibility(VISIBLE);
        footMsg.setVisibility(VISIBLE);
        footMsg.setText("正在加载");
        footView.setVisibility(VISIBLE);
        loadState = LOADING_STATE;
    }

    /**
     * 使用回调方法加载分页
     */
    private void onLoadMore() {
        if (onPullRefreshListener != null) {
            onPullRefreshListener.onLoadMore();
        }
    }

    /**
     * 刷新完成
     */
    public void onLoadMoreComplete() {
        Log.d(TAG, "onLoadMoreComplete");
        resetFooter();
    }

    /**
     * 滑动时动态改变头部视图的Padding
     *
     * @param ev
     */
    private void changeHeaderPadding(MotionEvent ev) {
        for (int i = 0; i < ev.getHistorySize(); i++) {
            if (isVerticalScrollBarEnabled()) {
                setVerticalScrollBarEnabled(false);
            }
//            int topPadding = (int) (((ev.getHistoricalY(i) - lastMotionY) - refreshViewHeight) / 1.7);
            int topPadding = (int) (((ev.getHistoricalY(i) - lastMotionY) - 2 * refreshViewHeight) / 1.7);
            if (topPadding <= headView.getPaddingTop()) {
                return;
            }
            headView.setPadding(headView.getPaddingLeft(),
                    topPadding,
                    headView.getPaddingRight(),
                    headView.getPaddingBottom());
        }
    }

    /**
     * 触摸事件的处理
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastMotionY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                // 垂直滚动条状态
                if (!isVerticalScrollBarEnabled()) {
                    setVerticalScrollBarEnabled(true);
                }
                if (getFirstVisiblePosition() == 0 && refreshState != REFRESHING_STATE) {
                    if ((headView.getBottom() >= refreshViewHeight || headView.getTop() >= 0)
                            && refreshState == REFRESH_RELEASE_STATE) {
                        prepareForRefresh();
                        onRefresh();
                    } else if (headView.getBottom() < refreshViewHeight || headView.getTop() <= 0) {
                        resetHeader();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                changeHeaderPadding(ev);
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // OnScrollListener.SCROLL_STATE_FLING; //屏幕处于甩动状态
        // OnScrollListener.SCROLL_STATE_IDLE; //停止滑动状态
        // OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;// 手指接触状态
        Log.d(TAG, "scrollState: " + scrollState);
        currentScrollState = scrollState;
    }

    /**
     * 列表滑动处理
     *
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // totalItemCount为2表示ListView中只有头尾部，此时下拉不会触发SCROLL_STATE_TOUCH_SCROLL状态
        if ((currentScrollState == SCROLL_STATE_TOUCH_SCROLL || totalItemCount == 2) && refreshState != REFRESHING_STATE) {
            if (firstVisibleItem == 0 && headView.getBottom() > 0) { // 如果第一个可见条目为0
                headArrowImg.setVisibility(VISIBLE);
                // 设置下拉刷新 或 释放刷新 的状态
                if ((headView.getBottom() > refreshViewHeight + 60)
                        && refreshState != REFRESH_RELEASE_STATE) {
                    headMsg.setText("释放后刷新");
                    headArrowImg.clearAnimation();
                    headArrowImg.startAnimation(downPullAnimation);
                    refreshState = REFRESH_RELEASE_STATE;
                } else if (headView.getBottom() < refreshViewHeight + 60 && refreshState != REFRESH_PULL_STATE) {
                    headMsg.setText("下拉刷新");
                    headArrowImg.clearAnimation();
                    headArrowImg.startAnimation(upPullAnimation);
                    refreshState = REFRESH_PULL_STATE;
                }
            } else {
                resetHeaderPadding();
            }
        }

        // 滑到底部开始加载新页面 且列表项数超过2：去掉头尾两个Item
        if ((firstVisibleItem + visibleItemCount) == (totalItemCount) && (totalItemCount > 2)) {
            Log.d(TAG, "totalItemCount:" + totalItemCount);
            if (loadState != LOADING_STATE) {
                prepareForLoad();
//                setSelection(totalItemCount);
                onLoadMore();
            }
        }
    }

    /**
     * 测量视图的大小
     *
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,
                0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    // 用于刷新监听
    private OnPullRefreshListener onPullRefreshListener;

    public void setOnPullRefreshListener(OnPullRefreshListener listener) {
        onPullRefreshListener = listener;
    }

    /**
     * 定义一个接口 使用回调方法刷新列表
     */
    public interface OnPullRefreshListener {
        public void onRefresh();

        public void onLoadMore();
    }
}
