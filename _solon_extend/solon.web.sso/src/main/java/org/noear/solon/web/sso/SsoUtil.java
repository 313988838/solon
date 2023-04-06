package org.noear.solon.web.sso;

import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;
import org.noear.solon.web.sso.impl.SsoService;

import java.io.Serializable;

/**
 * 单点工具类
 *
 * @author noear
 * @since 2.2
 */
public final class SsoUtil {
    static SsoService service = new SsoService();
    static SsoStorage storage;

    static {
        Solon.context().getBeanAsync(SsoStorage.class, bean -> {
            //从容器获取存储器
            storage = bean;
        });
    }

    /**
     * 手动设置存储器
     */
    public static void setStorage(SsoStorage storage) {
        SsoUtil.storage = storage;
    }


    /////////////////////////////////////////

    /**
     * 登录
     *
     * @param userId 用户Id
     */
    public static void login(Serializable userId) {
        login(Context.current(), userId);
    }

    /**
     * 登录
     *
     * @param userId 用户Id
     */
    public static void login(Context ctx, Serializable userId) {
        service.login(storage, ctx, userId);
    }

    /**
     * 登出（当前用户）
     */
    public static void logout() {
        //检测会话中的用户Id
        Serializable userId = service.getLoginedUserId(Context.current());

        if (userId != null) {
            logout(userId);
        }
    }

    /**
     * 登出（指定用户）
     */
    public static void logout(Serializable userId) {
        service.logout(storage, userId);
    }

    /**
     * 当前用户是否已登录
     */
    public static boolean isLogined() {
        return isLogined(Context.current());
    }

    /**
     * 当前用户是否已登录
     */
    public static boolean isLogined(Context ctx) {
        return service.isLogined(storage, ctx);
    }
}
