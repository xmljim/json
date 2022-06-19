package io.github.xmljim.json.factory.parser;

import java.math.BigDecimal;

/**
 * An enumeration of floating number strategies
 */
public enum FloatingNumberValueType implements NumericValueType {
    /**
     * Float
     */
    FLOAT {
        @Override
        public Number apply(String numericString) {
            return Float.valueOf(numericString);
        }
    },

    /**
     * Double
     */
    DOUBLE {
        @Override
        public Number apply(String numericString) {
            return Double.parseDouble(numericString);
        }
    },

    /**
     * Big Decimal
     */
    BIG_DECIMAL {
        @Override
        public Number apply(String numericString) {
            return new BigDecimal(numericString);
        }
    }
}
