package org.noear.solon.core;

/**
 * @author noear
 * @since 1.3
 */
public interface Signal {
    /**
     * 信号端口
     * */
    int port();

    /**
     * 信号协议
     * */
    String protocol();

    /**
     * 信号类型
     * */
    SignalType type();
}
