package io.xmljim.json.jsonpath.function.document;

import io.xmljim.json.factory.parser.InputData;
import io.xmljim.json.factory.parser.Parser;
import io.xmljim.json.factory.parser.ParserBuilder;
import io.xmljim.json.factory.parser.ParserFactory;
import io.xmljim.json.jsonpath.JsonPathException;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.model.JsonNode;
import io.xmljim.json.service.ServiceManager;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class DocumentFunction extends AbstractJsonPathFunction {
    public DocumentFunction(String name, List<Argument<?, ?>> arguments) {
        super(name, arguments);
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
