package com.ashlikun.adapter.recyclerview.group;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.ashlikun.adapter.R;
import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.multiltem.MultiItemCommonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的分组列表Adapter。通过它可以很方便的实现列表的分组效果。
 * 这个类提供了一系列的对列表的更新、删除和插入等操作的方法。
 * 使用者要使用这些方法的列表进行操作，而不要直接使用RecyclerView.Adapter的方法。
 * 因为当分组列表发生变化时，需要及时更新分组列表的组结构{@link GroupedCommonAdapter#mStructures}
 */
public abstract class GroupedCommonAdapter<T>
        extends MultiItemCommonAdapter<T> {

    public static final int TYPE_HEADER = R.id.adapter_type_header;
    public static final int TYPE_FOOTER = R.id.adapter_type_footer;
    public static final int TYPE_CHILD = R.id.adapter_type_child;

    private OnHeaderClickListener mOnHeaderClickListener;
    private OnFooterClickListener mOnFooterClickListener;
    private OnChildClickListener mOnChildClickListener;

    //保存分组列表的组结构
    protected ArrayList<GroupStructure> mStructures = new ArrayList<>();
    //数据是否发生变化。如果数据发生变化，要及时更新组结构。
    private boolean isDataChanged;
    private ArrayList<Integer> headerViewTypes = new ArrayList();
    private ArrayList<Integer> footerViewTypes = new ArrayList();
    private ArrayList<Integer> childViewTypes = new ArrayList();


    public GroupedCommonAdapter(Context context, List<T> datas) {
        super(context, datas);
        registerAdapterDataObserver(new GroupDataObserver());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            gridLayoutManager.setSpanSizeLookup(new SpanSizeLookupGroup(gridLayoutManager, this));
        }
        structureChanged();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //处理StaggeredGridLayout，保证组头和组尾占满一行。
        if (holder.isStaggeredGridLayout()) {
            if (judgePositionToType(holder.getPositionInside()) == TYPE_HEADER || judgePositionToType(holder.getPositionInside()) == TYPE_FOOTER) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams)
                        holder.itemView.getLayoutParams();
                p.setFullSpan(true);
            }
        }
    }
    /**
     * 重写父类，用组的position替代
     */
    @Override
    public T getItemData(int position) {
        final int groupPosition = getGroupPositionForPosition(position);
        return super.getItemData(groupPosition);
    }

    /**
     * 获取组数据
     */
    public T getGroupData(int groupPosition) {
        return dataHandle.getItemData(groupPosition);
    }

    @Override
    public void onItemClick(int viewType, ViewGroup parent, View view, T data, int position) {
        super.onItemClick(viewType, parent, view, data, position);
        int viewTypeZhenshi = judgeType(viewType);
        final int groupPosition = getGroupPositionForPosition(position);
        if (viewTypeZhenshi == TYPE_HEADER) {
            if (mOnHeaderClickListener != null) {
                int gPosition = parent instanceof FrameLayout ? groupPosition : getGroupPositionForPosition(position);
                if (gPosition >= 0 && gPosition < mStructures.size()) {
                    mOnHeaderClickListener.onHeaderClick(GroupedCommonAdapter.this, gPosition);
                }
            }
        } else if (viewTypeZhenshi == TYPE_FOOTER) {
            if (mOnFooterClickListener != null) {
                int gPosition = getGroupPositionForPosition(position);
                if (gPosition >= 0 && gPosition < mStructures.size()) {
                    mOnFooterClickListener.onFooterClick(GroupedCommonAdapter.this, gPosition);
                }
            }
        } else if (viewTypeZhenshi == TYPE_CHILD) {
            if (mOnChildClickListener != null) {
                int gPosition = getGroupPositionForPosition(position);
                int cPosition = getChildPositionForPosition(gPosition, position);
                if (gPosition >= 0 && gPosition < mStructures.size() && cPosition >= 0
                        && cPosition < mStructures.get(gPosition).getChildrenCount()) {
                    mOnChildClickListener.onChildClick(GroupedCommonAdapter.this, gPosition, cPosition);
                }
            }
        }
    }

    @Override
    public void convert(ViewHolder holder, T t) {
        int viewTypeZhenshi = judgeType(holder.getItemViewType());
        final int groupPosition = getGroupPositionForPosition(holder.getPositionInside());
        if (viewTypeZhenshi == TYPE_HEADER) {
            onBindHeaderViewHolder(holder, t, groupPosition);
        } else if (viewTypeZhenshi == TYPE_FOOTER) {
            onBindFooterViewHolder(holder, t, groupPosition);
        } else if (viewTypeZhenshi == TYPE_CHILD) {
            int childPosition = getChildPositionForPosition(groupPosition, holder.getPositionInside());
            onBindChildViewHolder(holder, t, groupPosition, childPosition);
        }
    }


    @Override
    public int getItemCount() {
        if (isDataChanged) {
            structureChanged();
        }
        int count = count();
        if (count > 0) {
            return count;
        } else {
            return 0;
        }
    }


    @Override
    public int getItemViewType(int position, T data) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        int groupPosition = getGroupPositionForPosition(position);
        int type = judgePositionToType(position);
        if (type == TYPE_HEADER) {
            return getHeaderViewType(groupPosition, getGroupData(groupPosition));
        } else if (type == TYPE_FOOTER) {
            return getFooterViewType(groupPosition, getGroupData(groupPosition));
        } else if (type == TYPE_CHILD) {
            int childPosition = getChildPositionForPosition(groupPosition, position);
            return getChildViewType(groupPosition, childPosition, getGroupData(groupPosition));
        }
        return super.getItemViewType(position);
    }

    public int getHeaderViewType(int groupPosition, T itemData) {
        if (headerViewTypes.size() > 0) {
            return headerViewTypes.get(0);
        }
        return TYPE_HEADER;
    }

    public int getFooterViewType(int groupPosition, T itemData) {
        if (footerViewTypes.size() > 0) {
            return footerViewTypes.get(0);
        }
        return TYPE_FOOTER;
    }

    public int getChildViewType(int groupPosition, int childPosition, T itemData) {
        if (childViewTypes.size() > 0) {
            return childViewTypes.get(0);
        }
        return TYPE_CHILD;
    }


    private int count() {
        return mStructures.isEmpty() ? 0 : mStructures.get(mStructures.size() - 1).getEnd() + 1;
    }

    /**
     * 判断item的type 头部 尾部 和 子项
     *
     * @param viewType
     * @return
     */
    public int judgeType(int viewType) {
        if (headerViewTypes.contains(viewType)) {
            return TYPE_HEADER;
        } else if (footerViewTypes.contains(viewType)) {
            return TYPE_FOOTER;
        } else if (childViewTypes.contains(viewType)) {
            return TYPE_CHILD;
        }
        return -1;
    }

    /**
     * 判断item的type 头部 尾部 和 子项
     *
     * @param position
     * @return
     */
    public int judgePositionToType(int position) {
        int groupCount = mStructures.size();
        for (int i = 0; i < groupCount; i++) {
            GroupStructure structure = mStructures.get(i);
            int type = structure.containsPositionType(position);
            if (type != -1) {
                return type;
            }

        }

        return -1;
    }

    public void addItemTypeHeader(int viewType, @LayoutRes int layoutResId) {
        if (!headerViewTypes.contains(viewType)) {
            headerViewTypes.add(viewType);
        }
        addItemType(viewType, layoutResId);
    }

    public void addItemTypeFooter(int viewType, @LayoutRes int layoutResId) {
        if (!footerViewTypes.contains(viewType)) {
            footerViewTypes.add(viewType);
        }
        addItemType(viewType, layoutResId);
    }

    public void addItemTypeChild(int viewType, @LayoutRes int layoutResId) {
        if (!childViewTypes.contains(viewType)) {
            childViewTypes.add(viewType);
        }
        addItemType(viewType, layoutResId);
    }

    /**
     * 重置组结构列表
     */
    private void structureChanged() {
        ArrayList<GroupStructure> structures = new ArrayList<>();
        structures.addAll(mStructures);
        mStructures.clear();
        int groupCount = getGroupCount();
        int startPosition = 0;
        for (int i = 0; i < groupCount; i++) {
            GroupStructure structure;
            T itemData = getGroupData(i);
            if (i < structures.size()) {
                structure = structures.get(i);
                structure.init(hasHeader(i, itemData), hasFooter(i, itemData), startPosition, getChildrenCount(i, itemData));
            } else {
                structure = new GroupStructure(hasHeader(i, itemData), hasFooter(i, itemData), startPosition, getChildrenCount(i, itemData));
            }
            mStructures.add(structure);
            startPosition = structure.getEnd() + 1;
        }
        structures.clear();
        isDataChanged = false;
    }

    /**
     * 根据下标计算position所在的组（groupPosition）
     *
     * @param position 下标
     * @return 组下标 groupPosition
     */
    public int getGroupPositionForPosition(int position) {
        for (int i = 0; i < mStructures.size(); i++) {
            GroupStructure structure = getGroupItem(i);
            if (structure != null && structure.containsPosition(position)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据下标计算position在组中位置（childPosition）
     *
     * @param groupPosition 所在的组
     * @param position      下标
     * @return 子项下标 childPosition
     */
    public int getChildPositionForPosition(int groupPosition, int position) {
        GroupStructure structure = getGroupItem(groupPosition);
        return structure == null ? -1 : structure.getChildPositionForPosition(position);
    }

    /**
     * 获取一个组的开始下标，这个下标可能是组头，可能是子项(如果没有组头)或者组尾(如果这个组只有组尾)
     *
     * @param groupPosition 组下标
     * @return
     */
    public int getPositionForGroup(int groupPosition) {
        GroupStructure structure = getGroupItem(groupPosition);
        return structure == null ? -1 : structure.getStart();
    }

    /**
     * 获取一个组的组头下标 如果该组没有组头 返回-1
     *
     * @param groupPosition 组下标
     * @return 下标
     */
    public int getPositionForGroupHeader(int groupPosition) {
        GroupStructure structure = getGroupItem(groupPosition);
        if (structure != null) {
            if (!structure.hasHeader()) {
                return -1;
            }
            return structure.getStart();
        }
        return -1;
    }

    /**
     * 获取一个组的组尾下标 如果该组没有组尾 返回-1
     *
     * @param groupPosition 组下标
     * @return 下标
     */
    public int getPositionForGroupFooter(int groupPosition) {
        GroupStructure structure = getGroupItem(groupPosition);
        if (structure != null) {
            if (!structure.hasFooter()) {
                return -1;
            }
            return structure.getEnd();
        }
        return -1;
    }

    /**
     * 获取一个组指定的子项下标 如果没有 返回-1
     *
     * @param groupPosition 组下标
     * @param childPosition 子项的组内下标
     * @return 下标
     */
    public int getPositionForChild(int groupPosition, int childPosition) {
        GroupStructure structure = mStructures.get(groupPosition);
        if (structure != null) {
            if (structure.getChildrenCount() > childPosition) {
                return childPosition + structure.getChildrenStart();
            }
        }
        return -1;
    }

    /**
     * 计算一个组里有多少个Item（头加尾加子项）
     */
    public int countGroupItem(int groupPosition) {
        GroupStructure item = getGroupItem(groupPosition);
        return item == null ? 0 : item.countGroupItem();
    }

    /**
     * 获取一个组
     */
    public GroupStructure getGroupItem(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < mStructures.size()) {
            return mStructures.get(groupPosition);
        }
        return null;
    }


    //****** 刷新操作 *****//

    /**
     * 通知数据列表刷新
     */
    public void notifyDataChanged() {
        isDataChanged = true;
        notifyDataSetChanged();
    }

    /**
     * 通知一组数据刷新，包括组头,组尾和子项
     *
     * @param groupPosition
     */
    public void notifyGroupChanged(int groupPosition) {
        int index = getPositionForGroup(groupPosition);
        int itemCount = countGroupItem(groupPosition);
        if (index >= 0 && itemCount > 0) {
            notifyItemRangeChanged(index, itemCount);
        }
    }

    /**
     * 通知多组数据刷新，包括组头,组尾和子项
     *
     * @param groupPosition
     */
    public void notifyGroupRangeChanged(int groupPosition, int count) {
        int start = getPositionForGroup(groupPosition);
        GroupStructure endGroup = getGroupItem(Math.min(groupPosition + count - 1, mStructures.size() - 1));
        int itemCount = (endGroup == null ? 0 : endGroup.getEnd()) - start + 1;
        if (start >= 0 && itemCount > 0) {
            notifyItemRangeChanged(start, itemCount);
        }
    }

    /**
     * 通知组头刷新
     *
     * @param groupPosition
     */
    public void notifyHeaderChanged(int groupPosition) {
        int start = getPositionForGroupHeader(groupPosition);
        if (start >= 0) {
            notifyItemChanged(start);
        }
    }

    /**
     * 通知组尾刷新
     *
     * @param groupPosition
     */
    public void notifyFooterChanged(int groupPosition) {
        int end = getPositionForGroupFooter(groupPosition);
        if (end >= 0) {
            notifyItemChanged(end);
        }
    }


    /**
     * 通知一组里的某个子项刷新
     *
     * @param groupPosition
     * @param childPosition
     */
    public void notifyChildChanged(int groupPosition, int childPosition) {
        int index = getPositionForChild(groupPosition, childPosition);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    /**
     * 通知一组里的多个子项刷新
     *
     * @param groupPosition
     * @param childPosition
     * @param count
     */
    public void notifyChildRangeChanged(int groupPosition, int childPosition, int count) {
        if (groupPosition < mStructures.size()) {
            int index = getPositionForChild(groupPosition, childPosition);
            if (index >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                if (structure.getChildrenCount() >= childPosition + count) {
                    notifyItemRangeChanged(index, count);
                } else {
                    notifyItemRangeChanged(index, structure.getChildrenCount() - childPosition);
                }
            }
        }
    }

    /**
     * 通知一组里的所有子项刷新
     *
     * @param groupPosition
     */
    public void notifyChildrenChanged(int groupPosition) {
        int index = getPositionForChild(groupPosition, 0);
        if (index >= 0) {
            GroupStructure structure = mStructures.get(groupPosition);
            notifyItemRangeChanged(index, structure.getChildrenCount());
        }
    }


    /**
     * 通知所有数据删除
     */
    public void notifyDataRemoved() {
        int count = getItemCount();
        mStructures.clear();
        notifyItemRangeRemoved(0, count);
    }


    /**
     * 通知一组数据删除，包括组头,组尾和子项
     *
     * @param groupPosition
     */
    public void notifyGroupRemoved(int groupPosition) {
        int start = getPositionForGroup(groupPosition);
        int itemCount = countGroupItem(groupPosition);
        if (start >= 0 && itemCount > 0) {
            structureChanged();
            notifyItemRangeRemoved(start, itemCount);
        }
    }

    /**
     * 通知多组数据删除，包括组头,组尾和子项
     *
     * @param groupPosition
     */
    public void notifyGroupRangeRemoved(int groupPosition, int count) {
        int start = getPositionForGroup(groupPosition);
        GroupStructure endGroup = getGroupItem(Math.min(groupPosition + count - 1, mStructures.size() - 1));
        int itemCount = endGroup == null ? 0 : endGroup.getEnd() + 1;
        if (start >= 0 && itemCount > 0) {
            structureChanged();
            notifyItemRangeChanged(start, itemCount);
        }
    }

    /**
     * 通知组头删除
     *
     * @param groupPosition
     */
    public void notifyHeaderRemoved(int groupPosition) {
        int index = getPositionForGroupHeader(groupPosition);
        if (index >= 0) {
            structureChanged();
            GroupStructure structure = mStructures.get(groupPosition);
            notifyItemRemoved(index);
        }
    }

    /**
     * 通知组尾删除
     *
     * @param groupPosition
     */
    public void notifyFooterRemoved(int groupPosition) {
        int index = getPositionForGroupFooter(groupPosition);
        if (index >= 0) {
            structureChanged();
            GroupStructure structure = mStructures.get(groupPosition);
            notifyItemRemoved(index);
        }
    }

    /**
     * 通知一组里的某个子项删除
     *
     * @param groupPosition
     * @param childPosition
     */
    public void notifyChildRemoved(int groupPosition, int childPosition) {
        int index = getPositionForChild(groupPosition, childPosition);
        if (index >= 0) {
            structureChanged();
            notifyItemRemoved(index);
        }
    }

    /**
     * 通知一组里的多个子项删除
     *
     * @param groupPosition
     * @param childPosition
     * @param count
     */
    public void notifyChildRangeRemoved(int groupPosition, int childPosition, int count) {
        int index = getPositionForChild(groupPosition, childPosition);
        if (index >= 0) {
            GroupStructure structure = mStructures.get(groupPosition);
            int childCount = structure.getChildrenCount();
            int removeCount = count;
            if (childCount < childPosition + count) {
                removeCount = childCount - childPosition;
            }
            structureChanged();
            notifyItemRangeRemoved(index, removeCount);
        }
    }

    /**
     * 通知一组里的所有子项删除
     *
     * @param groupPosition
     */
    public void notifyChildrenRemoved(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            int start = getPositionForChild(groupPosition, 0);
            if (start >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                int itemCount = structure.getChildrenCount();
                structureChanged();
                notifyItemRangeRemoved(start, itemCount);
            }
        }
    }

    //****** 插入操作 *****//

    /**
     * 通知一组数据插入
     *
     * @param groupPosition
     */
    public void notifyGroupInserted(int groupPosition) {
        structureChanged();
        GroupStructure structure = getGroupItem(groupPosition);
        int start = structure == null ? 0 : structure.getStart();
        int itemCount = countGroupItem(groupPosition);
        if (itemCount > 0) {
            notifyItemRangeInserted(start, itemCount);
        }
    }

    /**
     * 通知多组数据插入
     *
     * @param groupPosition
     * @param count
     */
    public void notifyGroupRangeInserted(int groupPosition, int count) {
        structureChanged();
        GroupStructure structure = getGroupItem(groupPosition);
        int start = structure == null ? 0 : structure.getStart();
        int itemCount = countGroupItem(groupPosition + count);
        if (itemCount > 0) {
            notifyItemRangeInserted(start, itemCount);
        }
    }

    /**
     * 通知组头插入
     *
     * @param groupPosition
     */
    public void notifyHeaderInserted(int groupPosition) {
        structureChanged();
        GroupStructure structure = getGroupItem(groupPosition);
        if (structure != null) {
            notifyItemInserted(structure.getStart());
        }
    }

    /**
     * 通知组尾插入
     *
     * @param groupPosition
     */
    public void notifyFooterInserted(int groupPosition) {
        structureChanged();
        GroupStructure structure = mStructures.get(groupPosition);
        if (structure != null) {
            notifyItemInserted(structure.getStart());
        }
    }

    /**
     * 通知一个子项到组里插入
     *
     * @param groupPosition
     * @param childPosition
     */
    public void notifyChildInserted(int groupPosition, int childPosition) {
        if (groupPosition < mStructures.size()) {
            structureChanged();
            int index = getPositionForChild(groupPosition, childPosition);
            if (index >= 0) {
                notifyItemInserted(index);
            }
        }
    }

    /**
     * 通知一组里的多个子项插入
     *
     * @param groupPosition
     * @param childPosition
     * @param count
     */
    public void notifyChildRangeInserted(int groupPosition, int childPosition, int count) {
        if (groupPosition < mStructures.size()) {
            structureChanged();
            GroupStructure structure = mStructures.get(groupPosition);
            if (count > 0 && structure != null) {
                notifyItemRangeInserted(structure.getStart() + childPosition, count);
            }
        }
    }

    /**
     * 通知一组里的所有子项插入
     *
     * @param groupPosition
     */
    public void notifyChildrenInserted(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            structureChanged();
            GroupStructure structure = mStructures.get(groupPosition);
            if (structure != null && structure.getChildrenCount() > 0) {
                notifyItemRangeInserted(structure.getStart(), structure.getChildrenCount());
            }
        }
    }

    //****** 设置点击事件 *****//

    /**
     * 设置组头点击事件
     *
     * @param listener
     */
    public void setOnHeaderClickListener(OnHeaderClickListener listener) {
        mOnHeaderClickListener = listener;
    }

    /**
     * 设置组尾点击事件
     *
     * @param listener
     */
    public void setOnFooterClickListener(OnFooterClickListener listener) {
        mOnFooterClickListener = listener;
    }

    /**
     * 设置子项点击事件
     *
     * @param listener
     */
    public void setOnChildClickListener(OnChildClickListener listener) {
        mOnChildClickListener = listener;
    }

    public int getGroupCount() {
        return getDataHandle().getItemCount();
    }

    public boolean hasHeader(int groupPosition, T itemData) {
        return true;
    }

    public boolean hasFooter(int groupPosition, T itemData) {
        return false;
    }

    public abstract int getChildrenCount(int groupPosition, T t);


    public abstract void onBindHeaderViewHolder(ViewHolder holder, T t, int groupPosition);

    public abstract void onBindFooterViewHolder(ViewHolder holder, T t, int groupPosition);

    public abstract void onBindChildViewHolder(ViewHolder holder, T t,
                                               int groupPosition, int childPosition);


    class GroupDataObserver extends RecyclerView.AdapterDataObserver {

        @Override
        public void onChanged() {
            isDataChanged = true;
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            isDataChanged = true;
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            isDataChanged = true;
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            isDataChanged = true;
        }
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(GroupedCommonAdapter adapter, int groupPosition);
    }

    public interface OnFooterClickListener {
        void onFooterClick(GroupedCommonAdapter adapter, int groupPosition);
    }

    public interface OnChildClickListener {
        void onChildClick(GroupedCommonAdapter adapter,
                          int groupPosition, int childPosition);
    }
}