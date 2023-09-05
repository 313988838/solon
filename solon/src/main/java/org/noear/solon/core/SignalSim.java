package org.noear.solon.core;

/**
 * 信号简单实现
 *
 * @author noear
 * @since 1.3
 */
public class SignalSim implements Signal {
    private String host;
    private int port;
    private String protocol;
    private SignalType type;
    private String name;

    /**
     * 信号名
     * */
    @Override
    public String name() {
        return name;
    }

    /**
     * 主机
     * */
    @Override
    public String host() {
        return host;
    }

    /**
     * 信号端口
     * */
    @Override
    public int port() {
        return port;
    }

    /**
     * 信号协议
     * */
    @Override
    public String protocol() {
        return protocol;
    }

    /**
     * 信号类型
     * */
    @Override
    public SignalType type() {
        return type;
    }

    public SignalSim(String name, String host, int port, String protocol, SignalType type) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.protocol = protocol.toLowerCase();
        this.type = type;
    }
}
