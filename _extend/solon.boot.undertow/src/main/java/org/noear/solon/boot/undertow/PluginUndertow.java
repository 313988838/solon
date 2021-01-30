package org.noear.solon.boot.undertow;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.*;
import org.noear.solon.SolonApp;
import org.noear.solon.boot.undertow.http.UtHandlerJspHandler;
import org.noear.solon.boot.undertow.websocket.UtWsConnectionCallback;
import org.noear.solon.boot.undertow.websocket._SessionManagerImpl;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.Plugin;
import org.noear.solon.socketd.SessionManager;

import static io.undertow.Handlers.websocket;

/**
 * @author  by: Yukai
 * @since : 2019/3/28 15:49
 */
class PluginUndertow extends PluginUndertowBase implements Plugin {
    Undertow _server;
    int port;
    public PluginUndertow(int port){
        this.port = port;
    }

    @Override
    public void start(SolonApp app) {
        try {
            setup(app);

            _server.start();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void stop() throws Throwable {
        if (_server != null) {
            _server.stop();
            _server = null;
        }
    }

    protected void setup(SolonApp app) throws Throwable {
        HttpHandler httpHandler = buildHandler();

        //************************** init server start******************
        Undertow.Builder builder = Undertow.builder();

        builder.setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false);
        builder.setServerOption(UndertowOptions.MAX_ENTITY_SIZE, XServerProp.request_maxRequestSize);

        builder.addHttpListener(port, "0.0.0.0");

        if (app.enableWebSocket()) {
            builder.setHandler(websocket(new UtWsConnectionCallback(), httpHandler));

            SessionManager.register(new _SessionManagerImpl());
        } else {
            builder.setHandler(httpHandler);
        }


        //1.1:分发事件（充许外部扩展）
        EventBus.push(builder);

        _server = builder.build();

        //************************* init server end********************
    }

    protected HttpHandler buildHandler() throws Exception {
        DeploymentInfo builder = initDeploymentInfo();

        //添加servlet
        builder.addServlet(new ServletInfo("ACTServlet", UtHandlerJspHandler.class).addMapping("/"));
        //builder.addInnerHandlerChainWrapper(h -> handler); //这个会使过滤器不能使用


        //开始部署
        final ServletContainer container = Servlets.defaultContainer();
        DeploymentManager manager = container.addDeployment(builder);
        manager.deploy();

        return manager.start();
    }
}
