package enums;

public enum MatchType {
    equal, contains, match;

    public static boolean match(String expected, String actual, MatchType matchType) {
        switch (matchType) {
            case equal:
                return expected.equals(actual);
            case contains:
                return expected.contains(actual);
            case match:
                return expected.matches(actual);
        }
        return false;
    }
}
