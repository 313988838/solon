package org.noear.solon.cloud.impl;

import org.noear.solon.cloud.CloudClient;
import org.noear.solon.logging.event.Appender;
import org.noear.solon.logging.event.Level;
import org.noear.solon.logging.event.LogEvent;

/**
 * cloud appender
 *
 * @author noear 2021/2/23 created
 */
public class CloudLogAppender implements Appender {
    @Override
    public Level getDefaultLevel() {
        return Level.INFO;
    }

    @Override
    public void append(LogEvent logEvent) {
        CloudClient.log().append(logEvent);
    }
}
