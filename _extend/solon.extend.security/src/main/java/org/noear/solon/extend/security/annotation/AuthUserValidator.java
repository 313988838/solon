package org.noear.solon.extend.security.annotation;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.extend.security.SecurityServiceProxy;
import org.noear.solon.extend.validation.Validator;

/**
 * @author noear
 * @since 1.3
 */
public class AuthUserValidator implements Validator<AuthUser> {
    public static final AuthUserValidator instance = new AuthUserValidator();


    @Override
    public String message(AuthUser anno) {
        return anno.message();
    }

    @Override
    public Result validate(Context ctx, AuthUser anno, String name, StringBuilder tmp) {
        if (SecurityServiceProxy.getInstance().verifyLogined()) {
            return Result.succeed();
        } else {
            return Result.failure();
        }
    }
}
