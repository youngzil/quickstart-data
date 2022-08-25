package org.quickstart.ceph.swift;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.HttpClient;
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
import java.io.InputStream;
import java.util.Collection;

public class SwiftCephTest {

    public static final String username = "XXXX";
    public static final String password = "XXX";
    //域名和S3一样，比S3多了/auth/1.0
    public static final String authUrl = "http://ceph.test.info/auth/1.0";

    @Test
    public void testSimple() {

        AccountConfig config = new AccountConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setAuthUrl(authUrl);
        config.setAuthenticationMethod(AuthenticationMethod.BASIC);
        Account account = new AccountFactory(config).createAccount();

        // listBuckets
        Collection<Container> containers = account.list();
        containers.stream().map(k -> k.getName()).forEach(System.out::println);

        String bucketName = "my-new-container";
        Container container = account.getContainer(bucketName);
        container.create();

        // StoredObject object = container.getObject("foo.txt");
        // object.uploadObject(new File("foo.txt"));


        /*if (container.exists()) {
            container.delete();
        }*/

        String fileName = "test.txt";
        StoredObject object = account.getContainer(bucketName).getObject(fileName);
        boolean exists = object.exists();

        String filename = "foo.txt";
        StoredObject ob = account.getContainer("test").getObject(filename);
        ob.uploadObject("xxx".getBytes());
        ob.setDeleteAfter(500);
        String ss = new String(ob.downloadObject());
        System.out.println("ss:" + ss);

        object = account.getContainer("test").getObject(filename);
        int expireSeconds = 2000;
        String contentType = "txt";
        byte[] fileSource = "test".getBytes();

        object.setDeleteAfter(expireSeconds);
        object.setContentType(contentType);

        // 上传三个方法：byte[] 、File、InputStream
        object.uploadObject(fileSource);
        // object.uploadObject(new File("/uetst/test"));
        /*object.uploadObject(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        });*/

        // 下载三个方法：byte[] 、File、InputStream
        InputStream is = object.downloadObjectAsInputStream();
        byte[] downObject = object.downloadObject();

        File targetFile = null;
        object.downloadObject(targetFile);
        String contentType2 = object.getContentType();
        


        /*long count = 0;
        long bytes = 0;
        int i = 0;
        Collection<Container> containers = account.list("kd-", null, 9999);
        for (Container container : containers) {
            count += container.getCount();
            bytes += container.getBytesUsed();
            i++;
        }
        System.out.println(i);
        System.out.println(count);//个数
        System.out.println(bytes/1024*1024);*/

        /*	Collection<Container> containers =  account.list("kd", null, 9999);
		int i = 0;
		for (Container container : containers) {
			for (StoredObject storedObject : container.list("AAABU--6", null, 10)) {
				System.out.println(storedObject.getName());

			}
			System.out.println(i);
		}*/


        /*	for (Container container : account.list()) {
			System.out.println(container.getName());
		}*/
        //		Container container = account.getContainer(BUCKET_NAEM);
		/*for (StoredObject storedObject : container.list()) {
			System.out.println(storedObject.getName());
		}*/
		/*new ContainerPaginationMap(container, null, 10).listAllItems().forEach(storedObject -> {
			System.out.println(storedObject.getName());
		});*/
	/*	System.out.println("=======================");
		for (StoredObject storedObject : container.list(null, null, 10)) {
			System.out.println(storedObject.getName());
		}
		System.out.println("=======================");
		for (StoredObject storedObject : container.list(null, "/aa/bb/Desert.jpg", 10)) {
			System.out.println(storedObject.getName());
		}*/
	/*	System.out.println(container.list());
		System.out.println(container.list().size());
		System.out.println(container.getCount());
		System.out.println(container.list());
		System.out.println(container.listDirectory());*/

    }

    @Test
    public void testSimple2() {
        final Integer timeout = 20000;

        AccountConfig config = new AccountConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setAuthUrl(authUrl);
        config.setAuthenticationMethod(AuthenticationMethod.BASIC);

        AccountFactory accountFactory = new AccountFactory(config);
        accountFactory.setSocketTimeout(timeout);
        accountFactory.setHttpClient(initHttpClient(timeout));
        Account account = accountFactory.createAccount();
        System.out.println(account);

    }

    private static HttpClient initHttpClient(int socketTimeout) {
        HttpClientFactory httpClientFactory = new HttpClientFactory();
        if (socketTimeout != -1) {
            httpClientFactory.setSocketTimeout(socketTimeout);
            httpClientFactory.setConnectTimeout(3000);
            httpClientFactory.setDefaultMaxPerRoute(80); //默认超时时间30s,
            httpClientFactory.setMaxTotal(160);
        }

        HttpClient client = httpClientFactory.getHttpClient();
        return client;
    }

    @Slf4j
    public static class HttpClientFactory {
        private int maxTotal = 200;
        private int defaultMaxPerRoute = 20;
        private int socketTimeout = 10000;
        private int connectTimeout = 10000;
        private int connectionRequestTimeout = 10000;
        private int maxPerRoute = 100;
        private ConnectionKeepAliveStrategy connectionKeepAliveStrategy = new DefaultConnectionKeepAliveStrategy();
        private HttpRequestRetryHandler httpRequestRetryHandler = new DefaultHttpRequestRetryHandler(2, false);

        public CloseableHttpClient getHttpClient() {
            return getHttpClient(null, -1);
        }

        public CloseableHttpClient getHttpClient(String hostName) {
            return getHttpClient(hostName, -1);
        }

        public CloseableHttpClient getHttpClient(String hostName, int port) {

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

            CloseableHttpClient httpClient =
                HttpClients.custom().setDefaultRequestConfig(requestConfig).setKeepAliveStrategy(connectionKeepAliveStrategy).setConnectionManager(cm)
                    .setServiceUnavailableRetryStrategy(new CustomServiceUnavailableRetryStrategy()).setRetryHandler(httpRequestRetryHandler).build();

            return httpClient;
        }

        public int getMaxTotal() {
            return maxTotal;
        }

        /**
         * 设置最大 http 长连接数
         *
         * @param maxTotal
         */
        public void setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
        }

        public int getDefaultMaxPerRoute() {
            return defaultMaxPerRoute;
        }

        /**
         * 设置每个路由的最大基础连接数
         *
         * @param defaultMaxPerRoute
         */
        public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
            this.defaultMaxPerRoute = defaultMaxPerRoute;
        }

        public int getSocketTimeout() {
            return socketTimeout;
        }

        public void setSocketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public int getConnectionRequestTimeout() {
            return connectionRequestTimeout;
        }

        public void setConnectionRequestTimeout(int connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
        }

        public int getMaxPerRoute() {
            return maxPerRoute;
        }

        /**
         * 设置目标主机的最大连接数
         *
         * @param maxPerRoute
         */
        public void setMaxPerRoute(int maxPerRoute) {
            this.maxPerRoute = maxPerRoute;
        }

        public ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
            return connectionKeepAliveStrategy;
        }

        /**
         * 设置保持keepAlive连接策略，默认等于服务端的keep-alive time
         *
         * @param connectionKeepAliveStrategy
         */
        public void setConnectionKeepAliveStrategy(ConnectionKeepAliveStrategy connectionKeepAliveStrategy) {
            this.connectionKeepAliveStrategy = connectionKeepAliveStrategy;
        }

        @Contract(threading = ThreadingBehavior.IMMUTABLE)
        private class CustomServiceUnavailableRetryStrategy implements ServiceUnavailableRetryStrategy {

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

}
