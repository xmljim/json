package io.github.xmljim.json.factory.parser;

import io.github.xmljim.json.exception.JsonException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Utility class the takes an input and converts it to an InputStream for used by a Parser
 *
 * @param inputStream
 */
public record InputData(InputStream inputStream) implements AutoCloseable {

    /**
     * Return the inputStream for this data
     *
     * @return the inputstream
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Create an InputData from a Json String
     *
     * @param data the Json String
     * @return a new InputData
     */
    public static InputData of(final String data) {
        return of(data, StandardCharsets.UTF_8);
    }

    /**
     * Create an InputData from a Json String
     *
     * @param data    the Json String
     * @param charSet the character set
     * @return a new InputData
     */
    public static InputData of(final String data, final Charset charSet) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.getBytes(charSet))) {
            return new InputData(byteArrayInputStream);
        } catch (IOException ioe) {
            throw new JsonException(ioe);
        }
    }

    /**
     * Create an InputData from a Path
     *
     * @param path the Path
     * @return a new InputData
     */
    public static InputData of(final Path path) {
        try (InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ)) {
            return new InputData(inputStream);
        } catch (IOException ioe) {
            throw new JsonException(ioe);
        }
    }

    /**
     * Create an InputData from a Reader
     *
     * @param reader the Json String
     * @return a new InputData
     */
    public static InputData of(final Reader reader) {
        try {
            char[] charBuffer = new char[8 * 1024];
            StringBuilder builder = new StringBuilder();
            int numCharsRead;
            while ((numCharsRead = reader.read(charBuffer, 0, charBuffer.length)) != -1) {
                builder.append(charBuffer, 0, numCharsRead);
            }

            try (InputStream targetStream = new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8))) {
                return new InputData(targetStream);
            }

        } catch (IOException ioe) {
            throw new JsonException(ioe);
        }

    }

    /**
     * Create an InputData from an InputStream
     *
     * @param inputStream the Json String
     * @return a new InputData
     */
    public static InputData of(final InputStream inputStream) {
        return new InputData(inputStream);
    }

    /**
     * Close the underlying inputstream
     *
     * @throws Exception thrown if a problem occurs
     */
    @Override
    public void close() throws Exception {
        if (this.inputStream != null) {
            inputStream.close();
        }
    }
}
