package com.lowellzhao.lnovel.common.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;

/**
 * HtmlUtil
 *
 * @author lowellzhao
 * @since 2022-05-28
 */
@Slf4j
public class HtmlUtil {

    public static String getHtml(String url) {
        // 创建浏览器
        try (WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
            // 关闭css渲染
            webClient.getOptions().setCssEnabled(false);
            // 启动js
            webClient.getOptions().setJavaScriptEnabled(true);
            // 启动重定向
            webClient.getOptions().setRedirectEnabled(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            //js运行时错误，是否抛出异常
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            // 等待js渲染执行 waitime等待时间(ms)
            webClient.waitForBackgroundJavaScript(60000);
            // 启动ajax代理
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            HtmlPage page = webClient.getPage(url);
            return page.asXml();
        } catch (Exception e) {
            log.error("HtmlUtil getHtml error");
        }
        return null;
    }

}
