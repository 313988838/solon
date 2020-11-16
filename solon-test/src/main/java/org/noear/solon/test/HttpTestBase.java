package org.noear.solon.test;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;

public class HttpTestBase {
    public boolean enablePrint(){
        return true;
    }

    public HttpUtils path(String path) {
        return http("http://localhost:" + Solon.global().port() + path);
    }

    public HttpUtils http(String url) {
        return HttpUtils.http(url).enablePrintln(enablePrint());
    }
}
