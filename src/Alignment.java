import java.util.ArrayList;

/**
 * Implements a simple Progressive Sequence Alignment algorithm using Needleman-Wunsch
 * pairwise global alignment. Designed for educational purposes in college courses.
 */
public class Alignment {

    private static final int MATCH_SCORE = 1;
    private static final int MISMATCH_SCORE = -1;
    private static final int GAP_SCORE = -1;

    /**
     * Performs a progressive multiple sequence alignment.
     * Uses the first sequence as the seed, and aligns each subsequent sequence
     * against the current aligned reference, propagating gaps back to already aligned sequences.
     *
     * @param sequences List of clean, unaligned protein sequences
     * @return List of aligned sequences of identical length
     */
    public static ArrayList<String> alignSequences(ArrayList<String> sequences) {
        ArrayList<String> result = new ArrayList<>();
        if (sequences == null || sequences.isEmpty()) {
            return result;
        }

        // Initialize alignment with the first sequence
        ArrayList<StringBuilder> alignedBuilders = new ArrayList<>();
        alignedBuilders.add(new StringBuilder(sequences.get(0)));

        // Align remaining sequences progressively
        for (int i = 1; i < sequences.size(); i++) {
            String reference = alignedBuilders.get(0).toString(); // The current reference (could contain gaps)
            String nextSeq = sequences.get(i);

            // Align nextSeq against the reference
            PairwiseResult pair = alignPairwise(reference, nextSeq);

            // Update existing sequences in alignedBuilders by inserting any new gaps introduced in the reference
            synchronizeGaps(alignedBuilders, reference, pair.alignedRef);

            // Add the newly aligned sequence to our list
            alignedBuilders.add(new StringBuilder(pair.alignedQuery));
        }

        // Convert Builders to String
        for (StringBuilder sb : alignedBuilders) {
            result.add(sb.toString());
        }
        return result;
    }

    /**
     * Data structure to hold the outcome of a pairwise alignment.
     */
    private static class PairwiseResult {
        String alignedRef;
        String alignedQuery;

        PairwiseResult(String alignedRef, String alignedQuery) {
            this.alignedRef = alignedRef;
            this.alignedQuery = alignedQuery;
        }
    }

    /**
     * Pairwise global alignment using the Needleman-Wunsch dynamic programming algorithm.
     *
     * @param ref   Reference sequence (may already contain gaps from previous alignments)
     * @param query Unaligned query sequence to align against ref
     */
    private static PairwiseResult alignPairwise(String ref, String query) {
        int lenRef = ref.length();
        int lenQuery = query.length();

        int[][] F = new int[lenRef + 1][lenQuery + 1];

        // Initialize base gap penalties along the edges
        for (int i = 0; i <= lenRef; i++) {
            F[i][0] = i * GAP_SCORE;
        }
        for (int j = 0; j <= lenQuery; j++) {
            F[0][j] = j * GAP_SCORE;
        }

        // Fill DP Matrix
        for (int i = 1; i <= lenRef; i++) {
            char rChar = ref.charAt(i - 1);
            for (int j = 1; j <= lenQuery; j++) {
                char qChar = query.charAt(j - 1);

                // Scoring check
                int score;
                if (rChar == '-' || qChar == '-') {
                    // Match gap to gap is 1, match gap to residue is mismatch
                    score = (rChar == qChar) ? MATCH_SCORE : MISMATCH_SCORE;
                } else {
                    score = (rChar == qChar) ? MATCH_SCORE : MISMATCH_SCORE;
                }

                int match = F[i - 1][j - 1] + score;
                int delete = F[i - 1][j] + GAP_SCORE;
                int insert = F[i][j - 1] + GAP_SCORE;

                F[i][j] = Math.max(match, Math.max(delete, insert));
            }
        }

        // Backtrack to find alignment strings
        StringBuilder alignedRef = new StringBuilder();
        StringBuilder alignedQuery = new StringBuilder();

        int i = lenRef;
        int j = lenQuery;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0) {
                char rChar = ref.charAt(i - 1);
                char qChar = query.charAt(j - 1);
                int score = (rChar == '-' || qChar == '-') ? 
                            ((rChar == qChar) ? MATCH_SCORE : MISMATCH_SCORE) : 
                            ((rChar == qChar) ? MATCH_SCORE : MISMATCH_SCORE);

                if (F[i][j] == F[i - 1][j - 1] + score) {
                    alignedRef.insert(0, rChar);
                    alignedQuery.insert(0, qChar);
                    i--;
                    j--;
                    continue;
                }
            }

            if (i > 0 && (j == 0 || F[i][j] == F[i - 1][j] + GAP_SCORE)) {
                // Delete: gap introduced in query, consume ref character
                alignedRef.insert(0, ref.charAt(i - 1));
                alignedQuery.insert(0, '-');
                i--;
            } else {
                // Insert: gap introduced in ref, consume query character
                alignedRef.insert(0, '-');
                alignedQuery.insert(0, query.charAt(j - 1));
                j--;
            }
        }

        return new PairwiseResult(alignedRef.toString(), alignedQuery.toString());
    }

    /**
     * Synchronizes new gaps introduced in the reference sequence back into
     * all previously aligned sequences in the list to maintain correct alignment structure.
     *
     * @param alignedBuilders The list of already aligned sequences
     * @param oldRef          The reference sequence before this alignment round
     * @param newRef          The reference sequence after this alignment round (which may have new gaps)
     */
    private static void synchronizeGaps(ArrayList<StringBuilder> alignedBuilders, String oldRef, String newRef) {
        int p = 0; // Pointer in oldRef
        for (int q = 0; q < newRef.length(); q++) {
            char c = newRef.charAt(q);
            if (p < oldRef.length() && c == oldRef.charAt(p)) {
                p++;
            } else if (c == '-') {
                // A new gap '-' was introduced in the reference at index q.
                // Insert this gap at the same position in all currently aligned builders.
                for (StringBuilder sb : alignedBuilders) {
                    sb.insert(q, '-');
                }
            }
        }
    }
}
