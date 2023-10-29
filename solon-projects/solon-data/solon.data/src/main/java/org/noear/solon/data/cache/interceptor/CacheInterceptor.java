package org.noear.solon.data.cache.interceptor;

import org.noear.solon.core.aspect.Invocation;
import org.noear.solon.data.cache.CacheExecutorImp;
import org.noear.solon.data.annotation.Cache;
import org.noear.solon.core.aspect.Interceptor;

/**
 * 缓存拦截器
 *
 * @author noear
 * @since 1.0
 * */
public class CacheInterceptor implements Interceptor {
    @Override
    public Object doIntercept(Invocation inv) throws Throwable {
        Cache anno = inv.getMethodAnnotation(Cache.class);

        return CacheExecutorImp.global
                .cache(anno, inv, () -> inv.invoke());
    }
}