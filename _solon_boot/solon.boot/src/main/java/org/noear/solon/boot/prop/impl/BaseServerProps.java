package org.noear.solon.boot.prop.impl;

import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.boot.prop.ServerExecutorProps;
import org.noear.solon.boot.prop.ServerSignalProps;

/**
 * @author noear
 * @since 1.10
 */
public abstract class BaseServerProps implements ServerSignalProps, ServerExecutorProps {
    private String PROP_NAME = "server.@@.name";
    private String PROP_PORT = "server.@@.port";
    private String PROP_HOST = "server.@@.host";
    private String PROP_IMAGE_PORT = "server.@@.imagePort";
    private String PROP_IMAGE_HOST = "server.@@.imageHost";

    private String PROP_CORETHREADS = "server.@@.coreThreads";
    private String PROP_MAXTHREADS = "server.@@.maxThreads";
    private String PROP_IDLETIMEOUT = "server.@@.idleTimeout";

    private String name;
    private int port;
    private String host;
    private int imagePort;
    private String imageHost;

    private int coreThreads;
    private int maxThreads;
    private long idleTimeout;

    protected BaseServerProps(String signalName, int portBase) {
        PROP_NAME = PROP_NAME.replace("@@", signalName);
        PROP_PORT = PROP_PORT.replace("@@", signalName);
        PROP_HOST = PROP_HOST.replace("@@", signalName);

        PROP_IMAGE_PORT = PROP_IMAGE_PORT.replace("@@", signalName);
        PROP_IMAGE_HOST = PROP_IMAGE_HOST.replace("@@", signalName);

        PROP_CORETHREADS = PROP_CORETHREADS.replace("@@", signalName);
        PROP_MAXTHREADS = PROP_MAXTHREADS.replace("@@", signalName);
        PROP_IDLETIMEOUT = PROP_IDLETIMEOUT.replace("@@", signalName);

        //
        initSignalProps(portBase);

        //
        initExecutorProps();
    }


   private void initSignalProps(int portBase){
       name = Solon.cfg().get(PROP_NAME);
       port = Solon.cfg().getInt(PROP_PORT, 0);
       host = Solon.cfg().get(PROP_HOST);

       imagePort = Solon.cfg().getInt(PROP_IMAGE_PORT, 0);
       imageHost = Solon.cfg().get(PROP_IMAGE_HOST);

       //host + port
       if (port < 1) {
           port = portBase + Solon.cfg().serverPort();
       }

       if(imagePort < 1){
           imagePort = port;
       }

       //
       if (Utils.isEmpty(host)) {
           host = Solon.cfg().serverHost();
       }

       if (Utils.isEmpty(imageHost)) {
           imageHost = host;
       }
   }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getHost() {
        return host;
    }

    /**
     * @since 1.12
     * */
    @Override
    public int getImagePort() {
        return imagePort;
    }

    /**
     * @since 1.12
     * */
    @Override
    public String getImageHost() {
        return imageHost;
    }

    ////////////////////////////////

    private void initExecutorProps(){
        idleTimeout = Solon.cfg().getLong(PROP_IDLETIMEOUT, 0L);

        //支持：16 或 x16（倍数）
        String coreThreadsStr = Solon.cfg().get(PROP_CORETHREADS);
        if (Utils.isNotEmpty(coreThreadsStr)) {
            if (coreThreadsStr.startsWith("x")) {
                //倍数模式
                if (coreThreadsStr.length() > 1) {
                    coreThreads = getCoreNum() * Integer.parseInt(coreThreadsStr.substring(1));
                } else {
                    coreThreads = 0;
                }
            } else {
                coreThreads = Integer.parseInt(coreThreadsStr);
            }
        }

        //支持：16 或 x16（倍数）
        String maxThreadsStr = Solon.cfg().get(PROP_MAXTHREADS);
        if (Utils.isNotEmpty(maxThreadsStr)) {
            if (maxThreadsStr.startsWith("x")) {
                //倍数模式
                if (maxThreadsStr.length() > 1) {
                    maxThreads = getCoreNum() * Integer.parseInt(maxThreadsStr.substring(1));
                } else {
                    maxThreads = 0;
                }
            } else {
                maxThreads = Integer.parseInt(maxThreadsStr);
            }
        }
    }

    /**
     * Cpu 核数
     */
    private int getCoreNum() {
        return Runtime.getRuntime().availableProcessors();
    }


    /**
     * 核心线程数
     */
    @Override
    public int getCoreThreads() {
        if (coreThreads > 0) {
            return coreThreads;
        } else {
            return Math.max(getCoreNum(), 2);
        }
    }

    /**
     * 最大线程数
     */
    @Override
    public int getMaxThreads(boolean bio) {
        if (maxThreads > 0) {
            return maxThreads;
        } else {
            if (bio) {
                return getCoreThreads() * 16;
            } else {
                return getCoreThreads() * 8;
            }
        }
    }

    /**
     * 闪置超时
     */
    @Override
    public long getIdleTimeout() { //idleTimeout
        if (idleTimeout > 0) {
            return idleTimeout;
        } else {
            return 60000;
        }
    }
}
