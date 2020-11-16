package org.noear.solon.core;

import org.noear.solon.Solon;

/**
 * 通用插件接口（实现 Plugin 架构；通过Solon ISP进行申明）
 *
 * @author noear
 * @since 1.0
 * */
@FunctionalInterface
public interface Plugin {
    /**
     * 启动 （stop 可通过: app.onStop(..) 实现）
     */
    void start(Solon app);

    /**
     * 停止
     * */
    default void stop() throws Throwable{}
}
