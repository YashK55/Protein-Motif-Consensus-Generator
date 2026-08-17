# Protein Motif Consensus Generator

**Analyze, Align, and Scan Protein Consensus Motifs**

A standalone, educational Java Swing desktop application designed as a college Java Programming project. The application aligns multiple protein sequences, identifies conserved columns, generates a consensus sequence, and produces a PROSITE-style motif pattern.

---

## 🚀 Key Features

* **Multiple Protein Sequence Input**: Accepts sequences of varying lengths, supporting both plain text and FASTA records (lines starting with `>`).
* **Sequence Alignment**: Aligns unaligned inputs using a custom Needleman-Wunsch progressive global alignment algorithm.
* **Consensus Sequence Extraction**: Identifies conserved positions column-by-column (using a 70% threshold).
* **PROSITE Pattern Generator**: Compiles column states into standard PROSITE notation (e.g. `G-A-[ST]-x(3)-K`), compressing consecutive variable positions.
* **Vertical Dashboard Layout**: A clean, single-scroll interface showing the input panel, consensus results, and the monospaced alignment view from top to bottom.
* **About Page & Credits**: Displays project documentation and lists the contributing developers.

---

## ⚙️ How the Alignment & Analysis Engine Works

The application processes protein sequences through a structured pipeline:
1. **Sequence Parsing & Cleaning**: [SequenceProcessor.java](file:///c:/Users/Yash%20katekhaye/eclipse-workspace/Protein%20Motif%20Consensus%20Generator/src/SequenceProcessor.java) parses the inputs. If sequences are formatted as FASTA, it ignores the header lines (starting with `>`) and extracts the raw characters. It validates all inputs against the 20 standard amino acid codes.
2. **Progressive Multiple Sequence Alignment**: [Alignment.java](file:///c:/Users/Yash%20katekhaye/eclipse-workspace/Protein%20Motif%20Consensus%20Generator/src/Alignment.java) runs global pairwise alignments progressively using the **Needleman-Wunsch dynamic programming algorithm** (Scoring rules: `Match: +1`, `Mismatch: -1`, `Gap Penalty: -1`). Gaps are progressively propagated back to previously aligned sequences to keep lengths synchronized.
3. **Consensus Identification**: [MotifGenerator.java](file:///c:/Users/Yash%20katekhaye/eclipse-workspace/Protein%20Motif%20Consensus%20Generator/src/MotifGenerator.java) determines the consensus character column-by-column:
   * **Rule 1 (>=70% Single Residue)**: If a single residue frequency is >= 70%, that residue is chosen.
   * **Rule 2 (Group conservation)**: If the frequency is < 70%, it collects residues with >= 15% frequency. If their combined frequency is >= 70% and there are at most 3 residues, it outputs them in alphabetical brackets, e.g., `[ST]`.
   * **Rule 3 (Variable)**: Otherwise, the column is represented as variable `x`.
4. **PROSITE Formatting**: Joins column representations with hyphens (e.g., `G-A-[ST]`) and compresses consecutive `x` entries into `x(N)` format (e.g., `x-x-x` becomes `x(3)`).

---

## 🛠️ Java Programming Concepts Demonstrated

This project is structured specifically to show key Java concepts taught in college programming courses:

* **Dynamic Programming**: Needleman-Wunsch scoring and backtracing matrix computation.
* **Java Collections**: Manages sequences using `ArrayList` and character counts using `HashMap`.
* **String & Stream Processing**: Methods like `replaceAll()`, `toUpperCase()`, `length()`, `charAt()`, and `String.join()`.
* **Exception Handling & Validation**: Custom validation catching missing sequences or invalid letters, raising user alert popup dialogs via `JOptionPane`.

---

## 📂 Project Structure

```text
Protein-Motif-Consensus-Generator/
│
├── src/
│   ├── Main.java               # UI dashboard layout, navigation, page routing, and events
│   ├── SequenceProcessor.java  # FASTA header removal and residue character validations
│   ├── MotifGenerator.java     # Column representation calculator and PROSITE pattern generator
│   ├── Alignment.java          # Needleman-Wunsch and progressive alignment algorithm
│   ├── Motif.java              # Model class representing a motif segment
│   └── PrositePattern.java     # Model representing predefined PROSITE database entries
│
├── package.bat                 # Windows compilation and jpackage setup installer script
├── logo.png                    # Brand logo displayed in header and about card
├── logo.ico                    # Windows icon embedded during installer packaging
└── README.md                   # Project documentation
```

---

## 🏃 How to Run the Project

Make sure you have JDK (tested on Java 25) installed and configured in your environment path.

1. Open a command prompt in the project folder.
2. Compile the source code:
   ```cmd
   mkdir bin
   javac -d bin src/*.java
   ```
3. Run the application:
   ```cmd
   java -cp bin Main
   ```

---

## 📦 Packaging into a Windows Installer

A Windows packaging script is included:

1. Double-click or run [package.bat](file:///c:/Users/Yash%20katekhaye/eclipse-workspace/Protein%20Motif%20Consensus%20Generator/package.bat) in Command Prompt.
2. The batch file compiles the classes, outputs a runnable JAR file, and runs Java's native `jpackage` utility to package the app with the customized icon [logo.ico](file:///c:/Users/Yash%20katekhaye/eclipse-workspace/Protein%20Motif%20Consensus%20Generator/logo.ico).
3. Once completed, a self-contained installation program (`ProteinMotifConsensusGenerator-1.0.exe`) is created inside the `dist/` directory.

---

## 👥 Developers & Credits

This project was created as a college course assignment by:
* **Yash Katekhaye**
* **Sujit Mohanty**
* **Aniruddha Naik**

