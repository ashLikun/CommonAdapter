package com.ashlikun.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/21 0021　上午 11:16
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：适配器的一些工具
 */
public class AdapterUtils {
    private static HashMap<Class, AccessibleObject> viewBindingGetMap;

    /**
     * 查找屏幕中最后一个item位置
     */
    public static int findLastVisiblePosition(RecyclerView.LayoutManager layoutManager) {
        int lastVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] into = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            lastVisibleItemPosition = findMaxOrMin(into, true);
        } else {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
        return lastVisibleItemPosition;
    }

    /**
     * 查找屏幕中第一个item位置
     *
     * @param layoutManager
     * @return
     */
    public static int findFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int firstVisibleItem = -1;
        if (layoutManager != null) {
            if (layoutManager instanceof GridLayoutManager) {
                firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] firstPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(firstPositions);
                firstVisibleItem = findMaxOrMin(firstPositions, false);
            }
        }
        return firstVisibleItem;
    }

    private static int findMaxOrMin(int[] lastPositions, boolean isMax) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (isMax) {
                if (value > max) {
                    max = value;
                }
            } else {
                if (value < max) {
                    max = value;
                }
            }
        }
        return max;
    }

    public static void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder
                    .itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    public static int viewTypeToVLayout(Object viewType) {
        return Math.abs(viewType.hashCode());
    }

    /**
     * 反射字段
     *
     * @param object    要反射的对象
     * @param fieldName 要反射的字段名称
     */
    public static Field setField(Object object, String fieldName, Object value) {
        if (object == null || fieldName == null || fieldName.isEmpty()) {
            return null;
        }
        try {
            Field field = getAllDeclaredField(object.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(object, value);
                return field;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定的字段
     */
    public static Field getAllDeclaredField(Class<?> claxx, String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return null;
        }

        while (claxx != null && claxx != Object.class) {
            try {
                Field f = claxx.getDeclaredField(fieldName);
                if (f != null) {
                    return f;
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            claxx = claxx.getSuperclass();
        }
        return null;
    }

    /**
     * 方法功能：从context中获取activity，如果context不是activity那么久返回null
     */
    public static Activity getActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        } else if (context instanceof ContextThemeWrapper) {
            return getActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 反射查找ViewBinding的view
     *
     * @return 实例是ViewBinding
     */
    public static ViewBinding getViewBinding(Object object) {
        if (object == null) {
            return null;
        }
        try {
            //检测是否有ViewBinding 库
            Class viewBindingCls = ViewBinding.class;
            if (viewBindingGetMap == null) {
                viewBindingGetMap = new HashMap<>();
            }
            Class objCls = object.getClass();
            //查找缓存
            AccessibleObject accessibleObject = viewBindingGetMap.get(objCls);
            if (accessibleObject != null) {
                accessibleObject.setAccessible(true);
                if (accessibleObject instanceof Method) {
                    return ((ViewBinding) ((Method) accessibleObject).invoke(object));
                } else if (accessibleObject instanceof Field) {
                    return ((ViewBinding) ((Field) accessibleObject).get(object));
                }
            }
            Class cls = objCls;
            while (cls != null && cls != Object.class) {
                //获取方法->返回值是
                Method[] declaredMethods = cls.getDeclaredMethods();
                for (Method m : declaredMethods) {
                    if (viewBindingCls.isAssignableFrom(m.getReturnType())) {
                        m.setAccessible(true);
                        ViewBinding view = ((ViewBinding) m.invoke(object));
                        viewBindingGetMap.put(objCls, m);
                        return view;
                    }
                }
                //获取父类的
                cls = cls.getSuperclass();
            }
        } catch (Exception e) {
            return null;
        } catch (NoClassDefFoundError e) {
            return null;
        }
        return null;
    }
}
