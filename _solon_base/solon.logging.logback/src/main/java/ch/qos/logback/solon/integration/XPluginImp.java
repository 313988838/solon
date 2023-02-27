package ch.qos.logback.solon.integration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.solon.SolonConfigurator;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.bean.InitializingBean;
import org.noear.solon.core.util.ResourceUtil;
import org.noear.solon.logging.LogOptions;
import org.noear.solon.logging.model.LoggerLevelEntity;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * @author noear
 * @since 1.6
 */
public class XPluginImp implements Plugin , InitializingBean {
    @Override
    public void afterFieldsInject() throws Throwable {
        URL url = ResourceUtil.getResource("logback.xml");
        if (url == null) {
            //尝试环境加载
            if (Utils.isNotEmpty(Solon.cfg().env())) {
                url = ResourceUtil.getResource("logback-solon-" + Solon.cfg().env() + ".xml");
            }

            //尝试应用加载
            if (url == null) {
                url = ResourceUtil.getResource("logback-solon.xml");
            }

            //尝试默认加载
            if (url == null) {
                url = ResourceUtil.getResource("META-INF/solon_def/logback-def.xml");
            }

            if (url == null) {
                return;
            }

            initDo(url);
        }
    }

    @Override
    public void start(AopContext context) throws Throwable{
        afterFieldsInject();
    }

    private void initDo(URL url) {
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            SolonConfigurator jc = new SolonConfigurator();
            jc.setContext(loggerContext);
            loggerContext.reset();
            jc.doConfigure(url);

            //同步 logger level 配置
            if (LogOptions.getLoggerLevels().size() > 0) {
                for (LoggerLevelEntity lle : LogOptions.getLoggerLevels()) {
                    Logger logger = loggerContext.getLogger(lle.getLoggerExpr());
                    logger.setLevel(Level.valueOf(lle.getLevel().name()));
                }
            }
        } catch (JoranException e) {
            throw new IllegalStateException(e);
        }
    }
}
