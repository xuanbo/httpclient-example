package example.support;

import com.google.gson.Gson;

/**
 * 构建JSON字符串
 * 需要`gson`支持
 *
 * Created by Administrator on 2016/12/16.
 */
public class JSONCreator {

    /**
     * 将对象转为JSON字符串
     * @param t 对象
     * @param <T> 泛型
     * @return JSON字符串
     */
    public static <T> String create(T t) {
        if (t instanceof CharSequence || t instanceof Number) {
            return t.toString();
        }
        Gson gson = new Gson();
        return gson.toJson(t);
    }

    /**
     * 将JSON字符串转为对象
     *
     * @param jsonString JSON字符串
     * @param clazz 对象Class
     * @param <T> 泛型
     * @return 对象
     */
    public static <T> T create(String jsonString, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, clazz);
    }
}
