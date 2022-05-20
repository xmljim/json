package io.github.xmljim.json.merger;

import io.github.xmljim.json.factory.merge.MergeOperation;
import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.service.ServiceManager;

import java.util.stream.IntStream;

final class MergeOperations {
    static MergeOperation<JsonArray, Integer> arrayMerge = ((primary, secondary, conflictStrategy, processor) -> {
        if (primary.isEquivalent(secondary)) {
            return primary;
        } else {
            ElementFactory elementFactory = ServiceManager.getProvider(ElementFactory.class);
            JsonArray mergedArray = elementFactory.newArray();

            int primarySize = primary.size();
            int secondarySize = secondary.size();
            int minCap = Math.min(primarySize, secondarySize);

            IntStream.range(0, minCap)
                .forEach(index -> conflictStrategy.apply(mergedArray, index, primary.getValue(index), secondary.getValue(index), processor));

            if (primarySize > secondarySize) {
                IntStream.range(minCap, primarySize).forEach(i -> mergedArray.add(primary.getValue(i)));
            } else if (secondarySize > primarySize) {
                IntStream.range(minCap, secondarySize).forEach(i -> mergedArray.add(secondary.getValue(i)));
            }

            return mergedArray;
        }


    });

    static MergeOperation<JsonObject, String> objectMerge = ((primary, secondary, conflictStrategy, processor) -> {
        if (primary.isEquivalent(secondary)) {
            return primary;
        } else {
            ElementFactory elementFactory = ServiceManager.getProvider(ElementFactory.class);
            JsonObject mergedObject = elementFactory.newObject();

            primary.keys().forEach(key -> {
                if (secondary.containsKey(key)) {
                    conflictStrategy.apply(mergedObject, key, primary.getValue(key), secondary.getValue(key), processor);
                } else {
                    mergedObject.put(key, primary.getValue(key));
                }
            });

            secondary.keys().forEach(key -> {
                if (!primary.containsKey(key)) {
                    mergedObject.put(key, secondary.getValue(key));
                }
            });

            return mergedObject;
        }
    });
}
