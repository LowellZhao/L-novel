package com.lowellzhao.lnovel.common.util;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author lowellzhao
 * @since 2022/8/31
 */
public class VoiceUtilTest {

    @Test
    public void test01() {
        VoiceUtil.speakingText("hello world");
    }

    @Test
    public void test02() throws IOException {
        File file = new File("F:\\lwz\\panlong.txt");
        // 获取文本文档的内容
        FileReader fileReader = new FileReader(file);

        // 从缓存区拿到数据
        BufferedReader bf = new BufferedReader(fileReader);

        // 拿到缓冲区的数据
        String content = bf.readLine();
        boolean flag = false;
        int rate = 1;
        while (content != null) {
            System.out.println(content);
            if (content.contains("章 老三？")) {
                flag = true;
            }
            if (flag) {
                VoiceUtil.speakingText(content, 100, rate);
            }
            content = bf.readLine();
        }
    }

    @Test
    public void test03() {
        VoiceUtil.speakingText("hello world");
        VoiceUtil.speakingText("您好");
    }


    @Test
    public void test04() {
        System.out.println(1 & 1);
        System.out.println(1 & 2);
        System.out.println(1 | 1);
        System.out.println(1 | 2);
        System.out.println((1 | 2) | 2);
        System.out.println((1 | 2) | 4);
        System.out.println(((1 | 2) | 4) & 2);
    }

}
