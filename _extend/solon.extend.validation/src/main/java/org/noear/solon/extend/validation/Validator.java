package org.noear.solon.extend.validation;

import org.noear.solon.core.handler.Context;
import org.noear.solon.core.handler.Result;

import java.lang.annotation.Annotation;

/**
 *
 * @author noear
 * @since 1.0
 * */
@FunctionalInterface
public interface Validator<T extends Annotation> {
    default String message(T anno) {
        return "";
    }

    Result validate(Context ctx, T anno, String name, StringBuilder tmp);
}
