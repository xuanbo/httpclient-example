package example;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * HttpClient的简单使用
 *
 * Created by Administrator on 2016/12/15.
 */
public class HttpClientTest {

    private static final Logger log = Logger.getLogger(HttpClientTest.class);

    @Test
    public void get() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = "http://localhost:8080/test/get";
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("username", "张三"));
        String params = EntityUtils.toString(new UrlEncodedFormEntity(nvps, Charset.defaultCharset()));
        HttpGet httpGet = new HttpGet(url + "?" + params);
        CloseableHttpResponse response = null;
        // The underlying HTTP connection is still held by the response object
        // to allow the response content to be streamed directly from the network socket.
        // In order to ensure correct deallocation of system resources
        // the user MUST call CloseableHttpResponse#close() from a finally clause.
        // Please note that if response content is not fully consumed the underlying
        // connection cannot be safely re-used and will be shut down and discarded
        // by the connection manager.
        try {
            response = httpclient.execute(httpGet);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            String content = EntityUtils.toString(entity, Charset.defaultCharset());
            log.debug(content);
            // and ensure it is fully consumed
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // call CloseableHttpResponse#close() from a finally clause.
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * post 表单提交
     */
    @Test
    public void postForm() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/test/postForm");
        CloseableHttpResponse response = null;
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("username", "张三"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Charset.defaultCharset()));
            response = httpclient.execute(httpPost);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            String content = EntityUtils.toString(entity, Charset.defaultCharset());
            log.debug(content);
            // and ensure it is fully consumed
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * post JSON
     */
    @Test
    public void postJson() {
        String json = "{\"username\": \"张三\", \"password\": \"123456\"}";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/test/postJson");
        CloseableHttpResponse response = null;
        try {
            // 设置json头
            httpPost.setHeader("Content-Type", "application/json");
            // Entity放入json字符串
            httpPost.setEntity(new StringEntity(json, Charset.defaultCharset()));
            response = httpclient.execute(httpPost);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            String content = EntityUtils.toString(entity, Charset.defaultCharset());
            log.debug(content);
            // and ensure it is fully consumed
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void getCookie() {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
//        HttpGet httpGet = new HttpGet("http://www.baidu.com");
//        try (CloseableHttpResponse httpResponse = client.execute(httpGet)) {
//            StatusLine statusLine = httpResponse.getStatusLine();
//            log.info(statusLine);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        List<Cookie> cookies = cookieStore.getCookies();
//        for (Cookie cookie : cookies) {
//            log.info(cookie.getName() + " -> " + cookie.getValue());
//        }

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("userName", "xxx"));
        nvps.add(new BasicNameValuePair("password", "xxx"));
        nvps.add(new BasicNameValuePair("type", "xs"));
        HttpPost httpPost = new HttpPost("http://sso.jwc.whut.edu.cn/Certification/login.do");
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Charset.defaultCharset()));
        try (CloseableHttpResponse httpResponse = client.execute(httpPost)) {
            for (Cookie cookie : cookieStore.getCookies()) {
            log.info(cookie.getName() + " -> " + cookie.getValue());
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
