package org.noear.solon.core.event;

import org.noear.solon.Solon;
import org.noear.solon.core.AopContext;

import java.util.HashSet;
import java.util.Set;

/**
 * 监听器（内部类，外部不要使用）
 *
 * @see AopContext#beanLoaded()
 * @see Solon#onEvent(Class, EventListener)
 * */
public final class EventBus {
    //异常订阅者
    private static Set<HH> sThrow = new HashSet<>();
    //其它订阅者
    private static Set<HH> sOther = new HashSet<>();

    /**
     * 推送事件
     */
    public static void push(Object event) {
        if (event != null) {
            if (event instanceof Throwable) {
                //异常分发
                push0(sThrow, event);

                if(Solon.cfg().isDebugMode() || Solon.cfg().isFilesMode()){
                    ((Throwable) event).printStackTrace();
                }
            } else {
                //其它事件分发
                push0(sOther, event);
            }
        }
    }

    private static void push0(Set<HH> hhs, Object event) {
        for (HH h1 : hhs) {
            if (h1.t.isInstance(event)) {
                try {
                    h1.h.onEvent(event);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 订阅事件
     */
    public static <T> void subscribe(Class<T> eventType, EventListener<T> handler) {
        if (Throwable.class.isAssignableFrom(eventType)) {
            sThrow.add(new HH(eventType, handler));
        } else {
            sOther.add(new HH(eventType, handler));
        }
    }

    /**
     * Handler Holder
     */
    static class HH {
        protected Class<?> t;
        protected EventListener h;

        public HH(Class<?> type, EventListener handler) {
            this.t = type;
            this.h = handler;
        }
    }
}