package com.lowellzhao.lnovel.common.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * BigDecimalSerializer
 *
 * @author lwoellzhao
 * @since 2022-05-26
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {


    private BigDecimalSerializer() {
    }

    static BigDecimalSerializer getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.stripTrailingZeros().toPlainString());
    }

    /**
     * Singleton
     */
    private enum Singleton {
        /**
         * INSTANCE
         */
        INSTANCE;

        private BigDecimalSerializer singleton;

        Singleton() {
            singleton = new BigDecimalSerializer();
        }

        public BigDecimalSerializer getInstance() {
            return singleton;
        }
    }

}
