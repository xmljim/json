package io.github.xmljim.json.service;

final class VersionPatterns {

    public static final String MAJOR = "major";
    public static final String MINOR = "minor";
    public static final String PATCH = "patch";
    public static final String PRERELEASE = "prerelease";
    public static final String BUILDMETADATA = "buildmetadata";
    public static final String OPERATOR = "operator";
    public static final String VERSION = "version";

    private static final String numericPattern = "0|[1-9]\\d";
    private static final String alphaNumericPattern = "[0-9a-zA-Z-]";
    private static final String alphaPattern = "[a-zA-Z-]";
    private static final String numericAlphaPattern = "\\d*" + alphaPattern + alphaNumericPattern + "*";
    private static final String dot = "\\.";
    private static final String majorPattern = "(?<" + MAJOR + ">" + numericPattern + "*)";
    private static final String minorPattern = "(" + dot + "(?<" + MINOR + ">" + numericPattern + "*))?";
    private static final String patchPattern = "(" + dot + "(?<" + PATCH + ">" + numericPattern + "*))?";
    private static final String preReleaseBase = "(?:" + numericPattern + "*|" + numericAlphaPattern + ")(?:" + dot + "(?:" + numericPattern + "*|" + numericAlphaPattern + "))*";
    private static final String preReleasePattern = "(?:-(?<" + PRERELEASE + ">" + preReleaseBase + "))?";
    private static final String buildMetadataBase = alphaNumericPattern + "+(?:" + dot + alphaNumericPattern + "+)*)";
    private static final String buildMetadataPattern = "(?:\\+(?<" + BUILDMETADATA + ">" + buildMetadataBase + ")?";
    private static final String operatorPattern = "(?<" + OPERATOR + ">[\\^~=><]{1,2})";
    public static final String suffixPattern = "(?:-?(?<" + PRERELEASE + ">" + preReleaseBase + "))?" + buildMetadataPattern;
    public static final String regex = "^" + majorPattern + minorPattern + patchPattern + preReleasePattern + buildMetadataPattern + "$";
    public static final String evalPattern = operatorPattern + "(?<" + VERSION + ">" + majorPattern + minorPattern + patchPattern + preReleasePattern + buildMetadataPattern + ")";


    private VersionPatterns() {
        //disable constructor
    }


}
