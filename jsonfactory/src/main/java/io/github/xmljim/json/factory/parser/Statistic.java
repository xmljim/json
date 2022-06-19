package io.github.xmljim.json.factory.parser;

/**
 * A parser statistic. Can be applied to any component of the parser (i.e., Processor, EventHandler, or Assembler)
 *
 * @param <T> the statistic value type
 */
public interface Statistic<T extends Number> {

    /**
     * Return the component name
     *
     * @return the component name
     */
    String getComponent();

    /**
     * Return the statistic name
     *
     * @return the statistic name
     */
    String getName();

    /**
     * return the statistic value
     *
     * @return the statistic value
     */
    T getValue();
}
