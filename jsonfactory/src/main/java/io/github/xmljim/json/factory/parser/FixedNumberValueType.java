package io.github.xmljim.json.factory.parser;

import java.math.BigInteger;

/**
 * An enumeration of fixed number strategies
 */
public enum FixedNumberValueType implements NumericValueType {
    /**
     * Integer
     */
    INTEGER {
        @Override
        public Number apply(String numericString) {
            return Integer.valueOf(numericString);
        }
    },

    /**
     * Long
     */
    LONG {
        @Override
        public Number apply(String numericString) {
            return Long.valueOf(numericString);
        }
    },

    /**
     * Big Integer
     */
    BIG_INTEGER {
        @Override
        public Number apply(String numericString) {
            return new BigInteger(numericString);
        }
    }
}
