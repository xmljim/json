package io.github.xmljim.json.mapper;

import io.github.xmljim.json.factory.mapper.ValueConverter;
import io.github.xmljim.json.mapper.exception.JsonMapperException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

interface ClassUtils {
    /**
     * Create a new class instance
     *
     * @param <T>           the class type
     * @param classToCreate The class
     * @return a new instance of the class;
     * @throws JsonMapperException thrown if an error occurs creating the class
     */
    static <T> T createInstance(Class<T> classToCreate) {
        final Optional<Constructor<?>> constructor = getDefaultConstructor(classToCreate);

        if (constructor.isPresent()) {
            try {
                constructor.get().setAccessible(true);
                @SuppressWarnings("unchecked") final T instance = (T) constructor.get().newInstance();
                return instance;
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                     | InvocationTargetException e) {
                throw new JsonMapperException("Error creating instance: " + classToCreate.getName(), e);
            }
        } else {
            throw new JsonMapperException("No default constructor found for class: " + classToCreate.getName());
        }
    }

    /**
     * Find the default constructor
     *
     * @param classToCreate the class containing the constructor
     * @param <T>           The class type
     * @return the default constructor
     */
    private static <T> Optional<Constructor<?>> getDefaultConstructor(Class<T> classToCreate) {
        return Arrays.stream(classToCreate.getConstructors())
            .filter(constructor -> constructor.getParameterCount() == 0).findFirst();
    }

    static <T> T createInstance(Class<T> classToCreate, Class<?>[] argTypes, Object... args) {
        try {
            Constructor<T> constructor;
            constructor = classToCreate.getConstructor(argTypes);
            return constructor.newInstance(args);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            throw new JsonMapperException("Error creating class: " + classToCreate + ": " + e.getMessage(), e);
        }
    }

    static ValueConverter<?> createValueConverter(Class<?> classToCreate, Map<String, String> args) {
        if (classToCreate == null) {
            return null;
        }

        try {
            Constructor<?> con;
            con = classToCreate.getConstructor(String[].class);
            con.setAccessible(true);
            return (ValueConverter<?>) con.newInstance(args);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            throw new JsonMapperException("Error creating class: " + classToCreate + ": " + e.getMessage(), e);
        }

    }
}
