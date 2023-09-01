package org.noear.solon.annotation;

import org.noear.solon.core.aspect.Interceptor;

import java.lang.annotation.*;

/**
 * 环绕拦截处理（针对 Controller、Service、Dao 等所有基于 MethodWrap 运行的目标，附加拦截器）
 *
 * <pre><code>
 * @Controller
 * public class DemoController{
 *     @Around(TranInterceptor.class)  //@Tran 注解即通过 @Around 实现
 *     @Mapping("/demo/*")
 *     public String hello(){
 *         return "heollo world;";
 *     }
 * }
 *
 * //
 * // 注解传导示例：（用于简化使用）
 * //
 * @Around(value = TranInterceptor.class, index = 7)
 * @Target({ElementType.METHOD})
 * @Retention(RetentionPolicy.RUNTIME)
 * public @interface Tran {
 *     ....
 * }
 *
 * @Around(value = CacheInterceptor.class, index = 8)
 * @Target({ElementType.METHOD})
 * @Retention(RetentionPolicy.RUNTIME)
 * public @interface Cache {
 *     ...
 * }
 * </code></pre>
 *
 * @author noear
 * @since 1.0
 * @deprecated 2.5
 * */
@Deprecated
@Inherited //要可继承
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Around { //intercept
    /**
     * 调用处理程序
     * */
    Class<? extends Interceptor> value();
    /**
     * 调用顺位
     * */
    int index() default 0;
}
