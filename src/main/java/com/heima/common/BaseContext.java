package com.heima.common;

/*
 * 基于TreadLocald封装的工具类,用户保存和获取当前登录用户的id
 * */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
