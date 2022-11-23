package com.lowellzhao.lnovel.common.util;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * HttpUtil
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@Slf4j
public class HttpUtil {

    /**
     * user-agent 集合
     */
    private static final List<String> userAgentList = Arrays.asList(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.67 Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 14_7_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.18(0x1800123f) NetType/4G Language/zh_CN",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.30(0x18001e29) NetType/WIFI Language/zh_CN",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.30(0x18001e2e) NetType/4G Language/zh_CN",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 16_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.28(0x18001c2e) NetType/4G Language/zh_CN",
            "Mozilla/5.0 (Linux; Android 11; MEIZU 18 Build/RKQ1.210715.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/86.0.4240.99 XWEB/4343 MMWEBSDK/20220903 Mobile Safari/537.36 MMWEBID/9400 MicroMessenger/8.0.28.2240(0x28001C57) WeChat/arm64 Weixin NetType/WIFI Language/zh_CN ABI/arm64 MiniProgramEnv/android",
            "Mozilla/5.0 (Linux; Android 10; NEO-AL00 Build/HUAWEINEO-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/86.0.4240.99 XWEB/4343 MMWEBSDK/20221011 Mobile Safari/537.36 MMWEBID/10000 MicroMessenger/8.0.30.2260(0x28001E51) WeChat/arm64 Weixin NetType/4G Language/zh_CN ABI/arm64 MiniProgramEnv/android",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 Edg/107.0.1418.56"
    );

    /**
     * 多线程共享实例
     */
    private static CloseableHttpClient httpClient;

    static {
        SSLContext sslContext = createSSLContext();
        SSLConnectionSocketFactory sslConnectionSocketFactory =
                new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        // 注册http套接字工厂和https套接字工厂
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslConnectionSocketFactory)
                .build();
        // 连接池管理器
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 连接池最大连接数
        connectionManager.setMaxTotal(300);
        // 每个路由最大连接数，设置的过小，无法支持大并发
        connectionManager.setDefaultMaxPerRoute(300);
        // 在从连接池获取连接时，连接不活跃多长时间后需要进行一次验证
        connectionManager.setValidateAfterInactivity(5 * 1000);
        // 请求参数配置管理器
        RequestConfig requestConfig = RequestConfig.custom()
                // 从连接池获取连接超时
                .setConnectionRequestTimeout(10000)
                // 连接超时
                .setConnectTimeout(10000)
                // 请求超时
                .setSocketTimeout(60000)
                .build();
        // 获取httpClient客户端
        httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * 配置证书
     *
     * @return SSLContext
     */
    private static SSLContext createSSLContext() {
        try {
            // 信任所有,支持导入ssl证书
            TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
            return SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        } catch (Exception e) {
            log.error("初始化ssl配置失败", e);
            throw new RuntimeException("初始化ssl配置失败");
        }
    }

    /**
     * 关闭流
     *
     * @param response response
     */
    private static void doResponseClose(CloseableHttpResponse response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                log.error("流关闭失败", e);
            }
        }
    }

    /**
     * get请求
     *
     * @param url url
     * @return get结果
     */
    public static String get(String url) {
        return get(url, "UTF-8");
    }

    public static String get(String url, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        // 默认utf-8
        if (StringUtils.isBlank(charset)) {
            charset = "UTF-8";
        }
        String res = null;
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("user-agent", getUserAgent());
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity, charset);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.OK.value()) {
                return res;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("get error", e);
        } finally {
            doResponseClose(response);
        }
        return res;
    }

    /**
     * 获取随机一个userAgent
     *
     * @return userAgent
     */
    private static String getUserAgent() {
        int randomInt = RandomUtil.randomInt(userAgentList.size() - 1);
        return userAgentList.get(randomInt);
    }

}
