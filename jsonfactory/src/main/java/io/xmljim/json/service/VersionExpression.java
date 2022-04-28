package io.xmljim.json.service;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing a version expression used in an evaluation of another version.
 * It follows Semantic Versioning guidelines for expression syntax. A VersionExpression
 * is used in several {@link Version} methods, specifically {@link Version#evaluateVersion(VersionExpression)}
 * which uses the operator value to determine the appropriate method to invoke to evaluate
 * a version.
 * @author jearley
 *
 */
final class VersionExpression {
    private final VersionOperator operator;
    private final Version version;

    /**
     * Constructor
     *
     * @param operator the version operator
     * @param version  the version
     */
    public VersionExpression(VersionOperator operator, Version version) {
        this.operator = operator;
        this.version = version;
    }

    /**
     * Constructor using a version expression string. The string value is parsed and must include
     * a version operator (see {@link VersionOperator} for valid symbol operators), and
     * a version string.
     * <p><b>Examples</b></p>
     * <ul>
     * 	<li>Find compatible versions: <code>^1.2.3</code></li>
     * 	<li>Find close versions: <code>~1.2.3</code></li>
     * 	<li>Find equal versions: <code>=1.2.3</code></li>
     * 	<li>Find greater than versions: <code>&gt;1.2.3</code></li>
     *  <li>Find greater than or equal versions: <code>&gt;=1.2.3</code></li>
     *  <li>Find less than versions: <code>&lt;1.2.3</code></li>
     *  <li>Find less than or equal versions: <code>&lt;=1.2.3</code></li>
     * </ul>
     *
     * @param versionExpression The expression string containing the operator and version string
     * @throws VersionError thrown if the expression is not valid
     */
    public VersionExpression(String versionExpression) {
        Pattern pattern = Pattern.compile(VersionPatterns.evalPattern);
        Matcher matcher = pattern.matcher(versionExpression);

        if (matcher.matches()) {
            String opVal = matcher.group(VersionPatterns.OPERATOR);
            String verVal = matcher.group(VersionPatterns.VERSION);

            try {
                this.operator = Objects.requireNonNull(VersionOperator.fromSymbol(opVal));
            } catch (NullPointerException npe) {
                throw new VersionError("Invalid Operator Symbol: " + opVal);
            }

            this.version = new Version(verVal);
        } else {
            throw new VersionError("Expected a version expression: `<operator><version>`, e.g., '~1.2.3' but got " + versionExpression);
        }
    }

    /**
     * Return the operator
     *
     * @return the operator
     */
    public VersionOperator getOperator() {
        return operator;
    }

    /**
     * Return the version
     *
     * @return the version
     */
    public Version getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return getOperator().getSymbol() + getVersion().toString();
    }

}