import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PrelimGradeCalculator extends JFrame {

    private JTextField attendanceField, lab1Field, lab2Field, lab3Field;
    private JTextArea outputArea;
    private JButton calculateBtn, resetBtn;

    public PrelimGradeCalculator() {
        setTitle("Student Prelim Grade Calculator");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        /* ===== HEADER ===== */
        JPanel header = new JPanel();
        header.setBackground(new Color(52, 73, 94));
        header.setPreferredSize(new Dimension(0, 80));
        header.setLayout(new BorderLayout());

        JLabel headerTitle = new JLabel("📊 PRELIM GRADE CALCULATOR");
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerTitle.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        header.add(headerTitle, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        /* ===== MAIN AREA ===== */
        JPanel mainArea = new JPanel(new GridLayout(1, 2, 20, 20));
        mainArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainArea.setBackground(new Color(236, 240, 241));

        /* ===== INPUT CARD ===== */
        JPanel inputCard = new JPanel();
        inputCard.setLayout(new GridLayout(6, 2, 10, 10));
        inputCard.setBackground(Color.WHITE);
        inputCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel inputTitle = new JLabel("📝 Student Inputs");
        inputTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        inputCard.add(inputTitle);
        inputCard.add(new JLabel(""));

        attendanceField = new JTextField();
        lab1Field = new JTextField();
        lab2Field = new JTextField();
        lab3Field = new JTextField();

        inputCard.add(new JLabel("Attendance Score"));
        inputCard.add(attendanceField);

        inputCard.add(new JLabel("Lab Work 1"));
        inputCard.add(lab1Field);

        inputCard.add(new JLabel("Lab Work 2"));
        inputCard.add(lab2Field);

        inputCard.add(new JLabel("Lab Work 3"));
        inputCard.add(lab3Field);

        calculateBtn = new JButton("CALCULATE GRADE");
        calculateBtn.setBackground(new Color(41, 128, 185));
        calculateBtn.setForeground(Color.WHITE);
        calculateBtn.setFocusPainted(false);
        calculateBtn.addActionListener(new CalculateHandler());

        resetBtn = new JButton("RESET");
        resetBtn.setBackground(new Color(192, 57, 43));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setFocusPainted(false);
        resetBtn.addActionListener(e -> clearFields());

        inputCard.add(calculateBtn);
        inputCard.add(resetBtn);

        /* ===== OUTPUT CARD ===== */
        JPanel outputCard = new JPanel(new BorderLayout());
        outputCard.setBackground(Color.WHITE);
        outputCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel outputTitle = new JLabel("📄 Grade Report");
        outputTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        outputCard.add(outputTitle, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setMargin(new Insets(10, 10, 10, 10));

        outputCard.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        mainArea.add(inputCard);
        mainArea.add(outputCard);

        add(mainArea, BorderLayout.CENTER);
    }

    /* ===== CALCULATION LOGIC ===== */
    private class CalculateHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                double attendance = Double.parseDouble(attendanceField.getText());
                double l1 = Double.parseDouble(lab1Field.getText());
                double l2 = Double.parseDouble(lab2Field.getText());
                double l3 = Double.parseDouble(lab3Field.getText());

                if (l1 > 100 || l2 > 100 || l3 > 100 || attendance < 0) {
                    throw new NumberFormatException();
                }

                double labAvg = (l1 + l2 + l3) / 3;
                double classStanding = (attendance * 0.40) + (labAvg * 0.60);

                double needPass = (75 - (classStanding * 0.70)) / 0.30;
                double needExcellent = (100 - (classStanding * 0.70)) / 0.30;

                StringBuilder report = new StringBuilder();
                report.append("STUDENT PERFORMANCE SUMMARY\n");
                report.append("====================================\n\n");
                report.append(String.format("Lab Average       : %.2f\n", labAvg));
                report.append(String.format("Class Standing    : %.2f\n\n", classStanding));

                report.append("PRELIM EXAM REQUIREMENTS\n");

                report.append(needPass <= 0
                        ? "✔ Passing (75): Already secured\n"
                        : needPass > 100
                        ? "✖ Passing (75): Not possible\n"
                        : String.format("➜ Passing (75): %.2f needed\n", needPass));

                report.append(needExcellent <= 0
                        ? "✔ Excellent (100): Already secured\n"
                        : needExcellent > 100
                        ? "✖ Excellent (100): Not possible\n"
                        : String.format("➜ Excellent (100): %.2f needed\n", needExcellent));

                outputArea.setText(report.toString());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        PrelimGradeCalculator.this,
                        "Please enter valid numbers only.\nGrades must be between 0 and 100.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void clearFields() {
        attendanceField.setText("");
        lab1Field.setText("");
        lab2Field.setText("");
        lab3Field.setText("");
        outputArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrelimGradeCalculator().setVisible(true));
    }
}
