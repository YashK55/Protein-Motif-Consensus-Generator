import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Main application frame. coordinates sequence input, dynamic progressive alignment,
 * consensus generation, and PROSITE-style pattern generation, presenting them in a unified vertical scroll.
 */
public class Main extends JFrame {

    // === EDITABLE PROJECT GROUP MEMBER NAMES ===
    private static final String DEVELOPER_1 = "Yash Katekhaye";
    private static final String DEVELOPER_2 = "Sujit Mohanty";
    private static final String DEVELOPER_3 = "Aniruddha Naik";

    // Layout page management
    private final CardLayout pageCardLayout;
    private final JPanel mainPageContainer;
    private JButton navHomeBtn;
    private JButton navAboutBtn;

    // Logic engines
    private final SequenceProcessor sequenceProcessor;
    private final MotifGenerator motifGenerator;

    // Home Page sequence input and consolidated results
    private JTextArea sequenceInputArea;
    private JTextArea alignmentViewArea;

    public Main() {
        this.sequenceProcessor = new SequenceProcessor();
        this.motifGenerator = new MotifGenerator();

        // 1200 x 850 Window Size as specified in Section 14
        setTitle("Protein Motif Consensus Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 850);
        setMinimumSize(new Dimension(1000, 750));
        setLocationRelativeTo(null);

        initTheme();

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(new Color(241, 245, 249));

        // Navigation Header Bar
        JPanel headerPanel = createHeaderPanel();
        rootPanel.add(headerPanel, BorderLayout.NORTH);

        // Content Area CardLayout (Home & About pages)
        pageCardLayout = new CardLayout();
        mainPageContainer = new JPanel(pageCardLayout);
        mainPageContainer.setOpaque(false);

        mainPageContainer.add(createHomePage(), "HOME");
        mainPageContainer.add(createAboutPage(), "ABOUT");

        rootPanel.add(mainPageContainer, BorderLayout.CENTER);
        setContentPane(rootPanel);

        switchPage("HOME");
    }

    private void initTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));

        // Load logo.ico for JFrame Window Icon
        try {
            File logoFile = new File("logo.png");
            if (logoFile.exists()) {
                ImageIcon frameIcon = new ImageIcon("logo.png");
                if (frameIcon.getIconWidth() > 0) {
                    setIconImage(frameIcon.getImage());
                }
            }
        } catch (Exception ignored) {
        }
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(24, 43, 73)); // Deep Navy
        header.setPreferredSize(new Dimension(1200, 70));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Left Section: Scaled logo.png + Title + Subtitle
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
        brandPanel.setOpaque(false);

        try {
            File logoFile = new File("logo.png");
            if (logoFile.exists()) {
                ImageIcon rawLogo = new ImageIcon("logo.png");
                if (rawLogo.getIconWidth() > 0) {
                    Image img = rawLogo.getImage();
                    Image scaledImg = img.getScaledInstance(-1, 40, Image.SCALE_SMOOTH);
                    JLabel logoLabel = new JLabel(new ImageIcon(scaledImg));
                    logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
                    brandPanel.add(logoLabel);
                }
            }
        } catch (Exception ignored) {
        }

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 2));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Protein Motif Consensus Generator");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("PROSITE Signature Scanning & Consensus Pattern Discovery");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        subtitleLabel.setForeground(new Color(203, 213, 225));

        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        brandPanel.add(titlePanel);

        header.add(brandPanel, BorderLayout.WEST);

        // Right Section: Page Switchers
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        navPanel.setOpaque(false);

        navHomeBtn = createNavButton("Home", "HOME");
        navAboutBtn = createNavButton("About", "ABOUT");

        navPanel.add(navHomeBtn);
        navPanel.add(navAboutBtn);

        header.add(navPanel, BorderLayout.EAST);

        return header;
    }

    private JButton createNavButton(String label, final String pageName) {
        final JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(24, 43, 73));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(new Color(13, 148, 136))) {
                    btn.setBackground(new Color(37, 65, 107));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(new Color(13, 148, 136))) {
                    btn.setBackground(new Color(24, 43, 73));
                }
            }
        });

        btn.addActionListener(e -> switchPage(pageName));
        return btn;
    }

    private void switchPage(String pageName) {
        pageCardLayout.show(mainPageContainer, pageName);

        Color inactiveNavy = new Color(24, 43, 73);
        Color activeTeal = new Color(13, 148, 136);

        navHomeBtn.setBackground(pageName.equals("HOME") ? activeTeal : inactiveNavy);
        navAboutBtn.setBackground(pageName.equals("ABOUT") ? activeTeal : inactiveNavy);
    }

    /**
     * Builds the Home page in a vertical layout.
     */
    private JPanel createHomePage() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- SECTION 1: PROTEIN SEQUENCE INPUT ---
        JPanel inputPanel = new JPanel(new BorderLayout(0, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel inputHeader = new JPanel(new GridLayout(2, 1, 0, 4));
        inputHeader.setOpaque(false);
        JLabel inputTitle = new JLabel("1. PROTEIN SEQUENCE INPUT");
        inputTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        inputTitle.setForeground(new Color(24, 43, 73));

        JLabel inputDesc = new JLabel("Enter at least two protein sequences. Sequences may be unaligned and may use FASTA format.");
        inputDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputDesc.setForeground(new Color(100, 116, 139));

        inputHeader.add(inputTitle);
        inputHeader.add(inputDesc);
        inputPanel.add(inputHeader, BorderLayout.NORTH);

        sequenceInputArea = new JTextArea();
        sequenceInputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        sequenceInputArea.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 1));
        sequenceInputArea.setMargin(new Insets(8, 8, 8, 8));

        JScrollPane inputScroll = new JScrollPane(sequenceInputArea);
        inputScroll.setPreferredSize(new Dimension(1100, 130)); // Reduced height from 180 to 130
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonRow.setOpaque(false);

        JButton btnAnalyze = createStyledButton("Analyze Sequence", new Color(13, 148, 136), Color.WHITE);
        JButton btnLoad = createStyledButton("Load Example", new Color(24, 43, 73), Color.WHITE);
        JButton btnClear = createStyledButton("Clear", new Color(100, 116, 139), Color.WHITE);

        btnAnalyze.addActionListener(e -> processAnalysis());
        btnLoad.addActionListener(e -> loadSampleSequences());
        btnClear.addActionListener(e -> clearInputAndResults());

        buttonRow.add(btnAnalyze);
        buttonRow.add(btnLoad);
        buttonRow.add(btnClear);
        inputPanel.add(buttonRow, BorderLayout.SOUTH);

        contentPanel.add(inputPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Margin separator

        // --- SECTION 2: VISUAL ALIGNMENT & MOTIF (Consolidated Results Section) ---
        JPanel resultsPanel = new JPanel(new BorderLayout(0, 10));
        resultsPanel.setBackground(Color.WHITE);
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel resultsTitle = new JLabel("2. VISUAL ALIGNMENT & MOTIF");
        resultsTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        resultsTitle.setForeground(new Color(24, 43, 73));
        resultsPanel.add(resultsTitle, BorderLayout.NORTH);

        alignmentViewArea = new JTextArea();
        alignmentViewArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        alignmentViewArea.setEditable(false);
        alignmentViewArea.setBackground(new Color(248, 250, 252));
        alignmentViewArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Horizontal scrolling is maintained by wrapping alignmentViewArea in a JScrollPane
        JScrollPane resultsScroll = new JScrollPane(alignmentViewArea);
        resultsScroll.setPreferredSize(new Dimension(1100, 360)); // Reduced height from 420 to 360
        resultsPanel.add(resultsScroll, BorderLayout.CENTER);

        contentPanel.add(resultsPanel);

        return contentPanel;
    }

    private JButton createStyledButton(String text, final Color bg, Color fg) {
        final JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bg.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bg);
            }
        });

        return button;
    }

    /**
     * Triggers sequence parsing, alignment calculations, and formats the unified results area.
     */
    private void processAnalysis() {
        try {
            String rawText = sequenceInputArea.getText();
            ArrayList<String> rawSequences = sequenceProcessor.cleanAndValidate(rawText);

            // Execute dynamic progressive alignment
            ArrayList<String> alignedList = Alignment.alignSequences(rawSequences);

            // Compute Consensus sequence and PROSITE pattern
            String consensus = motifGenerator.generateConsensus(alignedList);
            String prositePattern = motifGenerator.generatePrositePattern(alignedList);

            // Format layout text inside alignmentViewArea
            StringBuilder sb = new StringBuilder();
            int labelWidth = 13; // Space allocated for label prefixes e.g. "Sequence 1   "

            // 1. Aligned Sequences
            for (int i = 0; i < alignedList.size(); i++) {
                String label = "Sequence " + (i + 1);
                sb.append(String.format("%-" + labelWidth + "s%s\n", label, alignedList.get(i)));
            }

            // 2. Dash separator
            sb.append(String.format("%-" + labelWidth + "s", ""));
            int seqLen = alignedList.get(0).length();
            for (int i = 0; i < seqLen; i++) {
                sb.append("-");
            }
            sb.append("\n\n"); // spacing

            // 3. Consensus line
            sb.append(String.format("%-" + labelWidth + "s%s\n\n", "Consensus", consensus));

            // 4. PROSITE Pattern line
            sb.append("PROSITE Pattern\n");
            sb.append(String.format("%-" + labelWidth + "s%s\n", "", prositePattern));

            alignmentViewArea.setText(sb.toString());
            alignmentViewArea.setCaretPosition(0);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Sequence Validation Warning",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "An unexpected error occurred:\n" + ex.getMessage(),
                    "System Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearInputAndResults() {
        sequenceInputArea.setText("");
        alignmentViewArea.setText("");
    }

    private void loadSampleSequences() {
        String example = ">Protein 1\n" +
                         "GASGAGKS\n\n" +
                         ">Protein 2\n" +
                         "GATGAGKT\n\n" +
                         ">Protein 3\n" +
                         "GASGAGKS\n\n" +
                         ">Protein 4\n" +
                         "GASGAGKT";
        sequenceInputArea.setText(example);
    }

    /**
     * Builds the About page, incorporating logo.png and student credits.
     */
    private JPanel createAboutPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));
        panel.setOpaque(false);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        // Load and display logo.png inside the About Page card
        try {
            File logoFile = new File("logo.png");
            if (logoFile.exists()) {
                ImageIcon rawLogo = new ImageIcon("logo.png");
                if (rawLogo.getIconWidth() > 0) {
                    Image img = rawLogo.getImage();
                    Image scaledImg = img.getScaledInstance(-1, 100, Image.SCALE_SMOOTH); // Scale to height 100px
                    JLabel logoLabel = new JLabel(new ImageIcon(scaledImg));
                    logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    card.add(logoLabel);
                    card.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            }
        } catch (Exception ignored) {
        }

        JLabel title = new JLabel("About Protein Motif Consensus Generator");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(24, 43, 73));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        JTextArea desc = new JTextArea(
                "A simple Java desktop application that analyzes multiple protein amino-acid sequences, " +
                "identifies conserved regions, generates a consensus sequence, and produces a PROSITE-style motif pattern.\n\n" +
                "The application accepts sequences of different lengths, aligns them using dynamic programming (Needleman-Wunsch), " +
                "and generates a consensus and pattern representation detailing conserved (>= 70%) residues, variable bracket groups, " +
                "and compressed variable positions like x(3)."
        );
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        desc.setForeground(new Color(71, 85, 105));
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(desc);
        card.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        detailsPanel.setOpaque(false);
        detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblProj = new JLabel("Java Programming Project");
        lblProj.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblProj.setForeground(new Color(13, 148, 136));

        JLabel lblHeading = new JLabel("Created as a college assignment by:");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHeading.setForeground(new Color(15, 23, 42));

        JLabel lblDevs = new JLabel("• " + DEVELOPER_1 + "   • " + DEVELOPER_2 + "   • " + DEVELOPER_3);
        lblDevs.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblDevs.setForeground(new Color(24, 43, 73));

        detailsPanel.add(lblProj);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(lblHeading);
        detailsPanel.add(lblDevs);

        card.add(detailsPanel);
        card.add(Box.createVerticalGlue());

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
