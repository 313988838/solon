package org.apache.logging.log4j.solon.integration;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.util.ResourceUtil;
import org.noear.solon.logging.LogOptions;
import org.noear.solon.logging.model.LoggerLevelEntity;

import java.net.URL;

/**
 * @author noear
 * @since 1.6
 */
public class XPluginImp implements Plugin {
    @Override
    public void init(AopContext context) throws Throwable {
        URL url = ResourceUtil.getResource("log4j2.xml");
        if (url == null) {
            //尝试环境加载
            if (Utils.isNotEmpty(Solon.cfg().env())) {
                url = ResourceUtil.getResource("log4j2-solon-" + Solon.cfg().env() + ".xml");
            }

            //尝试应用加载
            if (url == null) {
                url = ResourceUtil.getResource("log4j2-solon.xml");
            }

            //尝试默认加载
            if (url == null) {
                url = ResourceUtil.getResource("META-INF/solon_def/log4j2-def.xml");
            }

            if (url == null) {
                return;
            }

            initDo(url);
        }
    }

    @Override
    public void start(AopContext context) throws Throwable{
        init(context);
    }

    private void initDo(URL url) {
        try {
            Configurator.reconfigure(url.toURI());

            //同步 logger level 配置
            if (LogOptions.getLoggerLevels().size() > 0) {
                LoggerContext lctx = LoggerContext.getContext(false);
                Configuration lcfg = lctx.getConfiguration();

                for (LoggerLevelEntity lle : LogOptions.getLoggerLevels()) {
                    LoggerConfig logger = new LoggerConfig(lle.getLoggerExpr(),
                            Level.valueOf(lle.getLevel().name()),
                            true);
                    lcfg.addLogger(logger.getName(), logger);
                }

                lctx.updateLoggers();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
