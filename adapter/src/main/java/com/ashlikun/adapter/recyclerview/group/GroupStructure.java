package com.ashlikun.adapter.recyclerview.group;

/**
 * 这个类是用来记录分组列表中组的结构的。
 * 通过GroupStructure记录每个组是否有头部，是否有尾部和子项的数量。从而能方便的计算
 * 列表的长度和每个组的组头、组尾和子项在列表中的位置。
 */
public class GroupStructure {

    private boolean hasHeader;
    private boolean hasFooter;
    private int childrenCount;
    private int childrenStart;
    private int childrenEnd;

    public GroupStructure(boolean hasHeader, boolean hasFooter, int childrenStart, int childrenCount) {
        init(hasHeader, hasFooter, childrenStart, childrenCount);
    }

    public void init(boolean hasHeader, boolean hasFooter, int childrenStart, int childrenCount) {
        this.hasHeader = hasHeader;
        this.hasFooter = hasFooter;
        if (hasHeader) {
            this.childrenStart = childrenStart + 1;
        } else {
            this.childrenStart = childrenStart;
        }

        this.childrenCount = childrenCount;
        this.childrenEnd = Math.max(0, this.childrenStart + this.childrenCount - 1);

    }

    public boolean hasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        if (this.hasHeader) {
            if (!hasHeader) {
                this.childrenStart = childrenStart - 1;
            }
        } else {
            if (hasHeader) {
                this.childrenStart = childrenStart + 1;
            }
        }
        this.hasHeader = hasHeader;
    }

    public boolean hasFooter() {
        return hasFooter;
    }

    public void setHasFooter(boolean hasFooter) {
        this.childrenEnd = Math.max(0, this.childrenStart + this.childrenCount - 1);
        this.hasFooter = hasFooter;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
        this.childrenEnd = Math.max(0, this.childrenStart + this.childrenCount - 1);
    }

    public int getChildrenStart() {
        return childrenStart;
    }

    public void setChildrenStart(int childrenStart) {
        this.childrenStart = childrenStart;
        this.childrenEnd = Math.max(0, this.childrenStart + this.childrenCount - 1);
    }

    public int getChildrenEnd() {
        return childrenEnd;
    }

    public int getStart() {
        if (hasHeader()) {
            return childrenStart - 1;
        }
        return childrenStart;
    }

    public int getEnd() {
        if (hasFooter()) {
            return childrenEnd + 1;
        }
        return childrenEnd;
    }

    /**
     * 计算一个组里有多少个Item（头加尾加子项）
     *
     * @return
     */
    public int countGroupItem() {
        int itemCount = 0;
        if (hasHeader()) {
            itemCount += 1;
        }
        itemCount += getChildrenCount();
        if (hasFooter()) {
            itemCount += 1;
        }
        return itemCount;
    }

    public boolean containsPosition(int position) {
        return position >= getStart() && position <= getEnd();
    }

    /**
     * 判断item的type 头部 尾部 和 子项
     *
     * @param position
     * @return
     */
    public int containsPositionType(int position) {
        if (hasHeader()) {
            if (position <= getStart()) {
                return GroupedCommonAdapter.TYPE_HEADER;
            }
        }
        if (position <= getChildrenEnd()) {
            return GroupedCommonAdapter.TYPE_CHILD;
        }
        if (hasFooter()) {
            if (position <= getEnd()) {
                return GroupedCommonAdapter.TYPE_FOOTER;
            }
        }
        return -1;
    }

    /**
     * 根据下标计算position在组中位置（childPosition）
     *
     * @param position 下标
     * @return 子项下标 childPosition
     */
    public int getChildPositionForPosition(int position) {
        if (containsPosition(position)) {
            return position - childrenStart;
        }
        return -1;
    }
}
