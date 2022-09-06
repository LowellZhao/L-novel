package com.lowellzhao.lnovel.common.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import lombok.extern.slf4j.Slf4j;

/**
 * 语音播放工具类
 *
 * @author lowellzhao
 * @since 2022/8/31
 */
@Slf4j
public class VoiceUtil {

    public static void speakingText(String readText) {
        speakingText(readText, 100, 1);
    }

    public static void speakingText(String readText, int volume, int rate) {
        // 拿到音响
        ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
        try {
            // 调节语速 音量大小
            sap.setProperty("Volume", new Variant(volume));
            sap.setProperty("Rate", new Variant(rate));
            Dispatch xj = sap.getObject();
            // 执行朗读 没有读完就继续读
            Dispatch.call(xj, "Speak", new Variant(readText));
            xj.safeRelease();
        } catch (Exception e) {
            log.error("VoiceUtil speakingText error, readText:{}", readText, e);
        } finally {
            sap.safeRelease();
        }
    }

}
