package org.noear.solon.cloud.extend.snowflake.service;

import org.noear.solon.cloud.extend.snowflake.impl.SnowflakeId;
import org.noear.solon.cloud.service.CloudIdService;

/**
 * @author noear
 * @since 1.3
 */
public class CloudIdServiceImp implements CloudIdService {

    private SnowflakeId snowFlakeId;

    public CloudIdServiceImp(String dataBlock){
        snowFlakeId = new SnowflakeId(dataBlock);
    }

    @Override
    public long generate() {
        return snowFlakeId.nextId();
    }
}
