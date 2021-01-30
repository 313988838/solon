package org.noear.solon.boot.jlhttp;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.core.*;
import org.noear.solon.core.handle.MethodType;

import javax.net.ssl.SSLServerSocketFactory;

public final class XPluginImp implements Plugin {
    private HTTPServer _server = null;

    public static String solon_boot_ver(){
        return "jlhttp 2.4/" + Solon.cfg().version();
    }

    @Override
    public  void start(SolonApp app) {
        if(app.enableHttp() == false){
            return;
        }
        //如果有jetty插件，就不启动了
        if(Utils.loadClass("org.noear.solon.boot.jetty.XPluginImp") != null){
            return;
        }

        //如果有undrtow插件，就不启动了
        if(Utils.loadClass("org.noear.solon.boot.undertow.XPluginImp") != null){
            return;
        }

        XServerProp.init();

        Aop.beanOnloaded(() -> {
            start0(app);
        });
    }

    private void start0(SolonApp app) {
        _server = new HTTPServer();

        long time_start = System.currentTimeMillis();

        JlHttpContextHandler _handler = new JlHttpContextHandler();

        if (System.getProperty("javax.net.ssl.keyStore") != null) { // enable SSL if configured
            _server.setServerSocketFactory(SSLServerSocketFactory.getDefault());
        }

        HTTPServer.VirtualHost host = _server.getVirtualHost((String) null);

        host.setDirectoryIndex(null);

        host.addContext("/", _handler,
                MethodType.HEAD.name,
                MethodType.GET.name,
                MethodType.POST.name,
                MethodType.PUT.name,
                MethodType.DELETE.name,
                MethodType.PATCH.name);

        System.out.println("solon.Server:main: JlHttpServer 2.4(jlhttp)");

        try {
            _server.setPort(app.port());
            _server.start();

            app.signalAdd(new SignalSim(app.port(), "http", SignalType.HTTP));

            long time_end = System.currentTimeMillis();

            System.out.println("solon.Connector:main: jlhttp: Started ServerConnector@{HTTP/1.1,[http/1.1]}{0.0.0.0:" + app.port() + "}");
            System.out.println("solon.Server:main: jlhttp: Started @" + (time_end - time_start) + "ms");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void stop() throws Throwable {
        if(_server != null) {
            _server.stop();
            _server = null;

            System.out.println("solon.Server:main: jlhttp: Has Stopped " + solon_boot_ver());
        }
    }
}



/*
1389行修改：
添加：getOriginalUri()；解决getUri()，无法拿到域和端口问题

1366 + 2111行修改：
获取：socket 的址址，作为：remoteAddr（否则没有远程连接地址）

1491行修改：
添加_paramsList，实现参数寄存功能（流只能读一次，后面就没了）

2807行修改：
将编译改为：UTF-8；解决中文参数乱码问题

1748行修改：（优先使用传进来的contentType，解决内部404之类的调用无法显示为html的问题 ）
public void sendHeaders(int status, long length, long lastModified,
                                String etag, String contentType, long[] range) throws IOException {
String ct = headers.get("Content-Type");
            if (ct == null) {
                ct = contentType != null ? contentType : "application/octet-stream";
                headers.add("Content-Type", ct);
            }else {
                if (contentType != null) { //xyj,20181220
                    ct = contentType;
                    headers.replace("Content-Type", ct);
                }
            }

* */
