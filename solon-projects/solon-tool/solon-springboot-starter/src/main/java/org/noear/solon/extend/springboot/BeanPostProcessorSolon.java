package org.noear.solon.extend.springboot;

import org.noear.solon.Solon;
import org.noear.solon.core.BeanWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author noear
 * @since 1.2
 */
public class BeanPostProcessorSolon implements BeanPostProcessor {
    static final Logger log = LoggerFactory.getLogger(BeanPostProcessorSolon.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.startsWith("org.springframework") == false) {
            if (bean.getClass().getAnnotations().length > 0) {
                try {
                    //
                    //支持Solon的注入能力（别的 solon 注入注解就不需要另外适配了）
                    //
                    Solon.context().beanInject(bean);
                } catch (Throwable e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.startsWith("org.springframework") == false) {

            if (bean.getClass().getAnnotations().length > 0) {
                try {
                    //
                    //同步到Solon的容器
                    //
                    BeanWrap bw = Solon.context().wrap(bean.getClass(), bean);

                    if (bean.getClass().getSimpleName().equalsIgnoreCase(beanName)) {
                        Solon.context().beanRegister(bw, null, true);
                    } else {
                        Solon.context().beanRegister(bw, beanName, true);
                    }
                } catch (Throwable e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }

        return bean;
    }
}
