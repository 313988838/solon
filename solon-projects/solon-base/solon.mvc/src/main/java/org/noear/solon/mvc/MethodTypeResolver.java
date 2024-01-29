package org.noear.solon.mvc;

import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.MethodType;

import java.util.Set;
import java.util.function.Predicate;

/**
 * MethodType 分析器
 *
 * @author noear
 * @since 2.7
 */
public class MethodTypeResolver {

    public static Set<MethodType> findAndFill(Set<MethodType> list, Predicate<Class> checker) {
        if (checker.test(Get.class)) {
            list.add(MethodType.GET);
        }

        if (checker.test(Post.class)) {
            list.add(MethodType.POST);
        }

        if (checker.test(Put.class)) {
            list.add(MethodType.PUT);
        }

        if (checker.test(Patch.class)) {
            list.add(MethodType.PATCH);
        }

        if (checker.test(Delete.class)) {
            list.add(MethodType.DELETE);
        }

        if (checker.test(Head.class)) {
            list.add(MethodType.HEAD);
        }

        if (checker.test(Options.class)) {
            list.add(MethodType.OPTIONS);
        }

        if (checker.test(Http.class)) {
            list.add(MethodType.HTTP);
        }

        if (checker.test(Socket.class)) {
            list.add(MethodType.SOCKET);
        }

        return list;
    }
}
