package org.noear.solon.boot.undertow.http;

import org.noear.solon.extend.servlet.SolonServletHandler;
import org.noear.solon.boot.undertow.XPluginImp;
import org.noear.solon.boot.undertow.XServerProp;
import org.noear.solon.core.handle.Context;

//Servlet模式
public class UtHandlerJspHandler extends SolonServletHandler {
    @Override
    protected void preHandle(Context ctx) {
        if (XServerProp.output_meta) {
            ctx.headerSet("solon.boot", XPluginImp.solon_boot_ver());
        }
    }
}