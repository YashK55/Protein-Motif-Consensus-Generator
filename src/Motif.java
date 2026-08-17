/**
 * Represents a detected or generated protein motif.
 */
public class Motif {
    private final String name;
    private final String pattern;
    private final int startPosition;
    private final int endPosition;
    private final String matchedSequence;
    private final String description;
    private final boolean isKnown;

    public Motif(String name, String pattern, int startPosition, int endPosition, 
                 String matchedSequence, String description, boolean isKnown) {
        this.name = name;
        this.pattern = pattern;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.matchedSequence = matchedSequence;
        this.description = description;
        this.isKnown = isKnown;
    }

    public String getName() {
        return name;
    }

    public String getPattern() {
        return pattern;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public String getMatchedSequence() {
        return matchedSequence;
    }

    public String getDescription() {
        return description;
    }

    public boolean isKnown() {
        return isKnown;
    }
}
