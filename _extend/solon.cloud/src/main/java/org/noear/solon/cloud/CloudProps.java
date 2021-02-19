package org.noear.solon.cloud;

import org.noear.solon.Solon;
import org.noear.solon.Utils;

/**
 * 云服务属性配置
 *
 * @author noear
 * @since 1.2
 */
public class CloudProps {
    public static String LOG_DEFAULT_LOGGER;

    private String SERVER = "solon.cloud.@@.server";
    private String TOKEN = "solon.cloud.@@.token";
    private String USERNAME = "solon.cloud.@@.username";
    private String PASSWORD = "solon.cloud.@@.password";
    private String ALARM = "solon.cloud.@@.alarm";

    //配置服务相关
    private String CONFIG_ENABLE = "solon.cloud.@@.config.enable";
    private String CONFIG_SERVER = "solon.cloud.@@.config.server";
    private String CONFIG_LOAD_GROUP = "solon.cloud.@@.config.loadGroup"; //（对某些框架来讲，可能没用处）
    private String CONFIG_LOAD_KEY = "solon.cloud.@@.config.loadKey";
    private String CONFIG_REFRESH_INTERVAL = "solon.cloud.@@.config.refreshInterval";

    //发现服务相关
    private String DISCOVERY_ENABLE = "solon.cloud.@@.discovery.enable";
    private String DISCOVERY_SERVER = "solon.cloud.@@.discovery.server";
    private String DISCOVERY_TAGS = "solon.cloud.@@.discovery.tags";
    private String DISCOVERY_UNSTABLE = "solon.cloud.@@.discovery.unstable";
    private String DISCOVERY_HEALTH_CHECK_PATH = "solon.cloud.@@.discovery.healthCheckPath";
    private String DISCOVERY_HEALTH_CHECK_INTERVAL = "solon.cloud.@@.discovery.healthCheckInterval";
    private String DISCOVERY_HEALTH_DETECTOR = "solon.cloud.@@.discovery.healthDetector";
    private String DISCOVERY_REFRESH_INTERVAL = "solon.cloud.@@.discovery.refreshInterval";

    //事件总线服务相关
    private String EVENT_ENABLE = "solon.cloud.@@.event.enable";
    private String EVENT_SERVER = "solon.cloud.@@.event.server";
    private String EVENT_RECEIVE = "solon.cloud.@@.event.receive";
    private String EVENT_EXCHANGE = "solon.cloud.@@.event.exchange";
    private String EVENT_PREFETCH_COUNT = "solon.cloud.@@.event.prefetchCount";
    private String EVENT_PUBLISH_TIMEOUT = "solon.cloud.@@.event.publishTimeout";
    private String EVENT_QUEUE = "solon.cloud.@@.event.queue";
    private String EVENT_SEAL = "solon.cloud.@@.event.seal";


    //锁服务相关
    private String LOCK_ENABLE = "solon.cloud.@@.lock.enable";

    //日志总线服务相关
    private String LOG_ENABLE = "solon.cloud.@@.log.enable";
    private String LOG_SERVER = "solon.cloud.@@.log.server";
    private String LOG_DEFAULT = "solon.cloud.@@.log.default";

    //链路跟踪服务相关
    private String TRACE_ENABLE = "solon.cloud.@@.trace.enable";


    public CloudProps(String frame) {
        SERVER = SERVER.replace("@@", frame);
        TOKEN = TOKEN.replace("@@", frame);
        USERNAME = USERNAME.replace("@@", frame);
        PASSWORD = PASSWORD.replace("@@", frame);
        ALARM = ALARM.replace("@@", frame);

        CONFIG_ENABLE = CONFIG_ENABLE.replace("@@", frame);
        CONFIG_SERVER = CONFIG_SERVER.replace("@@", frame);
        CONFIG_LOAD_GROUP = CONFIG_LOAD_GROUP.replace("@@", frame);
        CONFIG_LOAD_KEY = CONFIG_LOAD_KEY.replace("@@", frame);
        CONFIG_REFRESH_INTERVAL = CONFIG_REFRESH_INTERVAL.replace("@@", frame);

        DISCOVERY_ENABLE = DISCOVERY_ENABLE.replace("@@", frame);
        DISCOVERY_SERVER = DISCOVERY_SERVER.replace("@@", frame);
        DISCOVERY_TAGS = DISCOVERY_TAGS.replace("@@", frame);
        DISCOVERY_UNSTABLE = DISCOVERY_UNSTABLE.replace("@@", frame);
        DISCOVERY_HEALTH_CHECK_PATH = DISCOVERY_HEALTH_CHECK_PATH.replace("@@", frame);
        DISCOVERY_HEALTH_CHECK_INTERVAL = DISCOVERY_HEALTH_CHECK_INTERVAL.replace("@@", frame);
        DISCOVERY_HEALTH_DETECTOR = DISCOVERY_HEALTH_DETECTOR.replace("@@", frame);
        DISCOVERY_REFRESH_INTERVAL = DISCOVERY_REFRESH_INTERVAL.replace("@@", frame);

        EVENT_ENABLE = EVENT_ENABLE.replace("@@", frame);
        EVENT_SERVER = EVENT_SERVER.replace("@@", frame);
        EVENT_RECEIVE = EVENT_RECEIVE.replace("@@", frame);
        EVENT_EXCHANGE = EVENT_EXCHANGE.replace("@@", frame);
        EVENT_PREFETCH_COUNT = EVENT_PREFETCH_COUNT.replace("@@", frame);
        EVENT_PUBLISH_TIMEOUT = EVENT_PUBLISH_TIMEOUT.replace("@@", frame);
        EVENT_QUEUE = EVENT_QUEUE.replace("@@", frame);
        EVENT_SEAL = EVENT_SEAL.replace("@@", frame);

        LOCK_ENABLE = LOCK_ENABLE.replace("@@", frame);

        LOG_ENABLE = LOG_ENABLE.replace("@@", frame);
        LOG_SERVER = LOG_SERVER.replace("@@", frame);
        LOG_DEFAULT = LOG_DEFAULT.replace("@@", frame);

        TRACE_ENABLE = TRACE_ENABLE.replace("@@", frame);
    }


    //
    //公共
    //
    public String getServer() {
        return Solon.cfg().get(SERVER);
    }

    public String getToken() {
        return Solon.cfg().get(TOKEN);
    }

    public String getUsername() {
        return Solon.cfg().get(USERNAME);
    }

    public String getPassword() {
        return Solon.cfg().get(PASSWORD);
    }
    public String getAlarm() {
        return Solon.cfg().get(ALARM);
    }

    //
    //配置
    //
    public boolean getConfigEnable() {
        return Solon.cfg().getBool(CONFIG_ENABLE, true);
    }

    public String getConfigServer() {
        String tmp = Solon.cfg().get(CONFIG_SERVER);
        if (Utils.isEmpty(tmp)) {
            return getServer();
        } else {
            return tmp;
        }
    }

    public String getConfigLoadGroup() {
        return Solon.cfg().get(CONFIG_LOAD_GROUP);
    }

    public String getConfigLoadKey() {
        return Solon.cfg().get(CONFIG_LOAD_KEY);
    }

    public String getConfigRefreshInterval(String def) {
        return Solon.cfg().get(CONFIG_REFRESH_INTERVAL, def);//def:10s
    }


    //
    //发现
    //
    public boolean getDiscoveryEnable() {
        return Solon.cfg().getBool(DISCOVERY_ENABLE, true);
    }

    public String getDiscoveryServer() {
        String tmp = Solon.cfg().get(DISCOVERY_SERVER);
        if (Utils.isEmpty(tmp)) {
            return getServer();
        } else {
            return tmp;
        }
    }


    public String getDiscoveryTags() {
        return Solon.cfg().get(DISCOVERY_TAGS);
    }

    public boolean getDiscoveryUnstable() {
        return Solon.cfg().getBool(DISCOVERY_UNSTABLE, false);
    }

    public String getDiscoveryHealthCheckPath() {
        return Solon.cfg().get(DISCOVERY_HEALTH_CHECK_PATH, "/run/check/");
    }

    public String getDiscoveryHealthCheckInterval(String def) {
        return Solon.cfg().get(DISCOVERY_HEALTH_CHECK_INTERVAL, def); //def:5s
    }

    public String getDiscoveryHealthDetector() {
        return Solon.cfg().get(DISCOVERY_HEALTH_DETECTOR);
    }

    public String getDiscoveryRefreshInterval(String def) {
        return Solon.cfg().get(DISCOVERY_REFRESH_INTERVAL, def);//def:10s
    }

    //
    //事件总线服务相关
    //
    public boolean getEventEnable() {
        return Solon.cfg().getBool(EVENT_ENABLE, true);
    }

    public String getEventServer() {
        String tmp = Solon.cfg().get(EVENT_SERVER);
        if (Utils.isEmpty(tmp)) {
            return getServer();
        } else {
            return tmp;
        }
    }

    public String getEventReceive() {
        return Solon.cfg().get(EVENT_RECEIVE);
    }
    public void setEventReceive(String value) {
        Solon.cfg().setProperty(EVENT_RECEIVE, value);
    }
    public String getEventExchange() {
        return Solon.cfg().get(EVENT_EXCHANGE);
    }
    public int getEventPrefetchCount() {
        return Solon.cfg().getInt(EVENT_PREFETCH_COUNT,0);
    }
    public int getEventPublishTimeout() {
        return Solon.cfg().getInt(EVENT_PUBLISH_TIMEOUT,0);
    }
    public String getEventSeal() {
        return Solon.cfg().get(EVENT_SEAL);
    }
    public String getEventQueue() {
        return Solon.cfg().get(EVENT_QUEUE);
    }

    //
    //锁服务相关
    //
    public boolean getLockEnable() {
        return Solon.cfg().getBool(LOCK_ENABLE, true);
    }


    //
    //日志总线服务相关
    //
    public boolean getLogEnable() {
        return Solon.cfg().getBool(LOG_ENABLE, true);
    }

    public String getLogServer() {
        String tmp = Solon.cfg().get(LOG_SERVER);
        if (Utils.isEmpty(tmp)) {
            return getServer();
        } else {
            return tmp;
        }
    }

    public String getLogDefault() {
        return Solon.cfg().get(LOG_DEFAULT);
    }



    //
    //链路跟踪服务相关
    //
    public boolean getTraceEnable() {
        return Solon.cfg().getBool(TRACE_ENABLE, true);
    }


}
