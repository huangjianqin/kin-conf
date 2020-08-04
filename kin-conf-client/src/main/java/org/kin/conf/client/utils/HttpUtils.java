package org.kin.conf.client.utils;

import okhttp3.*;
import org.kin.framework.utils.ExceptionUtils;
import org.kin.framework.utils.JSON;
import org.kin.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author huangjianqin
 * @date 2019/6/18
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
    /**
     * 与原实例共享线程池、连接池和其他设置项，只需进行少量配置就可以实现特殊需求
     * .newBuilder()
     * <p>
     * ---
     * 最好只使用一个共享的OkHttpClient实例，将所有的网络请求都通过这个实例处理。因为每个OkHttpClient 实例都有自己的连接池和线程池，重用这个实例能降低延时，减少内存消耗，而重复创建新实例则会浪费资源。
     * OkHttpClient的线程池和连接池在空闲的时候会自动释放，所以一般情况下不需要手动关闭，但是如果出现极端内存不足的情况，可以使用以下代码释放内存：
     */
    private static final OkHttpClient CLIENT =
            new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .callTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(new LoggingInterceptor())
                    .build();
    private static final MediaType MEDIATYPE_JSON = MediaType.get("application/json; charset=utf-8");

    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            log.debug("Sending request {} on {}-{}", request.url(), chain.connection(), request.headers());

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            log.debug("Received response for {} in {}ms-{}", response.request().url(), (t2 - t1) / 1e6d, response.headers());

            return response;
        }
    }

    private static String converterMap2JsonStr(Map<String, Object> params) {
        String json = JSON.write(params);
        return StringUtils.isNotBlank(json) ? json : "{}";
    }

    public static String post(String url, Map<String, Object> params) {
        RequestBody body = RequestBody.create(MEDIATYPE_JSON, converterMap2JsonStr(params));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            ExceptionUtils.log(e);
        }

        return null;
    }
}
