package io.xmljim.json.mapper;


import io.xmljim.json.factory.mapper.ValueConverter;
import io.xmljim.json.factory.mapper.annotation.ConvertClass;
import io.xmljim.json.factory.mapper.annotation.ConvertValue;
import io.xmljim.json.factory.mapper.annotation.JsonElement;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

interface AnnotationUtils {
    /**
     * Find and return an Optional containing an Annotation
     *
     * @param <T>             The annotation type
     * @param annotationClass the annotation class
     * @param classMember     The class member (either a Field, Method or Class)
     * @return an Optional containing the Annotation
     */
    static <T extends Annotation> Optional<T> findAnnotation(Class<? extends T> annotationClass, AnnotatedElement classMember) {
        T annotation = null;

        if (classMember.isAnnotationPresent(annotationClass)) {
            annotation = classMember.getAnnotation(annotationClass);
        }

        return Optional.ofNullable(annotation);
    }

    static Optional<ValueConverter<?>> getConvertToJson(AnnotatedElement element) {
        ValueConverter<?> convert = null;
        final Optional<ConvertValue> valueConverter = findAnnotation(ConvertValue.class, element);

        if (valueConverter.isPresent()) {
            Map<String, String> jsonArgs = new HashMap<>();
            Arrays.stream(valueConverter.get().toJsonArgs()).forEach(arg -> jsonArgs.put(arg.name(), arg.value()));

            convert = ClassUtils.createValueConverter(valueConverter.get().toJson(), jsonArgs);
        }

        return Optional.ofNullable(convert);
    }

    static Optional<ValueConverter<?>> getConvertToValue(AnnotatedElement element) {
        ValueConverter<?> convert = null;
        final Optional<ConvertValue> valueConverter = findAnnotation(ConvertValue.class, element);

        if (valueConverter.isPresent()) {
            Map<String, String> valueArgs = new HashMap<>();
            Arrays.stream(valueConverter.get().toValueArgs()).forEach(arg -> valueArgs.put(arg.name(), arg.value()));
            convert = ClassUtils.createValueConverter(valueConverter.get().toValue(), valueArgs);
        }

        return Optional.ofNullable(convert);
    }

    static boolean findJsonElementIgnore(AnnotatedElement element) {
        return findAnnotation(JsonElement.class, element).map(JsonElement::ignore).orElse(false);
    }

    static Optional<String> findJsonElementKey(AnnotatedElement element) {
        return findAnnotation(JsonElement.class, element).filter(a -> !"".equals(a.key())).map(JsonElement::key);
    }

    static Optional<String> findJsonElementSetter(AnnotatedElement element) {
        return findAnnotation(JsonElement.class, element).filter(a -> !"".equals(a.setterMethod())).map(JsonElement::setterMethod);
    }

    static Optional<String> findJsonElementGetter(AnnotatedElement element) {
        return findAnnotation(JsonElement.class, element).filter(a -> !"".equals(a.getterMethod())).map(JsonElement::getterMethod);
    }

    static Optional<Class<?>> findConvertClass(AnnotatedElement element) {
        return findAnnotation(ConvertClass.class, element).map(ConvertClass::target);
    }
}
