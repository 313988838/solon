package org.noear.solon.serialization.jackson;

import org.noear.solon.Solon;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Bridge;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.RenderManager;
import org.noear.solon.serialization.prop.JsonProps;
import org.noear.solon.serialization.prop.JsonPropsUtil;

public class XPluginImp implements Plugin {
    public static boolean output_meta = false;

    @Override
    public void start(AopContext context) {
        output_meta = Solon.cfg().getInt("solon.output.meta", 0) > 0;
        JsonProps jsonProps = JsonProps.create(context);

        //绑定属性
        applyProps(JacksonRenderFactory.global, jsonProps);

        //事件扩展
        EventBus.push(JacksonRenderFactory.global);

        RenderManager.mapping("@json", JacksonRenderFactory.global.create());
        RenderManager.mapping("@type_json", JacksonRenderTypedFactory.global.create());

        //支持 json 内容类型执行
        JacksonActionExecutor executor = new JacksonActionExecutor();
        EventBus.push(executor);

        Bridge.actionExecutorAdd(executor);
    }

    private void applyProps(JacksonRenderFactory factory, JsonProps jsonProps) {
        if (JsonPropsUtil.apply(factory, jsonProps)) {
            NullValueSerializer typeNullSerializer = new NullValueSerializer(jsonProps);

            factory.config().getSerializerProvider()
                    .setNullValueSerializer(typeNullSerializer);
        }
    }
}
