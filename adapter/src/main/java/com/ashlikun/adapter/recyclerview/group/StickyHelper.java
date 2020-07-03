package com.ashlikun.adapter.recyclerview.group;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashlikun.adapter.AdapterUtils;
import com.ashlikun.adapter.ViewHolder;

/**
 * Depiction:头部吸顶布局。只要用StickyHeaderLayout包裹{@link RecyclerView},
 * 并且使用{@link GroupedCommonAdapter},就可以实现列表头部吸顶功能。
 * StickyHeaderLayout只能包裹RecyclerView，而且只能包裹一个RecyclerView。
 */
public class StickyHelper {

    private Context mContext;
    private RecyclerView mRecyclerView;

    //吸顶容器，用于承载吸顶布局。
    private FrameLayout mStickyLayout;

    //保存吸顶布局的缓存池。它以列表组头的viewType为key,ViewHolder为value对吸顶布局进行保存和回收复用。
    private final SparseArray<ViewHolder> mStickyViews = new SparseArray<>();

    //用于在吸顶布局中保存viewType的key。
    private final int VIEW_TAG_TYPE = -20001;

    //用于在吸顶布局中保存ViewHolder的key。
    private final int VIEW_TAG_HOLDER = -20002;

    //记录当前吸顶的组。
    private int mCurrentStickyGroup = -1;
    //记录当前吸顶Position
    private int mCurrentStickyPosition = -1;

    //是否吸顶。
    private boolean isSticky = true;

    //是否已经注册了adapter刷新监听
    private boolean isRegisterDataObserver = false;

    public StickyHelper(@NonNull RecyclerView recyclerView, FrameLayout stickyLayout) {
        mContext = recyclerView.getContext();
        mRecyclerView = recyclerView;
        mStickyLayout = stickyLayout;
        addOnScrollListener();
    }

    /**
     * 添加滚动监听
     */
    private void addOnScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // 在滚动的时候，需要不断的更新吸顶布局。
                if (isSticky) {
                    updateStickyView(false);
                }
            }
        });
    }

    /**
     * 强制更新吸顶布局。
     */
    public void updateStickyView() {
        updateStickyView(true);
    }

    /**
     * 更新吸顶布局。
     *
     * @param imperative 是否强制更新。
     */
    private void updateStickyView(boolean imperative) {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        //只有RecyclerView的adapter是GroupedCommonAdapter的时候，才会添加吸顶布局。
        if (adapter instanceof GroupedCommonAdapter) {
            GroupedCommonAdapter gAdapter = (GroupedCommonAdapter) adapter;
            registerAdapterDataObserver(gAdapter);
            //获取列表显示的第一个项。
            int firstVisibleItem = AdapterUtils.findFirstVisibleItemPosition(mRecyclerView.getLayoutManager());
            //通过显示的第一个项的position获取它所在的组。
            int groupPosition = gAdapter.getGroupPositionForPosition(firstVisibleItem);

            //如果当前吸顶的组头不是我们要吸顶的组头，就更新吸顶布局。这样做可以避免频繁的更新吸顶布局。
            if (imperative || mCurrentStickyGroup != groupPosition) {
                mCurrentStickyGroup = groupPosition;
                //通过groupPosition获取当前组的组头position。这个组头就是我们需要吸顶的布局。
                mCurrentStickyPosition = gAdapter.getPositionForGroupHeader(groupPosition);
                if (mCurrentStickyPosition != -1) {
                    //获取吸顶布局的viewType。
                    int viewType = gAdapter.getItemViewType(mCurrentStickyPosition);

                    //如果当前的吸顶布局的类型和我们需要的一样，就直接获取它的ViewHolder，否则就回收。
                    ViewHolder holder = recycleStickyView(viewType);

                    //标志holder是否是从当前吸顶布局取出来的。
                    boolean flag = holder != null;

                    if (holder == null) {
                        //从缓存池中获取吸顶布局。
                        holder = getStickyViewByType(viewType);
                    }

                    if (holder == null) {
                        //如果没有从缓存池中获取到吸顶布局，则通过GroupedCommonAdapter创建。
                        holder = (ViewHolder) gAdapter.createViewHolder(mStickyLayout, viewType);
                        holder.itemView.setTag(VIEW_TAG_TYPE, viewType);
                        holder.itemView.setTag(VIEW_TAG_HOLDER, holder);
                    }
                    try {
                        //设置Position
                        AdapterUtils.setField(holder, "mPreLayoutPosition", mCurrentStickyPosition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //通过GroupedCommonAdapter更新吸顶布局的数据。
                    //这样可以保证吸顶布局的显示效果跟列表中的组头保持一致。
                    gAdapter.onBindViewHolder(holder, mCurrentStickyPosition);


                    //如果holder不是从当前吸顶布局取出来的，就需要把吸顶布局添加到容器里。
                    if (!flag) {
                        mStickyLayout.addView(holder.itemView);
                    }
                } else {
                    //如果当前组没有组头，则不显示吸顶布局。
                    //回收旧的吸顶布局。
                    recycle();
                }
            }

            //这是是处理第一次打开时，吸顶布局已经添加到StickyLayout，但StickyLayout的高依然为0的情况。
            if (mStickyLayout.getChildCount() > 0 && mStickyLayout.getHeight() == 0) {
                mStickyLayout.requestLayout();
            }

            //设置mStickyLayout的Y偏移量。
            for (int i = 0; i < mStickyLayout.getChildCount(); i++) {
                mStickyLayout.getChildAt(i).setTranslationY(calculateOffset(gAdapter, firstVisibleItem, groupPosition + 1));
            }
        }
    }

    /**
     * 注册adapter刷新监听
     */
    private void registerAdapterDataObserver(GroupedCommonAdapter adapter) {
        if (!isRegisterDataObserver) {
            isRegisterDataObserver = true;
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    updateStickyViewDelayed();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    updateStickyViewDelayed();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    updateStickyViewDelayed();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    updateStickyViewDelayed();
                }

            });
        }
    }

    private void updateStickyViewDelayed() {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateStickyView(true);
            }
        }, 64);
    }

    /**
     * 判断是否需要先回收吸顶布局，如果要回收，则回收吸顶布局并返回null。
     * 如果不回收，则返回吸顶布局的ViewHolder。
     * 这样做可以避免频繁的添加和移除吸顶布局。
     *
     * @param viewType
     * @return
     */
    private ViewHolder recycleStickyView(int viewType) {
        if (mStickyLayout.getChildCount() > 0) {
            View view = mStickyLayout.getChildAt(0);
            int type = (int) view.getTag(VIEW_TAG_TYPE);
            if (type == viewType) {
                return (ViewHolder) view.getTag(VIEW_TAG_HOLDER);
            } else {
                recycle();
            }
        }
        return null;
    }

    /**
     * 回收并移除吸顶布局
     */
    public void recycle() {
        if (mStickyLayout.getChildCount() > 0) {
            View view = mStickyLayout.getChildAt(0);
            mStickyViews.put((int) (view.getTag(VIEW_TAG_TYPE)),
                    (ViewHolder) (view.getTag(VIEW_TAG_HOLDER)));
            mStickyLayout.removeAllViews();
        }
    }

    /**
     * 从缓存池中获取吸顶布局
     *
     * @param viewType 吸顶布局的viewType
     * @return
     */
    public ViewHolder getStickyViewByType(int viewType) {
        return mStickyViews.get(viewType);
    }

    /**
     * 计算StickyLayout的偏移量。因为如果下一个组的组头顶到了StickyLayout，
     * 就要把StickyLayout顶上去，直到下一个组的组头变成吸顶布局。否则会发生两个组头重叠的情况。
     *
     * @param gAdapter
     * @param firstVisibleItem 当前列表显示的第一个项。
     * @param groupPosition    下一个组的组下标。
     * @return 返回偏移量。
     */
    private float calculateOffset(GroupedCommonAdapter gAdapter, int firstVisibleItem, int groupPosition) {
        int groupHeaderPosition = gAdapter.getPositionForGroupHeader(groupPosition);
        if (groupHeaderPosition != -1) {
            int index = groupHeaderPosition - firstVisibleItem;
            if (mRecyclerView.getChildCount() > index) {
                //获取下一个组的组头的itemView。
                View view = mRecyclerView.getChildAt(index);
                float off = view.getY() - mStickyLayout.getHeight();
                if (off < 0) {
                    return off;
                }
            }
        }
        return 0;
    }


    public SparseArray<ViewHolder> getStickyViews() {
        return mStickyViews;
    }

    /**
     * 是否吸顶
     *
     * @return
     */
    public boolean isSticky() {
        return isSticky;
    }

    /**
     * 设置是否吸顶。
     *
     * @param sticky
     */
    public void setSticky(boolean sticky) {
        if (isSticky != sticky) {
            isSticky = sticky;
            if (mStickyLayout != null) {
                if (isSticky) {
                    mStickyLayout.setVisibility(View.VISIBLE);
                    updateStickyView(false);
                } else {
                    recycle();
                    mStickyLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 当前吸顶的组
     *
     * @return
     */
    public int getCurrentStickyGroup() {
        return mCurrentStickyGroup;
    }

    /**
     * 当前吸顶的Position 全部列表的
     *
     * @return
     */
    public int getCurrentStickyPosition() {
        return mCurrentStickyGroup;
    }
}
