package org.noear.solon.boot.smarthttp.http;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.boot.smarthttp.XPluginImp;
import org.noear.solon.boot.smarthttp.XServerProp;
import org.noear.solon.core.event.EventBus;
import org.smartboot.http.HttpRequest;
import org.smartboot.http.HttpResponse;
import org.smartboot.http.enums.HttpStatus;
import org.smartboot.http.server.handle.HttpHandle;

import java.io.IOException;

public class SmartHttpContextHandler extends HttpHandle {


    @Override
    public void doHandle(HttpRequest request, HttpResponse response) throws IOException {
        /*
         *
         * jlhttp 流程
         *
         * 1.处理，并暂存结果
         * 2.输出头
         * 3.输出内容
         *
         * */

        try {
            SmartHttpContext context = new SmartHttpContext(request, response);

            context.contentType("text/plain;charset=UTF-8");
            if (XServerProp.output_meta) {
                context.headerSet("solon.boot", XPluginImp.solon_boot_ver());
            }

            Solon.global().tryHandle(context);

            if (context.getHandled() && context.status() != 404) {
                context.commit();
            } else {
                context.statusSet(404);
                context.commit();
            }
        } catch (Throwable ex) {
            EventBus.push(ex);
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        finally {
//            response.close(); //性能非常差
//        }
    }
}
