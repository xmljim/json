package io.github.xmljim.json.jsonpath.function.document;

import io.github.xmljim.json.factory.parser.InputData;
import io.github.xmljim.json.factory.parser.Parser;
import io.github.xmljim.json.factory.parser.ParserBuilder;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.jsonpath.JsonPathException;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.github.xmljim.json.jsonpath.function.Argument;
import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.model.JsonNode;
import io.github.xmljim.json.service.ServiceManager;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class DocumentFunction extends AbstractJsonPathFunction {
    public DocumentFunction(String name, List<Argument<?, ?>> arguments, Global global) {
        super(name, arguments, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        List<Context> inputContexts = contextStream.toList();

        Context contextToUse = inputContexts.isEmpty() ? Context.defaultContext() :
            inputContexts.get(0);

        String path = getArgumentValue("path", contextToUse);

        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);
        ParserBuilder builder = factory.newParserBuilder();
        Parser parser = builder.setUseStrict(false).build();
        JsonNode node = null;
        try (InputData inputData = InputData.of(Paths.get(path))) {
            node = parser.parse(inputData);
            return Stream.of(Context.create(node));
        } catch (Exception io) {
            throw new JsonPathException(io);
        }

    }
}
