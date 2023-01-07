package cn.tqxd.reggie.utils;

/**
 * 基于ThreadLocal 封装，用于保存和获取当前用户id
 *
 * ThreadLocal 为每个线程提供一份单独空间,具有线程隔离的效果，只有在线程内才能获取到对应的值，线程外则不能访问
 *
 * 公共字段自动填充
 * 实现步骤
 * 1.编写BaseContext 工具类，基于ThreadLocal封装的工具类
 * 2.在LoginCheckFilter的 doFilter方法中调用 BaseContext来设置当前登录者的id
 * 2.在MyMetaObjectHandler中调用BaseContext获取当前登录的id
 *
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
