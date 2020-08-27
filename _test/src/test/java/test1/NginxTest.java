package test1;

import org.junit.Test;

public class NginxTest {
    @Test
    public void test1() throws Exception {
        new Thread(() -> {
            test1Do();
        }).start();

        System.in.read();
    }

    private void test1Do() {
        String url = "http://139.196.28.6:1013/getAppByID?appID=10970";

        while (true) {
            try {
                HttpUtils.http(url).get();
                System.out.println(System.currentTimeMillis());
            } catch (Throwable ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
}
