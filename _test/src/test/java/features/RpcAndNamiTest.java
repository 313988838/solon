package features;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.nami.Nami;
import org.noear.nami.annotation.NamiClient;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;
import webapp.demo5_rpc.protocol.UserModel;
import webapp.demo5_rpc.protocol.UserService;
import webapp.demo5_rpc.protocol.UserService4;
import webapp.demo5_rpc.protocol.UserService5;

@RunWith(SolonJUnit4ClassRunner.class)
@SolonTest(webapp.TestApp.class)
public class RpcAndNamiTest {
    //直接指定服务端地址
    @NamiClient(url = "http://localhost:8080/demo5/user/")
    UserService userService;

    //使用负载
    @NamiClient(name = "local", path = "/demo5/user/")
    UserService userService2;

    @Test
    public void test() {
        UserModel user = userService.getUser(12);
        System.out.println(user);
        assert user.getId() == 12;
    }

    @Test
    public void test2() {
        UserModel user = userService2.getUser(23);
        System.out.println(user);
        assert user.getId() == 23;
    }

    @Test
    public void test3() {
        UserService userService3 = Nami.builder()
                .url("http://localhost:8080/demo5/user/")
                .create(UserService.class);

        UserModel user = userService3.getUser(23);
        System.out.println(user);
        assert user.getId() == 23;
    }

    @Test
    public void test3_2() {
        UserService userService3 = Nami.builder()
                .name("local")
                .path("/demo5/user/")
                .upstream(() -> "http://localhost:8080")
                .create(UserService.class);

        UserModel user = userService3.getUser(23);
        System.out.println(user);
        assert user.getId() == 23;
    }

    @Test
    public void test4() {
        UserService4 userService4 = Nami.builder()
                .url("http://localhost:8080/demo5/user/")
                .create(UserService4.class);

        UserModel user = userService4.xxx(23);
        System.out.println(user);
        assert user.getId() == 23;
    }

    @Test
    public void test5() {
        UserService5 userService5 = Nami.builder()
                .url("http://localhost:8080/demo5/")
                .create(UserService5.class);

        UserModel user = userService5.xxx(23);
        System.out.println(user);
        assert user.getId() == 23;
    }
}
