import java.util.ArrayList;

/**
 * Encapsulates a predefined PROSITE database signature.
 */
public class PrositePattern {
    private final String id;
    private final String name;
    private final String pattern;
    private final String description;

    public PrositePattern(String id, String name, String pattern, String description) {
        this.id = id;
        this.name = name;
        this.pattern = pattern;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPattern() {
        return pattern;
    }

    public String getDescription() {
        return description;
    }

    public String toRegex() {
        String regex = pattern.replace("-", "");
        regex = regex.replaceAll("\\{([^\\}]+)\\}", "[^$1]");
        regex = regex.replaceAll("\\((\\d+(,\\d+)?)\\)", "{$1}");
        regex = regex.replace("x", ".");
        return regex;
    }

    public static ArrayList<PrositePattern> getBuiltInDatabase() {
        ArrayList<PrositePattern> db = new ArrayList<>();
        db.add(new PrositePattern(
                "PS00108",
                "PROTEIN_KINASE_ST",
                "[LIVMFYC]-x-[HY]-x-D-[LIVMFY]-K-x(2)-N-[LIVMFYCT](3)",
                "Protein kinase active-site signature."
        ));
        db.add(new PrositePattern(
                "PS00107",
                "PROTEIN_KINASE_ATP",
                "[LIV]-G-x-G-x(2)-[GS]-x-V-x(12,18)-K",
                "Protein kinase ATP-binding region."
        ));
        db.add(new PrositePattern(
                "PS00001",
                "ASN_GLYCOSYLATION",
                "N-{P}-[ST]-{P}",
                "N-glycosylation site."
        ));
        return db;
    }
}
