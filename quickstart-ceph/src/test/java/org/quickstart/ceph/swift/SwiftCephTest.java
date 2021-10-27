package org.quickstart.ceph.swift;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.junit.Test;

import java.io.File;

public class SwiftCephTest {

    @Test
    public void testSimple() {

        String username = "G9VHIWJGK07HB7WULKFC";
        String password = "1mn3mo3EboZZ25kh1iXj1ZjCqk8eBPfCCOT8vPsn";
        String authUrl = "http://api.ceph.rgw.cache.test.wacai.info";

        AccountConfig config = new AccountConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setAuthUrl(authUrl);
        config.setAuthenticationMethod(AuthenticationMethod.BASIC);
        Account account = new AccountFactory(config).createAccount();


        Container container = account.getContainer("my-new-container");
        container.create();

        StoredObject object = container.getObject("foo.txt");
        object.uploadObject(new File("foo.txt"));


    }

    @Test
    public void testSimple2() {

        String username = "G9VHIWJGK07HB7WULKFC";
        String password = "1mn3mo3EboZZ25kh1iXj1ZjCqk8eBPfCCOT8vPsn";
        String authUrl = "http://api.ceph.rgw.cache.test.wacai.info";
        final Integer timeout = 20000;

        AccountConfig config = new AccountConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setAuthUrl(authUrl);
        config.setAuthenticationMethod(AuthenticationMethod.BASIC);

        AccountFactory accountFactory = new AccountFactory(config);
        accountFactory.setSocketTimeout(timeout);
        accountFactory.setHttpClient(getHttpClient(null, -1));
        Account account = accountFactory.createAccount();

        System.out.println(account);

    }

    public CloseableHttpClient getHttpClient(String hostName, int port) {

        int maxTotal = 200;
        int defaultMaxPerRoute = 20;
        int socketTimeout = 10000;
        int connectTimeout = 10000;
        int connectionRequestTimeout = 10000;
        int maxPerRoute = 100;
        ConnectionKeepAliveStrategy connectionKeepAliveStrategy = new DefaultConnectionKeepAliveStrategy();
        HttpRequestRetryHandler httpRequestRetryHandler = new DefaultHttpRequestRetryHandler(2, false);

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        //设置最大 http 长连接数
        cm.setMaxTotal(maxTotal);
        //设置每个路由的最大基础连接数
        cm.setDefaultMaxPerRoute(defaultMaxPerRoute);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)    // 设置数据等待时间
            .setConnectTimeout(connectTimeout)   // 设置连接超时时间
            .setConnectionRequestTimeout(connectionRequestTimeout) //设置获取连接池中长连接的等待时间
            .build();

        if (hostName != null) {
            //设置目标主机的最大连接数,连接主机系统路由
            HttpHost requestHost = new HttpHost(hostName, port);
            cm.setMaxPerRoute(new HttpRoute(requestHost), maxPerRoute);
        }

        CloseableHttpClient httpClient = HttpClients.custom()//
            .setDefaultRequestConfig(requestConfig)//
            .setKeepAliveStrategy(connectionKeepAliveStrategy)//
            .setConnectionManager(cm)//
            .setServiceUnavailableRetryStrategy(new CustomServiceUnavailableRetryStrategy())//
            .setRetryHandler(httpRequestRetryHandler)//
            .build();

        return httpClient;
    }

    @Contract(threading = ThreadingBehavior.IMMUTABLE)
    @Slf4j
    private static class CustomServiceUnavailableRetryStrategy implements ServiceUnavailableRetryStrategy {

        private final int maxRetries;

        private final long retryInterval;

        public CustomServiceUnavailableRetryStrategy(final int maxRetries, final int retryInterval) {
            super();
            Args.positive(maxRetries, "Max retries");
            Args.positive(retryInterval, "Retry interval");
            this.maxRetries = maxRetries;
            this.retryInterval = retryInterval;
        }

        public CustomServiceUnavailableRetryStrategy() {
            this(2, 600);
        }

        @Override
        public boolean retryRequest(final HttpResponse response, final int executionCount, final HttpContext context) {
            if (executionCount <= maxRetries && (response.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_GATEWAY
                || response.getStatusLine().getStatusCode() == HttpStatus.SC_SERVICE_UNAVAILABLE)) {
                log.warn("retry status code:{} executionCount:{} context:{}", response.getStatusLine().getStatusCode(), executionCount, context);
                return true;
            }
            return false;
        }

        @Override
        public long getRetryInterval() {
            return retryInterval;
        }
    }

}
