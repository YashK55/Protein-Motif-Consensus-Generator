import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Handles generating PROSITE-style consensus patterns and consensus sequences
 * from aligned sequences.
 */
public class MotifGenerator {

    /**
     * Computes the representations of each column in aligned sequences.
     */
    private ArrayList<String> getColumnRepresentations(ArrayList<String> alignedList) {
        ArrayList<String> colRepresentations = new ArrayList<>();
        if (alignedList == null || alignedList.isEmpty()) {
            return colRepresentations;
        }

        int colLength = alignedList.get(0).length();
        int numSeqs = alignedList.size();

        for (int col = 0; col < colLength; col++) {
            HashMap<Character, Integer> counts = new HashMap<>();
            int totalLetters = 0;

            for (String seq : alignedList) {
                char aa = seq.charAt(col);
                if (aa != '-') {
                    counts.put(aa, counts.getOrDefault(aa, 0) + 1);
                    totalLetters++;
                }
            }

            // If the column is entirely gaps
            if (totalLetters == 0) {
                colRepresentations.add("x");
                continue;
            }

            // Find the highest frequency character
            char maxChar = '?';
            int maxCount = 0;
            for (HashMap.Entry<Character, Integer> entry : counts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    maxChar = entry.getKey();
                }
            }

            double maxPercent = (maxCount * 100.0) / totalLetters;

            // Rule 1: 100% same residue or >= 70% single residue conservation
            if (maxPercent >= 70.0) {
                colRepresentations.add(String.valueOf(maxChar));
            } else {
                // Rule 2: Collect all residues that have at least 15% frequency
                ArrayList<Character> represented = new ArrayList<>();
                int sumCount = 0;
                for (HashMap.Entry<Character, Integer> entry : counts.entrySet()) {
                    double pct = (entry.getValue() * 100.0) / totalLetters;
                    if (pct >= 15.0) {
                        represented.add(entry.getKey());
                        sumCount += entry.getValue();
                    }
                }

                double sumPercent = (sumCount * 100.0) / totalLetters;

                // Rule 3: Check if the top residues cover at least 70% of the column
                if (sumPercent >= 70.0 && represented.size() <= 3) {
                    // Sort represented alphabetically
                    Collections.sort(represented);
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    for (char aa : represented) {
                        sb.append(aa);
                    }
                    sb.append("]");
                    colRepresentations.add(sb.toString());
                } else {
                    // Rule 4: Otherwise no strong conservation, represent as variable 'x'
                    colRepresentations.add("x");
                }
            }
        }
        return colRepresentations;
    }

    /**
     * Generates a PROSITE-style pattern from multiple aligned sequences of equal length.
     * Formats columns into exact letters (e.g. G), brackets (e.g. [ST]), or variable (x).
     * Compresses consecutive x positions into x(N) format.
     *
     * @param alignedList List of aligned protein sequences containing gaps
     * @return Generated PROSITE pattern string
     */
    public String generatePrositePattern(ArrayList<String> alignedList) {
        if (alignedList == null || alignedList.isEmpty()) {
            return "";
        }

        ArrayList<String> colRepresentations = getColumnRepresentations(alignedList);

        // Compress consecutive variable 'x' positions (e.g. x-x-x -> x(3))
        ArrayList<String> compressedParts = new ArrayList<>();
        int i = 0;
        while (i < colRepresentations.size()) {
            String rep = colRepresentations.get(i);
            if (rep.equals("x")) {
                int count = 0;
                while (i < colRepresentations.size() && colRepresentations.get(i).equals("x")) {
                    count++;
                    i++;
                }
                if (count == 1) {
                    compressedParts.add("x");
                } else {
                    compressedParts.add("x(" + count + ")");
                }
            } else {
                compressedParts.add(rep);
                i++;
            }
        }

        return String.join("-", compressedParts);
    }

    /**
     * Generates an uncompressed consensus sequence string from multiple aligned sequences.
     *
     * @param alignedList List of aligned protein sequences containing gaps
     * @return Consensus string (e.g., GA[ST]GAGK[ST])
     */
    public String generateConsensus(ArrayList<String> alignedList) {
        if (alignedList == null || alignedList.isEmpty()) {
            return "";
        }
        ArrayList<String> colRepresentations = getColumnRepresentations(alignedList);
        return String.join("", colRepresentations);
    }
}
