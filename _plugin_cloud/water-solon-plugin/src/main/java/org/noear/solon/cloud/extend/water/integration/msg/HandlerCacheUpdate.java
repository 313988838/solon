package org.noear.solon.cloud.extend.water.integration.msg;

import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudEventHandler;
import org.noear.solon.cloud.extend.water.service.CloudDiscoveryServiceImp;
import org.noear.solon.cloud.model.Event;
import org.noear.water.WW;
import org.noear.weed.WeedConfig;
import org.noear.weed.cache.ICacheServiceEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author noear
 * @since 1.2
 */
public class HandlerCacheUpdate implements CloudEventHandler {
    static Logger logger = LoggerFactory.getLogger(WW.water_log_upstream);

    CloudDiscoveryServiceImp discoveryService;
    public HandlerCacheUpdate(CloudDiscoveryServiceImp discoveryService){
        this.discoveryService = discoveryService;
    }

    @Override
    public boolean handler(Event event) {
        String[] tag_keys = event.content().split(";");

        for (String tagKey : tag_keys) {
            if (Utils.isNotEmpty(tagKey)) {
                this.cacheUpdateHandler0(tagKey);
                this.cacheUpdateHandler1(tagKey);
            }
        }

        return true;
    }

    /**
     * 更新 upstream
     * */
    public void cacheUpdateHandler0(String tag) {
        String[] ss = tag.split(":");
        if ("upstream".equals(ss[0])) {
            String service = ss[1];
            try {
                discoveryService.onUpdate(Solon.cfg().appGroup(), service);
            } catch (Exception ex) {
                ex.printStackTrace();//最后日志记录到服务端
                logger.error(ss[1], "reload", "", ex);
            }
        }
    }

    /**
     * 更新 cache
     * */
    public void cacheUpdateHandler1(String tag) {
        if (tag.indexOf(".") > 0) {
            String[] ss = tag.split("\\.");
            if (ss.length == 2) {
                ICacheServiceEx cache = WeedConfig.libOfCache.get(ss[0]);
                if (cache != null) {
                    cache.clear(ss[1]);
                }
            }
        } else {
            for (ICacheServiceEx cache : WeedConfig.libOfCache.values()) {
                cache.clear(tag);
            }
        }
    }
}
