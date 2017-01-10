package example;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * HttpResponseHandler的一个默认实现
 * 如果状态码为 200 则返回HttpResponse中的内容；否则返回异常的状态码
 *
 * Created by Administrator on 2016/12/16.
 */
public class DefaultHttpResponseHandler implements HttpResponseHandler<String> {

    @Override
    public String handler(HttpResponse response) throws IOException {
        String result = "";
        int code = response.getStatusLine().getStatusCode();
        if (code == HttpStatus.SC_OK) {
           result = EntityUtils.toString(response.getEntity());
        } else {
            result = code + "";
        }
        return result;
    }
}
