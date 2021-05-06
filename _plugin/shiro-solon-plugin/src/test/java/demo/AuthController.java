package demo;

import org.apache.shiro.authz.annotation.*;

/**
 * @author noear 2021/5/6 created
 */
public class AuthController {
    @RequiresAuthentication
    public void testAuthentication(){
        //验证用户是否登录，等同于方法subject.isAuthenticated() 结果为true时。
    }
    @RequiresGuest
    public void testGuest(){
        /**
         * 验证是否是一个guest的请求，与@RequiresUser完全相反。
         * 换言之，RequiresUser  == !RequiresGuest。
         * 此时subject.getPrincipal() 结果为null.
         * */
    }
    @RequiresUser
    public void testUser(){
        /**
         * 验证用户是否被记忆，user有两种含义：
         * 一种是成功登录的（subject.isAuthenticated() 结果为true）；
         * 另外一种是被记忆的（subject.isRemembered()结果为true）。
         * */
    }
    @RequiresRoles("admin")
    public void testRoles(){
        //如果subject中有aRoleName角色才可以访问方法someMethod。如果没有这个权限则会抛出异常AuthorizationException。
    }
    @RequiresPermissions({"file:read", "write:aFile.txt", "xx:xx:xx"} )
    public void xxx(){
        //要求subject中必须同时含有file:read和write:aFile.txt的权限才能执行方法someMethod()。否则抛出异常AuthorizationException。
    }
}
