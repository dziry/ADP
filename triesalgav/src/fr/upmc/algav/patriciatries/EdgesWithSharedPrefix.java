package fr.upmc.algav.patriciatries;

/**
 * Created by amadeus on 27.11.16.
 */
public class EdgesWithSharedPrefix {
    private String firstEdgeValue;
    private String secondEdgeValue;
    private String sharedPrefix;

    public EdgesWithSharedPrefix(String firstEdgeValue, String secondEdgeValue, String sharedPrefix) {
        this.firstEdgeValue = firstEdgeValue;
        this.secondEdgeValue = secondEdgeValue;
        this.sharedPrefix = sharedPrefix;
    }

    public String getFirstEdgeValue() {
        return firstEdgeValue;
    }

    public String getSecondEdgeValue() {
        return secondEdgeValue;
    }

    public String getSharedPrefix() {
        return sharedPrefix;
    }

    public boolean edgesAreIdentical() {
        return firstEdgeValue != null && secondEdgeValue != null && sharedPrefix != null &&
                firstEdgeValue.equals(sharedPrefix) && secondEdgeValue.equals(sharedPrefix) &&
                firstEdgeValue.equals(secondEdgeValue);
    }

    public boolean firstEdgeAndPrefixAreIdentical() {
        return firstEdgeValue != null && sharedPrefix != null && firstEdgeValue.equals(sharedPrefix);
    }

    public boolean secondEdgeAndPrefixAreIdentical() {
        return secondEdgeValue != null && sharedPrefix != null && secondEdgeValue.equals(sharedPrefix);
    }

    public String getFirstEdgeValueWithoutCommonPrefix() {
        return (firstEdgeValue == null || sharedPrefix == null) ?
                null : firstEdgeValue.substring(sharedPrefix.length());
    }

    public String getSecondEdgeValueWithoutCommonPrefix() {
        return (secondEdgeValue == null || sharedPrefix == null) ?
                null : secondEdgeValue.substring(sharedPrefix.length());
    }
}
