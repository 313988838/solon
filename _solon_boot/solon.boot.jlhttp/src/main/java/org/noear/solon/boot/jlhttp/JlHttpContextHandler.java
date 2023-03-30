package org.noear.solon.boot.jlhttp;

import org.noear.solon.Solon;
import org.noear.solon.boot.ServerProps;
import org.noear.solon.core.event.EventBus;

import java.io.IOException;

public class JlHttpContextHandler implements HTTPServer.ContextHandler {

    @Override
    public int serve(HTTPServer.Request request, HTTPServer.Response response) throws IOException {
        try {
            return handleDo(request, response);
        } catch (Throwable e) {
            //context 初始化时，可能会出错
            //
            EventBus.pushTry(e);

            response.sendHeaders(500);
            return 0;
        }
    }

    protected int handleDo(HTTPServer.Request request, HTTPServer.Response response) throws IOException {
        JlHttpContext ctx = new JlHttpContext(request, response);

        ctx.contentType("text/plain;charset=UTF-8");

        if (ServerProps.output_meta) {
            ctx.headerSet("Solon-Boot", XPluginImp.solon_boot_ver());
        }

        Solon.app().tryHandle(ctx);

        if (ctx.getHandled() || ctx.status() >= 200) {
            ctx.commit();

            return 0;
        } else {
            return 404;
        }
    }
}
