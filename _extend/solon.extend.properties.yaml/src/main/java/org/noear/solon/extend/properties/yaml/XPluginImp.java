package org.noear.solon.extend.properties.yaml;

import org.noear.solon.Solon;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.PropsLoader;

public class XPluginImp implements Plugin {
    @Override
    public void start(Solon app) {
        if (PropsLoader.global() instanceof org.noear.solon.extend.properties.yaml.PropertiesLoader) {
            return;
        } else {
            //切换配置加载器
            PropsLoader.globalSet(org.noear.solon.extend.properties.yaml.PropertiesLoader.g);

            //尝试.yml的配置加载
            app.prop().loadAdd("application.yml");
        }
    }
}
