package org.noear.solon.core;

/**
 * 通用插件接口（实现 Plugin 架构；通过Solon SPI进行申明）
 *
 * @author noear
 * @since 1.0
 * */
@FunctionalInterface
public interface Plugin {
    /**
     * 启动
     */
    void start(AopContext context) throws Throwable;

    /**
     * 预停止
     * */
    default void prestop() throws Throwable{}

    /**
     * 停止
     * */
    default void stop() throws Throwable{}
}
