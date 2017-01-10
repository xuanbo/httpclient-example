package example;

import example.support.URLCreator;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created by Administrator on 2016/12/16.
 */
public class URLCreatorTest {

    private static final Logger log = Logger.getLogger(URLCreatorTest.class);

    @Test
    public void create() {
        URLCreator creator = new URLCreator();
        String url = creator.schema("http")
                .host("localhost")
                .port(8080)
                .absolutePath("/test/get")
                .addParam("username", "张三")
                .addParam("age", 10)
                .builder();
        log.debug(url);
    }
}
