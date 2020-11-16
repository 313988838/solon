package org.noear.solon.extend.feign;

import feign.Request;
import feign.RequestTemplate;
import feign.Target;
import org.noear.solon.Utils;
import org.noear.solon.core.UpstreamService;

public class FeignTarget<T> implements Target<T> {
    private final Class<T> type;
    private final String name;
    private final String path;
    private final UpstreamService upstream;

    public FeignTarget(Class<T> type, String name, String path, UpstreamService upstream) {
        this.type = type;
        this.name = name;
        this.path = path;
        this.upstream = upstream;
    }

    @Override
    public Class<T> type() {
        return this.type;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String url() {
        if (Utils.isEmpty(path)) {
            return this.upstream.getServer();
        } else {
            return this.upstream.getServer() + path;
        }
    }

    @Override
    public Request apply(RequestTemplate input) {
        if (input.url().indexOf("http") != 0) {
            input.insert(0, this.url());
        }

        return input.request();
    }
}
