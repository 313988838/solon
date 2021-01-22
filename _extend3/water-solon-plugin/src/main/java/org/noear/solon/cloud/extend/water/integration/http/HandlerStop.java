package org.noear.solon.cloud.extend.water.integration.http;

import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.cloud.model.Instance;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.water.WaterClient;
import org.noear.water.utils.IPUtils;

/**
 * @author noear
 * @since 1.2
 */
public class HandlerStop implements Handler {
    @Override
    public void handle(Context ctx) throws Throwable {
        ctx.output(handle0(ctx));
    }

    private String handle0(Context ctx) throws Throwable {
        String ip = IPUtils.getIP(ctx);
        if (WaterClient.Whitelist.existsOfMasterIp(ip)) {
            stateSet(false);
            Solon.stop();
            return "OK";
        } else {
            return (ip + ",not is whitelist!");
        }
    }

    public void stateSet(boolean enabled) {
        Instance instance = Instance.local();

        if (Utils.isNotEmpty(instance.address())) {
            String meta = null;
            if (Instance.local().meta() != null) {
                meta = ONode.stringify(instance.meta());
            }

            WaterClient.Registry.set(instance.service(), instance.address(), meta, enabled);
        }
    }
}
