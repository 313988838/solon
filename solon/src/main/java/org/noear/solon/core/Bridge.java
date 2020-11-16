package org.noear.solon.core;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Note;
import org.noear.solon.core.handle.*;
import org.noear.solon.core.tran.TranExecutor;

import java.util.*;

/**
 * 内部扩展桥接器
 *
 * <pre><code>
 * //示例：替换 SessionState 服务 (solon.extend.sessionstate.redis: org.noear.solon.extend.sessionstate.redis.XPluginImp.class)
 * public class PluginImp implements Plugin{
 *     @Override
 *     public void start(Solon app) {
 *         //检测 sessionState 是否存在；且优先级是否低于 RedisSessionState
 *         if (Bridge.sessionState() != null
 *                 && Bridge.sessionState().priority() >= RedisSessionState.SESSION_STATE_PRIORITY) {
 *             return;
 *         }
 *
 *         //如果条件满足，则替换掉已有的 sessionState
 *         Bridge.sessionStateSet(RedisSessionState.create());
 *     }
 * }
 *
 * //示例：替换 TranExecutor 服务 (solon.extend.data: org.noear.solon.extend.data.XPluginImp.class)
 * public class PluginImp implements Plugin{
 *     @Override
 *     public void start(Solon app) {
 *         if (app.enableTransaction()) {
 *             //如果有启用事务，则替换 tranExecutor
 *             Bridge.tranExecutorSet(TranExecutorImp.global);
 *         }
 *     }
 * }
 *
 * </code></pre>
 *
 * @author noear
 * @since 1.0
 * */
public class Bridge {
    //
    // SessionState 对接 //与函数同名，_开头
    //
    private static SessionState _sessionState = new XSessionStateDefault();
    private static boolean sessionStateUpdated;

    /**
     * 设置Session状态管理器
     */
    @Note("设置Session状态管理器")
    public static void sessionStateSet(SessionState ss) {
        if (ss != null) {
            _sessionState = ss;

            if (sessionStateUpdated == false) {
                sessionStateUpdated = true;

                Solon.global().before("**", MethodType.HTTP, (c) -> {
                    _sessionState.sessionRefresh();
                });
            }
        }
    }

    /**
     * 获取Session状态管理器
     */
    @Note("获取Session状态管理器")
    public static SessionState sessionState() {
        return _sessionState;
    }

    static class XSessionStateDefault implements SessionState {
        @Override
        public String sessionId() {
            return null;
        }

        @Override
        public Object sessionGet(String key) {
            return null;
        }

        @Override
        public void sessionSet(String key, Object val) {

        }
    }


    //
    // UpstreamFactory 对接
    //
    private static Upstream.Factory _upstreamFactory = null;

    /**
     * 获取负载工厂
     */
    @Note("获取负载工厂")
    public static Upstream.Factory upstreamFactory() {
        return _upstreamFactory;
    }

    /**
     * 设置负载工厂
     */
    @Note("设置负载工厂")
    public static void upstreamFactorySet(Upstream.Factory uf) {
        if (uf != null) {
            _upstreamFactory = uf;
        }
    }


    //
    // XActionExecutor 对接
    //

    /**
     * 动作默认执行器
     */
    private static ActionExecutor _actionExecutorDef = new ActionExecutorDefault();
    /**
     * 动作执行库
     */
    private static Set<ActionExecutor> _actionExecutors = new HashSet<>();

    /**
     * 获取默认的Action执行器
     */
    @Note("获取默认的Action执行器")
    public static ActionExecutor actionExecutorDef() {
        return _actionExecutorDef;
    }

    /**
     * 设置默认的Action执行器
     */
    @Note("设置默认的Action执行器")
    public static void actionExecutorDefSet(ActionExecutor ae) {
        if (ae != null) {
            _actionExecutorDef = ae;
        }
    }

    /**
     * 获取所有Action执行器
     */
    @Note("获取所有Action执行器")
    public static Set<ActionExecutor> actionExecutors() {
        return Collections.unmodifiableSet(_actionExecutors);
    }

    /**
     * 添加Action执行器
     */
    @Note("添加Action执行器")
    public static void actionExecutorAdd(ActionExecutor e) {
        if (e != null) {
            _actionExecutors.add(e);
        }
    }


    //
    // XRender 对接
    //

    /**
     * 注册渲染器
     *
     * @param render 渲染器
     */
    @Note("注册渲染器")
    public static void renderRegister(Render render) {
        if (render != null) {
            RenderManager.register(render);
        }
    }

    /**
     * 印射渲染关系
     *
     * @param suffix 文件后缀名
     * @param render 渲染器
     */
    @Note("印射渲染关系")
    public static void renderMapping(String suffix, Render render) {
        if (suffix != null && render != null) {
            RenderManager.mapping(suffix, render);
        }
    }

    /**
     * 印射渲染关系
     *
     * @param suffix    文件后缀名
     * @param className 渲染器类名
     */
    @Note("印射渲染关系")
    public static void renderMapping(String suffix, String className) {
        if (suffix != null && className != null) {
            RenderManager.mapping(suffix, className);
        }
    }


    //
    // XTranExecutor 对接
    //
    private static TranExecutor _tranExecutor = () -> false;

    /**
     * 获取事务执行器
     */
    @Note("获取事务执行器")
    public static TranExecutor tranExecutor() {
        return _tranExecutor;
    }

    /**
     * 设置事务执行器
     */
    @Note("设置事务执行器")
    public static void tranExecutorSet(TranExecutor te) {
        if (te != null) {
            _tranExecutor = te;
        }
    }

}