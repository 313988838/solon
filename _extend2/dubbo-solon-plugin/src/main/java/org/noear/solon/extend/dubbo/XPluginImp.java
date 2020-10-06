package org.noear.solon.extend.dubbo;

import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.noear.solon.XApp;
import org.noear.solon.core.Aop;
import org.noear.solon.core.XPlugin;


public class XPluginImp implements XPlugin {
    DubboAdapter _server;

    @Override
    public void start(XApp app) {
        if (app.source().getAnnotation(EnableDubbo.class) == null) {
            return;
        }

        _server = DubboAdapter.global(app);

        //支持duboo.Service注解
        Aop.context().beanBuilderAdd(Service.class, ((clz, bw, anno) -> {
            Class<?>[] ifs = bw.clz().getInterfaces();
            if (ifs.length > 0) {
                ServiceConfig cfg = new ServiceConfig(anno);
                if (cfg.getInterface() == null) {
                    cfg.setInterface(ifs[0]);
                }
                cfg.setRef(bw.raw());

                // 暴露及注册服务
                _server.regService(cfg);
            }
        }));

        //支持dubbo.Reference注入
        Aop.context().beanInjectorAdd(Reference.class, ((fwT, anno) -> {
            if (fwT.getType().isInterface()) {
                Object raw = _server.getService(fwT.getType(), anno);
                fwT.setValue(raw);
            }
        }));
    }


    @Override
    public void stop() throws Throwable {
        if (_server == null) {
            return;
        }

        _server.stopBlock();
        _server = null;
    }
}
