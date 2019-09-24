package org.kin.conf.client.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.util.EntityUtils;
import org.kin.framework.exception.HttpException;
import org.kin.framework.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by huangjianqin on 2019/6/18.
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
    private static final Charset UTF8 = Charset.forName("utf-8");
    private static final int TIMEOUT = 15000;

    //http
    private static final SocketConfig DEFAULT_HTTP_SOCKET_CONFIG = SocketConfig.custom().setTcpNoDelay(true)
            .setSoTimeout(TIMEOUT).build();
    private static final RequestConfig DEFAULT_HTTP_REQUEST_CONFIG = RequestConfig.custom().setConnectTimeout(TIMEOUT)
            .setConnectionRequestTimeout(TIMEOUT).setSocketTimeout(TIMEOUT).setMaxRedirects(3).build();
    private static final PoolingHttpClientConnectionManager HTTP_CONNECTION_MANAGER;
    private static final ResponseHandler<HttpResponseWrapper> DEFAULT_HTTP_RESPONSE_HANDLER = (httpResponse) -> {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
        HttpEntity httpEntity = httpResponse.getEntity();
        if (200 <= statusCode && statusCode < 300) {
            if (httpEntity != null) {
                return new HttpResponseWrapper(statusCode, EntityUtils.toString(httpEntity, UTF8), reasonPhrase);
            } else {
                return new HttpResponseWrapper(statusCode);
            }
        } else {
            EntityUtils.consume(httpEntity);
            throw new HttpException(statusCode, reasonPhrase, httpResponse.toString());
        }
    };

    static {
        PoolingHttpClientConnectionManager httpConnectionManager = new PoolingHttpClientConnectionManager();
        httpConnectionManager.setMaxTotal(200);
        httpConnectionManager.setDefaultMaxPerRoute(20);
        HTTP_CONNECTION_MANAGER = httpConnectionManager;


        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor();
        } catch (IOReactorException e) {
            log.error(e.getMessage(), e);
        }
        PoolingNHttpClientConnectionManager nHttpConnectionManager = new PoolingNHttpClientConnectionManager(ioReactor);
        nHttpConnectionManager.setMaxTotal(100);
        nHttpConnectionManager.setDefaultMaxPerRoute(50);
    }

    public static class HttpResponseWrapper {
        private int statusCode;
        private String content;
        private String reasonPhrase;

        public HttpResponseWrapper(int statusCode, String content, String reasonPhrase) {
            this.statusCode = statusCode;
            this.content = content;
            this.reasonPhrase = reasonPhrase;
        }

        public HttpResponseWrapper(int statusCode) {
            this(statusCode, "", "");
        }

        public JSONObject json() {
            return JSON.parseObject(content);
        }

        public <T> T obj(Type type) {
            return JSON.parseObject(content, type);
        }

        public <T> T obj(Class<T> claxx) {
            return JSON.parseObject(content, claxx);
        }

        //getter
        public int getStatusCode() {
            return statusCode;
        }

        public String getContent() {
            return content;
        }

        public String getReasonPhrase() {
            return reasonPhrase;
        }

        @Override
        public String toString() {
            return "HttpResponseWrapper{" +
                    "statusCode=" + statusCode +
                    ", content='" + content + '\'' +
                    ", reasonPhrase='" + reasonPhrase + '\'' +
                    '}';
        }
    }

    //-----------------------------------------------------------------------------------------------------
    public static String toQueryStr(Map<String, Object> params) {
        if (CollectionUtils.isNonEmpty(params)) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                Object value = entry.getValue();
                Object[] values;
                if (value.getClass().isArray()) {
                    values = (Object[]) value;
                } else {
                    values = new Object[]{value};
                }

                for (Object item : values) {
                    sb.append(entry.getKey() + "=" + item.toString() + "&");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }

        return "";
    }

    //------------------------------------------http请求------------------------------------------------------
    private static void setHttpGetParams(HttpUriRequest request, String url, Map<String, Object> params) {
        HttpGet httpGet = (HttpGet) request;
        String paramStr = toQueryStr(params);
        if (url.indexOf("?") > 0) {
            url = url + "&" + paramStr;
        } else {
            url = url + "?" + paramStr;
        }
        httpGet.setURI(URI.create(url));
    }

    private static HttpUriRequest setJsonParams(HttpUriRequest request, String url, Map<String, Object> params) {
        if (CollectionUtils.isNonEmpty(params)) {
            if (request instanceof HttpEntityEnclosingRequestBase) {
                HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = (HttpEntityEnclosingRequestBase) request;
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
                StringEntity entity = new StringEntity(jsonObject.toJSONString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpEntityEnclosingRequestBase.setEntity(entity);
            } else if (request instanceof HttpGet) {
                setHttpGetParams(request, url, params);
            }

        }
        return request;
    }

    public static CloseableHttpClient httpClient() {
        return HttpClientBuilder.create()
                .setConnectionManager(HTTP_CONNECTION_MANAGER)
                .setDefaultSocketConfig(DEFAULT_HTTP_SOCKET_CONFIG)
                .setDefaultRequestConfig(DEFAULT_HTTP_REQUEST_CONFIG)
                .build();
    }

    public static HttpResponseWrapper post(String url, Map<String, Object> params) {
        log.debug("http post '{}', params: '{}'", url, params);
        HttpPost httpPost = new HttpPost(url);
        setJsonParams(httpPost, url, params);

        try {
            return httpClient().execute(httpPost, DEFAULT_HTTP_RESPONSE_HANDLER);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}
