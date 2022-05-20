package io.github.xmljim.json.jsonpath.compiler;

/**
 * A token is simply a string buffer containing characters that will be used to instantiate JsonPath elements
 */
class Token {

    private final StringBuffer buffer = new StringBuffer();

    /**
     * Append character to the buffer
     *
     * @param character the character
     */
    public void append(Character character) {
        buffer.append(character);
    }

    /**
     * Clear the token buffer, deleting all characters
     */
    public void clear() {
        buffer.delete(0, buffer.length());
    }

    /**
     * Returns whether the token contains any characters
     *
     * @return {@code true} if the buffer size is empty;
     */
    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    /**
     * Reverse of {@link #isEmpty()}
     *
     * @return {@code true} if the buffer has any characters
     */
    public boolean hasContent() {
        return !isEmpty();
    }

    /**
     * Return the buffer length
     *
     * @return the number of characters in the buffer
     */
    public int length() {
        return buffer.length();
    }

    /**
     * Evaluates whether the current buffer matches the specified regular expression
     *
     * @param regex the regular expression
     * @return {@code true} if the token matches; {@code false} otherwise
     */
    public boolean matches(String regex) {
        return toString().matches(regex);
    }

    /**
     * Return the current buffer as a string
     *
     * @return the string of the current buffer
     */
    public String toString() {
        return buffer.toString();
    }
}
