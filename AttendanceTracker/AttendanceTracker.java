import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Attendance Tracker Application
 * A Java Swing application for tracking student attendance
 * with automatic time stamping and e-signature generation
 */
public class AttendanceTracker extends JFrame {
    
    // UI Components
    private JTextField nameField;
    private JTextField courseYearField;
    private JTextField timeInField;
    private JTextField eSignatureField;
    private JButton submitButton;
    private JButton clearButton;
    
    /**
     * Constructor - Initializes the attendance tracker window
     */
    public AttendanceTracker() {
        // Set up the main frame
        setTitle("Attendance Tracker");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on screen
        
        // Initialize and add components
        initializeComponents();
        
        // Make the frame visible
        setVisible(true);
    }
    
    /**
     * Initialize and arrange all UI components
     */
    private void initializeComponents() {
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Student Attendance System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 102, 204));
        titlePanel.add(titleLabel);
        
        // Create form panel with GridBagLayout for better control
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8); // Spacing between components
        
        // Row 0: Attendance Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel nameLabel = new JLabel("Attendance Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        // Row 1: Course/Year
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel courseYearLabel = new JLabel("Course/Year:");
        courseYearLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(courseYearLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        courseYearField = new JTextField(20);
        formPanel.add(courseYearField, gbc);
        
        // Row 2: Time In
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel timeInLabel = new JLabel("Time In:");
        timeInLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(timeInLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        timeInField = new JTextField(20);
        timeInField.setEditable(false); // Make it read-only
        timeInField.setBackground(Color.WHITE);
        formPanel.add(timeInField, gbc);
        
        // Row 3: E-Signature
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        JLabel eSignatureLabel = new JLabel("E-Signature:");
        eSignatureLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(eSignatureLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        eSignatureField = new JTextField(20);
        eSignatureField.setEditable(false); // Make it read-only
        eSignatureField.setBackground(Color.WHITE);
        formPanel.add(eSignatureField, gbc);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        // Submit button - generates timestamp and e-signature
        submitButton = new JButton("Generate Attendance");
        submitButton.setFont(new Font("Arial", Font.BOLD, 12));
        submitButton.setBackground(new Color(0, 153, 76));
        submitButton.setForeground(Color.BLACK);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(e -> generateAttendance());
        
        // Clear button - resets all fields
        clearButton = new JButton("Clear Form");
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearButton.setBackground(new Color(204, 0, 0));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(e -> clearForm());
        
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        
        // Add all panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    /**
     * Generate attendance record with current timestamp and e-signature
     */
    private void generateAttendance() {
        // Validate that required fields are filled
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter your name!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }
        
        if (courseYearField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter your course/year!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            courseYearField.requestFocus();
            return;
        }
        
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeIn = now.format(formatter);
        timeInField.setText(timeIn);
        
        // Generate unique e-signature using UUID
        String eSignature = UUID.randomUUID().toString();
        eSignatureField.setText(eSignature);
        
        // Show success message
        JOptionPane.showMessageDialog(this, 
            "Attendance recorded successfully!\n\nName: " + nameField.getText() + 
            "\nCourse/Year: " + courseYearField.getText() + 
            "\nTime In: " + timeIn,
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Clear all form fields
     */
    private void clearForm() {
        nameField.setText("");
        courseYearField.setText("");
        timeInField.setText("");
        eSignatureField.setText("");
        nameField.requestFocus(); // Set focus back to name field
    }
    
    /**
     * Main method - Entry point of the application
     */
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater for thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Create and display the attendance tracker
            new AttendanceTracker();
        });
    }
}