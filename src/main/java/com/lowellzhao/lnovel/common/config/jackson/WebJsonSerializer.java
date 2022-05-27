package com.lowellzhao.lnovel.common.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * WebJsonSerializer
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
public class WebJsonSerializer extends JsonSerializer<Object> {

    private static final ConcurrentMap<JavaType, WebJsonSerializer> SERIALIZER_MAP = new ConcurrentHashMap<>();
    private JavaType clazz;

    private WebJsonSerializer(JavaType clazz) {
        this.clazz = clazz;
    }

    static WebJsonSerializer getSingleton(JavaType type) {
        WebJsonSerializer ob = SERIALIZER_MAP.get(type);
        if (ob == null) {
            synchronized (SERIALIZER_MAP) {
                ob = new WebJsonSerializer(type);
                SERIALIZER_MAP.put(type, ob);
            }
        }
        return ob;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (clazz.isTypeOrSubTypeOf(String.class)
                || clazz.isTypeOrSubTypeOf(Date.class)
                || clazz.isTypeOrSubTypeOf(LocalDateTime.class)
                || clazz.isTypeOrSubTypeOf(LocalDate.class)
                || clazz.isTypeOrSubTypeOf(LocalTime.class)) {
            // String和时间处理为""
            gen.writeString("");
        } else if (clazz.isArrayType()
                || clazz.isTypeOrSubTypeOf(Collection.class)
                || clazz.isTypeOrSubTypeOf(List.class)
                || clazz.isTypeOrSubTypeOf(Set.class)) {
            // 数组，list，set 处理为[]
            gen.writeStartArray();
            gen.writeEndArray();
        } else if (clazz.isTypeOrSubTypeOf(Number.class)) {
            // 数字类型处理为0
            gen.writeNumber(0);
        } else if (clazz.isTypeOrSubTypeOf(Boolean.class)) {
            // Boolean处理为false
            gen.writeBoolean(false);
        } else {
            // 其他空对象处理为 {}
            gen.writeStartObject();
            gen.writeEndObject();
        }
    }

}
