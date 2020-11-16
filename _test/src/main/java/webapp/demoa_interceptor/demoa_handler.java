package webapp.demoa_interceptor;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handler.Context;
import org.noear.solon.core.handler.Handler;

@Mapping("/demoa/trigger")
@Controller
public class demoa_handler implements Handler {
    @Override
    public void handle(Context context) throws Throwable {
        context.output(context.path());
    }
}
