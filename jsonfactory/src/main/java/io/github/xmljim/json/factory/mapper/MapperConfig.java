package io.github.xmljim.json.factory.mapper;

import java.util.Optional;
import java.util.Set;

/**
 * Interface for the Mapper Configuration
 */
public interface MapperConfig {

    /**
     * Return the target class for this Mapper
     *
     * @return the target class for this Mapper
     */
    Optional<Class<?>> getTargetClass();

    /**
     * Return a value converter for this Mapper
     *
     * @return the value converter for this mapper
     */
    Optional<ValueConverter<?>> getValueConverter();

    /**
     * Return the keyNameCase for this mapper
     *
     * @return the keyNameCase for this mapper
     */
    KeyNameCase getKeyNameCase();

    Set<String> getIgnoreKeys();

}
