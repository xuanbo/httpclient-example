package example;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2016/12/16.
 */
public class HttpClientFluentTest {

    private static final Logger log = Logger.getLogger(HttpClientFluentTest.class);

    @Test
    public void sampler_get() throws IOException, URISyntaxException {
        // URIBuilder构建url和参数
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme("http")
                .setHost("localhost")
                .setPort(8080)
                .setPath("/test/get")
                .addParameter("username", "张三");

        // GET
        String content = Request.Get(uriBuilder.build()).connectTimeout(3000)
                .execute().returnContent().asString(Charset.defaultCharset());
        log.debug(content);
    }

    @Test
    public void sampler_post_form() throws IOException {
        // 构建Form表单
        Form form = Form.form()
                .add("username", "张三")
                .add("password", "李四");

        // POST
        String content = Request.Post("http://localhost:8080/test/postForm")
                .bodyForm(form.build(), Charset.defaultCharset())
                .connectTimeout(3000)
                .execute()
                .returnContent()
                .asString(Charset.defaultCharset());
        log.debug(content);
    }

    @Test
    public void sampler_post_json() throws IOException {
        String json = "{\"username\": \"张三\", \"password\": \"123456\"}";

        String content = Request.Post("http://localhost:8080/test/postJson")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .body(new StringEntity(json, Charset.defaultCharset()))
                .execute()
                .returnContent()
                .asString(Charset.defaultCharset());
        log.debug(content);
    }

    @Test
    public void sampler() throws IOException {
        String json = "{\"username\": \"张三\", \"password\": \"123456\"}";

        String content = Request.Post("http://localhost:8080/test/postJson")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .body(new StringEntity(json, Charset.defaultCharset()))
                .execute()
                .handleResponse(new ResponseHandler<String>() {
                    @Override
                    public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                        if (response.getStatusLine().getStatusCode() == 200) {
                            return EntityUtils.toString(response.getEntity(), Charset.defaultCharset());
                        }
                        return response.getStatusLine().getStatusCode() + "";
                    }
                });
        log.debug(content);
    }

}
