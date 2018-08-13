package com.example.capture.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 排序工具类
 */
public class SortListUtil<E> {
    public void sortByMethod(List<E> list, final String method, final boolean reverseFlag) {
        Collections.sort(list, new Comparator<Object>() {
            @SuppressWarnings("unchecked")
            public int compare(Object arg1, Object arg2) {
                int result = 0;
                try {
                    Method m1 = ((E) arg1).getClass().getMethod(method, null);
                    Method m2 = ((E) arg2).getClass().getMethod(method, null);
                    Object obj1 = m1.invoke(((E) arg1), null);
                    Object obj2 = m2.invoke(((E) arg2), null);
                    if (obj1 instanceof String) {
                        // 字符串
                        result = obj1.toString().compareTo(obj2.toString());
                    } else if (obj1 instanceof Long) {
                        // 整型
                        result = (int) ((Long) obj1 - (Long) obj2);
                    } else {
                        // 不支持的对象，直接转换为String，然后比较
                        result = obj1.toString().compareTo(obj2.toString());
                    }
                    if (reverseFlag) {
                        // 倒序
                        result = -result;
                    }
                } catch (NoSuchMethodException nsme) {
                    nsme.printStackTrace();
                } catch (IllegalAccessException iae) {
                    iae.printStackTrace();
                } catch (InvocationTargetException ite) {
                    ite.printStackTrace();
                }
                return result;
            }
        });
    }
}
