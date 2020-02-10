package com.chong.mcspczuul.Holder;

import com.chong.mcspczuul.entity.ThreadLocalParamEntity;

public class ThreadLocalHolder {
    private static ThreadLocal<ThreadLocalParamEntity> currentThreadLocal
            = new ThreadLocal<ThreadLocalParamEntity>();

    public static ThreadLocal<ThreadLocalParamEntity> getCurrentThreadLocal() {
        return currentThreadLocal;
    }

    public static void setCurrentThreadLocal(ThreadLocal<ThreadLocalParamEntity> currentThreadLocal) {
        ThreadLocalHolder.currentThreadLocal = currentThreadLocal;
    }
}
