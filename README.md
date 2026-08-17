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

---

## 🛠️ Java Programming Concepts Demonstrated

This project is structured specifically to show key Java concepts taught in college programming courses:

* **Dynamic Programming**: Needleman-Wunsch scoring and backtracing matrix computation.
* **Java Collections**: Manages objects using `ArrayList` and character counts using `HashMap`.
* **String & Stream Processing**: Methods like `replaceAll()`, `toUpperCase()`, `length()`, `charAt()`, and `String.join()`.
* **Exception Handling & Validation**: Custom validation catching missing sequences or invalid letters, raising user alert popup dialogs via `JOptionPane`.

---

## 📂 Project Structure

```text
Protein-Motif-Consensus-Generator/
│
├── src/
│   ├── Main.java               # UI dashboard layout, navigation, and events
│   ├── SequenceProcessor.java  # FASTA header removal and residue character validations
│   ├── MotifGenerator.java     # Column representation calculator and PROSITE pattern generator
│   └── Alignment.java          # Needleman-Wunsch and progressive alignment algorithm
│
├── package.bat                 # Windows compilation and jpackage setup installer script
├── Logo.png                    # Brand logo displayed in header and window bar
└── README.md                   # Project documentation
```

---

## 🏃 How to Run the Project

Make sure you have JDK (tested on Java 25) installed and configured in your path.

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

1. Double-click or run `package.bat` in Command Prompt.
2. The batch file compiles the classes, outputs a runnable JAR file, and runs Java's native `jpackage` utility.
3. Once completed, a self-contained installation program (`ProteinMotifConsensusGenerator-1.0.exe`) is created inside the `dist/` directory.
