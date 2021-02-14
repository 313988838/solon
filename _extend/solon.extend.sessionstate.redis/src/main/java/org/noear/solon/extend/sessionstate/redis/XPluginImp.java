package org.noear.solon.extend.sessionstate.redis;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.core.Bridge;
import org.noear.solon.core.Plugin;

public class XPluginImp implements Plugin {
    @Override
    public void start(SolonApp app) {
        if (Solon.global().enableSessionState() == false) {
            return;
        }

        if (Bridge.sessionStateFactory().priority() >= RedisSessionStateFactory.SESSION_STATE_PRIORITY) {
            return;
        }
        /*
         *
         * server.session.state.redis:
         * server:
         * password:
         * db: 31
         * maxTotaol: 200
         *
         * */
        XServerProp.init();

        if (RedisSessionStateFactory.getInstance().getRedisX() == null) {
            return;
        }

        Bridge.sessionStateFactorySet(RedisSessionStateFactory.getInstance());

        System.out.println("solon:: Redis session state plugin is loaded");
    }
}
