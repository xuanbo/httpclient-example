package example;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装HttpClient的操作流程
 *
 * Created by Administrator on 2016/12/16.
 */
public class HttpOperatorTemplate {

    private static final Logger log = Logger.getLogger(HttpOperatorTemplate.class);

    public <T> T get(String url, HttpResponseHandler<T> httpResponseHandler) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        T result = null;
        try {
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            result = httpResponseHandler.handler(response);
            // consume Entity Stream
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
        return result;
    }

    public <T> T postForm(String url, Map<String, Object> params, HttpResponseHandler<T> handler) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        T result = null;
        List<NameValuePair> nvps = null;
        if (params != null && !params.isEmpty()) {
            nvps = new ArrayList<>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
        }
        try {
            if (nvps != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, Charset.defaultCharset()));
            }
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // do somethings
            result = handler.handler(response);
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
        return result;
    }

    public <T> T postJson(String url, String json, HttpResponseHandler<T> handler) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        T result = null;
        try {
            // 设置json头
            httpPost.setHeader("Content-Type", "application/json");
            // Entity放入json字符串
            httpPost.setEntity(new StringEntity(json, Charset.defaultCharset()));
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            result = handler.handler(response);
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
        return result;
    }

}
