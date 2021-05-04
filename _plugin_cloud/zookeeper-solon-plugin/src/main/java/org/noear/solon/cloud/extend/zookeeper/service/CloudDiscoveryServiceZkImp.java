package org.noear.solon.cloud.extend.zookeeper.service;

import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudDiscoveryHandler;
import org.noear.solon.cloud.extend.zookeeper.impl.ZkClient;
import org.noear.solon.cloud.model.Discovery;
import org.noear.solon.cloud.model.Instance;
import org.noear.solon.cloud.service.CloudDiscoveryObserverEntity;
import org.noear.solon.cloud.service.CloudDiscoveryService;

import java.util.List;
import java.util.Map;

/**
 * @author noear
 * @since 1.3
 */
public class CloudDiscoveryServiceZkImp implements CloudDiscoveryService {
    private static final String PATH_ROOT = "/solon/register";
    private ZkClient client;

    public CloudDiscoveryServiceZkImp(ZkClient client) {
        this.client = client;
        this.client.connectServer();

        this.client.createNode("/solon");
        this.client.createNode(PATH_ROOT);

        /**
         * reg
         * /solon/register/{group}/{name}/ip = meta;
         *
         * cfg
         * /solon/config/{group}/
         * */
    }


    @Override
    public void register(String group, Instance instance) {
        registerState(group, instance, true);
    }

    @Override
    public void registerState(String group, Instance instance, boolean health) {
        if (health) {
            client.createNode(
                    String.format("%s/%s", PATH_ROOT, group));

            client.createNode(
                    String.format("%s/%s/%s", PATH_ROOT, group, instance.service()));

            String info = ONode.stringify(instance);
            client.createNode(
                    String.format("%s/%s/%s/%s", PATH_ROOT, group, instance.service(), instance.address()),
                    info, false);

        } else {
            deregister(group, instance);
        }
    }

    @Override
    public void deregister(String group, Instance instance) {
        client.removeNode(
                String.format("%s/%s/%s/%s", PATH_ROOT, group, instance.service(), instance.address()));
    }

    @Override
    public Discovery find(String group, String service) {
        if (Utils.isEmpty(group)) {
            group = Solon.cfg().appGroup();
        }

        Discovery discovery = new Discovery(service);

        List<String> nodeDataList = client.findChildrenNode(
                String.format("%s/%s/%s", PATH_ROOT, group, service));

        for (String v : nodeDataList) {
            Instance instance = ONode.deserialize(v, Instance.class);
            discovery.instanceAdd(instance);
        }

        return discovery;
    }

    @Override
    public void attention(String group, String service, CloudDiscoveryHandler observer) {
        if (Utils.isEmpty(group)) {
            group = Solon.cfg().appGroup();
        }

        CloudDiscoveryObserverEntity entity = new CloudDiscoveryObserverEntity(group, service, observer);

        client.watchChildrenNode(String.format("%s/%s/%s", PATH_ROOT, group, service), (event) -> {
            Discovery discovery = find(entity.group, service);
            entity.handler(discovery);
        });

    }
}
