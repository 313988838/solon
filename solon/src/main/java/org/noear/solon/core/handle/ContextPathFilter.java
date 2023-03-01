package org.noear.solon.core.handle;

import org.noear.solon.Solon;

/**
 * 提供 ContextPath 类似的功能（优先级要极高）
 *
 * @author noear
 * @since 1.8
 */
public class ContextPathFilter implements Filter {
    private final String path;
    private final boolean forced;

    /**
     * @param path '/demo/'
     */
    public ContextPathFilter(String path, boolean forced) {
        String newPath = null;
        if (path.endsWith("/")) {
            newPath = path;
        } else {
            newPath = path + "/";
        }

        if (newPath.startsWith("/")) {
            this.path = newPath;
        } else {
            this.path = "/" + newPath;
        }

        this.forced = forced;

        //有可能是 ContextPathFilter 是用户手动添加的！需要补一下配置
        Solon.cfg().serverContextPath(this.path);
    }

    public ContextPathFilter(String path) {
        this(path, false);
    }


    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        if (ctx.pathNew().startsWith(path)) {
            ctx.pathNew(ctx.path().substring(path.length() - 1));
        } else {
            if (forced) {
                return;
            }
        }

        chain.doFilter(ctx);
    }
}
