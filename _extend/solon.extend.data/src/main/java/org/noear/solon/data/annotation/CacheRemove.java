package org.noear.solon.data.annotation;

import org.noear.solon.extend.data.around.CacheRemoveInterceptor;
import org.noear.solon.annotation.*;
import java.lang.annotation.*;

/**
 * 缓存移除注解器
 *
 * @author noear
 * @since 1.0
 * */
@Around(value = CacheRemoveInterceptor.class, index = -9)
@Inherited //要可继承
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheRemove {
    /**
     * 缓存服务
     * */
    @Note("缓存服务")
    String service() default "";
    /**
     * 例：user_${user_id} ，user_id 为参数
     * */
    @Note("清除缓存标签，多个以逗号隔开")
    String tags();
}
