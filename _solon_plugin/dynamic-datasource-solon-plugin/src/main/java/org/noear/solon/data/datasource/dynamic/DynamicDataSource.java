package org.noear.solon.data.datasource.dynamic;

import org.noear.solon.data.datasource.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * 动态数据源
 *
 * @author noear
 * @since 1.11
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    public DynamicDataSource(Properties props) {
        if (props == null || props.size() == 0) {
            //缺少配置
            throw new IllegalStateException("Missing dynamic data source configuration");
        }

        String strictStr = props.getProperty("strict", "false");
        props.remove("strict");

        Map<String, DataSource> dataSourceMap = DynamicDsUtils.buildDsMap(props);

        //::获取默认数据源
        DataSource defSource = dataSourceMap.get("default");

        if (defSource == null) {
            throw new IllegalStateException("Missing default data source configuration");
        }

        //::初始化
        setStrict(Boolean.parseBoolean(strictStr));
        setTargetDataSources(dataSourceMap);
        setDefaultTargetDataSource(defSource);
    }


    @Override
    protected String determineCurrentKey() {
        return DynamicDsUtils.getCurrent();
    }
}