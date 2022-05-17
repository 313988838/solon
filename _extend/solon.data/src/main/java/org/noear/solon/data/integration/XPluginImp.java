package org.noear.solon.data.integration;

import org.noear.solon.Solon;
import org.noear.solon.core.*;
import org.noear.solon.data.annotation.*;
import org.noear.solon.data.cache.*;
import org.noear.solon.data.tran.TranExecutor;
import org.noear.solon.data.around.CacheInterceptor;
import org.noear.solon.data.around.CachePutInterceptor;
import org.noear.solon.data.around.CacheRemoveInterceptor;
import org.noear.solon.data.around.TranInterceptor;
import org.noear.solon.data.tran.TranExecutorImp;

public class XPluginImp implements Plugin {
    @Override
    public void start(AopContext context) {
        //注册缓存工厂
        CacheLib.cacheFactoryAdd("local", new LocalCacheFactoryImpl());

        //添加事务控制支持
        if (Solon.global().enableTransaction()) {
            Aop.wrapAndPut(TranExecutor.class, TranExecutorImp.global);

            Aop.context().beanAroundAdd(Tran.class, new TranInterceptor(), 120);
        }

        //添加缓存控制支持
        if (Solon.global().enableCaching()) {
            CacheLib.cacheServiceAddIfAbsent("", LocalCacheService.instance);

            Solon.global().onEvent(BeanWrap.class, new CacheServiceEventListener());

            Aop.context().beanOnloaded((ctx) -> {
                if (ctx.hasWrap(CacheService.class) == false) {
                    ctx.wrapAndPut(CacheService.class, LocalCacheService.instance);
                }
            });

            Aop.context().beanAroundAdd(CachePut.class, new CachePutInterceptor(), 110);
            Aop.context().beanAroundAdd(CacheRemove.class, new CacheRemoveInterceptor(), 110);
            Aop.context().beanAroundAdd(Cache.class, new CacheInterceptor(), 111);
        }
    }
}
