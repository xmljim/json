package io.github.xmljim.json.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a <a href="https://semver.org/" target="_blank">Semantic Version</a>
 *
 * @author Jim Earley (xml.jim@gmail.com)
 */
final class Version implements Comparable<Version> {

    private int major;
    private int minor;
    private int patch;
    private String preRelease;
    private String buildMetadata;

    /**
     * Default constructor. Creates a default version of 0.0.1
     */
    @SuppressWarnings("unused")
    public Version() {
        setMajor(0);
        setMinor(0);
        setPatch(1);
    }

    /**
     * Create a semantic version using a version string. Some examples of valid semantic versions:
     * <ul>
     * 	<li><code>1</code>:  Equal to <code>1.0.0</code></li>
     *  <li><code>1.2</code>: Equivalent to <code>1.2.0</code></li>
     *  <li><code>1.2.3</code></li>
     *  <li><code>1.2-rc.1</code>: Equivalent to <code>1.2.0-rc.1</code></li>
     *  <li><code>1.2.3-rc.1</code></li>
     *  <li><code>1.2.3-rc.1+build-2</code></li>
     *  <li><code>1.2.3+build-2</code></li>
     * </ul>
     *
     * @param versionString a valid semantic version string
     * @throws VersionError thrown if the version string is not a valid semantic version
     */
    @SuppressWarnings("unused")
    public Version(String versionString) {
        Pattern pattern = Pattern.compile(VersionPatterns.regex);
        Matcher matcher = pattern.matcher(versionString);

        if (matcher.matches()) {

            int maj = Integer.parseInt(matcher.group(VersionPatterns.MAJOR));
            int min = matcher.group(VersionPatterns.MINOR) != null ? Integer.parseInt(matcher.group(VersionPatterns.MINOR)) : 0;
            int pat = matcher.group(VersionPatterns.PATCH) != null ? Integer.parseInt(matcher.group(VersionPatterns.PATCH)) : 0;

            setMajor(maj);
            setMinor(min);
            setPatch(pat);
            setPreRelease(matcher.group(VersionPatterns.PRERELEASE));
            setBuildMetadata(matcher.group(VersionPatterns.BUILDMETADATA));
        } else {
            throw new VersionError("Invalid Semantic Version: " + versionString + " Expected pattern: " + VersionPatterns.regex);
        }
    }

    /**
     * Create a semantic version using core major, minor, and patch values
     *
     * @param major the major version
     * @param minor the minor version
     * @param patch the patch level
     */
    @SuppressWarnings("unused")
    public Version(int major, int minor, int patch) {
        setMajor(major);
        setMinor(minor);
        setPatch(patch);
    }

    /**
     * Create a semantic version with core values and suffix containing optional prerelease and/or build metadata values
     *
     * @param major  the major version
     * @param minor  the minor version
     * @param patch  the patch level
     * @param suffix the prerelease/build string. Can be null. Must follow the following format:
     *               <p>
     *               <code>[-][&lt;prerelease value&gt;][+&lt;build&gt;]</code>
     *               </p>
     *               <p><b>Examples</b></p>
     *               <ul>
     *               	<li>Prerelease only (without dash): <code>rc-1</code></li>
     *                   <li>Prerelease only (with dash): <code>-rc-1</code></li>
     *               	<li>Prerelease (without leading dash) and build: <code>rc-1+build-01</code></li>
     *               	<li>Prerelease (with dash) and build: <code>-rc-1+build-01</code></li>
     *                   <li>Build only (MUST include '+' sign): <code>+build-01</code></li>
     *               </ul>
     */
    @SuppressWarnings("unused")
    public Version(int major, int minor, int patch, String suffix) {
        this(major, minor, patch);

        if (suffix != null) {
            Pattern pattern = Pattern.compile(VersionPatterns.suffixPattern);
            Matcher matcher = pattern.matcher(suffix);
            if (matcher.matches()) {
                setPreRelease(matcher.group(VersionPatterns.PRERELEASE));
                setBuildMetadata(matcher.group(VersionPatterns.BUILDMETADATA));
            } else {
                throw new VersionError("Invalid Semantic Version Suffix: " + suffix + ". Expected pattern: " + VersionPatterns.suffixPattern);
            }
        }
    }

    /**
     * Set the major version value
     *
     * @param major the major version value. Must be a positive integer
     * @throws VersionError thrown if major version is less than 0
     */
    public void setMajor(int major) {
        if (major < 0) {
            throw new VersionError("Major version cannot be less than 0");
        }

        this.major = major;
    }

    /**
     * Returns the major version number
     *
     * @return the major version number
     */
    public int getMajor() {
        return major;
    }

    /**
     * Set the minor version number
     *
     * @param minor the minor version number. Must be a positive integer.
     * @throws VersionError thrown if minor version number is less than 0
     */
    @SuppressWarnings("unused")
    public void setMinor(int minor) {
        if (minor < 0) {
            throw new VersionError("Minor version cannot be less than 0");
        }

        this.minor = minor;
    }

    /**
     * Return the minor version number
     *
     * @return the minor version number
     */
    @SuppressWarnings("unused")
    public int getMinor() {
        return minor;
    }

    /**
     * Set the patch level
     *
     * @param patch the patch level number. Must be a positive integer
     * @throws VersionError throw if patch level number is less than 0
     */
    @SuppressWarnings("unused")
    public void setPatch(int patch) {
        if (patch < 0) {
            throw new VersionError("Patch level cannot be less than 0");
        }

        this.patch = patch;
    }

    /**
     * Return the patch level
     *
     * @return the patch level number
     */
    @SuppressWarnings("unused")
    public int getPatch() {
        return patch;
    }

    /**
     * Set the prerelease String
     *
     * @param preRelease the prerelease value. Can be null
     */
    public void setPreRelease(String preRelease) {
        this.preRelease = preRelease;
    }

    /**
     * Return the prerelease value
     *
     * @return the prerelease value. Can be null
     */
    public String getPreRelease() {
        return preRelease;
    }

    /**
     * Set the build value
     *
     * @param buildMetadata the build metadata value. Can be null
     */
    public void setBuildMetadata(String buildMetadata) {
        this.buildMetadata = buildMetadata;
    }

    /**
     * Return the build metadata value
     *
     * @return the build metadata value. Can be null
     */
    public String getBuildMetadata() {
        return buildMetadata;
    }

    /**
     * Returns whether the version is stable.
     *
     * @return <code>true</code> if both {@link #getPreRelease()} and {@link #getBuildMetadata()} are <code>null</code>; <code>false</code> otherwise.
     */
    public boolean isStable() {
        return getSuffix().contentEquals("");
    }

    /**
     * Returns a concatenated, formatted value containing the values from {@link #getPreRelease()} and {@link #getBuildMetadata()}.
     *
     * @return a concatenated, formatted value containing the values from {@link #getPreRelease()} and {@link #getBuildMetadata()}. If
     * both are <code>null</code>, it will return an empty string (never null).
     */
    public String getSuffix() {
        StringBuilder builder = new StringBuilder();

        if (getPreRelease() != null) {
            builder.append("-").append(getPreRelease());
        }

        if (getBuildMetadata() != null) {
            builder.append("+").append(getBuildMetadata());
        }

        return builder.toString();
    }

    /**
     * Return a new version by incrementing the major version. Minor and Patch numbers will be set to 0.
     * For example if the current version is <code>1.2.3</code>,
     * it will return a new version instance with a value of <code>2.0.0</code>.
     * Prerelease and build metadata will not be included (null).
     *
     * @return a new version by incrementing the major version
     */
    public Version nextMajor() {
        return new Version(getMajor() + 1, 0, 0);
    }

    /**
     * Return a new version by incrementing the minor version number. The major version remains the same as the
     * current minor version, and the patch level is set to 0.  For example, <code>1.2.3</code> will return a new
     * version of <code>1.3.0</code>. Prerelease and build metadata will not be included (null).
     *
     * @return a new version by incrementing the minor version number
     */
    public Version nextMinor() {
        return new Version(getMajor(), getMinor() + 1, 0);
    }

    /**
     * Return a new version by incrementing the patch level number. Major and minor version numbers remain the
     * same. For example <code>1.2.3</code> will return a new version of <code>1.2.4</code>. Prerelease and
     * build metadata will not be included (null).
     *
     * @return Return a new version by incrementing the patch level number
     */
    @SuppressWarnings("unused")
    public Version nextPatch() {
        return new Version(getMajor(), getMinor(), getPatch() + 1);
    }

    /**
     * Return a formatted semantic version string
     */
    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append(getMajor()).append(".")
            .append(getMinor()).append(".")
            .append(getPatch())
            .append(getSuffix());

        return builder.toString();
    }


    /**
     * Returns whether this version is compatible with another version. Compatibility means that the current version
     * is greater than or equal to the other version, but less than the other's next major version. For example,
     * If the current version is <code>1.2.3</code>, then a compatible version would be <code>1.2.3</code> or higher, but less
     * than <code>2.0.0</code>. The equivalent expression is <code>^1.2.3</code>
     *
     * @param anotherVersion the version to compare
     * @return <code>true</code> if the versions are compatible; <code>false</code> otherwise
     */
    public boolean isCompatibleWith(Version anotherVersion) {

        // 1.2.3 ~ (1.2.3 - <2.0.0)
        boolean gte = isGreaterThanOrEqualTo(anotherVersion);
        boolean lt = anotherVersion.isLessThan(nextMajor());

        return gte && lt;
    }


    /**
     * Returns whether this version is compatible with another version
     *
     * @param versionString the version string
     * @return <code>true</code> if the versions are compatible; <code>false</code> otherwise
     * @throws VersionError thrown if the versionString is not a valid semantic version
     * @see #isCompatibleWith(Version)
     */
    @SuppressWarnings("unused")
    public boolean isCompatibleWith(String versionString) {
        Version anotherVersion = new Version(versionString);
        return isCompatibleWith(anotherVersion);
    }

    /**
     * Returns whether the current version is close to another version.
     * "Close to" evaluates whether the current version is greater than or equal to another version,
     * and is less than the next minor version.  E.g., if the current version {@code 1.2.1}
     *
     * @param anotherVersion another version
     * @return true if the other version is close to the current version
     */
    @SuppressWarnings("unused")
    public boolean isCloseTo(Version anotherVersion) {
        boolean gte = isGreaterThanOrEqualTo(anotherVersion);
        boolean ilt = isLessThan(anotherVersion.nextMinor());

        return gte && ilt;
    }

    @SuppressWarnings("unused")
    public boolean isCloseTo(String versionString) {
        return isCloseTo(new Version(versionString));
    }

    /**
     * Returns whether the current version is less than the version to compare
     *
     * @param anotherVersion the version to compare
     * @return <code>true</code> if the current version is less than the compared version; <code>false</code> otherwise
     */
    @SuppressWarnings("unused")
    public boolean isLessThan(Version anotherVersion) {
        boolean lessThan;

        boolean lessThanMajor = compareMajor(anotherVersion) < 0;
        boolean lessThanMinor = compareMinor(anotherVersion) < 0;
        boolean lessThanPatch = comparePatch(anotherVersion) < 0;

        lessThan = lessThanMajor || lessThanMinor || lessThanPatch;

        if (!lessThan) {
            if (comparePatch(anotherVersion) == 0 && !isStable()) {
                if (anotherVersion.isStable()) {
                    lessThan = true;
                } else {
                    lessThan = getSuffix().compareTo(anotherVersion.getSuffix()) < 0;
                }
            }
        }

        return lessThan;
    }

    /**
     * Returns whether the current version is less than the version to compare
     *
     * @param versionString the version string to compare
     * @return <code>true</code> if the current version is less than the compared version; <code>false</code> otherwise
     * @throws VersionError thrown if the versionString is not a valid semantic version
     * @see #isLessThan(Version)
     */
    @SuppressWarnings("unused")
    public boolean isLessThan(String versionString) {
        Version anotherVersion = new Version(versionString);
        return isLessThan(anotherVersion);
    }

    /**
     * Evaluates whether the current version is less than or equal to a version to compare
     *
     * @param anotherVersion the version to compare
     * @return <code>true</code> if the current version is less than or equal to the compared version; <code>false</code> otherwise
     */
    @SuppressWarnings("unused")
    public boolean isLessThanOrEqualTo(Version anotherVersion) {
        boolean ilt = isLessThan(anotherVersion);
        boolean iet = isEqualTo(anotherVersion);
        return ilt || iet;
    }


    /**
     * Evaluates whether the current version is less than or equal to a version to compare
     *
     * @param versionString the version string to compare
     * @return <code>true</code> if the current version is less than or equal to the compared version; <code>false</code> otherwise
     * @throws VersionError thrown if the versionString is not a valid semantic version
     * @see #isLessThanOrEqualTo(Version)
     */
    @SuppressWarnings("unused")
    public boolean isLessThanOrEqualTo(String versionString) {
        return isLessThanOrEqualTo(new Version(versionString));
    }

    /**
     * Evaluates whether the current version is greater than a version to compare
     *
     * @param anotherVersion the version to compare
     * @return <code>true</code> if the current version is greater than the compared version; <code>false</code> otherwise
     */
    @SuppressWarnings("unused")
    public boolean isGreaterThan(Version anotherVersion) {
        boolean gtMajor = compareMajor(anotherVersion) > 0;
        boolean gtMinor = compareMinor(anotherVersion) > 0;
        boolean gtPatch = comparePatch(anotherVersion) > 0;

        boolean versionGreaterThan = gtMajor || gtMinor || gtPatch;

        if (!versionGreaterThan) {
            if (comparePatch(anotherVersion) == 0 && !isStable()) {
                if (anotherVersion.isStable()) {
                    return false;
                } else {
                    return getSuffix().compareTo(anotherVersion.getSuffix()) > 0;
                }
            } else {
                if (!anotherVersion.isStable()) {
                    return true;
                } else {
                    return getSuffix().compareTo(anotherVersion.getSuffix()) > 0;
                }
            }
        }

        return versionGreaterThan;
    }

    /**
     * Evaluates whether the current version is greater than a version to compare
     *
     * @param versionString the version string to compare
     * @return <code>true</code> if the current version is greater than the compared version; <code>false</code> otherwise
     * @throws VersionError thrown if the versionString is not a valid semantic version
     * @see #isGreaterThan(Version)
     */
    @SuppressWarnings("unused")
    public boolean isGreaterThan(String versionString) {
        return isGreaterThan(new Version(versionString));
    }

    /**
     * Evaluates whether the current version is greater than or equal to a version to compare
     *
     * @param anotherVersion the version to compare
     * @return <code>true</code> if the current version is greater than or equal to the compared version; <code>false</code> otherwise
     */
    @SuppressWarnings("unused")
    public boolean isGreaterThanOrEqualTo(Version anotherVersion) {
        return isEqualTo(anotherVersion) || isGreaterThan(anotherVersion);
    }

    /**
     * Evaluates whether the current version is greater than or equal to a version to compare
     *
     * @param versionString the version string to compare
     * @return <code>true</code> if the current version is greater than or equal to the compared version; <code>false</code> otherwise
     * @throws VersionError thrown if the versionString is not a valid semantic version
     * @see #isGreaterThanOrEqualTo(Version)
     */
    @SuppressWarnings("unused")
    public boolean isGreaterThanOrEqualTo(String versionString) {
        return isGreaterThanOrEqualTo(new Version(versionString));
    }

    /**
     * Evaluates whether the current version is equal to a comparable version. Equality
     * includes prerelease and build metadata values
     *
     * @param anotherVersion the version to compare
     * @return <code>true</code> if the values are equivalent; <code>false</code> otherwise
     */
    @SuppressWarnings("unused")
    public boolean isEqualTo(Version anotherVersion) {
        return toString().contentEquals(anotherVersion.toString());
    }

    /**
     * Evaluates whether the current version is equal to a comparable version. Equality
     * includes prerelease and build metadata values
     *
     * @param versionString the version to compare
     * @return <code>true</code> if the values are equivalent; <code>false</code> otherwise
     * @throws VersionError thrown if the versionString is not a valid semantic version
     * @see #isEqualTo(Version)
     */
    @SuppressWarnings("unused")
    public boolean isEqualTo(String versionString) {
        return isEqualTo(new Version(versionString));
    }


    /**
     * Evaluates a {@link VersionExpression} to determine if the current version meets its criteria.
     * The {@link VersionExpression#getOperator()} determines which evaluation method to invoke and
     * uses the {@link VersionExpression#getVersion()} as the version to compare with the current version.
     *
     * @param expression the version expression
     * @return <code>true</code> if the current version meets the criteria, <code>false</code> otherwise
     */
    @SuppressWarnings("unused")
    public boolean evaluateVersion(VersionExpression expression) {
        boolean eval = false;

        switch (expression.getOperator()) {
            case CLOSE_TO:
                eval = isCloseTo(expression.getVersion());
                break;
            case COMPATIBLE:
                eval = isCompatibleWith(expression.getVersion());
                break;
            case EQUAL_TO:
                eval = isEqualTo(expression.getVersion());
                break;
            case GREATER_THAN:
                eval = isGreaterThan(expression.getVersion());
                break;
            case GREATER_THAN_EQUAL_TO:
                eval = isGreaterThanOrEqualTo(expression.getVersion());
                break;
            case LESS_THAN:
                eval = isLessThan(expression.getVersion());
                break;
            case LESS_THAN_EQUAL_TO:
                eval = isLessThanOrEqualTo(expression.getVersion());
                break;
        }

        return eval;
    }

    /**
     * Evaluates a version expression to determine if the current version meets its criteria.
     * The expression syntax is: <code>&lt;operator&gt;&lt;version&gt;</code>, where the
     * operator is one of the following:
     * <ul>
     * 	<li><b>^</b>: "is compatible with". Maps to {@link VersionOperator#COMPATIBLE}, which is used to invoke {@link #isCompatibleWith(Version)} </li>
     * 	<li><b>~</b>: "is close to". Maps to {@link VersionOperator#CLOSE_TO}, which is used to invoke {@link #isCloseTo(Version)}</li>
     * 	<li><b>=</b>: "is equal to". Maps to {@link VersionOperator#EQUAL_TO}, which is used to invoke {@link #isEqualTo(Version)}</li>
     *  <li><b>&gt;</b>: "is greater than". Maps to {@link VersionOperator#GREATER_THAN}, which is used to invoke {@link #isGreaterThan(Version)}</li>
     *  <li><b>&gt;=</b>: "is greater than or equal to". Maps {@link VersionOperator#GREATER_THAN_EQUAL_TO}, which is used to invoke {@link #isGreaterThanOrEqualTo(Version)}</li>
     *  <li><b>&lt;</b>: "is less than". Maps {@link VersionOperator#LESS_THAN}, which is used to invoke {@link #isLessThan(Version)}</li>
     *  <li><b>&lt;=</b>: "is less than or equal to". Maps to {@link VersionOperator#LESS_THAN_EQUAL_TO}, which is used to invoke {@link #isLessThanOrEqualTo(Version)}</li>
     * </ul>
     * <p><b>Examples</b></p>
     * <p><code>^1.0</code>: Evaluates to true if the current version falls in the range <code>&gt;=1.0.0 and &lt;2.0.0</code></p>
     * <p><code>~1.2.4</code>: Evaluates to true if the current version falls in the range <code>&gt;=1.2.4</code> and <code>&lt;1.3.0</code></p>
     * <p><code>=1.6.2</code>: Evaluates to true if the current version equals <code>1.6.2</code></p>
     * <p><code>&gt;1.2.0</code>: Evaluates to true if the current version is greater than <code>1.2.0</code></p>
     * <p><code>&gt;=1.2.0</code>: Evaluates to true if the current version is greater than or equal to <code>1.2.0</code></p>
     * <p><code>&lt;1.2.0</code>: Evaluates to true if the current version is less than <code>1.2.0</code></p>
     * <p><code>&lt;=1.2.0</code>: Evaluates to true if the current version is less than or equal to <code>1.2.0</code></p>
     *
     * @param versionExpression The version expression, which will be converted to a {@link VersionExpression} instance
     * @return <code>true</code> if the current version meets the expression criteria, <code>false</code> otherwise.
     * @throws VersionError thrown if the version expression is invalid
     */
    public boolean evaluateVersion(String versionExpression) {
        return evaluateVersion(new VersionExpression(versionExpression));
    }

    /**
     * Evaluates whether the current version falls within a specified version range,
     * <i>inclusive</i> of the start and end version values, e.g., <code>&gt;=1.0.0</code> and
     * <code>&lt;=2.0.0</code>.
     *
     * @param start the start of the version range
     * @param end   then end of the version range
     * @return <code>true</code> if the current version falls within the specified range, <code>false</code> otherwise
     */
    @SuppressWarnings("unused")
    public boolean isInRangeInclusive(Version start, Version end) {
        boolean inRange;

        boolean gte = isGreaterThanOrEqualTo(start);
        boolean lte = isLessThanOrEqualTo(end);
        return gte && lte;

    }

    /**
     * Evaluates whether the current version falls within a specified version range,
     * <i>inclusive</i> of the start and end version values, e.g., <code>&gt;=1.0.0</code> and
     * <code>&lt;=2.0.0</code>.
     *
     * @param versionStringStart the starting version
     * @param versionStringEnd   the end version
     * @return <code>true</code> if the current version falls within the specified range, <code>false</code> otherwise
     * @throws VersionError thrown if either version string is not a valid semantic version
     * @see #isInRangeInclusive(Version, Version)
     */
    @SuppressWarnings("unused")
    public boolean isInRangeInclusive(String versionStringStart, String versionStringEnd) {
        Version start = new Version(versionStringStart);
        Version end = new Version(versionStringEnd);
        return isInRangeInclusive(start, end);
    }

    /**
     * Evaluates whether the current version falls within a specified version range,
     * <i>exclusive</i> of the start and end version values, e.g., <code>&gt;1.0.0</code> and
     * <code>&lt;2.0.0</code>.
     *
     * @param start the start of the version range
     * @param end   then end of the version range
     * @return <code>true</code> if the current version falls within the specified range, <code>false</code> otherwise
     */
    @SuppressWarnings("unused")
    public boolean isInRangeExclusive(Version start, Version end) {
        boolean inRange;

        boolean gt = isGreaterThan(start);
        boolean lt = isLessThan(end);
        return gt && lt;

    }

    /**
     * Evaluates whether the current version falls within a specified version range,
     * <i>exclusive</i> of the start and end version values, e.g., <code>&gt;1.0.0</code> and
     * <code>&lt;2.0.0</code>.
     *
     * @param versionStringStart the starting version
     * @param versionStringEnd   the end version
     * @return <code>true</code> if the current version falls within the specified range, <code>false</code> otherwise
     * @throws VersionError thrown if either version string is not a valid semantic version
     * @see #isInRangeExclusive(Version, Version)
     */
    @SuppressWarnings("unused")
    public boolean isInRangeExclusive(String versionStringStart, String versionStringEnd) {
        Version start = new Version(versionStringStart);
        Version end = new Version(versionStringEnd);
        return isInRangeExclusive(start, end);
    }

    /**
     * Evaluates whether the current version falls within a specified version range.
     * Expressions must use operators allowed in compound ranges (i.e., &lt;,&gt;,&lt;=,&gt;=).
     *
     * @param start the starting expression
     * @param end   the ending expression
     * @return <code>true</code> if the current version falls within the specified range, <code>false</code> otherwise
     * @throws VersionError thrown if a VersionExpression's {@link VersionExpression#getOperator()} value is not a valid compound range operator
     */
    @SuppressWarnings("unused")
    public boolean isInRange(VersionExpression start, VersionExpression end) {
        boolean inRange = false;
        boolean startRange;
        boolean endRange;

        if (start.getOperator().isCompoundRangeEnabled()) {
            startRange = evaluateVersion(start);
        } else {
            throw new VersionError("Invalid compound range operator [start]");
        }

        if (end.getOperator().isCompoundRangeEnabled()) {
            endRange = evaluateVersion(end);
        } else {
            throw new VersionError("Invalid compound range operator [end]");
        }

        return startRange && endRange;
    }

    /**
     * Evaluates whether the current version falls within a specified version range.
     * Expressions must use operators allowed in compound ranges (i.e., &lt;,&gt;,&lt;=,&gt;=).
     * This enables validation for partial-left or partial-right ranges in conditions such as
     * <code>&gt;X &amp;&amp; &lt;=Y</code> or vice versa.
     *
     * @param versionExpressionStart the starting expression
     * @param versionExpressionEnd   the ending expression
     * @return <code>true</code> if the current version falls within the specified range, <code>false</code> otherwise
     * @throws VersionError thrown if a VersionExpression's {@link VersionExpression#getOperator()} value is not a valid compound range operator
     */
    @SuppressWarnings("unused")
    public boolean isInRange(String versionExpressionStart, String versionExpressionEnd) {
        VersionExpression start = new VersionExpression(versionExpressionStart);
        VersionExpression end = new VersionExpression(versionExpressionEnd);
        return isInRange(start, end);
    }

    /**
     * Evaluates whether the current version matches any of the specified criteria (version expressions).
     * The current version only needs to match one expression to evaluate to true. Semantically,
     * this is the equivalent of <code><i>expr1</i> || <i>expr2</i> ...</code>
     *
     * @param expressions the version expressions to evaluate
     * @return <code>true</code> if any of the expressions evaluate to true, <code>false</code> otherwise
     */
    @SuppressWarnings("unused")
    public boolean isAnyOf(VersionExpression... expressions) {
        boolean anyOf = false;

        //we only need to find the first expression that evaluates to true
        for (VersionExpression expression : expressions) {
            if (evaluateVersion(expression)) {
                anyOf = true;
                break;
            }
        }

        return anyOf;
    }

    /**
     * Evaluates whether the current version matches any of the specified criteria (version expressions).
     * The current version only needs to match one expression to evaluate to true. Semantically,
     * this is the equivalent of <code><i>expr1</i> || <i>expr2</i> ...</code>
     *
     * @param versionExpressions the version expressions to evaluate
     * @return <code>true</code> if any of the expressions evaluate to true, <code>false</code> otherwise
     */
    @SuppressWarnings("unused")
    public boolean isAnyOf(String... versionExpressions) {
        boolean anyOf = false;

        for (String expression : versionExpressions) {
            if (evaluateVersion(expression)) {
                anyOf = true;
                break;
            }
        }

        return anyOf;
    }

    private int compareMajor(Version anotherVersion) {
        return getMajor() - anotherVersion.getMajor();
    }

    private int compareMinor(Version anotherVersion) {
        return getMinor() - anotherVersion.getMinor();
    }

    private int comparePatch(Version anotherVersion) {
        return getPatch() - anotherVersion.getPatch();
    }

    @Override
    public int compareTo(final Version o) {
        if (compareMajor(o) != 0) {
            return 1000 + compareMajor(o) + compareMinor(o) + comparePatch(o);
        } else if (compareMajor(o) == 0 && compareMinor(o) != 0) {
            return 100 + compareMinor(o) + comparePatch(o);
        } else if (compareMajor(o) == 0 && compareMinor(o) == 0 && comparePatch(o) != 0) {
            return 10 + comparePatch(o);
        } else {
            return 0;
        }
    }
}