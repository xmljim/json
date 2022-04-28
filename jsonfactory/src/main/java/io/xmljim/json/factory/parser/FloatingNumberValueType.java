package io.xmljim.json.factory.parser;

import java.math.BigDecimal;

public enum FloatingNumberValueType implements NumericValueType {
    FLOAT {
        @Override
        public Number apply(String numericString) {
            return Float.valueOf(numericString);
        }
    },

    DOUBLE {
        @Override
        public Number apply(String numericString) {
            return Double.parseDouble(numericString);
        }
    },

    BIG_DECIMAL {
        @Override
        public Number apply(String numericString) {
            return new BigDecimal(numericString);
        }
    }
}
