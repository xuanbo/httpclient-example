package example;

import example.support.JSONCreator;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/16.
 */
public class JSONCreatorTest {

    private static final Logger log = Logger.getLogger(URLCreatorTest.class);

    @Test
    public void create() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", "张三");
        map.put("age", 10);
        log.debug(JSONCreator.create(map));

        String json = "{\"age\":10,\"username\":\"张三\"}";
        Map map1 = JSONCreator.create(json, Map.class);
        log.debug(map1.get("username"));
        log.debug(map1.get("age"));
    }
}
