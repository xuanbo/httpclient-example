package example;

import example.support.JSONCreator;
import example.support.URLCreator;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/16.
 */
public class HttpOperatorTemplateTest {

    private static final Logger log = Logger.getLogger(URLCreatorTest.class);

    private HttpOperatorTemplate template;

    @Before
    public void init() {
        template = new HttpOperatorTemplate();
    }

    @Test
    public void get() {
        URLCreator creator = new URLCreator();
        String url = creator.schema("http").host("localhost").port(8080).absolutePath("/test/get")
                .addParam("username", "张三").addParam("age", 10)
                .builder();
        String content = template.get(url, response -> {return EntityUtils.toString(response.getEntity());});
        log.debug(content);
    }

    @Test
    public void postForm() {
        URLCreator creator = new URLCreator();
        String url = creator.schema("http").host("localhost").port(8080).absolutePath("/test/postForm").builder();
        Map<String, Object> map = new HashMap<>();
        map.put("username", "张三");
        map.put("age", 10);

        String content = template.postForm(url, map, response -> {return EntityUtils.toString(response.getEntity());});
        log.debug(content);
    }

    @Test
    public void postJson() {
        URLCreator creator = new URLCreator();
        String url = creator.schema("http").host("localhost").port(8080).absolutePath("/test/postJson").builder();
        Map<String, Object> map = new HashMap<>();
        map.put("username", "张三");
        map.put("age", 10);
        String json = JSONCreator.create(map);

        String content = template.postJson(url, json, new DefaultHttpResponseHandler());
        log.debug(content);
    }
}
