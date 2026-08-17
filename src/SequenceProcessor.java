import java.util.ArrayList;

/**
 * Handles cleaning and validation of protein sequence inputs.
 * Supports FASTA format and plain text formats (single or multiple sequences).
 */
public class SequenceProcessor {

    private static final String VALID_AMINO_ACIDS = "ACDEFGHIKLMNPQRSTVWY";

    /**
     * Cleans and validates the input text.
     * Detects and ignores FASTA headers, handles whitespace, and validates amino acid characters.
     *
     * @param inputText Raw text entered by the user
     * @return List of clean, uppercase protein sequences
     * @throws IllegalArgumentException if validation fails
     */
    public ArrayList<String> cleanAndValidate(String inputText) throws IllegalArgumentException {
        if (inputText == null || inputText.trim().isEmpty()) {
            throw new IllegalArgumentException("Input sequence(s) cannot be empty.");
        }

        ArrayList<String> sequences = new ArrayList<>();

        if (inputText.contains(">")) {
            // FASTA parser
            String[] records = inputText.split(">");
            for (String record : records) {
                String trimmedRecord = record.trim();
                if (trimmedRecord.isEmpty()) {
                    continue;
                }

                // Split the record into lines
                String[] lines = trimmedRecord.split("\\r?\\n");
                
                // First line (lines[0]) is the FASTA description header
                // Concatenate the remaining lines as sequence data
                StringBuilder seqBuilder = new StringBuilder();
                for (int i = 1; i < lines.length; i++) {
                    seqBuilder.append(lines[i].replaceAll("\\s+", "")); // strip all whitespaces
                }

                String cleanedSeq = seqBuilder.toString().toUpperCase();
                if (cleanedSeq.isEmpty()) {
                    throw new IllegalArgumentException("FASTA record contains header but no sequence data.");
                }

                validateSequenceCharacters(cleanedSeq);
                sequences.add(cleanedSeq);
            }
        } else {
            // Plain text parser: treats each non-empty line as a sequence
            String[] lines = inputText.split("\\r?\\n");
            for (String line : lines) {
                String cleaned = line.replaceAll("\\s+", "").toUpperCase();
                if (!cleaned.isEmpty()) {
                    validateSequenceCharacters(cleaned);
                    sequences.add(cleaned);
                }
            }
        }

        if (sequences.size() < 2) {
            throw new IllegalArgumentException("Please enter at least 2 protein sequences.");
        }

        return sequences;
    }

    /**
     * Helper to validate that a sequence contains only standard amino-acid characters.
     */
    private void validateSequenceCharacters(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            char aa = seq.charAt(i);
            if (VALID_AMINO_ACIDS.indexOf(aa) == -1) {
                throw new IllegalArgumentException("Invalid amino-acid character: " + aa);
            }
        }
    }
}
