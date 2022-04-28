package io.xmljim.json.factory.parser;

import java.math.BigInteger;

public enum FixedNumberValueType implements NumericValueType {
    INTEGER {
        @Override
        public Number apply(String numericString) {
            return Integer.valueOf(numericString);
        }
    },

    LONG {
        @Override
        public Number apply(String numericString) {
            return Long.valueOf(numericString);
        }
    },

    BIG_INTEGER {
        @Override
        public Number apply(String numericString) {
            return new BigInteger(numericString);
        }
    }
}
