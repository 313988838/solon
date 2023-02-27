package org.noear.solon.boot.jdkhttp;

import com.sun.net.httpserver.*;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.boot.ServerConstants;
import org.noear.solon.boot.ServerProps;
import org.noear.solon.boot.prop.impl.HttpServerProps;
import org.noear.solon.boot.ssl.SslContextFactory;
import org.noear.solon.core.*;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.util.ClassUtil;
import org.noear.solon.core.util.LogUtil;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.net.InetSocketAddress;

public final class XPluginImp implements Plugin {
    private static Signal _signal;

    public static Signal signal() {
        return _signal;
    }

    private HttpServer _server = null;

    public static String solon_boot_ver() {
        return "jdk http/" + Solon.version();
    }

    @Override
    public void start(AopContext context) {
        if (Solon.app().enableHttp() == false) {
            return;
        }

        //如果有jetty插件，就不启动了
        if (ClassUtil.loadClass("org.noear.solon.boot.jetty.XPluginImp") != null) {
            return;
        }

        //如果有undrtow插件，就不启动了
        if (ClassUtil.loadClass("org.noear.solon.boot.undertow.XPluginImp") != null) {
            return;
        }

        //如果有smarthttp插件，就不启动了
        if (ClassUtil.loadClass("org.noear.solon.boot.smarthttp.XPluginImp") != null) {
            return;
        }

        context.beanOnloaded((ctx) -> {
            try {
                start0(Solon.app());
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private void start0(SolonApp app) throws Throwable {
        //初始化属性
        ServerProps.init();

        HttpServerProps props = new HttpServerProps();
        final String _host = props.getHost();
        final int _port = props.getPort();
        final String _name = props.getName();

        long time_start = System.currentTimeMillis();

        LogUtil.global().info("Server:main: Sun.net.HttpServer(jdkhttp)");


        if (System.getProperty(ServerConstants.SSL_KEYSTORE) != null) {
            // enable SSL if configured
            if (Utils.isNotEmpty(_host)) {
                _server = HttpsServer.create(new InetSocketAddress(_host, _port), 0);
            } else {
                _server = HttpsServer.create(new InetSocketAddress(_port), 0);
            }

            addSslConfig((HttpsServer) _server);
        } else {
            if (Utils.isNotEmpty(_host)) {
                _server = HttpServer.create(new InetSocketAddress(_host, _port), 0);
            } else {
                _server = HttpServer.create(new InetSocketAddress(_port), 0);
            }
        }

        HttpContext httpContext = _server.createContext("/", new JdkHttpContextHandler());
        httpContext.getFilters().add(new ParameterFilter());

        _server.setExecutor(props.getBioExecutor("jdkhttp-"));
        _server.start();

        final String _wrapHost = props.getWrapHost();
        final int _wrapPort = props.getWrapPort();
        _signal = new SignalSim(_name, _wrapHost, _wrapPort, "http", SignalType.HTTP);

        app.signalAdd(_signal);

        long time_end = System.currentTimeMillis();

        LogUtil.global().info("Connector:main: jdkhttp: Started ServerConnector@{HTTP/1.1,[http/1.1]}{http://localhost:" + _port + "}");
        LogUtil.global().info("Server:main: jdkhttp: Started @" + (time_end - time_start) + "ms");
    }

    private void addSslConfig(HttpsServer httpsServer) throws IOException {
        SSLContext sslContext = SslContextFactory.create();

        httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            public void configure(HttpsParameters params) {
                try {
                    // Initialise the SSL context
                    SSLContext c = SSLContext.getDefault();
                    SSLEngine engine = c.createSSLEngine();
                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    // Get the default parameters
                    SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
                    params.setSSLParameters(defaultSSLParameters);
                } catch (Throwable e) {
                    //"Failed to create HTTPS port"
                    EventBus.pushTry(e);
                }
            }
        });
    }

    @Override
    public void stop() throws Throwable {
        if (_server == null) {
            return;
        }

        _server.stop(0);
        _server = null;
        LogUtil.global().info("Server:main: jdkhttp: Has Stopped " + solon_boot_ver());
    }
}
