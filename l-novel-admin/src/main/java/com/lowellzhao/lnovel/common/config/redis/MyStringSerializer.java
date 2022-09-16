package com.lowellzhao.lnovel.common.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;

import java.nio.charset.StandardCharsets;

/**
 * MyStringSerializer
 *
 * @author lowellzhao
 */
@Slf4j
public class MyStringSerializer extends StringRedisSerializer {

    @Override
    public byte[] serialize(@Nullable String string) {
        if (string == null) {
            return null;
        }
        string = "lnovel:" + string;
        if (log.isDebugEnabled()) {
            log.debug("redis 序列化结果:{}", string);
        }
        return string.getBytes(StandardCharsets.UTF_8);
    }

}
