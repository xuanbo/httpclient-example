package example.support;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 构建URL
 *
 * Created by Administrator on 2016/12/16.
 */
public class URLCreator {

    private static final Logger log = Logger.getLogger(URLCreator.class);

    private static final String QUESTION = "?";
    private static final String AND = "&";
    private static final String LEFT_SLASH = "/";
    private static final String COLONS = ":";
    private static final String EQUAL = "=";
    private static final String DEFAULT_SCHEMA = "http";
    private static final int DEFAULT_PORT = 80;

    // 这四个属性必须填写
    private String schema = DEFAULT_SCHEMA;
    private String host;
    private int port = DEFAULT_PORT;
    private String absolutePath;

    private StringBuilder url = new StringBuilder();
    private Map<String, Object> params;
    private Charset charset = Charset.forName("UTF-8");

    public URLCreator schema(String schema) {
        this.schema = schema;
        return this;
    }

    public URLCreator host(String host) {
        this.host = host;
        return this;
    }

    public URLCreator port(int port) {
        this.port = port;
        return this;
    }

    /**
     * 添加资源的绝对路径，以/开始
     * 例如构建http://localhost:8080/index?xxx=xxx..., absolutePath 就相当于 /index
     *
     * @param absolutePath 资源的绝对路径
     * @return this
     */
    public URLCreator absolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
        return this;
    }

    /**
     * 添加参数
     *
     * @param key key
     * @param value value
     * @return this
     */
    public URLCreator addParam(String key, Object value) {
        if (this.params == null) {
            this.params = new HashMap<>();
        }
        if (key != null && !key.isEmpty()) {
            this.params.put(key, value);
        }
        return this;
    }

    /**
     * 添加参数
     *
     * @param params Map<String, Object>
     * @return this
     */
    public URLCreator addParam(Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            this.params.putAll(params);
        }
        return this;
    }

    /**
     * 使用默认编码构建URI
     *
     * @return URI
     */
    public String builder() {
        check();
        url.append(schema).append(COLONS).append(LEFT_SLASH).append(LEFT_SLASH).append(host).append(COLONS)
                .append(port).append(absolutePath);
        if (params == null || params.isEmpty()) {
            return url.toString();
        }
        url.append(QUESTION);
        int index = 0;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (index != 0) {
                url.append(AND);
            }
            url.append(entry.getKey()).append(EQUAL).append(urlEncoder(entry.getValue().toString()));
            index++;
        }
        String createdURI = url.toString();
        log.debug("构建的URI: " + createdURI);
        return createdURI;
    }

    /**
     * 采用给定的编码对URI进行构建
     *
     * @param charset 给定的URI
     * @return URI
     */
    public String builder(Charset charset) {
        if (charset.isRegistered()) {
            this.charset = charset;
        }
        return builder();
    }

    /**
     * 对URI中的value进行编码
     * 例如XX?username=张三 => XX?username=%D7%E3%BF
     *
     * @param value 例如 张三
     * @return 编码后的值
     */
    private String urlEncoder(String value) {
        String encodeValue = value;
        try {
            encodeValue = URLEncoder.encode(value, charset.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeValue;
    }

    /**
     * 检查四个属性是否赋值
     */
    private void check() {
        IS_NULL(schema);
        IS_NULL(host);
        IS_NULL(absolutePath);
        IS_START(absolutePath, LEFT_SLASH);
    }

    private void IS_NULL(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("[" + value + "]:不能为空");
        }
    }

    private void IS_START(String value, String start) {
        IS_NULL(value);
        if (!value.startsWith(start)) {
            throw new IllegalArgumentException("[" + value + "]:必须以" + "[" + value + "]开始");
        }
    }

}
