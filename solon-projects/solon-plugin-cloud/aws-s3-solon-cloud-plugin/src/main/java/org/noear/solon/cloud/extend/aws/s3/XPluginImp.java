package org.noear.solon.cloud.extend.aws.s3;

import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudManager;
import org.noear.solon.cloud.CloudProps;
import org.noear.solon.cloud.extend.aws.s3.service.CloudFileServiceOfS3HttpImpl;
import org.noear.solon.cloud.extend.aws.s3.service.CloudFileServiceOfS3SdkImpl;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.util.ClassUtil;

/**
 * @author noear
 * @since 1.3
 */
public class XPluginImp implements Plugin {
    final String AWS_SDK_TAG = "com.amazonaws.services.s3.AmazonS3ClientBuilder";

    @Override
    public void start(AopContext context) {
        CloudProps cloudProps = new CloudProps(context, "aws.s3");

        if (cloudProps.getFileEnable()) {
            if (Utils.isEmpty(cloudProps.getFileAccessKey())) {
                return;
            }

            if (ClassUtil.loadClass(AWS_SDK_TAG) == null) {
                CloudManager.register(new CloudFileServiceOfS3HttpImpl(cloudProps));
            } else {
                CloudManager.register(new CloudFileServiceOfS3SdkImpl(cloudProps));
            }
        }
    }
}
