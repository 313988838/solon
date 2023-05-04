package org.noear.solon.boot.undertow;

import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.util.DefaultClassIntrospector;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.boot.ServerLifecycle;
import org.noear.solon.boot.ServerProps;
import org.noear.solon.boot.prop.impl.HttpServerProps;
import org.noear.solon.boot.undertow.http.UtContainerInitializer;
import org.noear.solon.core.runtime.NativeDetector;
import org.noear.solon.core.util.ResourceUtil;

import javax.servlet.MultipartConfigElement;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

abstract class UndertowServerBase implements ServerLifecycle {
    protected HttpServerProps props = new HttpServerProps();

    public HttpServerProps getProps() {
        return props;
    }

    protected DeploymentInfo initDeploymentInfo() {
        MultipartConfigElement configElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));

        DeploymentInfo builder = new DeploymentInfo()
                .setClassLoader(XPluginImp.class.getClassLoader())
                .setDeploymentName("solon")
                .setContextPath("/")
                .setDefaultEncoding(ServerProps.request_encoding)
                .setDefaultMultipartConfig(configElement)
                .setClassIntrospecter(DefaultClassIntrospector.INSTANCE);

        //添加容器初始器
        builder.addServletContainerInitializer(UtContainerInitializer.info());
        builder.setEagerFilterInit(true);

        if (ServerProps.session_timeout > 0) {
            builder.setDefaultSessionTimeout(ServerProps.session_timeout);
        }

        return builder;
    }

    protected String getResourceRoot() throws FileNotFoundException {
        URL rootURL = getRootPath();
        if (rootURL == null) {
            if(NativeDetector.inNativeImage()){
                return "";
            }

            throw new FileNotFoundException("Unable to find root");
        }

        String resURL = rootURL.toString();

        if (Solon.cfg().isDebugMode() && (resURL.startsWith("jar:") == false)) {
            int endIndex = resURL.indexOf("target");
            return resURL.substring(0, endIndex) + "src/main/resources/";
        }

        return "";
    }

    protected URL getRootPath() {
        URL root = ResourceUtil.getResource("/");
        if (root != null) {
            return root;
        }
        try {
            URL temp = ResourceUtil.getResource("");
            if (temp == null) {
                return null;
            }

            String path = temp.toString();
            if (path.startsWith("jar:")) {
                int endIndex = path.indexOf("!");
                path = path.substring(0, endIndex + 1) + "/";
            } else {
                return null;
            }
            return new URL(path);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
