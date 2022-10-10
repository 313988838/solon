package com.yomahub.liteflow.solon.integration;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.solon.LiteflowMonitorProperty;
import com.yomahub.liteflow.solon.LiteflowProperty;
import com.yomahub.liteflow.solon.config.LiteflowMainAutoConfiguration;
import com.yomahub.liteflow.solon.config.LiteflowPropertyAutoConfiguration;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;

import java.util.Properties;

/**
 * @author noear
 * @since 2.9
 */
public class XPluginImpl implements Plugin {
    @Override
    public void start(AopContext context) {
        //加载默认配置
        Properties defProps = Utils.loadProperties("META-INF/liteflow-default.properties");
        if (defProps != null && defProps.size() > 0) {
            defProps.forEach((k, v) -> {
                Solon.cfg().putIfAbsent(k, v);
            });
        }

        //是否启用
        boolean enable = Solon.cfg().getBool("liteflow.enable", false);

        if (!enable) {
            return;
        }

        //订阅 NodeComponent 组件
        context.subWrapsOfType(NodeComponent.class, bw -> {
            NodeComponent node1 = bw.raw();

            if (Utils.isNotEmpty(bw.name())) {
                node1.setName(bw.name());
                node1.setNodeId(bw.name());
            }

            FlowBus.addSpringScanNode(node1.getNodeId(), node1);
        });

        //扫描相关组件
        context.beanScan(LiteflowProperty.class);

//        context.beanOnloaded((ctx) -> {
//            context.beanScan(LiteflowProperty.class);
//        });
//
//        context.beanMake(LiteflowMonitorProperty.class);
//        context.beanMake(LiteflowProperty.class);
//        context.beanMake(LiteflowMainAutoConfiguration.class);
//        context.beanMake(LiteflowPropertyAutoConfiguration.class);
    }
}
