package org.noear.solon.boot.jlhttp;

import org.noear.solon.XApp;

class XServerProp {
    public static int request_maxRequestSize = 1024 * 1024 * 2;//默认2mb
    public static int session_timeout = 0;
    public static String session_state_domain;
    public static boolean output_meta = false;
    public static String encoding_request = "UTF-8";
    public static String encoding_response = "UTF-8";

    public static void init() {
        String tmp = XApp.cfg().get("server.request.maxRequestSize", "").trim().toLowerCase();//k数
        if (tmp.endsWith("mb")) {
            int val = Integer.parseInt(tmp.substring(0, tmp.length() - 2));
            request_maxRequestSize = val * 1204 * 1204;
        } else if (tmp.endsWith("kb")) {
            int val = Integer.parseInt(tmp.substring(0, tmp.length() - 2));
            request_maxRequestSize = val * 1204;
        } else if (tmp.length() > 0) {
            request_maxRequestSize = Integer.parseInt(tmp) * 1204;
        }

        session_timeout = XApp.cfg().getInt("server.session.timeout", 0);
        session_state_domain = XApp.cfg().get("server.session.state.domain");
        output_meta = XApp.cfg().getInt("solon.output.meta", 0) > 0;

        encoding_request = XApp.cfg().get("solon.encoding.request", "UTF-8");
        encoding_response = XApp.cfg().get("solon.encoding.response", "UTF-8");
    }
}
