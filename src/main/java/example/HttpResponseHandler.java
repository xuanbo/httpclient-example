package example;

import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/16.
 */
@FunctionalInterface
public interface HttpResponseHandler<T> {

    /**
     * 自定义处理
     * NOTE:不要返回HttpEntity或者从中获取的流，返回处理后的结果即可
     *      因为，HttpOperatorTemplate中封装的模板会释放HttpEntity的流，并关闭HttpResponse
     *
     * @param response HttpResponse
     * @return 处理后的结果
     * @throws IOException I/O exception
     */
    T handler(HttpResponse response) throws IOException;

}
