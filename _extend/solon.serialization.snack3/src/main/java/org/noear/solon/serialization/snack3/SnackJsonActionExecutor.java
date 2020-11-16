package org.noear.solon.serialization.snack3;

import org.noear.snack.ONode;
import org.noear.solon.core.handler.ActionExecutorDefault;
import org.noear.solon.core.handler.Context;

import java.lang.reflect.Parameter;

public class SnackJsonActionExecutor extends ActionExecutorDefault {
    private static final String label = "/json";

    @Override
    public boolean matched(Context ctx, String ct) {
        if (ct != null && ct.contains(label)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected Object changeBody(Context ctx) throws Exception {
        return ONode.loadStr(ctx.body());
    }

    @Override
    protected Object changeValue(Context ctx, Parameter p, int pi, Class<?> pt, Object bodyObj) throws Exception {
        if (bodyObj == null) {
            return null;
        }

        if (ctx.paramMap().containsKey(p.getName())) {
            return super.changeValue(ctx, p, pi, pt, bodyObj);
        }

        ONode tmp = (ONode) bodyObj;

        if (tmp.isObject()) {
            if (tmp.contains(p.getName())) {
                return tmp.get(p.getName()).toObject(pt);
            } else if (ctx.paramMap().containsKey(p.getName())) {
                //有可能是path变量
                //
                return super.changeValue(ctx, p, pi, pt, bodyObj);
            } else {
                return tmp.toObject(pt);
            }
        }

        if (tmp.isArray()) {
            return tmp.toObject(pt);
        }

        return tmp.val().getRaw();
    }
}
