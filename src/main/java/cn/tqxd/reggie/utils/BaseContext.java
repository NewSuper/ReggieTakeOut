package cn.tqxd.reggie.utils;

/**
 * 基于ThreadLocal 封装，用于保存和获取当前用户id
 */
public class BaseContext {

    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }
}
