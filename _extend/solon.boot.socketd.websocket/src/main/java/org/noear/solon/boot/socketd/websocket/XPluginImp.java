package org.noear.solon.boot.socketd.websocket;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.Signal;
import org.noear.solon.core.SignalSim;
import org.noear.solon.core.SignalType;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.util.PrintUtil;
import org.noear.solon.socketd.SessionManager;

public class XPluginImp implements Plugin {
    private WsServer _server = null;

    public static String solon_boot_ver() {
        return "org.java_websocket 1.5.0/" + Solon.cfg().version();
    }

    @Override
    public void start(SolonApp app) {
        //注册会话管理器
        SessionManager.register(new _SessionManagerImpl());

        if (app.enableWebSocket() == false) {
            return;
        }

        String _name = app.cfg().get("server.websocket.name");
        int _port = app.cfg().getInt("server.websocket.port", 0);
        if (_port < 1) {
            _port = 15000 + app.port();
        }

        long time_start = System.currentTimeMillis();


        PrintUtil.info("solon.server:main: org.java_websocket 1.5.0(websocketd)");

        try {
            _server = new WsServer(_port);

            _server.start();


            app.signalAdd(new SignalSim(_name, _port, "ws", SignalType.WEBSOCKET));

            long time_end = System.currentTimeMillis();

            PrintUtil.info("solon.connector:main: websocketd: Started ServerConnector@{HTTP/1.1,[WebSocket]}{0.0.0.0:" + _port + "}");
            PrintUtil.info("solon.server:main: websocketd: Started @" + (time_end - time_start) + "ms");
        } catch (Exception ex) {
            EventBus.push(ex);
        }
    }

    @Override
    public void stop() throws Throwable {
        if (_server != null) {
            _server.stop();
            _server = null;

            PrintUtil.info("solon.server:main: websocketd: Has Stopped " + solon_boot_ver());
        }
    }
}
